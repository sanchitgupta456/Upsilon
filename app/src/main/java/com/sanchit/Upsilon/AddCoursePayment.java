package com.sanchit.Upsilon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class AddCoursePayment extends AppCompatActivity {

    EditText accountNumber,ifscCode,mobileNumber,upiId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_payment);
    }
}