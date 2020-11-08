package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.sanchit.Upsilon.courseData.Course;

import org.w3c.dom.Text;

public class ViewCourseActivity extends AppCompatActivity {

    private TextView CourseName;
    private RatingBar CourseRating;
    private String courseName;
    private View ActionBarView;
    private ImageView BackButton;
    private TextView ActionBarCourseName,CourseDescription,CourseDuration,CourseMode,CourseCost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.layout_custom_action_bar_view_course);
        ActionBarView = getSupportActionBar().getCustomView();
        BackButton = findViewById(R.id.action_bar_back_button);
        ActionBarCourseName = findViewById(R.id.action_bar_course_name);
        CourseName = (TextView) findViewById(R.id.textCourseCoverCard);
        CourseRating = (RatingBar) findViewById(R.id.courseRatings);
        CourseDescription = (TextView) findViewById(R.id.textCourseDescription);
        CourseDuration = (TextView) findViewById(R.id.view_course_duration);
        CourseCost = (TextView) findViewById(R.id.view_course_fees);
        CourseMode = (TextView) findViewById(R.id.view_course_mode);

        Intent intent = getIntent();
        Course course = (Course)intent.getSerializableExtra("Course");

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CourseName.setText(course.getCourseName());
        ActionBarCourseName.setText(course.getCourseName());
        CourseRating.setStepSize(0.01f);
        CourseRating.setRating((float) course.getCourseRating());
        CourseDescription.setText(course.getCourseDescription());
        CourseMode.setText(course.getCourseMode());
        CourseCost.setText(course.getCourseFees());
        CourseDuration.setText(course.getCourseDuration() + course.getCourseDurationMeasure());

        //Toast.makeText(getApplicationContext(), "Course Rating"+(double) course.getCourseRating(),Toast.LENGTH_LONG).show();




    }


}
