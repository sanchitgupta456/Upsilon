package com.sanchit.Upsilon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CoursesAdapter;

import org.bson.Document;

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

    public static final List<String> TvShows  = new ArrayList<String>();
    public static final int[] TvShowImgs = {R.drawable.google, R.drawable.facebook};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCourseData();
    }

    public void getCourseData(){
        Realm.init(this); // initialize Realm, required before interacting with SDK
        final App app = new App(new AppConfiguration.Builder(appID)
                .build());

// an authenticated user is required to access a MongoDB instance
        Credentials credentials = Credentials.anonymous();
        app.loginAsync(credentials, it -> {
            if (it.isSuccess()) {
                final User user = app.currentUser();
                assert user != null;
                MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
                MongoDatabase mongoDatabase = mongoClient.getDatabase("Upsilon");
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
            });

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