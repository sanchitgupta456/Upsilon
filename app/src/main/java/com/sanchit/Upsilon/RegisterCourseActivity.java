package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.courseData.Course;
import com.squareup.picasso.Picasso;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.types.ObjectId;
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

public class RegisterCourseActivity extends AppCompatActivity {

    String appID = "upsilon-ityvn";
    Course course;
    TextView courseName,studentName,studentContact,studentAddress;
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

        courseName = (TextView) findViewById(R.id.courseName);
        courseImage = (ImageView) findViewById(R.id.courseImage);
        studentName = (TextView) findViewById(R.id.nameStudent);
        studentContact = (TextView) findViewById(R.id.contactNumberStudent);
        studentAddress = (TextView) findViewById(R.id.addressStudent);
        proceedToPay = (Button) findViewById(R.id.btnProceedToPay);

        myRegisteredCourses = new ArrayList<String>();

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        user = app.currentUser();

        org.bson.Document userData = user.getCustomData();
        Log.v("userdata", String.valueOf(userData));

        Log.v("userData", String.valueOf(userData));
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
        Picasso.with(getApplicationContext()).load(course.getCourseImage()).into(courseImage);

        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");

        proceedToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MongoCollection<Document> mongoCollection1  = mongoDatabase.getCollection("CourseData");

                //Blank query to find every single course in db
                //TODO: Modify query to look for user preferred course IDs
                Document queryFilter  = new Document("userid",user.getId());

                RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

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
                                            startActivity(new Intent(RegisterCourseActivity.this,MainActivity.class));
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
            }
        });

    }
}
