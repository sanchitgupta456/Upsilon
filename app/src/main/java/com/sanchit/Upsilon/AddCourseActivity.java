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

import org.bson.Document;

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
                if(offline_online.isChecked())
                {
                    mode="Online";
                }
                else
                {
                    mode="Offline";
                }

                Document courseDetails = new Document();
                courseDetails.append("courseName",courseName);
                /*courseDetails.append("courseDescription",courseDescription);
                courseDetails.append("courseDuration",courseDuration);
                courseDetails.append("mode",mode);
                courseDetails.append("numberOfBatches",numOfBatches);*/

                mongoCollection.insertOne(courseDetails.append("courseId","heha")).getAsync(result -> {
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
