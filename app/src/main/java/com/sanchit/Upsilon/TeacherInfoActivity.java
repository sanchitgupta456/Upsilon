package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CoursesAdapter1;
import com.sanchit.Upsilon.paymentLog.PaymentLog;

import java.util.ArrayList;
import java.util.Objects;

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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
        //getSupportActionBar().setTitle("View Teacher Details");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_teacher_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        if(item.getItemId() == R.id.notification) {
            startActivity(new Intent(getApplicationContext(), PaymentLogActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}