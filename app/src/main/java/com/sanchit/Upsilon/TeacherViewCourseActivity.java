package com.sanchit.Upsilon;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.ui.login.LoginActivity;
import com.squareup.picasso.Picasso;

import org.bson.Document;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.options.UpdateOptions;

import static android.app.ActionBar.DISPLAY_SHOW_CUSTOM;

public class TeacherViewCourseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    String appID = "upsilon-ityvn";
    Course course;
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
        course = (Course) intent.getSerializableExtra("Course");

        fragmentContainer = (FrameLayout) findViewById(R.id.course_teacher_frame);

        Objects.requireNonNull(this.getSupportActionBar()).setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_teachers_view_of_course);
        getSupportActionBar().setElevation(12);

        courseName = (TextView) findViewById(R.id.courseNameTeachersView);
        imageButtonBack = (ImageButton) findViewById(R.id.imgBtnBackTeachersViewCourse);
        imageButtonUpdate = (ImageButton) findViewById(R.id.imgBtnUpdateTeachersViewCourse);

        loadFragment(new TeacherViewCourseActivityHomeFragment());

        bottomNavigationView = findViewById(R.id.bottom_navigation_teacher);
        bottomNavigationView.setOnNavigationItemSelectedListener(TeacherViewCourseActivity.this);

        courseName.setText(course.getCourseName());
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
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("Course",course);

        Fragment fragment = new TeacherViewCourseActivityHomeFragment();
        fragment.setArguments(bundle);

        switch (item.getItemId()) {
            case R.id.bottomNavMenuHomeTeacher:
                fragment = new TeacherViewCourseActivityHomeFragment();
                fragment.setArguments(bundle);
                break;

            case R.id.bottomNavMenuNotificationsTeacher:
                //fragment = new TeacherViewCourseActivityHomeFragment();
                //fragment.setArguments(bundle);
                break;

            case R.id.bottomNavMenuResources:
                //fragment = new TeacherViewCourseActivityHomeFragment();
                //fragment.setArguments(bundle);
                break;

            case R.id.bottomNavMenuForumTeacher:
                fragment = new TeacherViewCourseActivityForumFragment();
                fragment.setArguments(bundle);
                break;
            case R.id.bottomNavMenuSettingsTeacher:
                //fragment = new TeacherViewCourseActivityHomeFragment();
                //fragment.setArguments(bundle);
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

}
