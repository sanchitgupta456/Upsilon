package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
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
    private View bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_courses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.explore);
        bar = getSupportActionBar().getCustomView();
        /*ImageButton imageButton = (ImageButton) bar.findViewById(R.id.imgBtnBackExploreCourses);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExploreActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });*/
        recyclerView = findViewById(R.id.exploreList);
        courseArrayList = new ArrayList<>();
        app = new App(new AppConfiguration.Builder(appID).build());
        courseAdapter = new CoursesAdapter1(courseArrayList);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
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

                    try {
                        Course course = gson.fromJson(currentDoc.toJson(),Course.class);

                        courseArrayList.add(course);
                        courseAdapter.notifyDataSetChanged();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
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
