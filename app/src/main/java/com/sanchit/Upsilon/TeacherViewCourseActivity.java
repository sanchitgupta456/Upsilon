package com.sanchit.Upsilon;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.ui.login.LoginActivity;
import com.squareup.picasso.Picasso;

import org.bson.Document;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.options.UpdateOptions;

import static android.app.ActionBar.DISPLAY_SHOW_CUSTOM;

public class TeacherViewCourseActivity extends AppCompatActivity {

    String appID = "upsilon-ityvn";
    Course course;
    private ImageView courseImage;
    private TextView rating,enrolled,nextClass, courseName;
    private View actionBar;
    ImageButton imageButtonBack, imageButtonUpdate;
    Button ScheduleClass, UpdateLink;
    int year,month,day;
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    View dialogView;
    AlertDialog alertDialog;
    private Gson gson;
    private GsonBuilder gsonBuilder;
    private EditText meetLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teachers_viewof_course);
        Intent intent = getIntent();
        course = (Course) intent.getSerializableExtra("Course");

        dialogView = View.inflate(TeacherViewCourseActivity.this, R.layout.date_time_picker, null);
        TimePicker tp = dialogView.findViewById(R.id.time_picker);
        tp.setIs24HourView(true);

        alertDialog = new AlertDialog.Builder(TeacherViewCourseActivity.this).create();

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();

        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");

        this.getSupportActionBar().setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_teachers_view_of_course);
        getSupportActionBar().setElevation(10);
        actionBar = getSupportActionBar().getCustomView();
        imageButtonBack = (ImageButton)findViewById(R.id.imgBtnBackTeachersViewCourse); //yet to be set up
        imageButtonUpdate = (ImageButton)findViewById(R.id.imgBtnUpdateTeachersViewCourse); //yet to be set up
        courseName = (TextView) findViewById(R.id.courseNameTeachersView);
        courseName.setText(course.getCourseName());
        courseImage = (ImageView) findViewById(R.id.imgCourseImage);
        rating = (TextView) findViewById(R.id.teacher_view_course_rating);
        enrolled = (TextView) findViewById(R.id.teacher_view_course_enrolled);
        ScheduleClass = (Button) findViewById(R.id.btnScheduleClass);
        UpdateLink = (Button) findViewById(R.id.btnUpdateLink); //yet to be set up
        nextClass = (TextView) findViewById(R.id.view_course_teacher_schedule_class);
        meetLink = (EditText) findViewById(R.id.teacher_view_course_meet_link);

        Picasso.with(getApplicationContext()).load(course.getCourseImage()).into(courseImage);
        rating.setText("Rating "+course.getCourseRating() + "/5");
        enrolled.setText(course.getNumberOfStudentsEnrolled() + " students have enrolled in this course");
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(course.getNextLectureOn()));
        String date = DateFormat.format("dd-MM-yyyy HH:mm:ss", cal).toString();
        nextClass.setText(date);
        meetLink.setText(course.getMeetLink());



        ScheduleClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DateDialog();*/
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });
        UpdateLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = meetLink.getText().toString();
                if(link.isEmpty())
                {
                    meetLink.setError("Please Enter a valid meet link");
                    Animation shake = AnimationUtils.loadAnimation(TeacherViewCourseActivity.this, R.anim.shake);
                    meetLink.startAnimation(shake);
                    meetLink.requestFocus();
                }
                else
                {
                    course.setMeetLink(link);
                    gsonBuilder = new GsonBuilder();
                    gson = gsonBuilder.create();
                    String json = gson.toJson(course,Course.class);
                    Document document = Document.parse(json);
                    //document.append("nextLectureOn",String.valueOf(time));

                    mongoCollection.updateOne(new Document("courseId",course.getCourseId()),document).getAsync(result -> {
                        if(result.isSuccess())
                        {
                            Log.v("MeetLink","Meet Link Updated");
                        }
                        else
                        {
                            Log.v("MeetLink","Error"+result.getError().toString());
                        }
                    });

                }
                //update link
            }
        });
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherViewCourseActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
                timePicker.setIs24HourView(true);

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getHour(),
                        timePicker.getMinute());
                long time = calendar.getTimeInMillis();

                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(time);
                String date = DateFormat.format("dd-MM-yyyy HH:mm:ss", cal).toString();
                nextClass.setText(date);
                alertDialog.dismiss();

                UpdateOptions updateOptions = new UpdateOptions();
                updateOptions.upsert(true);

                course.setNextLectureOn(String.valueOf(time));
                gsonBuilder = new GsonBuilder();
                gson = gsonBuilder.create();
                String json = gson.toJson(course,Course.class);
                Document document = Document.parse(json);
                //document.append("nextLectureOn",String.valueOf(time));

                mongoCollection.updateOne(new Document("courseId",course.getCourseId()),document).getAsync(result -> {
                    if(result.isSuccess())
                    {
                        Log.v("Schedule","Class Scheduled");
                    }
                    else
                    {
                        Log.v("Schedule","Error"+result.getError().toString());
                    }
                });
            }});
    }

   /* public void DateDialog(){
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                nextClass.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                //Date date = new Date()
            }};
        DatePickerDialog dpDialog=new DatePickerDialog(TeacherViewCourseActivity.this, listener, year, month, day);
        dpDialog.show();
    }*/
}
