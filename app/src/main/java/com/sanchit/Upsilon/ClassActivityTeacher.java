package com.sanchit.Upsilon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.sanchit.Upsilon.classData.ScheduledClass;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.IntroductoryContentAdapter;
import com.sanchit.Upsilon.courseData.VideoResourceAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class ClassActivityTeacher extends AppCompatActivity {

    private static final String TAG = "ClassActivityTeacher";
    private Course course;
    private ScheduledClass scheduledClass;
    private TextInputEditText date;
    private TextInputEditText start_time;
    private TextInputEditText end_time;
//    private CardView alter, alter2;
//    private LinearLayout progressBarAlter;
//    private Button mark, markTrue;
    private RecyclerView recyclerView;

    private Button addVideo;
    private Button updateChange;

    private ArrayList<String> videos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_teacher);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        date = (TextInputEditText) findViewById(R.id.etDate);
        start_time = (TextInputEditText) findViewById(R.id.etStartTime);
        end_time = (TextInputEditText) findViewById(R.id.etEndTime);
        addVideo = (Button) findViewById(R.id.btnAdd);
        updateChange = (Button) findViewById(R.id.updateChange);
        recyclerView = (RecyclerView) findViewById(R.id.video_resources);

        //get the scheduled class in scheduledClass
        scheduledClass = (ScheduledClass) intent.getExtras().get("ScheduledClass");
        if(scheduledClass == null) {
            Log.d(TAG, "onCreate: scheduled class is null");
            return;
        }
        getSupportActionBar().setTitle(scheduledClass.getClassName());
        date.setText(scheduledClass.getMonth() + " " + scheduledClass.getDay());
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
}