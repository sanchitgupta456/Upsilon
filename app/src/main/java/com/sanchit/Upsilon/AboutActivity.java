package com.sanchit.Upsilon;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        getSupportActionBar().hide();
    }
}


