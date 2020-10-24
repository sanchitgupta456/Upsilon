package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class UserDataSetupActivity3 extends AppCompatActivity {

    String appID = "upsilon-ityvn";
    Button nextButton;
    EditText City,Pincode,PhoneNumber;
    String city,pincode,phonenumber;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_3);

        nextButton = (Button) findViewById(R.id.selectLaterNext3);
        City = (EditText) findViewById(R.id.cityNameHolder);
        Pincode = (EditText) findViewById(R.id.pincodeHolder);
        PhoneNumber = (EditText) findViewById(R.id.contactNumberHolder);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");

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

                Document queryFilter  = new Document("userid",user.getId());
                city = City.getText().toString();
                pincode = Pincode.getText().toString();
                phonenumber = PhoneNumber.getText().toString();
                RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                findTask.getAsync(task -> {
                    if (task.isSuccess()) {
                        MongoCursor<Document> results = task.get();
                        if(!results.hasNext())
                        {
                            mongoCollection.insertOne(
                                    new Document("userid", user.getId()).append("favoriteColor", "pink").append("city",city).append("pincode",pincode).append("phonenumber",phonenumber))
                                    .getAsync(result -> {
                                        if (result.isSuccess()) {
                                            Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                    + result.get().getInsertedId());
                                        } else {
                                            Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                        }
                                    });
                        }
                        else
                        {
                            Document userdata = results.next();
                            userdata.append("city",city).append("pincode",pincode).append("phonenumber",phonenumber);

                            mongoCollection.updateOne(
                                    new Document("userid", user.getId()),(userdata))
                                    .getAsync(result -> {
                                        if (result.isSuccess()) {
                                            Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                    + result.get().getModifiedCount());
                                        } else {
                                            Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                        }
                                    });
                        }
                        while (results.hasNext()) {
                            //Log.v("EXAMPLE", results.next().toString());
                            Document currentDoc = results.next();
                            Log.v("User",currentDoc.getString("userid"));
                        }
                    } else {
                        Log.v("User","Failed to complete search");
                    }
                });


                Intent intent = new Intent(UserDataSetupActivity3.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}