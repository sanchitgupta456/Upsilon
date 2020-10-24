package com.sanchit.Upsilon;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.bson.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class UserDataSetupActivity1 extends AppCompatActivity {

    String appID = "upsilon-ityvn";
    Button nextButton,addInterests;
    SearchView searchView;
    RecyclerView showInterests;
    ArrayList<String> interests;
    String interest;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    RecyclerView.LayoutManager layoutManager;
    InterestsAdapter adapter;
    int i=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_1);

        nextButton = (Button) findViewById(R.id.selectLaterNext1);
        addInterests = (Button) findViewById(R.id.addInterest);
        searchView = (SearchView) findViewById(R.id.searchInterests);
        showInterests = (RecyclerView) findViewById(R.id.listInterests);
        layoutManager = new LinearLayoutManager(this);
        showInterests.setLayoutManager(layoutManager);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        user = app.currentUser();
        interests = new ArrayList<>();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Document queryFilter  = new Document("userid",user.getId());

                RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                findTask.getAsync(task -> {
                    if (task.isSuccess()) {
                        MongoCursor<Document> results = task.get();
                        if(!results.hasNext())
                        {
                            mongoCollection.insertOne(
                                    new Document("userid", user.getId()).append("favoriteColor", "pink").append("interests",interests))
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
                            userdata.append("interests",interests);

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



            Intent intent = new Intent(UserDataSetupActivity1.this,UserDataSetupActivity2.class);
                startActivity(intent);
            }
        });


        adapter = new InterestsAdapter(interests,getApplicationContext());
        showInterests.setAdapter((RecyclerView.Adapter) adapter);

        addInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButton.setText("next");
                interest = searchView.getQuery().toString();
                interests.add(i,interest.toString());
                adapter.notifyDataSetChanged();
                searchView.setQuery("",false);
                i++;
            }
        });




    }
}
