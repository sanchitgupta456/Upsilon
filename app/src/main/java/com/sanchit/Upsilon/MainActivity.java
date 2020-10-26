package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.google.android.material.navigation.NavigationView;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CoursesAdapter;
import com.sanchit.Upsilon.ui.login.LoginActivity;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private float x1,x2;
    boolean swapped = false;
    static final int MIN_DISTANCE = 150;
    DrawerLayout drawer;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        drawer.closeDrawer(GravityCompat.START);

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
                        Log.v("User",currentDoc.getString("favoriteColor"));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_drawer_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        //TODO: remove this later, this is just to test the image uploading
        Intent intent = new Intent(MainActivity.this, UserDataSetupActivity1.class);
        startActivity(intent);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(swapped){
     /*Make sure you don't swap twice,
since the dispatchTouchEvent might dispatch your touch event to this function again!*/
            swapped = false;
            return super.onTouchEvent(event);
        }
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (deltaX > MIN_DISTANCE)
                {
                    drawer.openDrawer(GravityCompat.START);
                    //you already swapped, set flag swapped = true
                    swapped = true;
                }
                else
                {
                    // not swapping
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        this.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }
}