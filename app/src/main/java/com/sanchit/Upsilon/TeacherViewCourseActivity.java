package com.sanchit.Upsilon;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sanchit.Upsilon.courseData.Course;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class TeacherViewCourseActivity extends AppCompatActivity {

    Course course;
    private ImageView courseImage;
    private TextView rating,enrolled,nextClass;
    Button ScheduleClass;
    int year,month,day;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teachers_viewof_course);
        Intent intent = getIntent();
        course = (Course) intent.getSerializableExtra("Course");

        courseImage = (ImageView) findViewById(R.id.imgCourseImage);
        rating = (TextView) findViewById(R.id.teacher_view_course_rating);
        enrolled = (TextView) findViewById(R.id.teacher_view_course_enrolled);
        ScheduleClass = (Button) findViewById(R.id.btnScheduleClass);
        nextClass = (TextView) findViewById(R.id.view_course_teacher_schedule_class);


        Picasso.with(getApplicationContext()).load(course.getCourseImage()).into(courseImage);
        rating.setText("Rating "+course.getCourseRating() + "/5");
        enrolled.setText(course.getNumberOfStudentsEnrolled() + "Students Have Enrolled in this Course");

        ScheduleClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DateDialog();
            }
        });
    }

    public void DateDialog(){
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                nextClass.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
            }};
        DatePickerDialog dpDialog=new DatePickerDialog(TeacherViewCourseActivity.this, listener, year, month, day);
        dpDialog.show();
    }
}
