package com.sanchit.Upsilon;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sanchit.Upsilon.courseData.Course;

import java.util.Calendar;
import java.util.Locale;

public class RegisteredStudentViewCourseHomeFragment extends Fragment {

    private TextView nextLecture;
    private Course course;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_active_course_home,null);
        course = (Course) getArguments().get("Course");
        nextLecture = view.findViewById(R.id.textDateTimeNextLecture);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(course.getNextLectureOn()));
        String date = DateFormat.format("dd-MM-yyyy HH:mm:ss", cal).toString();
        nextLecture.setText(date);
        return view;
    }
}
