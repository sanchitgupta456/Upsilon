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
import com.sanchit.Upsilon.courseData.Course;

import org.bson.types.BasicBSONList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class RegisteredStudentViewCourseScheduleFragment extends Fragment {

    ArrayList<ScheduledClass> classes = new ArrayList<>();
    private Course course;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_active_course_schedule,null);
        course = (Course) getArguments().get("Course");
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
        BasicBSONList scheduledClasses = course.getScheduledClasses();
        if(scheduledClasses==null)
        {
            scheduledClasses=new BasicBSONList();
        }
        for(int i=0;i<scheduledClasses.size();i++)
        {
            try {
                Object scheduled = (Object) scheduledClasses.get(i);
                Log.v("scheduled", String.valueOf(scheduled));
                LinkedHashMap Class = (LinkedHashMap) scheduled;
                classes.add(new ScheduledClass(Class.get("ClassName").toString(), Class.get("Date").toString(), Class.get("Month").toString(), Class.get("Time").toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /* this is for test */
        /* begin test */
        /*Log.d(TAG, "getClasses: getting entries");
        classes.add(new ScheduledClass("Getting Started with Python!", "7", "December", "08:00 AM"));
        classes.add(new ScheduledClass("Knowing Syntax Better!", "8", "December", "09:00 AM"));
        classes.add(new ScheduledClass("Data Types in Python", "9", "December", "08:00 AM"));
        classes.add(new ScheduledClass("Lists in Python", "10", "December", "10:00 AM"));
        classes.add(new ScheduledClass("Functions", "11", "December", "08:00 AM"));
        classes.add(new ScheduledClass("Object Oriented Programming!", "13", "December", "09:00 AM"));*/
        /* end test */
    }
}
