package com.sanchit.Upsilon;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AddCourseActivity extends AppCompatActivity {
    private static final String TAG = "AddCourseActivity";
    
    private RecyclerView recyclerView ;
    /*
    private ArrayList<String> mCourseNames = new ArrayList<>();
    private ArrayList<Integer> mImageUrls = new ArrayList<>();
    */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        //getCoursesTaught();
    }
    //for testing only. To be removed later.
    /*
    public void getCoursesTaught () {
        mCourseNames.add("JAVA") ;
        mCourseNames.add("JAVA") ;
        mCourseNames.add("JAVA") ;
        mCourseNames.add("JAVA") ;
        mCourseNames.add("JAVA") ;
        mImageUrls.add(R.drawable.darklogo);
        mImageUrls.add(R.drawable.darklogo);
        mImageUrls.add(R.drawable.darklogo);
        mImageUrls.add(R.drawable.darklogo);
        mImageUrls.add(R.drawable.darklogo);

        initRecyclerView();
    }

    public void initRecyclerView () {
        CourseListAdaptorHome adapter = new CourseListAdaptorHome(this, mCourseNames, mImageUrls);
        recyclerView = (RecyclerView) findViewById(R.id.coursesTaught);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }*/
}
