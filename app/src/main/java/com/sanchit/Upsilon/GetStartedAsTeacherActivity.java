package com.sanchit.Upsilon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GetStartedAsTeacherActivity extends AppCompatActivity {
    Button getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started_as_teacher);
        getStarted = (Button) findViewById(R.id.btnGetStarted);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TeacherDataSetupActivity.class));
            }
        });
    }
}