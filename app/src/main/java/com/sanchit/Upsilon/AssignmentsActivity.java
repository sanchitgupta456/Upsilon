package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseFinal;
import com.sanchit.Upsilon.ui.assignment.AssignmentAdapter;
import com.sanchit.Upsilon.ui.assignment.AssignmentAdapterCompleted;
import com.sanchit.Upsilon.ui.assignment.AssignmentStudentData;

import java.util.ArrayList;
import java.util.Objects;

public class AssignmentsActivity extends AppCompatActivity {
    RecyclerView pending, completed;
    AssignmentAdapter adapter1;
    AssignmentAdapterCompleted adapter2;
    ArrayList<AssignmentStudentData> listPending = new ArrayList<>();
    ArrayList<AssignmentStudentData> listCompleted = new ArrayList<>();

    AssignmentAdapter.ItemClickListener listener1;
    AssignmentAdapterCompleted.ItemClickListener listener2;

    CourseFinal course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        course = (CourseFinal) getIntent().getSerializableExtra("Course");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(course.getCourseName());

        pending = findViewById(R.id.list_assignments);
        completed = findViewById(R.id.list_assignments_completed);

        adapter1 = new AssignmentAdapter(listPending, getApplicationContext());
        adapter2 = new AssignmentAdapterCompleted(listCompleted, getApplicationContext());


        listener1 = new AssignmentAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //TODO: navigate to particular assignment
                Intent intent = new Intent(getApplicationContext(), AssignmentActivity.class);
                intent.putExtra("Assignment", listPending.get(position));
                startActivity(intent);
            }
        };

        listener2 = new AssignmentAdapterCompleted.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //TODO: navigate to particular assignment
                Intent intent = new Intent(getApplicationContext(), AssignmentActivity.class);
                intent.putExtra("Assignment", listCompleted.get(position));
                startActivity(intent);
            }
        };

        adapter1.setClickListener(listener1);
        adapter2.setClickListener(listener2);

        pending.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
        completed.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));

        pending.setAdapter(adapter1);
        completed.setAdapter(adapter2);

        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();

        getAssignments();
    }

    public void getAssignments() {
        listPending.add(new AssignmentStudentData("Demo"));
        listPending.add(new AssignmentStudentData("Assignment 1"));
        adapter1.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}