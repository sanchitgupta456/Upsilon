package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CoursesAdapter;
import com.sanchit.Upsilon.ui.login.LoginActivity;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class CoursesTaughtActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CoursesAdapter coursesAdapter;
    ArrayList<Course> courseArrayList = new ArrayList<Course>();
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    String appID = "upsilon-ityvn";

    private Gson gson;
    private GsonBuilder gsonBuilder;
    private Button addCourseButton;
    private View bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_courses_taught);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.teach_at_upsilon);
        bar = getSupportActionBar().getCustomView();

        addCourseButton = (Button) findViewById(R.id.addCourseButton_coursesTaught);


        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoursesTaughtActivity.this,AddCourseActivity.class);
                startActivity(intent);
            }
        });

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();

        if(user==null)
        {
            Intent intent = new Intent(CoursesTaughtActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else
        {
            getCourseData();
        }

    }

    public void getCourseData(){


// an authenticated user is required to access a MongoDB instance

        if (app.currentUser()!=null) {
            final User user = app.currentUser();
            assert user != null;
            mongoClient = user.getMongoClient("mongodb-atlas");
            mongoDatabase = mongoClient.getDatabase("Upsilon");
            MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");

            //Blank query to find every single course in db
            //TODO: Modify query to look for user preferred course IDs
            Document queryFilter  = new Document();

            RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

            findTask.getAsync(task -> {
                if (task.isSuccess()) {
                    MongoCursor<Document> results = task.get();
                    Log.v("COURSEHandler", "successfully found all courses:");
                    while (results.hasNext()) {
                        //Log.v("EXAMPLE", results.next().toString());
                        Document currentDoc = results.next();

                        if(currentDoc.get("nextLectureOn")==null)
                        {
                            currentDoc.append("nextLectureOn",0);
                        }
                        currentDoc.toJson();
                        gsonBuilder = new GsonBuilder();
                        gson = gsonBuilder.create();

                        Course course = gson.fromJson(currentDoc.toJson(),Course.class);
                        if(course.getTutorId().toString().equals(user.getId().toString()))
                        {
                            courseArrayList.add(course);
                        }

                        //course = currentDoc;
                        //course.setCourseName(currentDoc.getString("courseName"));
                        //TODO : implement card image fetching via database
                        //course.setCardImgID(TvShowImgs[0]);
                        //courseArrayList.add(course);
                    }
                    displayCoursesInRecycler();
                } else {
                    Log.e("COURSEHandler", "failed to find courses with: ", task.getError());
                }
            });


            MongoCollection<Document> mongoCollection1  = mongoDatabase.getCollection("UserData");

            /*//Blank query to find every single course in db
            //TODO: Modify query to look for user preferred course IDs
            Document queryFilter1  = new Document();
            queryFilter1.append("userid",user.getId());

            RealmResultTask<MongoCursor<Document>> findTask1 = mongoCollection1.find(queryFilter1).iterator();

            findTask1.getAsync(task -> {
                if (task.isSuccess()) {
                    MongoCursor<Document> results = task.get();
                    Log.v("COURSEHandler", "successfully found all courses:");
                    Document document = results.next();
                    String url = document.getString("profilePicUrl");
                    Toast.makeText(CoursesTaughtActivity.this,url,Toast.LENGTH_LONG).show();
                    //Log.v("User","Hi"+ url);
                    //Picasso.with(getApplicationContext()).load(url).into(imageView);

                } else {
                    Log.e("COURSEHandler", "failed to find courses with: ", task.getError());
                }
            });*/
        }
        else {
            Log.e("User", "Error logging into the Realm app. Make sure that anonymous authentication is enabled.");
        }

        User user = app.currentUser();

    }

    public void displayCoursesInRecycler(){
        coursesAdapter = new CoursesAdapter(courseArrayList);

        recyclerView = (RecyclerView)findViewById(R.id.listCurrentCoursesTaught);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(coursesAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getActionView()==bar){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
