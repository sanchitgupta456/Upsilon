package com.sanchit.Upsilon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CoursesAdapter1;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherInfoActivity extends AppCompatActivity {
    //add more variables as and when required

    CircleImageView imageView;
    TextView name, speciality, qualifications;
    RecyclerView recyclerView;
    ArrayList<Course> list = new ArrayList<>();
    CoursesAdapter1 adapter;
    LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info);
        imageView = (CircleImageView) findViewById(R.id.img_tutor);
        name = (TextView) findViewById(R.id.text_tutor_name);
        speciality = (TextView) findViewById(R.id.text_tutor_speciality);
        qualifications = (TextView) findViewById(R.id.text_tutor_qualifications);
        recyclerView = (RecyclerView) findViewById(R.id.list_courses_by_tutor);
        getTutorData();
    }
    public void getTutorData(){
        //TODO: Get data here and set up the text fields and course array list

        setupRecycler();
    }
    public void setupRecycler(){
        if(list != null){
            adapter = new CoursesAdapter1(list);
            manager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(manager);
        }
    }
}