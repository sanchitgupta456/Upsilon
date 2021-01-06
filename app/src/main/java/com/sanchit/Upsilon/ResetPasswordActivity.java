package com.sanchit.Upsilon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sanchit.Upsilon.ui.login.LoginActivity;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText newPassword, confirmPassword;
    Button submit;
    ProgressBar loading;
    String appID = "upsilon-ityvn";
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    String data1;
    String data2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Intent intent = getIntent();
        Uri data = intent.getData();
        data1= data.getQueryParameter("token"); // you will get the value "value1" from application 1
        data2= data.getQueryParameter("tokenId");
        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.new_password_confirm);
        loading = findViewById(R.id.loadingResetPassword);
        newPassword.setSelectAllOnFocus(true);
        confirmPassword.setSelectAllOnFocus(true);
        submit = findViewById(R.id.submit_password);
        //textView.setText(data1+"                  "+data2);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();
        confirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO || i== EditorInfo.IME_ACTION_NEXT) {
                    ConfirmPassword();
                    return true;
                }
                return false;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmPassword();
            }
        });
    }

    public void ConfirmPassword() {
        loading.setVisibility(View.VISIBLE);
        if(confirmPassword.getText().toString().equals(newPassword.getText().toString())){
            app.getEmailPassword().resetPasswordAsync(data1,data2,newPassword.getText().toString(),result -> {
                if(result.isSuccess())
                {
                    Log.v("Reset","Reset Successful");
                    startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                } else {
                    Log.v("Reset",result.getError().toString());
                }
            });
        } else {
            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            confirmPassword.startAnimation(shake);
            confirmPassword.setError("Passwords don't match!");
            confirmPassword.requestFocus();
            loading.setVisibility(View.GONE);
        }
        loading.setVisibility(View.GONE);
    }
}