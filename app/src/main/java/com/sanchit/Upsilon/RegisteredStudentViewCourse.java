package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sanchit.Upsilon.courseData.CourseFinal;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;

public class RegisteredStudentViewCourse extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private FrameLayout fragmentContainer;
    private BottomNavigationView bottomNavigationView;
    private CourseFinal course;
    //private TextView courseName;
    //private ImageButton btnBack;
    View actionBar;
    String appID = "upsilon-ityvn";
    App app;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_course_main);
        Intent intent = getIntent();
        course = (CourseFinal) intent.getSerializableExtra("Course");
        fragmentContainer = (FrameLayout) findViewById(R.id.student_registered_frame);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(course.getCourseName());
        actionBar = getSupportActionBar().getCustomView();
        //courseName = (TextView) actionBar.findViewById(R.id.textCourseNameActiveCourse);
        //btnBack = (ImageButton) actionBar.findViewById(R.id.imgBtnBackActiveCourse);
        //courseName.setText(course.getCourseName());

        loadFragment(new RegisteredStudentViewCourseScheduleFragment());

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(RegisteredStudentViewCourse.this);
    }
        /*
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisteredStudentViewCourse.this, MainActivity.class);
                startActivity(intent);
            }
        });*/


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("Course",course);
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.student_registered_frame, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Course",course);

        Fragment fragment = new RegisteredStudentViewCourseScheduleFragment();
        fragment.setArguments(bundle);

        switch (item.getItemId()) {
            case R.id.bottomNavMenuHome:
                fragment = new RegisteredStudentViewCourseHomeFragment();
                fragment.setArguments(bundle);
                break;

            case R.id.bottomNavMenuTestsAssignments:
//                fragment = new RegisteredStudentViewCourseATFragment();
//                fragment.setArguments(bundle);
//                Intent intent = new Intent(getApplicationContext(), RegisteredStudentViewCourseAssignmentsFragment.class);
//                intent.putExtra("Course", course);
//                startActivity(intent);
                break;

            case R.id.bottomNavMenuSchedule:
                fragment = new RegisteredStudentViewCourseScheduleFragment();
                fragment.setArguments(bundle);
                break;
//
//            case R.id.bottomNavMenuReview:
//                fragment = new RegisteredStudentViewCourseReviewFragment();
//                fragment.setArguments(bundle);
//                break;
            case R.id.bottomNavMenuForum:
                fragment = new RegisteredStudentViewCourseForumFragment();
                fragment.setArguments(bundle);
        }

        return loadFragment(fragment);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registered_student_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        if(item.getItemId() == R.id.assignments) {
//            Intent intent = new Intent(getApplicationContext(), RegisteredStudentViewCourseAssignmentsFragment.class);
//            intent.putExtra("Course", course);
//            startActivity(intent);
            loadFragment(new RegisteredStudentViewCourseAssignmentsFragment());
        }
        if(item.getItemId()==R.id.menuItemReview)
        {
            Log.v("RegisteredStudent","Review");
            loadFragment(new RegisteredStudentViewCourseReviewFragment());
        }
        if(item.getItemId()==R.id.menuItemNotifications)
        {
            Log.v("RegisteredStudent","Notifications");
            Bundle bundle = new Bundle();
            bundle.putSerializable("Course",course);
            Fragment fragment = new RegisteredStudentViewCourseNotificationFragment();
            fragment.setArguments(bundle);
            loadFragment(fragment);
        }
//        else if(item.getItemId()==R.id.menuItemSettings)
//        {
//            //open settings page
//        }
        else if (item.getItemId()==R.id.menuItemUnregister)
        {

//            app = new App(new AppConfiguration.Builder(appID)
//                    .build());
//            User user = app.currentUser();
//            mongoClient = user.getMongoClient("mongodb-atlas");
//            mongoDatabase = mongoClient.getDatabase("Upsilon");
//            MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("DropRequests");
//
//            Document queryFilter  = new Document("userid",user.getId()).append("courseId",course.getCourseId());
//
//            RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
//
//            findTask.getAsync(task -> {
//                if (task.isSuccess()) {
//                    MongoCursor<Document> results = task.get();
//                    if(!results.hasNext())
//                    {
//                        mongoCollection.insertOne(
//                                new Document("userid", user.getId()).append("courseId",course.getCourseId()).append("timeStamp",System.currentTimeMillis()))
//                                .getAsync(result -> {
//                                    if (result.isSuccess()) {
//                                        Toast.makeText(getApplicationContext(),"Your request has been registered",Toast.LENGTH_LONG).show();
//                                        Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
//                                                + result.get().getInsertedId());
//                                    } else {
//                                        Toast.makeText(getApplicationContext(),"Error registering request . Please try again later",Toast.LENGTH_LONG).show();
//                                        Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
//                                    }
//                                });
//                    }
//                    else
//                    {
//                        Toast.makeText(getApplicationContext(),"Your request has already been registered",Toast.LENGTH_LONG).show();
//                        Log.v("User", "successfully found the user");
////                        getCourseData();
//                    }
//
//                } else {
//                    Log.v("User","Failed to complete search"+task.getError());
//                }
//            });
            //unregister the course if registration happened < 7 days before, else do not
        }
        else if (item.getItemId()==R.id.menuItemRefresh)
        {
            //refresh the layout - meant for the forum page
        }
        return true;
    }
}
