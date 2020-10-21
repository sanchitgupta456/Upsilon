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

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class UserDataSetupActivity2 extends AppCompatActivity {

    String appID = "upsilon-ityvn";
    Button nextButton;
    EditText Name;
    String name;
    int flag=0;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_2);

        nextButton = (Button) findViewById(R.id.selectLaterNext2);
        Name = (EditText) findViewById(R.id.userNameHolder);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==1)
                {
                    name = Name.getText().toString();
                    mongoClient = user.getMongoClient("mongodb-atlas");
                    mongoDatabase = mongoClient.getDatabase("Upsilon");
                    MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");

                    //Blank query to find every single course in db
                    //TODO: Modify query to look for user preferred course IDs
                    Document queryFilter  = new Document("userid",user.getId());

                    RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                    findTask.getAsync(task -> {
                        if (task.isSuccess()) {
                            MongoCursor<Document> results = task.get();
                            if(!results.hasNext())
                            {
                                mongoCollection.insertOne(
                                        new Document("userid", user.getId()).append("favoriteColor", "pink"))
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
                                userdata.append("name",name);

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
                }
                Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                startActivity(intent);
            }
        });

        TextWatcher textWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                nextButton.setText("Next");
                flag=1;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                nextButton.setText("Select Later");
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            nextButton.setText("Next");
            flag=1;
            }
        };

        Name.addTextChangedListener(textWatcher);


    }
}