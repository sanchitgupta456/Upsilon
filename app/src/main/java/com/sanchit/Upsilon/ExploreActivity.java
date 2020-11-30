package com.sanchit.Upsilon;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseAdapter2;
import com.sanchit.Upsilon.courseData.CoursesAdapter1;

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

import static android.view.View.GONE;

public class ExploreActivity extends AppCompatActivity {
    //vars
    private RecyclerView recyclerView;
    ArrayList<Course> courseArrayList;
    String appID = "upsilon-ityvn";
    private static final String TAG = "MainActivity";
    private CoursesAdapter1 courseAdapter;
    private App app;
    private LinearLayoutManager linearLayoutManager;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    private User user;
    private Gson gson;
    private GsonBuilder gsonBuilder;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_courses);

        recyclerView = findViewById(R.id.exploreList);
        courseArrayList = new ArrayList<>();
        app = new App(new AppConfiguration.Builder(appID).build());
        courseAdapter = new CoursesAdapter1(courseArrayList);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(courseAdapter);
        progressBar = findViewById(R.id.loadingExplore);
        user = app.currentUser();

        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");

        Document queryFilter  = new Document();

        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

        //final int[] flag = {0};
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                Log.v("COURSEHandler", "successfully found all courses:");
                while (results.hasNext()) {
                    progressBar.setVisibility(View.VISIBLE);
                    //Log.v("EXAMPLE", results.next().toString());
                    Document currentDoc = results.next();

                    //Log.v("IMPORTANT","Error:"+currentDoc.getString("nextLectureOn"));

                    if(currentDoc.getString("nextLectureOn")==null)
                    {
                        currentDoc.append("nextLectureOn","0");
                    }
                    currentDoc.toJson();
                    gsonBuilder = new GsonBuilder();
                    gson = gsonBuilder.create();

                    Course course = gson.fromJson(currentDoc.toJson(),Course.class);

                    courseArrayList.add(course);
                    courseAdapter.notifyDataSetChanged();
                    if(!results.hasNext())
                    {
                        progressBar.setVisibility(GONE);
                    }
                }
            } else {
                Log.e("COURSEHandler", "failed to find courses with: ", task.getError());
            }
        });





    }
}
