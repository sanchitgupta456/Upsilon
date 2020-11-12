package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class AddCourseActivityContinued extends AppCompatActivity {

    private Document courseDetails;
    String appID = "upsilon-ityvn";
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    Button nextButton;
    JSONObject json;
    String insertedId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_contd);

        Intent intent = getIntent();
        insertedId = (String) intent.getExtras().get("InsertedDocument");
        Log.v("Continued", String.valueOf(courseDetails));

        nextButton = (Button) findViewById(R.id.btnAddCourse);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<org.bson.Document> mongoCollection  = mongoDatabase.getCollection("CourseData");

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mongoCollection.insertOne((org.bson.Document) courseDetails).getAsync(result -> {
                    if(result.isSuccess())
                    {
                        App.Result result1 = result;
                        Log.v("AddCourse", String.valueOf(result)+ result.get().getInsertedId());
                        Intent intent = new Intent(AddCourseActivityContinued.this,MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Successfully Added The Course", Toast.LENGTH_LONG).show();
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
