package com.sanchit.Upsilon;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.Upsilon.classData.ScheduleAdapter;
import com.sanchit.Upsilon.classData.ScheduledClass;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class RegisteredStudentViewCourseScheduleFragment extends Fragment {

    ArrayList<ScheduledClass> classes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_active_course_schedule,null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.classSchedule);
        getClasses();
        ScheduleAdapter adapter = new ScheduleAdapter(getContext(), classes);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(Objects.requireNonNull(getContext()), manager.getOrientation());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }

    public void getClasses() {
        //get classes :

        /* this is for test */
        /* begin test */
        Log.d(TAG, "getClasses: getting entries");
        classes.add(new ScheduledClass("Getting Started with Python!", "7", "December", "08:00 AM"));
        classes.add(new ScheduledClass("Knowing Syntax Better!", "8", "December", "09:00 AM"));
        classes.add(new ScheduledClass("Data Types in Python", "9", "December", "08:00 AM"));
        classes.add(new ScheduledClass("Lists in Python", "10", "December", "10:00 AM"));
        classes.add(new ScheduledClass("Functions", "11", "December", "08:00 AM"));
        classes.add(new ScheduledClass("Object Oriented Programming!", "13", "December", "09:00 AM"));
        /* end test */
    }
}
