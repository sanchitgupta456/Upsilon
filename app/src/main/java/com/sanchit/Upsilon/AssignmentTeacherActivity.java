package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.sanchit.Upsilon.ui.assignment.AssignmentStudentData;
import com.sanchit.Upsilon.ui.assignment.AssignmentTeacherData;

import java.util.Objects;

public class AssignmentTeacherActivity extends AppCompatActivity {

    private static final String TAG = "Assignment";

    AssignmentTeacherData assignmentTeacherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_teacher);

        assignmentTeacherData = (AssignmentTeacherData) getIntent().getSerializableExtra("Assignment");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Assignment #");

//        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
//        ViewPager viewPager = findViewById(R.id.view_pager);
//        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
//        tabs.setupWithViewPager(viewPager);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Assignment",assignmentTeacherData);
        Fragment fragment = new AssignmentContentTeacherFragment();
        fragment.setArguments(bundle);
        loadFragment(fragment);
        getSupportActionBar().setTitle(assignmentTeacherData.getName());

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Assignment",assignmentTeacherData);

                Fragment fragment = new AssignmentContentTeacherFragment();
                fragment.setArguments(bundle);

                switch (tab.getPosition()) {
                    case 0:
                        fragment = new AssignmentContentTeacherFragment();
                        fragment.setArguments(bundle);
                        break;
                    case 1:
                        fragment = new AssignmentSubmissionsTeacherFragment();
                        fragment.setArguments(bundle);
                        break;
                    case 2:
                        fragment = new AssignmentEvaluateTeacherFragment();
                        fragment.setArguments(bundle);
                        break;
                }
                loadFragment(fragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        FloatingActionButton fab = findViewById(R.id.fab);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Add a submission, currently unavailable", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private String TestPDF() {
//        return "data:application/octet-stream;base64,n54eED8Eo4ElPWzdYiuQEIB/HN97OARUv7YiRMk3QoaO4YAr/ql7DI+lkXlfxRwHMKXQG4nAF9WuR7/yPObTpD1YYlY7rYuyXHpJLQOfUGXHzaarRRR6y9i0HanFzp84fKUrig==";

        Log.d(TAG, "TestPDF: ");
        return getResources().getString(R.string.testPdfSource);
    }

    public void makeSubmission() {
        //TODO: file upload implementation here

        //TODO: navigate to assignment submission panel
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("Assignment",assignmentTeacherData);
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}