package com.sanchit.Upsilon;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import org.bson.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class UserDataSetupActivity1 extends AppCompatActivity {
    private static final String TAG = "UserDataSetupActivity1";

    String appID = "upsilon-ityvn";
    View actionBarView;
    Button nextButton,addInterests;
    TextInputEditText text;
    ChipGroup group;
    ArrayList<String> interests;
    String interest;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    ArrayList<String> mycourses;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_1);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        actionBarView = getSupportActionBar().getCustomView(); //to be implemented
        nextButton = (Button) findViewById(R.id.selectLaterNext1);
        addInterests = (Button) findViewById(R.id.addInterest);
        //searchView = (SearchView)findViewById(R.id.searchInterests);
        text = (TextInputEditText) findViewById(R.id.textEnterInterests);
        group = (ChipGroup) findViewById(R.id.groupInterests);
        group.removeAllViews();

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        user = app.currentUser();
        interests = new ArrayList<>();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");

        mycourses = new ArrayList<>();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String s : interests) {
                    Log.d(TAG, "onClick: interests: " + s);
                }
                Document queryFilter = new Document("userid", user.getId());

                RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                findTask.getAsync(task -> {
                    if (task.isSuccess()) {
                        MongoCursor<Document> results = task.get();
                        if (!results.hasNext()) {
                            mongoCollection.insertOne(
                                    new Document("userid", user.getId()).append("myCourses", mycourses).append("profilePicCounter", 0).append("favoriteColor", "pink").append("interests", interests))
                                    .getAsync(result -> {
                                        if (result.isSuccess()) {
                                            Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                    + result.get().getInsertedId());
                                        } else {
                                            Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                        }
                                    });
                        } else {
                            Document userdata = results.next();
                            userdata.append("interests", interests);
                            userdata.append("profilePicCounter", 0);
                            userdata.append("myCourses", mycourses);

                            mongoCollection.updateOne(
                                    new Document("userid", user.getId()), (userdata))
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
                            Log.v("User", currentDoc.getString("userid"));
                        }
                    } else {
                        Log.v("User", "Failed to complete search");
                    }
                });


                Intent intent = new Intent(UserDataSetupActivity1.this, UserDataSetupActivity2.class);
                startActivity(intent);
            }
        });

        /*
        group.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if(group.getChildCount() > 0) {
                    nextButton.setText(R.string.next);
                } else {
                    nextButton.setText(R.string.select_later);
                }
                text.setText("");
            }
        });*/


        addInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButton.setText(R.string.next);
                LayoutInflater inflater = getLayoutInflater();
                String[] _interests = text.getText().toString().toLowerCase().trim().split(" ");
                for(String interest : _interests) {
                    interests.add(interest);
                    Chip chip = (Chip) inflater.inflate(R.layout.layout_interests_recycler_view, group, false);
                    chip.setText(interest);
                    group.addView(chip);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            group.removeView(view);
                            interests.remove(chip.getText().toString());
                        }
                    });
                }
                text.setText("");
            }
        });
    }
}
