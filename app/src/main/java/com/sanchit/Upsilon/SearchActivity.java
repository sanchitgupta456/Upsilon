package com.sanchit.Upsilon;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";

    private ArrayList<String> courseNames = new ArrayList<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_courses);
        Log.d(TAG, "onCreate: started.");
        initCourseNames();


    }

    private void initCourseNames () {
        Log.d(TAG, "initCourseNames: added names.");
        courseNames.add("T1") ;
        courseNames.add("T2") ;
        courseNames.add("T3") ;
        courseNames.add("T4") ;
        courseNames.add("T5") ;

        initRecyclerView();
    }

    private void initRecyclerView () {
        RecyclerView recyclerView = findViewById(R.id.courseList) ;
        CourseListAdapter adapter = new CourseListAdapter(this, courseNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
