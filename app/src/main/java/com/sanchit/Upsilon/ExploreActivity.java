package com.sanchit.Upsilon;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.sanchit.Upsilon.courseData.Course;
import java.util.ArrayList;

public class ExploreActivity extends AppCompatActivity {
    //vars
    private RecyclerView recyclerView;
    ArrayList<Course> courseArrayList;
    String appID = "upsilon-ityvn";
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_courses);
        recyclerView.findViewById(R.id.exploreList);
    }
}
