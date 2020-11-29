package com.sanchit.Upsilon;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.courseData.Course;
import com.squareup.picasso.Picasso;

import org.bson.Document;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.options.UpdateOptions;

import static io.realm.Realm.getApplicationContext;

public class TeacherViewCourseActivityHomeFragment extends Fragment {
    String appID = "upsilon-ityvn";
    int year,month,day;
    private TextView rating,enrolled,nextClass, courseName;
    private ImageView courseImage;
    Button ScheduleClass, UpdateLink;
    private EditText meetLink;
    private Course course;
    View dialogView;
    AlertDialog alertDialog;
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    private Gson gson;
    private GsonBuilder gsonBuilder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teachers_viewof_course_home,null);
        course = (Course) getArguments().get("Course");
        dialogView = View.inflate(getApplicationContext(), R.layout.date_time_picker, null);
        TimePicker tp = dialogView.findViewById(R.id.time_picker);
        tp.setIs24HourView(true);

        alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getApplicationContext())).create();

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();

        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");
        courseImage = (ImageView) view.findViewById(R.id.imgCourseImage);
        rating = (TextView) view.findViewById(R.id.teacher_view_course_rating);
        enrolled = (TextView) view.findViewById(R.id.teacher_view_course_enrolled);
        ScheduleClass = (Button) view.findViewById(R.id.btnScheduleClass);
        UpdateLink = (Button) view.findViewById(R.id.btnUpdateLink); //yet to be set up
        nextClass = (TextView) view.findViewById(R.id.view_course_teacher_schedule_class);
        meetLink = (EditText) view.findViewById(R.id.teacher_view_course_meet_link);

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
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
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
                //nextClass.setText(date);
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
        return view;
    }
    public void DateDialog(){
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                nextClass.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                //Date date = new Date()
            }};
        DatePickerDialog dpDialog=new DatePickerDialog(getApplicationContext(), listener, year, month, day);
        dpDialog.show();
    }
}



