package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;

import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseReview;

import org.bson.BsonArray;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.BasicBSONList;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

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
                courseDetails.append("instructorLocation","Here");
                //courseDetails.append("courseDurationMeasure","hours");
                courseDetails.append("numberOfStudentsEnrolled",10);
                courseDetails.append("courseImage","this is image");
                courseDetails.append("courseDuration",courseDuration);
                courseDetails.append("numberOfBatches",numOfBatches);
                courseDetails.append("courseReviews",courseReviews);

                Intent intent = new Intent(AddCourseActivity.this,AddCourseActivityContinued.class);
                intent.putExtra("courseDetails",courseDetails.toJson().toString());
                startActivity(intent);
                /*mongoCollection.insertOne(courseDetails).getAsync(result -> {
                    if(result.isSuccess())
                    {
                        Intent intent = new Intent(AddCourseActivity.this,AddCourseActivityContinued.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Successfully Added The Course",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Log.v("User",result.getError().toString());
                    }
                });*/
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
}
