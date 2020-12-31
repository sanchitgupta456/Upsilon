package com.sanchit.Upsilon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.sanchit.Upsilon.ui.login.LoginActivity;

import org.bson.Document;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class AddCoursePayment extends AppCompatActivity {

    EditText accountNumber,ifscCode,mobileNumber,upiId;
    Button submit;
    String AccountNumber,IfscCode,MobileNumber,UpiId;
    String appID = "upsilon-ityvn";
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_payment);
        accountNumber = findViewById(R.id.account_number);
        ifscCode = findViewById(R.id.ifsc_code);
        mobileNumber = findViewById(R.id.mobile_number);
        upiId = findViewById(R.id.upi_code);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();

        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("TeacherPaymentData");

        submit = findViewById(R.id.btnSubmitPaymentInfo);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountNumber = accountNumber.getText().toString();
                IfscCode = ifscCode.getText().toString();
                MobileNumber = mobileNumber.getText().toString();
                UpiId = upiId.getText().toString();

                if(UpiId.isEmpty())
                {
                    if(AccountNumber.isEmpty())
                    {
                        Animation shake = AnimationUtils.loadAnimation(AddCoursePayment.this, R.anim.shake);
                        accountNumber.startAnimation(shake);
                        accountNumber.setError("Please Enter either bank details or UPI Id");
                        accountNumber.requestFocus();
                        upiId.startAnimation(shake);
                        upiId.setError("Please Enter either bank details or UPI Id");
                        upiId.requestFocus();
                    }
                    else
                    {
                        mongoCollection.insertOne(new Document("userid", user.getId()).append("accountNumber", AccountNumber).append("ifscCode", IfscCode).append("mobileNumber", MobileNumber).append("UpiId", UpiId)).getAsync(result -> {
                            if (result.isSuccess()) {
                                startActivity(new Intent(AddCoursePayment.this, MainActivity.class));
                            } else {

                            }
                        });
                    }
                }
                else {
                    mongoCollection.insertOne(new Document("userid", user.getId()).append("accountNumber", AccountNumber).append("ifscCode", IfscCode).append("mobileNumber", MobileNumber).append("UpiId", UpiId)).getAsync(result -> {
                        if (result.isSuccess()) {
                            startActivity(new Intent(AddCoursePayment.this, MainActivity.class));
                        } else {

                        }
                    });
                }
            }
        });
    }
}