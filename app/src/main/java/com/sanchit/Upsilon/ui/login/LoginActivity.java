package com.sanchit.Upsilon.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sanchit.Upsilon.R;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = (Button)findViewById(R.id.login);
        final Button signUpButton = (Button)findViewById(R.id.signUp);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final ImageView GoogleSignIn = findViewById(R.id.googleSignIn);
        //Realm realm = Realm.getDefaultInstance();
        //realm.close();
        Realm.init(this); // context, usually an Activity or Application

        String appID = "upsilon-ityvn";
        App app = new App(new AppConfiguration.Builder(appID)
                .build());

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                goToSignInActivity();
            }
        });

        GoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Credentials googleCredentials = Credentials.google("<token>");

                app.loginAsync(googleCredentials, it -> {
                    if (it.isSuccess()) {
                        Log.v(TAG, "Successfully authenticated using Google OAuth.");
                        User user = app.currentUser();
                    } else {
                        Log.e(TAG, it.getError().toString());
                    }
                });
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String email = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                Credentials emailPasswordCredentials = Credentials.emailPassword(email, password);

                app.loginAsync(emailPasswordCredentials, new App.Callback<User>() {
                    @Override
                    public void onResult(App.Result<User> it) {
                        if (it.isSuccess()) {
                            Log.v(TAG, "Successfully authenticated using an email and password.");
                            User user = app.currentUser();
                        } else {
                            Log.v(TAG, "LOGIN FAILED!");
                            Log.e(TAG, it.getError().toString());
                        }
                    }
                });
            }
        });
    }

    public void goToSignInActivity(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}