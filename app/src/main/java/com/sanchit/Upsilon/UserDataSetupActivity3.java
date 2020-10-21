package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserDataSetupActivity3 extends AppCompatActivity {

    Button nextButton;
    EditText City,Pincode,PhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_3);

        nextButton = (Button) findViewById(R.id.selectLaterNext3);
        City = (EditText) findViewById(R.id.cityNameHolder);
        Pincode = (EditText) findViewById(R.id.pincodeHolder);
        PhoneNumber = (EditText) findViewById(R.id.contactNumberHolder);

        TextWatcher textWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                nextButton.setText("Next");
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                nextButton.setText("Select Later");
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                nextButton.setText("Next");
            }
        };

        City.addTextChangedListener(textWatcher);
        PhoneNumber.addTextChangedListener(textWatcher);
        Pincode.addTextChangedListener(textWatcher);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDataSetupActivity3.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}