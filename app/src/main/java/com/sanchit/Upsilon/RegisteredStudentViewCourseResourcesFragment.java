package com.sanchit.Upsilon;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.IntroductoryContentAdapter;

import java.util.ArrayList;

public class RegisteredStudentViewCourseResourcesFragment extends Fragment {

    private RecyclerView courseResources;
    private IntroductoryContentAdapter resourcesAdapter;
    ArrayList<String> introductoryImages = new ArrayList<String>();
    ArrayList<String> introductoryVideos = new ArrayList<String>();
    Course course;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_active_course_resources,null);
        courseResources = view.findViewById(R.id.listResources);
        //resourcesAdapter = new IntroductoryContentAdapter(introductoryImages,introductoryVideos);
        course = (Course) getArguments().get("Course");
        Log.v("CourseResources",course.getCourseId());
        introductoryImages = course.getIntroductoryContentImages();
        introductoryVideos = course.getIntroductoryContentVideos();

        //course = getArguments().getBundle().get("Course");

        initialise();
    //resourcesAdapter.notifyDataSetChanged();
        return view;
    }

    private void initialise()
    {
        courseResources.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        courseResources.setItemAnimator(new DefaultItemAnimator());

        introductoryImages = course.getIntroductoryContentImages();
        introductoryVideos = course.getIntroductoryContentVideos();
        courseResources.setAdapter(resourcesAdapter);
        resourcesAdapter = new IntroductoryContentAdapter(introductoryImages,introductoryVideos);
        courseResources.setAdapter(resourcesAdapter);

        introductoryImages = course.getIntroductoryContentImages();
        introductoryVideos = course.getIntroductoryContentVideos();
        //Log.v("Course", String.valueOf(introductoryImages));

        //introductoryImages.add("http://res.cloudinary.com/upsilon175/image/upload/v1605196689/Upsilon/Courses/5fad2ca3600686e14bc0950b/IntroductoryContent/Images/IntroductoryImage0.jpg");
        //Log.v("introductoryImages", String.valueOf(introductoryImages));
        resourcesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialise();
    }

    @Override
    public void onStart() {
        super.onStart();
        initialise();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (introductoryImages != null) {
            introductoryImages.clear();
        }
        if (introductoryVideos != null) {
            introductoryVideos.clear();
        }
        resourcesAdapter.notifyDataSetChanged();
        releaseExoPlayer(resourcesAdapter.exoPlayer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (introductoryImages != null) {
            introductoryImages.clear();
        }
        if (introductoryVideos != null) {
            introductoryVideos.clear();
        }
        resourcesAdapter.notifyDataSetChanged();
        releaseExoPlayer(resourcesAdapter.exoPlayer);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (introductoryImages != null) {
            introductoryImages.clear();
        }
        if (introductoryVideos != null) {
            introductoryVideos.clear();
        }
        resourcesAdapter.notifyDataSetChanged();
        releaseExoPlayer(resourcesAdapter.exoPlayer);

    }

    public static void releaseExoPlayer(SimpleExoPlayer exoPlayer) {

        if (exoPlayer != null) {
            exoPlayer.release();
        }

    }
}
