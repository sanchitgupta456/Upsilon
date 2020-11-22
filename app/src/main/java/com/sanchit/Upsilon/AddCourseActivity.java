package com.sanchit.Upsilon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseReview;

import org.bson.BsonArray;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.BasicBSONList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class AddCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    String appID = "upsilon-ityvn";
    EditText CourseName,CourseDescription,CourseDuration,NumberOfBatches;
    String courseName,courseDescription,courseDuration,numOfBatches,mode,courseDurationMeasure;
    Button nextButton;
    RadioButton Group,Individual,Free,Paid;
    ToggleButton offline_online;
    //Spinner spinner;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    ArrayList courseReviews;
    ImageView CourseImage;
    String CourseImageUrl;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int WRITE_PERMISSION = 0x01;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_add_course);
        getSupportActionBar().setElevation(10);
        CourseName = (EditText) findViewById(R.id.add_course_name);
        CourseDescription = (EditText) findViewById(R.id.add_course_description);
        CourseDuration = (EditText) findViewById(R.id.add_course_duration);
        NumberOfBatches = (EditText) findViewById(R.id.add_course_num_batches);
        nextButton = (Button) findViewById(R.id.btnNext);
        offline_online = (ToggleButton) findViewById(R.id.add_course_mode);
        CourseImage = (ImageView) findViewById(R.id.imgAddCourseImage);
        //spinner = (Spinner) findViewById(R.id.courseDurationMeasureSpinner);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");

        CourseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestWritePermission();
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        //String[] measureOfTime = {"minutes","hours","days","weeks","months","years"};

        //ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_expandable_list_item_1,measureOfTime);
        //spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener(this);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                courseName = CourseName.getText().toString();
                courseDescription = CourseDescription.getText().toString();
                courseDuration = CourseDuration.getText().toString();
                numOfBatches = NumberOfBatches.getText().toString();

                //courseReviews = new ArrayList<CourseReview>();
                if(offline_online.isChecked())
                {
                    mode="Online";
                }
                else {
                    mode = "Offline";
                }
                //Object object = new CourseReview("Hi",5,"Hello");
                courseReviews = new ArrayList();
                Document test = new Document().append("review","This is a test review").append("reviewRating",2.75).append("reviewAuthorId",user.getId());
                courseReviews.add(test);
                //courseReviews.add(2,object);
                //courseReviews.put("hello",object);
                /*course.setRatingAuthorId("h");
                course.setReview("fd");
                course.setReviewRating(1.23);
                courseReviews.add(course);*/
                Document courseDetails = new Document();

                courseDetails.append("_partitionkey","_partitionKey");
                courseDetails.append("courseName",courseName);
                courseDetails.append("tutorId",user.getId().toString());
                courseDetails.append("courseDescription",courseDescription);
                courseDetails.append("coursePreReq","");
                courseDetails.append("courseRating",4.98);
                courseDetails.append("courseMode",mode);
                courseDetails.append("courseFees",0);
                courseDetails.append("courseImage","balh");
                courseDetails.append("instructorLocation","Here");
                //courseDetails.append("courseDurationMeasure","hours");
                courseDetails.append("numberOfStudentsEnrolled",10);
                courseDetails.append("courseDuration",courseDuration);
                courseDetails.append("numberOfBatches",numOfBatches);
                courseDetails.append("courseReviews",courseReviews);
                courseDetails.append("courseImageCounter",0);

                /*Intent intent = new Intent(AddCourseActivity.this,AddCourseActivityContinued.class);
                intent.putExtra("courseDetails",courseDetails.toJson().toString());
                startActivity(intent);*/
                mongoCollection.insertOne(courseDetails).getAsync(result -> {
                    if(result.isSuccess())
                    {
                        String id= String.valueOf(result.get().getInsertedId().asObjectId().getValue());
                        Log.v("Added Course",id);
                                       String requestId = MediaManager.get().upload(CourseImageUrl)
                        .unsigned("preset1")
                        .option("resource_type", "image")
                        .option("folder", "Upsilon/Courses/".concat(id))
                        .option("public_id", "CourseImage "+0)
                        .callback(new UploadCallback() {
                            @Override
                            public void onStart(String requestId) {
                            }

                            @Override
                            public void onProgress(String requestId, long bytes, long totalBytes) {

                            }

                            @Override
                            public void onSuccess(String requestId, Map resultData) {


                                courseDetails.append("courseImage",resultData.get("url").toString());
                                mongoCollection.updateOne(new Document("_id",result.get().getInsertedId()),courseDetails).getAsync(result1 -> {
                                    if(result1.isSuccess())
                                    {
                                        Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                + result1.get().getModifiedCount());
                                        Log.v("AddCourse","Updated Image Successfully");
                                        Intent intent = new Intent(AddCourseActivity.this,AddCourseActivityContinued.class);
                                        intent.putExtra("InsertedDocument", result.get().getInsertedId().asObjectId().getValue().toString());
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Log.v("AddCourse",result1.getError().toString());
                                    }
                                });
                            }

                            @Override
                            public void onError(String requestId, ErrorInfo error) {

                            }

                            @Override
                            public void onReschedule(String requestId, ErrorInfo error) {

                            }
                        })
                        .dispatch();
                        //Toast.makeText(getApplicationContext(),"Successfully Added The Course",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Log.v("User",result.getError().toString());
                    }
                });
            }
        });
    }
    // Defining the Callback methods here
    public void onItemSelected(AdapterView parent, View view, int pos,
                               long id) {

        //courseDurationMeasure = spinner.getItemAtPosition(pos).toString();

        //Toast.makeText(getApplicationContext(),
          //      spinner.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG)
            //    .show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            /*if (requestCode == 1) {

                // currImageURI is the global variable I'm using to hold the content:// URI of the image
                Uri currImageURI = data.getData();
                Bundle bundle = data.getExtras();
                profilepic.setImageURI(currImageURI);
                Profilepicpath = getRealPathFromURI(currImageURI);

            }*/
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                CourseImageUrl = cursor.getString(columnIndex);
                cursor.close();
                // String picturePath contains the path of selected Image
                ImageView imageView = (ImageView) findViewById(R.id.imgAddCourseImage);
                imageView.setImageBitmap(BitmapFactory.decodeFile(CourseImageUrl));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == WRITE_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Log.d("Hello", "Write Permission Failed");
                Toast.makeText(this, "You must allow permission write external storage to your mobile device.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void requestWritePermission(){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
        }
    }

}
