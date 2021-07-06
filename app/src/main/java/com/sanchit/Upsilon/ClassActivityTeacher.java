package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sanchit.Upsilon.classData.ScheduledClass;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.IntroductoryContentAdapter;
import com.sanchit.Upsilon.courseData.VideoResourceAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class ClassActivityTeacher extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ClassActivityTeacher";
    private Course course;
    private ScheduledClass scheduledClass;
    private TextInputLayout dateLayout, startTimeLayout, endTimeLayout;
    private TextInputEditText date;
    private TextInputEditText start_time;
    private TextInputEditText end_time;
//    private CardView alter, alter2;
//    private LinearLayout progressBarAlter;
//    private Button mark, markTrue;
    private RecyclerView recyclerView;

    private FloatingActionButton addVideo;
    private Button updateChange;

    View dialogView;
    private SimpleDateFormat dateFormatter;

    private ArrayList<String> videos = new ArrayList<>();
    private int startMonth;
    private int startYear;
    private int startDay;
    DatePickerDialog datePickerDialog;
    TimePickerDialog startTimePickerDialog, endTimePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_teacher);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        Intent intent = getIntent();

        date = (TextInputEditText) findViewById(R.id.etDate);
        dateLayout = (TextInputLayout) findViewById(R.id.ll1);
        startTimeLayout = (TextInputLayout) findViewById(R.id.ll2);
        endTimeLayout = (TextInputLayout) findViewById(R.id.ll3);
        start_time = (TextInputEditText) findViewById(R.id.etStartTime);
        end_time = (TextInputEditText) findViewById(R.id.etEndTime);
        addVideo = (FloatingActionButton) findViewById(R.id.btnAdd);
        updateChange = (Button) findViewById(R.id.updateChange);
        recyclerView = (RecyclerView) findViewById(R.id.video_resources);

        setDateTimeField();

//        date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar calendar = Calendar.getInstance();
//                startDay = calendar.get(Calendar.DAY_OF_MONTH);
//                startMonth = calendar.get(Calendar.MONTH);
//                startYear = calendar.get(Calendar.YEAR);
//                datePickerDialog = new DatePickerDialog(
//                        getApplicationContext(), ClassActivityTeacher.this, startYear, startMonth, startDay);
//                datePickerDialog.show();
//            }
//        });
        dateLayout.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        startTimeLayout.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimePickerDialog.show();
            }
        });
        endTimeLayout.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTimePickerDialog.show();
            }
        });

        //get the scheduled class in scheduledClass
        scheduledClass = (ScheduledClass) intent.getExtras().get("ScheduledClass");
        if(scheduledClass == null) {
            Log.d(TAG, "onCreate: scheduled class is null");
            return;
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(scheduledClass.getClassName());
        String dateString = scheduledClass.getMonth() + " " + scheduledClass.getDay();
        date.setText(dateString);
        start_time.setText(scheduledClass.getTime());
        end_time.setText(scheduledClass.getEndTime());

        addVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: upload a video
            }
        });

        updateChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: update class information of date, start and end times
            }
        });

        VideoResourceAdapter adapter = new VideoResourceAdapter(videos);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        getVideos();
        adapter.notifyDataSetChanged();
    }

    public void getVideos() {
        //TODO: fetch videos
    }

    private void setDateTimeField() {
        dateLayout.setOnClickListener(this);
        date.setOnClickListener(this);
        dateLayout.setStartIconOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(dateFormatter.format(newDate.getTime()));
                //TODO: update in backend
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        startTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                StringBuilder timeString = new StringBuilder();
                timeString.append(hourOfDay).append(":");
                if(minute<10) timeString.append("0");
                timeString.append(minute);
                start_time.setText(timeString.toString());
                //TODO: update in backend
            }
        },newCalendar.get(Calendar.HOUR), newCalendar.get(Calendar.MINUTE), true);

        endTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                StringBuilder timeString = new StringBuilder();
                timeString.append(hourOfDay).append(":");
                if(minute<10) timeString.append("0");
                timeString.append(minute);
                end_time.setText(timeString.toString());
                //TODO: update in backend
            }
        },newCalendar.get(Calendar.HOUR), newCalendar.get(Calendar.MINUTE), true);

    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: clicked");
//        if(v == dateLayout) datePickerDialog.show();
//        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}