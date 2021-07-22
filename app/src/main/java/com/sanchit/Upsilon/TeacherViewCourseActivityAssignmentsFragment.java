package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseFinal;
import com.sanchit.Upsilon.ui.assignment.AssignmentAdapter;
import com.sanchit.Upsilon.ui.assignment.AssignmentAdapterCompleted;
import com.sanchit.Upsilon.ui.assignment.AssignmentStudentData;
import com.sanchit.Upsilon.ui.assignment.AssignmentTeacherAdapter;
import com.sanchit.Upsilon.ui.assignment.AssignmentTeacherData;

import java.util.ArrayList;
import java.util.Objects;

public class TeacherViewCourseActivityAssignmentsFragment extends Fragment {

    RecyclerView pending, completed;
    AssignmentTeacherAdapter adapter1;
//    AssignmentAdapterCompleted adapter2;
    ArrayList<AssignmentTeacherData> listPending = new ArrayList<>();
//    ArrayList<AssignmentTeacherData> listCompleted = new ArrayList<>();

    AssignmentTeacherAdapter.ItemClickListener listener1;
//    AssignmentAdapterCompleted.ItemClickListener listener2;

    FloatingActionButton newAssignment;

    CourseFinal course;

    TeacherViewCourseActivityAssignmentsFragment() {
        //empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_assignments_teacher);

        View view = inflater.inflate(R.layout.activity_assignments_teacher, null, false);

        assert getArguments() != null;
        course = (CourseFinal) getArguments().get("Course");

//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(course.getCourseName());

        pending = view.findViewById(R.id.list_assignments);
        completed = view.findViewById(R.id.list_assignments_completed);
        newAssignment = view.findViewById(R.id.new_assignment);

        newAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: create new blank assignment
            }
        });

        adapter1 = new AssignmentTeacherAdapter(listPending, getContext());
//        adapter2 = new AssignmentAdapterCompleted(listCompleted, getContext());


        listener1 = new AssignmentTeacherAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //TODO: navigate to particular assignment
                Intent intent = new Intent(getContext(), AssignmentTeacherActivity.class);
                intent.putExtra("Assignment", listPending.get(position));
                startActivity(intent);
            }
        };

//        listener2 = new AssignmentAdapterCompleted.ItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                //TODO: navigate to particular assignment
//                Intent intent = new Intent(getContext(), AssignmentTeacherActivity.class);
//                intent.putExtra("Assignment", listCompleted.get(position));
//                startActivity(intent);
//            }
//        };

        adapter1.setClickListener(listener1);
//        adapter2.setClickListener(listener2);

        pending.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true));
        completed.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true));

        pending.setAdapter(adapter1);
//        completed.setAdapter(adapter2);

        adapter1.notifyDataSetChanged();
//        adapter2.notifyDataSetChanged();

        getAssignments();

        return view;
    }

    public void getAssignments() {
        listPending.add(new AssignmentTeacherData("Demo"));
        listPending.add(new AssignmentTeacherData("Assignment 1"));
        adapter1.notifyDataSetChanged();
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId()==android.R.id.home) {
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}