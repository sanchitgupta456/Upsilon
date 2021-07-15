package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.Upsilon.classData.ScheduleAdapter;
import com.sanchit.Upsilon.classData.ScheduledClass;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseFinal;

import org.bson.types.BasicBSONList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class RegisteredStudentViewCourseScheduleFragment extends Fragment implements ScheduleAdapter.ItemClickListener {

    ArrayList<ScheduledClass> classes = new ArrayList<>();
    private CourseFinal course;
    RecyclerView recyclerView;
    ScheduleAdapter adapter;
    CardView alter;
    Button refresh;
    LinearLayout loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_active_course_schedule,null);
        course = (CourseFinal) getArguments().get("Course");
        TextView nextLecture = view.findViewById(R.id.textDateTimeNextLecture);
        TextView meetLink = view.findViewById(R.id.meetingLink);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//        cal.setTimeInMillis(Long.parseLong(course.getNextLectureOn()));
        String date = DateFormat.format("dd-MM-yyyy HH:mm:ss", cal).toString();
        nextLecture.setText(date);
//        meetLink.setText(course.getMeetLink());
        recyclerView = (RecyclerView) view.findViewById(R.id.classSchedule);
        alter = (CardView) view.findViewById(R.id.alter);
        refresh = (Button) view.findViewById(R.id.btnRefresh);
        loading = (LinearLayout) view.findViewById(R.id.llProgress);
        getClasses();
        adapter = new ScheduleAdapter(getContext(), classes);
        adapter.setClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), manager.getOrientation());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
        loading.setVisibility(View.GONE);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
//                getClasses();
            }
        });
        return view;
    }

    public void getClasses() {
        classes = course.getScheduledClasses();
        //get classes :
//        BasicBSONList scheduledClasses = course.getScheduledClasses();
//        if(scheduledClasses==null)
//        {
//            scheduledClasses=new BasicBSONList();
//        }
//        for(int i=0;i<scheduledClasses.size();i++)
//        {
//            try {
//                Object scheduled = (Object) scheduledClasses.get(i);
//                Log.v("scheduled", String.valueOf(scheduled));
//                LinkedHashMap Class = (LinkedHashMap) scheduled;
//                classes.add(new ScheduledClass(Class.get("ClassName").toString(), Class.get("Date").toString(), Class.get("Month").toString(), Class.get("Time").toString()));
//                adapter.notifyDataSetChanged();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        loading.setVisibility(View.INVISIBLE);
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
        if(classes.size() == 0) {
            alter.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            alter.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        try {
            Intent intent = new Intent(getContext(), ClassActivity.class);
            intent.putExtra("ScheduledClass", classes.get(position));
            requireContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onItemClick: Didn't work");
        }
        
    }
}
