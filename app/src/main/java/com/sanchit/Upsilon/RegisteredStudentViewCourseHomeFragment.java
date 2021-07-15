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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseFinal;
import com.sanchit.Upsilon.courseData.IntroductoryContentAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RegisteredStudentViewCourseHomeFragment extends Fragment {

    private CourseFinal course;
    private RecyclerView introductoryRecyclerView;
    ArrayList<String> introductoryImages = new ArrayList<String>();
    ArrayList<String> introductoryVideos = new ArrayList<String>();
    IntroductoryContentAdapter courseIntroductoryMaterialAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_active_course_home,null);
        course = (CourseFinal) getArguments().get("Course");
        TextView courseDescription = (TextView) view.findViewById(R.id.textCourseDescription);
        introductoryRecyclerView = (RecyclerView) view.findViewById(R.id.listIntroductoryMaterial);
        courseDescription.setText(course.getCourseDescription());
        getCourseIntroductoryContent();
        return view;
    }

    public void getCourseIntroductoryContent()
    {

        introductoryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));
        introductoryRecyclerView.setItemAnimator(new DefaultItemAnimator());

        introductoryImages = course.getIntroductoryContentImages();
        introductoryVideos = course.getIntroductoryContentVideos();

        courseIntroductoryMaterialAdapter = new IntroductoryContentAdapter(introductoryImages,introductoryVideos);
        if(introductoryVideos == null || introductoryImages == null) {
            introductoryRecyclerView.setVisibility(View.GONE);
        } else if(introductoryImages.size()+introductoryVideos.size()==0) {
            introductoryRecyclerView.setVisibility(View.GONE);
        } else {
            introductoryRecyclerView.setVisibility(View.VISIBLE);
        }
        introductoryRecyclerView.setAdapter(courseIntroductoryMaterialAdapter);
        courseIntroductoryMaterialAdapter.notifyDataSetChanged();
    }
}
