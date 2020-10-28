package com.sanchit.Upsilon;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;

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
    RadioButton Online,Offline,Group,Individual,Free,Paid;
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

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");




    }
}
