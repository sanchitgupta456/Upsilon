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

import com.google.android.material.textfield.TextInputEditText;
import com.sanchit.Upsilon.ui.login.LoginActivity;

import org.bson.Document;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class AddCoursePayment extends AppCompatActivity {

    TextInputEditText accountNumber,ifscCode,mobileNumber,upiId;
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
        Document queryFilter  = new Document("userid",user.getId());
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

        submit = findViewById(R.id.btnSubmitPaymentInfo);

        findTask.getAsync(result -> {
            if(result.isSuccess())
            {
                MongoCursor<Document> results = result.get();
                if(results.hasNext())
                {
                    Document payment = results.next();
                    accountNumber.setText(payment.getString("accountNumber"));
                    ifscCode.setText(payment.getString("ifscCode"));
                    mobileNumber.setText(payment.getString("mobileNumber"));
                    upiId.setText(payment.getString("UpiId"));
                }
            }
            else
            {

            }
        });

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
                        findTask.getAsync(result -> {
                            if(result.isSuccess())
                            {
                                MongoCursor<Document> results = result.get();
                                if(results.hasNext())
                                {
                                    Document payment = results.next();
                                    accountNumber.setText(payment.getString("accountNumber"));
                                    ifscCode.setText(payment.getString("ifscCode"));
                                    mobileNumber.setText(payment.getString("mobileNumber"));
                                    upiId.setText(payment.getString("UpiId"));
                                    payment.append("accountNumber",AccountNumber);
                                    payment.append("ifscCode",IfscCode);
                                    payment.append("mobileNumber",MobileNumber);
                                    payment.append("UpiId",UpiId);

                                    mongoCollection.updateOne(new Document("userid",user.getId()),payment).getAsync(result1 -> {
                                        if(result1.isSuccess())
                                        {
                                            startActivity(new Intent(AddCoursePayment.this, MainActivity.class));

                                        }
                                        else
                                        {

                                        }
                                    });
                                }
                                else
                                {
                                    mongoCollection.insertOne(new Document("userid", user.getId()).append("accountNumber", AccountNumber).append("ifscCode", IfscCode).append("mobileNumber", MobileNumber).append("UpiId", UpiId)).getAsync(result1 -> {
                                        if (result1.isSuccess()) {
                                            startActivity(new Intent(AddCoursePayment.this, MainActivity.class));
                                        } else {

                                        }
                                    });
                                }
                            }
                            else
                            {

                            }
                        });

                    }
                }
                else {
                    findTask.getAsync(result -> {
                        if(result.isSuccess())
                        {
                            MongoCursor<Document> results = result.get();
                            if(results.hasNext())
                            {
                                Document payment = results.next();
                                accountNumber.setText(payment.getString("accountNumber"));
                                ifscCode.setText(payment.getString("ifscCode"));
                                mobileNumber.setText(payment.getString("mobileNumber"));
                                upiId.setText(payment.getString("UpiId"));
                                payment.append("accountNumber",AccountNumber);
                                payment.append("ifscCode",IfscCode);
                                payment.append("mobileNumber",MobileNumber);
                                payment.append("UpiId",UpiId);

                                mongoCollection.updateOne(new Document("userid",user.getId()),payment).getAsync(result1 -> {
                                    if(result1.isSuccess())
                                    {
                                        startActivity(new Intent(AddCoursePayment.this, MainActivity.class));

                                    }
                                    else
                                    {

                                    }
                                });
                            }
                            else
                            {
                                mongoCollection.insertOne(new Document("userid", user.getId()).append("accountNumber", AccountNumber).append("ifscCode", IfscCode).append("mobileNumber", MobileNumber).append("UpiId", UpiId)).getAsync(result1 -> {
                                    if (result1.isSuccess()) {
                                        startActivity(new Intent(AddCoursePayment.this, MainActivity.class));
                                    } else {

                                    }
                                });
                            }
                        }
                        else
                        {

                        }
                    });
                }
            }
        });
    }
}