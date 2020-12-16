package com.sanchit.Upsilon;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TeachActivity extends AppCompatActivity {
    /*
    private RecyclerView recyclerView ;
    private RecyclerView recyclerView2 ;
    private ArrayList<String> mCourseNames = new ArrayList<>();
    private ArrayList<Integer> mImageUrls = new ArrayList<>();
    private ArrayList<String> mStatDatas = new ArrayList<>();
    private ArrayList<String> mStatFields = new ArrayList<>();
    */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Course Name");


        //getCoursesTaught();
    }

    /* testing purpose only : to be removed or altered
    public void getCoursesTaught () {
        mCourseNames.add("JAVA") ;
        mCourseNames.add("JAVA") ;
        mCourseNames.add("JAVA") ;
        mCourseNames.add("JAVA") ;
        mCourseNames.add("JAVA") ;
        mImageUrls.add(R.drawable.background);
        mImageUrls.add(R.drawable.background);
        mImageUrls.add(R.drawable.darklogo);
        mImageUrls.add(R.drawable.darklogo);
        mImageUrls.add(R.drawable.darklogo);

        mStatDatas.add("7");
        mStatFields.add("Courses Taught");
        mStatDatas.add("155");
        mStatFields.add("Hours Taught");
        mStatDatas.add("234");
        mStatFields.add("Students Taught");
        mStatDatas.add("409");
        mStatFields.add("Rating Points");



        initRecyclerViews();
    }

    public void initRecyclerViews () {
        CourseListAdaptorHome adapter = new CourseListAdaptorHome(this, mCourseNames, mImageUrls);
        TeachingStatListAdapter adapter2 = new TeachingStatListAdapter(this, mStatDatas, mStatFields);

        recyclerView = (RecyclerView) findViewById(R.id.listCurrentCoursesTaught);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        recyclerView2 = (RecyclerView) findViewById(R.id.listTeachingStats);
        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        manager2.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView2.setLayoutManager(manager2);
        recyclerView2.setAdapter(adapter2);
    }
    */
}
