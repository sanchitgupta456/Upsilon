package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sanchit.Upsilon.courseData.Course;

public class ViewCourseActivity extends AppCompatActivity {

    private TextView CourseName;
    private RatingBar CourseRating;
    private String courseName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);
        Intent intent = getIntent();
        Course course = (Course)intent.getSerializableExtra("Course");

        CourseName = (TextView) findViewById(R.id.textCourseName);
        CourseRating = (RatingBar) findViewById(R.id.courseRatings);

        CourseName.setText(course.getCourseName());
        CourseRating.setStepSize(0.01f);
        CourseRating.setRating((float) course.getCourseRating());
        Toast.makeText(getApplicationContext(), "Course Rating"+(double) course.getCourseRating(),Toast.LENGTH_LONG).show();




    }
}
