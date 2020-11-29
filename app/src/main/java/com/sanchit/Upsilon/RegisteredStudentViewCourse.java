package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sanchit.Upsilon.courseData.Course;

import java.util.List;

public class RegisteredStudentViewCourse extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private FrameLayout fragmentContainer;
    private BottomNavigationView bottomNavigationView;
    private Course course;
    private TextView courseName;
    private ImageButton btnBack;
    View actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_course_main);

        Intent intent = getIntent();
        course = (Course) intent.getSerializableExtra("Course");

        fragmentContainer = (FrameLayout) findViewById(R.id.student_registered_frame);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_active_course);
        getSupportActionBar().setElevation(12);
        actionBar = getSupportActionBar().getCustomView();
        courseName = (TextView) actionBar.findViewById(R.id.textCourseNameActiveCourse);
        btnBack = (ImageButton) actionBar.findViewById(R.id.imgBtnBackActiveCourse);
        courseName.setText(course.getCourseName());

        loadFragment(new RegisteredStudentViewCourseHomeFragment());

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(RegisteredStudentViewCourse.this);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisteredStudentViewCourse.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

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

        Fragment fragment = new RegisteredStudentViewCourseHomeFragment();
        fragment.setArguments(bundle);

        switch (item.getItemId()) {
            case R.id.bottomNavMenuHome:
                fragment = new RegisteredStudentViewCourseHomeFragment();
                fragment.setArguments(bundle);
                break;

            case R.id.bottomNavMenuNotifications:
                fragment = new RegisteredStudentViewCourseNotificationFragment();
                fragment.setArguments(bundle);
                break;

            case R.id.bottomNavMenuResources:
                fragment = new RegisteredStudentViewCourseResourcesFragment();
                fragment.setArguments(bundle);
                break;

            case R.id.bottomNavMenuReview:
                fragment = new RegisteredStudentViewCourseReviewFragment();
                fragment.setArguments(bundle);
                break;
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
        if(item.getItemId()==R.id.menuItemReview)
        {
            loadFragment(new RegisteredStudentViewCourseReviewFragment());
        }
        else if(item.getItemId()==R.id.menuItemSettings)
        {
            //open settings page
        }
        else if (item.getItemId()==R.id.menuItemUnregister)
        {
            //unregister the course if registration happened < 7 days before, else do not
        }
        else if (item.getItemId()==R.id.menuItemRefresh)
        {
            //refresh the layout - meant for the forum page
        }
        return true;
    }
}
