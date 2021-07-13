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
import com.google.gson.JsonSyntaxException;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseFinal;
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

    RecyclerView recyclerView, recyclerView1;
    CoursesAdapter coursesAdapter;
    TeachingStatListAdapter statListAdapter;
    ArrayList<CourseFinal> courseArrayList = new ArrayList<CourseFinal>();
    ArrayList<TeachingStatData> teachingStatDataArrayList = new ArrayList<TeachingStatData>();
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
            getTeacherStatData();
            initStatRecycler();
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

                        try {
                            CourseFinal course = gson.fromJson(currentDoc.toJson(),CourseFinal.class);
                            if(course.getTutorId().toString().equals(user.getId().toString()))
                            {
                                courseArrayList.add(course);
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
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

    public void getTeacherStatData() {
        User user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
        MongoCollection<Document> mongoCollection1  = mongoDatabase.getCollection("CourseData");


        Document queryFilter  = new Document("userid",user.getId());
        Document queryFilter1  = new Document("tutorId",user.getId());

        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
        RealmResultTask<MongoCursor<Document>> findTask1 = mongoCollection1.find(queryFilter1).iterator();


        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                Document result = results.next();
                teachingStatDataArrayList.add(new TeachingStatData("Total earnings with us",String.valueOf(result.get("totalEarningWithUs"))));
                statListAdapter.notifyDataSetChanged();
            } else {
                Log.e("Courses Taught Activity", "failed to find courses with: ", task.getError());
            }
        });

        final Double[] sum = {0.00};
        final Double[] count = { 0.00 };
        final Double[] avgrating = new Double[1];
        final int[] studentCount = {0};

        findTask1.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                while (results.hasNext())
                {
                    Document result = results.next();
                    sum[0] = sum[0] +Double.parseDouble(String.valueOf(result.get("courseRating")));
                    count[0] = count[0] +1;
                    studentCount[0] = studentCount[0] +Integer.parseInt(String.valueOf(result.get("numberOfStudentsEnrolled")));
                    Log.v("CoursesTaughtActivity", String.valueOf(sum[0]));
                    Log.v("CoursesTaughtActivity", String.valueOf(count[0]));
                }
                avgrating[0] = sum[0] / count[0];
                teachingStatDataArrayList.add(new TeachingStatData("Number of Students Taught",String.valueOf(studentCount[0])));
                teachingStatDataArrayList.add(new TeachingStatData("Average rating on your courses",String.format("%.3f",avgrating[0])));
                teachingStatDataArrayList.add(new TeachingStatData("Number of Courses Taught",String.valueOf(count[0])));
                statListAdapter.notifyDataSetChanged();
            } else {
                Log.e("Courses Taught Activity", "failed to find courses with: ", task.getError());
            }
        });

        /* for test purposes : begin */
//        teachingStatDataArrayList.add(new TeachingStatData("Total earnings last month","Rs. 1,089/-"));
        /* end */
    }

    public void initStatRecycler() {
        statListAdapter = new TeachingStatListAdapter(getApplicationContext(),teachingStatDataArrayList);

        recyclerView1 = (RecyclerView)findViewById(R.id.listTeachingStats);
        recyclerView1.setLayoutManager(new CenterZoomLayoutManager(this,CenterZoomLayoutManager.HORIZONTAL,false));
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(statListAdapter);
    }

}
