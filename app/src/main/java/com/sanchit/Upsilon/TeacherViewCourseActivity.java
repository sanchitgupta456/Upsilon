package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sanchit.Upsilon.courseData.Course;
import com.squareup.picasso.Picasso;

public class TeacherViewCourseActivity extends AppCompatActivity {

    Course course;
    private ImageView courseImage;
    private TextView rating,enrolled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teachers_viewof_course);
        Intent intent = getIntent();
        course = (Course) intent.getSerializableExtra("Course");

        courseImage = (ImageView) findViewById(R.id.imgCourseImage);
        rating = (TextView) findViewById(R.id.teacher_view_course_rating);
        enrolled = (TextView) findViewById(R.id.teacher_view_course_enrolled);

        Picasso.with(getApplicationContext()).load(course.getCourseImage()).into(courseImage);
        rating.setText("Rating "+course.getCourseRating() + "/5");


    }
}
