package com.sanchit.Upsilon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CoursesAdapter;
import com.sanchit.Upsilon.ui.login.LoginActivity;

import org.bson.Document;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class MainActivity extends AppCompatActivity {
    String appID = "upsilon-ityvn";
    private static final String TAG = "MainActivity";

    RecyclerView recyclerView;
    CoursesAdapter coursesAdapter;
    ArrayList<Course> courseArrayList = new ArrayList<Course>();
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    public static final List<String> TvShows  = new ArrayList<String>();
    public static final int[] TvShowImgs = {R.drawable.google, R.drawable.facebook};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this); // initialize Realm, required before interacting with SDK
        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();
        if(user==null)
        {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else
        {
            mongoClient = user.getMongoClient("mongodb-atlas");
            mongoDatabase = mongoClient.getDatabase("Upsilon");
            MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");

            //Blank query to find every single course in db
            //TODO: Modify query to look for user preferred course IDs
            Document queryFilter  = new Document("userid",user.getId());

            RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

            findTask.getAsync(task -> {
                if (task.isSuccess()) {
                    MongoCursor<Document> results = task.get();
                    if(!results.hasNext())
                    {
                        mongoCollection.insertOne(
                                new Document("userid", user.getId()).append("favoriteColor", "pink"))
                                .getAsync(result -> {
                                    if (result.isSuccess()) {
                                        Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                + result.get().getInsertedId());
                                        goToSetupActivity();
                                    } else {
                                        Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                    }
                                });
                    }
                    else
                    {
                        Log.v("User", "successfully found the user");
                    }
                    while (results.hasNext()) {
                        //Log.v("EXAMPLE", results.next().toString());
                        Document currentDoc = results.next();
                        Log.v("User",currentDoc.getString("userid"));
                    }
                } else {
                   Log.v("User","Failed to complete search");
                }
            });
        }
        getCourseData();
    }

    private void goToSetupActivity() {
        Intent intent = new Intent(MainActivity.this,UserDataSetupActivity1.class);
        startActivity(intent);
    }

    public void getCourseData(){


// an authenticated user is required to access a MongoDB instance

            if (app.currentUser()!=null) {
                final User user = app.currentUser();
                assert user != null;
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

                            Course course = new Course();

                            course.setCourseName(currentDoc.getString("courseName"));
                            //TODO : implement card image fetching via database
                            course.setCardImgID(TvShowImgs[0]);
                            courseArrayList.add(course);
                        }
                        displayCoursesInRecycler();
                    } else {
                        Log.e("COURSEHandler", "failed to find courses with: ", task.getError());
                    }
                });
            }
            else {
                    Log.e(TAG, "Error logging into the Realm app. Make sure that anonymous authentication is enabled.");
                }

        User user = app.currentUser();

    }

    public void displayCoursesInRecycler(){
        coursesAdapter = new CoursesAdapter(courseArrayList);

        recyclerView = (RecyclerView)findViewById(R.id.currentCourseListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(coursesAdapter);
    }
}