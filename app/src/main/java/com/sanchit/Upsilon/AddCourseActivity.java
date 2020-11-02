package com.sanchit.Upsilon;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseReview;

import org.bson.BsonArray;
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

public class AddCourseActivity extends AppCompatActivity {


    String appID = "upsilon-ityvn";
    EditText CourseName,CourseDescription,CourseDuration,NumberOfBatches;
    String courseName,courseDescription,courseDuration,numOfBatches,mode;
    Button addCourseButton;
    RadioButton Group,Individual,Free,Paid;
    ToggleButton offline_online;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    BasicBSONList courseReviews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        CourseName = (EditText) findViewById(R.id.add_course_name);
        CourseDescription = (EditText) findViewById(R.id.add_course_description);
        CourseDuration = (EditText) findViewById(R.id.add_course_duration);
        NumberOfBatches = (EditText) findViewById(R.id.add_course_num_batches);
        addCourseButton = (Button) findViewById(R.id.btnAddCourse);
        offline_online = (ToggleButton) findViewById(R.id.add_course_mode);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");

        addCourseButton.setOnClickListener(new View.OnClickListener() {
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
                Object object = new CourseReview("Hi",5,"Hello");
                //course = new CourseReview("Hi",5,"Hello");
                courseReviews = new BasicBSONList();
                courseReviews.put(1,object);
                courseReviews.put(2,object);
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
                courseDetails.append("courseDurationMeasure","hours");
                courseDetails.append("numberOfStudentsEnrolled",10);
                courseDetails.append("courseImage","this is image");
                courseDetails.append("courseDuration",courseDuration);
                courseDetails.append("numberOfBatches",numOfBatches);
                courseDetails.append("courseReviews",courseReviews);

                mongoCollection.insertOne(courseDetails).getAsync(result -> {
                    if(result.isSuccess())
                    {
                        Toast.makeText(getApplicationContext(),"Successfully Added The Course",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Log.v("User",result.getError().toString());
                    }
                });


            }
        });




    }
}
