package com.sanchit.Upsilon.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.sanchit.Upsilon.R;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        String appID = "upsilon-ityvn";
        App app = new App(new AppConfiguration.Builder(appID)
                .build());

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText cnfPasswordEditText = findViewById(R.id.confirmPassword);
        final EditText emailEditText = findViewById(R.id.email);
        final Button signUpButton = findViewById(R.id.signupBtn);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                app.getEmailPassword().registerUserAsync(email, password, it -> {
                    if (it.isSuccess()) {
                        Log.i(TAG,"Successfully registered user.");
                    } else {
                        Log.e(TAG,"Failed to register user: ${it.error}");
                    }
                });
            }
        });
    }
}