package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.classData.ScheduledClass;
import com.sanchit.Upsilon.courseData.CourseFinal;

import java.util.ArrayList;
import java.util.Objects;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;

public class TeacherViewCourseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "TeacherViewCourse";

    String appID = "upsilon-ityvn";
    CourseFinal course;
    //private ImageView courseImage;
    //private TextView rating,enrolled,nextClass, courseName;
    private TextView courseName;
    private View actionBar;
    ImageButton imageButtonBack, imageButtonUpdate;
    //Button ScheduleClass, UpdateLink;
    int year,month,day;
    App app;
    View dialogView;
    AlertDialog alertDialog;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    private Gson gson;
    private GsonBuilder gsonBuilder;
    private EditText meetLink;
    private FrameLayout fragmentContainer;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teachers_viewof_course);
        Intent intent = getIntent();
        course = (CourseFinal) intent.getSerializableExtra("Course");

        fragmentContainer = (FrameLayout) findViewById(R.id.course_teacher_frame);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(course.getCourseName());
        actionBar = (View) getSupportActionBar().getCustomView();

        loadFragment(new TeacherViewCourseActivityScheduling());

        bottomNavigationView = findViewById(R.id.bottom_navigation_teacher);
        bottomNavigationView.setOnNavigationItemSelectedListener(TeacherViewCourseActivity.this);

        /*
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherViewCourseActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        imageButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        setCheckable(true);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Course",course);

        Fragment fragment = new TeacherViewCourseActivityScheduling();
        fragment.setArguments(bundle);

        switch (item.getItemId()) {
            case R.id.bottomNavMenuHomeTeacher:
                fragment = new TeacherViewCourseActivityHomeFragment();
                fragment.setArguments(bundle);
                break;

            case R.id.bottomNavMenuResourcesTeacher:
                //TODO
                fragment = new TeacherViewCourseActivityResourcesFragment();
                fragment.setArguments(bundle);
                break;
            case R.id.bottomNavMenuForumTeacher:
                fragment = new TeacherViewCourseActivityForumFragment();
                fragment.setArguments(bundle);
                break;
            case R.id.bottomNavMenuScheduleTeacher:
                fragment = new TeacherViewCourseActivityScheduling();
                fragment.setArguments(bundle);
                break;
            case R.id.assignments_tests:
                fragment = new TeacherViewCourseActivityAssignmentsFragment();
                fragment.setArguments(bundle);
                break;
            default:
                break;
        }

        return loadFragment(fragment);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("Course",course);
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.course_teacher_frame, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.course_stats) {
            return true;
        } else if (itemId == R.id.student_performance_stats) {
            return true;
        } else if (itemId == R.id.payments_history) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("Course",course);
            Fragment fragment = new TeacherViewCourseActivityPaymentsFragment();
            fragment.setArguments(bundle);
            loadFragment(fragment);
            return true;
        } else if (itemId == R.id.course_review) {
            return true;
        } else if (itemId == R.id.notifications) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("Course",course);
            Fragment fragment = new TeacherViewCourseActivityNotificationsFragment();
            fragment.setArguments(bundle);
            loadFragment(fragment);
            return true;
        } else if (itemId == R.id.bottomNavMenuSettingsTeacher) {
            setCheckable(false);
            setChecked(false);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Course",course);
            Fragment fragment = new TeacherViewCourseActivitySettingsFragment();
            fragment.setArguments(bundle);
            loadFragment(fragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            course = (CourseFinal) data.getSerializableExtra("course");
            //do the things u wanted
        }
    }

    public void setClasses(ArrayList<ScheduledClass> classes)
    {
        course.setScheduledClasses(classes);
    }

    public void setCheckable(boolean checkable) {
        final Menu menu = bottomNavigationView.getMenu();
        for(int i = 0; i < menu.size(); i++)
            menu.getItem(i).setCheckable(checkable);
    }

    public void setChecked(boolean checked) {
        final Menu menu = bottomNavigationView.getMenu();
        for(int i = 0; i < menu.size(); i++)
            menu.getItem(i).setChecked(checked);
    }
}
