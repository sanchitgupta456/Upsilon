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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CoursesAdapter;
import com.sanchit.Upsilon.ui.login.LoginActivity;
import com.squareup.picasso.Picasso;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.Console;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private float x1,x2;
    boolean swapped = false;
    static final int MIN_DISTANCE = 500;
    DrawerLayout drawer;

    String appID = "upsilon-ityvn";
    private static final String TAG = "MainActivity";

    private Gson gson;
    private GsonBuilder gsonBuilder;
    private ArrayList<String> myCourses;

    RecyclerView recyclerView,recyclerView1,recyclerView2;
    CoursesAdapter coursesAdapter,coursesAdapter1,coursesAdapter2;
    ArrayList<Course> courseArrayList = new ArrayList<Course>();
    ArrayList<Course> courseArrayList1 = new ArrayList<>();
    ArrayList<Course> courseArrayList2 = new ArrayList<>();
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    ImageView imageView;

    public static final List<String> TvShows  = new ArrayList<String>();
    public static final int[] TvShowImgs = {R.drawable.google, R.drawable.facebook};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        //imageView = (ImageView) findViewById(R.id.profilePhotoTest);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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
                        getCourseData();
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
    }

    private void goToSetupActivity() {
        Intent intent = new Intent(MainActivity.this,UserDataSetupActivity1.class);
        startActivity(intent);
    }

    public void getCourseData(){

        displayCoursesInRecycler();
// an authenticated user is required to access a MongoDB instance

            if (app.currentUser()!=null) {
                final User user = app.currentUser();
                assert user != null;
                MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");
                MongoCollection<Document> mongoCollection2  = mongoDatabase.getCollection("UserData");

                //Blank query to find every single course in db
                //TODO: Modify query to look for user preferred course IDs
                Document queryFilter  = new Document();
                Document userdata = user.getCustomData();
                 myCourses = (ArrayList<String>) userdata.get("myCourses");
                if(myCourses==null)
                {
                    myCourses=new ArrayList<>();
                }

                RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                findTask.getAsync(task -> {
                    if (task.isSuccess()) {
                        MongoCursor<Document> results = task.get();
                        Log.v("COURSEHandler", "successfully found all courses:");
                        while (results.hasNext()) {
                            //Log.v("EXAMPLE", results.next().toString());
                            Document currentDoc = results.next();

                            currentDoc.toJson();
                            gsonBuilder = new GsonBuilder();
                            gson = gsonBuilder.create();

                            Course course = gson.fromJson(currentDoc.toJson(),Course.class);

                            //course = currentDoc;
                            //course.setCourseName(currentDoc.getString("courseName"));
                            //TODO : implement card image fetching via database
                            //course.setCardImgID(TvShowImgs[0]);

                            //Log.v("MyCourses", String.valueOf(myCourses));
                            for(int i=0;i<myCourses.size();i++)
                            {
                                Log.v("currentCourse", course.getCourseId() + myCourses.get(i));
                                if(myCourses.get(i).equals(course.getCourseId()))
                                {
                                    Log.v("CourseAdded","Added");
                                    courseArrayList1.add(course);
                                    coursesAdapter1.notifyDataSetChanged();
                                    break;
                                }
                            }
                            if(!course.getTutorId().equals(user.getId())) {
                                courseArrayList.add(course);
                                coursesAdapter.notifyDataSetChanged();
                                //courseArrayList2.add(course);
                                Document queryFilter1 = new Document("userid", course.getTutorId());

                                RealmResultTask<MongoCursor<Document>> findTask1 = mongoCollection2.find(queryFilter1).iterator();

                                findTask1.getAsync(task1 -> {
                                    if (task1.isSuccess()) {
                                        MongoCursor<Document> results1 = task1.get();
                                        if (!results1.hasNext()) {
                                            Log.v("ViewCourse", "Couldnt Find The Tutor");
                                        } else {
                                            Log.v("User", "successfully found the Tutor");

                                        }
                                        while (results1.hasNext()) {
                                            //Log.v("EXAMPLE", results.next().toString());
                                            Document currentDoc1 = results1.next();
                                            Log.v("CourseBySenior", (String) currentDoc1.get("college"));
                                            Log.v("CourseBySenior", (String)  userdata.get("college"));

                                            if (currentDoc1.getString("college").equals(userdata.getString("college"))) {
                                                Log.v("CourseBy","Hello");
                                                courseArrayList2.add(course);
                                                coursesAdapter2.notifyDataSetChanged();
                                                Log.v("CoursesySeniors", String.valueOf(courseArrayList2));
                                            }
                                            Log.v("User", currentDoc1.getString("userid"));
                                        }
                                    } else {
                                        Log.v("User", "Failed to complete search");
                                    }
                                });
                            }
                        }
                    } else {
                        Log.e("COURSEHandler", "failed to find courses with: ", task.getError());
                    }
                });


                MongoCollection<Document> mongoCollection1  = mongoDatabase.getCollection("UserData");

                //Blank query to find every single course in db
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
                        Toast.makeText(MainActivity.this,url,Toast.LENGTH_LONG).show();
                        //Log.v("User","Hi"+ url);
                        //Picasso.with(getApplicationContext()).load(url).into(imageView);
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
        coursesAdapter1 = new CoursesAdapter(courseArrayList1);
        coursesAdapter2 = new CoursesAdapter(courseArrayList2);

        recyclerView = (RecyclerView)findViewById(R.id.exploreCourseListView);
        recyclerView1 = (RecyclerView)findViewById(R.id.currentCourseListView);
        recyclerView2 = (RecyclerView) findViewById(R.id.recommendedCourseListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView1.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(coursesAdapter);
        recyclerView1.setAdapter(coursesAdapter1);
        recyclerView2.setAdapter(coursesAdapter2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.three_dot_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.signOut)
        {
            signOut();
        }
        return true;
    }

    private void signOut() {
        User user = app.currentUser();
        LoginManager.getInstance().logOut();
        user.logOutAsync(new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if(result.isSuccess())
                {
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
                else
                {

                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.homeDrawerMenuItem1)
        {
            Intent intent = new Intent(MainActivity.this,AddCourseActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.homeDrawerMenuItem2)
        {
            Intent intent = new Intent(MainActivity.this,CoursesTaughtActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.homeDrawerMenuItem3)
        {
            Intent intent = new Intent(MainActivity.this,ProfileViewActivity.class);
            startActivity(intent);
        }
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