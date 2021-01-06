package com.sanchit.Upsilon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sanchit.Upsilon.ui.login.LoginActivity;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;

public class ResetPasswordActivity extends AppCompatActivity {

    TextView textView;
    EditText newPassword;
    Button submit;
    String appID = "upsilon-ityvn";
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Intent intent = getIntent();
        Uri data = intent.getData();
        String data1= data.getQueryParameter("token"); // you will get the value "value1" from application 1
        String data2= data.getQueryParameter("tokenId");
        textView = findViewById(R.id.reset_password);
        newPassword = findViewById(R.id.new_password);
        submit = findViewById(R.id.submit_password);
        //textView.setText(data1+"                  "+data2);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.getEmailPassword().resetPasswordAsync(data1,data2,newPassword.getText().toString(),result -> {
                    if(result.isSuccess())
                    {
                        Log.v("Reset","Reset Successful");
                        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                    } else {
                        Log.v("Reset",result.getError().toString());
                    }
                });
            }
        });
    }
}