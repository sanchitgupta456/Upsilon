package com.sanchit.Upsilon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.ui.login.LoginActivity;
import com.squareup.picasso.Picasso;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class RegisterCourseActivity extends AppCompatActivity implements PaymentResultListener {

    String appID = "upsilon-ityvn";
    String TAG = "Payment Error";
    Course course;
    TextView courseName,courseFees;
    EditText studentName,studentContact,studentAddress;
    ImageView courseImage;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    Button proceedToPay;
    ArrayList<String> myRegisteredCourses;
    private Gson gson;
    private GsonBuilder gsonBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_register);
        getSupportActionBar().hide();
        Checkout.preload(getApplicationContext());
       // checkout.setKeyID("rzp_test_mVNL5hJS3jvkHP");

        courseName = (TextView) findViewById(R.id.courseName);
        courseImage = (ImageView) findViewById(R.id.courseImage);
        studentName = (EditText) findViewById(R.id.nameStudent);
        studentContact = (EditText) findViewById(R.id.contactNumberStudent);
        studentAddress = (EditText) findViewById(R.id.addressStudent);
        proceedToPay = (Button) findViewById(R.id.btnProceedToPay);
        courseFees = (TextView) findViewById(R.id.textFees);

        myRegisteredCourses = new ArrayList<String>();

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        user = app.currentUser();
        fillFields();

        org.bson.Document userData = user.getCustomData();
        Log.v("userdata", String.valueOf(userData));

        studentName.setText(userData.getString("name"));
        studentContact.setText(userData.getString("phonenumber"));
        studentAddress.setText(userData.getString("pincode"));
        myRegisteredCourses = (ArrayList<String>) userData.get("myCourses");

        if(myRegisteredCourses==null)
        {
            myRegisteredCourses = new ArrayList<String>();
        }

        Intent intent = getIntent();
        course = (Course) intent.getSerializableExtra("course");

        courseName.setText(course.getCourseName());
        courseFees.setText("Rs. " + course.getCourseFees());
        Picasso.with(getApplicationContext()).load(course.getCourseImage()).into(courseImage);

        proceedToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(studentName.getText().toString().isEmpty())
                {
                    Animation shake = AnimationUtils.loadAnimation(RegisterCourseActivity.this, R.anim.shake);
                    studentName.startAnimation(shake);
                    studentName.setError("Please Enter a Valid Name");
                    studentName.requestFocus();
                }
                else if(studentContact.getText().toString().isEmpty())
                {
                    Animation shake = AnimationUtils.loadAnimation(RegisterCourseActivity.this, R.anim.shake);
                    studentContact.startAnimation(shake);
                    studentContact.setError("Please Enter a Valid Contact Number");
                    studentContact.requestFocus();
                }
                else if(studentAddress.getText().toString().isEmpty())
                {
                    Animation shake = AnimationUtils.loadAnimation(RegisterCourseActivity.this, R.anim.shake);
                    studentAddress.startAnimation(shake);
                    studentAddress.setError("Please Enter a Valid Address");
                    studentAddress.requestFocus();
                }
                else
                {
                        mongoClient = user.getMongoClient("mongodb-atlas");
                        mongoDatabase = mongoClient.getDatabase("Upsilon");
                        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
                        Document queryFilter = new Document().append("userid",user.getId());
                        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
                        findTask.getAsync(task->{
                            if(task.isSuccess())
                            {
                                MongoCursor<Document> results = task.get();
                                Document document = results.next();
                                if(document.getString("name")==null)
                                {
                                    document.append("name",studentName.getText().toString()).append("phonenumber",studentContact.getText().toString()).append("pincode",studentAddress.getText().toString());
                                    mongoCollection.updateOne(queryFilter,document).getAsync(result -> {
                                        if(result.isSuccess())
                                        {
                                            Log.v("Success RegisterCourse","Successfully Updated Details");
                                        }
                                        else
                                        {
                                            Log.v("Error RegisterCourse","Failed to update name and details");
                                        }
                                    });
                                }

                                if (course.getCourseFees() == 0) {
                                    RegisterStudent();
                                } else {
                                    startPayment(course.getCourseFees() * 100);
                                }

                            }
                            else
                            {
                                Log.v("Register Course ","Error , failed to complete user search");
                            }
                        });




                }
            }
        });

    }

    public void fillFields()
    {
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
        Document queryFilter = new Document().append("userid",user.getId());
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
        findTask.getAsync(task->{
            if(task.isSuccess())
            {
                MongoCursor<Document> results = task.get();
                Document document = results.next();
                if(document.getString("name")==null)
                {

                }
                else
                {
                    try {
                        studentName.setText(document.getString("name"));
                        studentContact.setText(document.getString("phonenumber"));
                        studentAddress.setText(document.getString("pincode"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            else
            {
                Log.v("Register Course ","Error , failed to complete user search");
            }
        });

    }

    public void RegisterStudent()
    {
        startActivity(new Intent(RegisterCourseActivity.this,MainActivity.class));
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
        MongoCollection<Document> mongoCollection1  = mongoDatabase.getCollection("CourseData");
        MongoCollection<Document> mongoCollection2  = mongoDatabase.getCollection("TeacherPaymentData");
        MongoCollection<Document> mongoCollection3  = mongoDatabase.getCollection("Transactions");
        MongoCollection<Document> mongoCollection4  = mongoDatabase.getCollection("WalletAmount");



        //Blank query to find every single course in db
        //TODO: Modify query to look for user preferred course IDs
        Document queryFilter  = new Document("userid",user.getId());
        Document queryFilter1  = new Document("userid",course.getTutorId());

        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
        RealmResultTask<MongoCursor<Document>> findTask1 = mongoCollection.find(queryFilter1).iterator();
        RealmResultTask<MongoCursor<Document>> findTask2 = mongoCollection2.find(queryFilter1).iterator();
        RealmResultTask<MongoCursor<Document>> findTask3 = mongoCollection4.find(queryFilter1).iterator();


        Document transaction = new Document();
        transaction.append("date",System.currentTimeMillis());
        transaction.append("userid",user.getId());
        transaction.append("tutorId",course.getTutorId());
        transaction.append("courseId",course.getCourseId());
        transaction.append("courseName",course.getCourseName());
        transaction.append("amount",course.getCourseFees());
        mongoCollection3.insertOne(transaction).getAsync(result -> {
            if(result.isSuccess())
            {
                Log.v("Transaction","Transaction Sucessful");
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please contact the customer Support as your transaction couldnt be verified",Toast.LENGTH_LONG).show();
            }
        });

        findTask3.getAsync(task ->{
            if(task.isSuccess())
            {
                MongoCursor<Document> results = task.get();
                if(results.hasNext())
                {
                    Document wallet = results.next();
                    ArrayList<Document> tobepaid = (ArrayList<Document>) wallet.get("tobepaid");
                    Document now = new Document();
                    now.append("date",System.currentTimeMillis());
                    now.append("amount",course.getCourseFees());
                    if(tobepaid==null)
                    {
                        tobepaid = new ArrayList<>();
                    }
                    tobepaid.add(now);
                    wallet.append("tobepaid",tobepaid);
                    mongoCollection4.updateOne(new Document("userid",course.getTutorId()),wallet).getAsync(result -> {
                        if(result.isSuccess())
                        {

                        }
                        else
                        {
                            Log.v("ToBePaid","Failed ToBePaid");
                        }
                    });
                }
                else
                {
                    Document wallet = new Document();
                    ArrayList<Document> tobepaid;
                    Document now = new Document();
                    now.append("date",System.currentTimeMillis());
                    now.append("amount",course.getCourseFees());
                    tobepaid = new ArrayList<>();
                    tobepaid.add(now);
                    wallet.append("tobepaid",tobepaid);
                    wallet.append("userid",course.getTutorId());
                    mongoCollection4.insertOne(wallet).getAsync(result -> {
                        if(result.isSuccess())
                        {

                        }
                        else
                        {
                            Log.v("ToBePaid","Failed ToBePaid");
                        }
                    });
                }
            }
            else
            {
                Log.v("User","Failed to complete search");
            }
        });

        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                if(!results.hasNext())
                {

                }
                else
                {
                    Log.v("User", "successfully found the user");
                    Document userdata = results.next();
                    myRegisteredCourses = (ArrayList<String>) userdata.get("myCourses");
                    if(myRegisteredCourses == null)
                    {
                        myRegisteredCourses = new ArrayList<>();
                    }
                    Log.v("Register", String.valueOf(course.getCourseId()));
                    myRegisteredCourses.add(course.getCourseId());
                    userdata.append("myCourses",myRegisteredCourses);
                    //userData.remove("_id");
                    mongoCollection.updateOne(new Document("userid",user.getId()),userdata).getAsync(result -> {
                        if(result.isSuccess())
                        {
                            course.setNumberOfStudentsEnrolled(course.getNumberOfStudentsEnrolled()+1);
                            BsonDocument courseDoc = new BsonDocument();
                            gsonBuilder = new GsonBuilder();
                            gson = gsonBuilder.create();

                            String object = gson.toJson(course,Course.class);

                            courseDoc = BsonDocument.parse(object);

                            mongoCollection1.updateOne(new Document("courseId",course.getCourseId()),courseDoc).getAsync(result1 -> {
                                if(result1.isSuccess())
                                {
                                    Toast.makeText(getApplicationContext(),"Successfully Registered for the Course",Toast.LENGTH_LONG).show();
                                    //startActivity(new Intent(RegisterCourseActivity.this,MainActivity.class));
                                }
                            });

                        }
                        else
                        {
                            Log.e("RegisterError", "Unable to Register. Error: " + result.getError());
                        }
                    });


                    // getCourseData();
                }
            } else {
                Log.v("User","Failed to complete search");
            }
        });

        findTask1.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                if(!results.hasNext())
                {

                }
                else
                {
                    Log.v("User", "successfully found the user");
                    Document data = results.next();
                    int amount = course.getCourseFees();
                    if(data.getInteger("WalletAmountToBePaid")!=null)
                    {
                        amount = amount+data.getInteger("WalletAmountToBePaid");
                    }
                    data.append("WalletAmountToBePaid",amount);
                    //userData.remove("_id");
                    mongoCollection.updateOne(new Document("userid",course.getTutorId()),data).getAsync(result -> {
                        if(result.isSuccess())
                        {

                        }
                        else
                        {
                            Log.e("RegisterError", "Unable to Register. Error: " + result.getError());
                        }
                    });


                    // getCourseData();
                }
            } else {
                Log.v("User","Failed to complete search");
            }
        });

        findTask2.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                if(!results.hasNext())
                {

                }
                else
                {
                    Log.v("User", "successfully found the user");
                    Document data = results.next();
                    int amount = course.getCourseFees();
                    if(data.getInteger("WalletAmountToBePaid")!=null)
                    {
                        amount = amount+data.getInteger("WalletAmountToBePaid");
                    }
                    data.append("WalletAmountToBePaid",amount);
                    //userData.remove("_id");
                    mongoCollection2.updateOne(new Document("userid",course.getTutorId()),data).getAsync(result -> {
                        if(result.isSuccess())
                        {

                        }
                        else
                        {
                            Log.e("RegisterError", "Unable to Register. Error: " + result.getError());
                        }
                    });


                    // getCourseData();
                }
            } else {
                Log.v("User", "successfully found the user");
                Document data = new Document("userid",user.getId());
                int amount = course.getCourseFees();
                if(data.getInteger("WalletAmountToBePaid")!=null)
                {
                    amount = amount+data.getInteger("WalletAmountToBePaid");
                }
                data.append("WalletAmountToBePaid",amount);
                mongoCollection2.insertOne(data).getAsync(result -> {
                    if(result.isSuccess())
                    {

                    }
                    else
                    {
                        Log.e("RegisterError", "Unable to Register. Error: " + result.getError());
                    }
                });
                Log.v("User","Failed to complete search");
            }
        });
    }

    public void startPayment(int amount1) {

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_mVNL5hJS3jvkHP");
        /**
         * Set your logo here
         */
        //checkout.setImage(R.drawable.logo);
        /**
         * Reference to current activity
         */
        //final Activity activity = getCallingActivity();

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();
            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "Upsilon");
            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "From "+user.getId()+" for "+course.getCourseId());
            //options.put("image", R.drawable.lightlogo1);
            //options.put("order_id", "order_9A33XWu170gUtm");
            options.put("currency", "INR");
            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", amount1);

            checkout.open(RegisterCourseActivity.this, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(RegisterCourseActivity.this,"Payment successful",Toast.LENGTH_SHORT).show();
        RegisterStudent();
        //Toast.makeText(RegisterCourseActivity.this,"Done",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(RegisterCourseActivity.this,"Error",Toast.LENGTH_SHORT).show();

    }
}
