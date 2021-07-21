package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanchit.Upsilon.classData.ScheduledClass;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.ResourceAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class ClassActivity extends AppCompatActivity {
    private static final String TAG = "ClassActivity";
    private Course course;
    private ScheduledClass scheduledClass;
    private TextView date;
    private TextView start_time;
    private TextView end_time;
    private CardView alter, alter2;
    private LinearLayout progressBarAlter;
    private Button mark, markTrue;
    private RecyclerView recyclerView;

//    private ArrayList<String> images;
    private ArrayList<String> videos = new ArrayList<>();
    private ArrayList<String> docs = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        date = (TextView) findViewById(R.id.date);
        start_time = (TextView) findViewById(R.id.start_time);
        end_time = (TextView) findViewById(R.id.end_time);
        alter = (CardView) findViewById(R.id.alter);
        alter2 = (CardView) findViewById(R.id.alter2);
        progressBarAlter = (LinearLayout) findViewById(R.id.llProgress);
        mark = (Button) findViewById(R.id.btnMark);
        markTrue = (Button) findViewById(R.id.btnMarkTrue);
        recyclerView = (RecyclerView) findViewById(R.id.video_resources);

        //get the scheduled class in scheduledClass
        scheduledClass = (ScheduledClass) intent.getExtras().get("ScheduledClass");
        if(scheduledClass == null) {
            Log.d(TAG, "onCreate: scheduled class is null");
            return;
        }
        getSupportActionBar().setTitle(scheduledClass.getClassName());
        date.setText(scheduledClass.getMonth() + " " + scheduledClass.getDate());
        start_time.setText(scheduledClass.getTime());
        end_time.setText(scheduledClass.getEndtime());

        //attendance handling

        //test code
        alter2.setVisibility(View.GONE);
        alter.setVisibility(View.VISIBLE);
        progressBarAlter.setVisibility(View.GONE);
        markTrue.setVisibility(View.GONE);
        mark.setVisibility(View.VISIBLE);
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mark.setVisibility(View.GONE);
                progressBarAlter.setVisibility(View.VISIBLE);
                new CountDownTimer(5000, 1000) {
                    public void onFinish() {
                        progressBarAlter.setVisibility(View.GONE);
                        markTrue.setVisibility(View.VISIBLE);
                    }
                    public void onTick(long millisUntilFinished) {
                        // millisUntilFinished    The amount of time until finished.
                    }
                }.start();
                //TODO: do something
            }
        });
        //end of test code

        ResourceAdapter adapter = new ResourceAdapter(videos, docs, images);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        getData();
        adapter.notifyDataSetChanged();
    }

    public void getData() {
        //TODO: fetch videos, docs and images
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}