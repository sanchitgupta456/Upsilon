package com.sanchit.Upsilon;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RegisteredStudentViewCourse extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private FrameLayout fragmentContainer;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_course_main);

        fragmentContainer = (FrameLayout) findViewById(R.id.student_registered_frame);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_active_course);
        getSupportActionBar().setElevation(12);

        loadFragment(new RegisteredStudentViewCourseHomeFragment());

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(RegisteredStudentViewCourse.this);


    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
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
        Fragment fragment = new RegisteredStudentViewCourseHomeFragment();

        switch (item.getItemId()) {
            case R.id.bottomNavMenuHome:
                fragment = new RegisteredStudentViewCourseHomeFragment();
                break;

            case R.id.bottomNavMenuNotifications:
                fragment = new RegisteredStudentViewCourseNotificationFragment();
                break;

            case R.id.bottomNavMenuResources:
                fragment = new RegisteredStudentViewCourseResourcesFragment();
                break;

            case R.id.bottomNavMenuReview:
               // fragment = new Regi();
                break;
        }

        return loadFragment(fragment);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
