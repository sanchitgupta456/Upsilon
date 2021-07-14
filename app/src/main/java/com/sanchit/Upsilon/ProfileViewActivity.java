package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.bson.Document;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class ProfileViewActivity extends AppCompatActivity {

    TextInputEditText UserName,PhoneNumber,Email;
    CircleImageView profileImage;
    App app;
    String appID = "upsilon-ityvn";
    Button update;

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    private ProgressBar progressBar;
//    private View bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.your_profile);
//        bar = getSupportActionBar().getCustomView();
        UserName = (TextInputEditText) findViewById(R.id.editTextUserName);
        PhoneNumber = (TextInputEditText) findViewById(R.id.editTextPhoneNumber);
        Email = (TextInputEditText) findViewById(R.id.editTextEmailId);
        update = (Button) findViewById(R.id.updateChange);
//        NumberOfCoursesTaken = (TextView) findViewById(R.id.profileNumCoursesTaken);
//        NumberOfCoursesTaught = (TextView) findViewById(R.id.profileNumCoursesTaught);
        profileImage = (CircleImageView) findViewById(R.id.imgProfileImage);
        progressBar = (ProgressBar) findViewById(R.id.loadingUserProfile);
//        app = new App(new AppConfiguration.Builder(appID)
//                .build());
//
//        User user = app.currentUser();
//        Document queryfilter = new Document("userid",user.getId());
//
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("Upsilon");
//        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
//
//        //Blank query to find every single course in db
//        //TODO: Modify query to look for user preferred course IDs
//        Document queryFilter  = new Document("userid",user.getId());
//
//        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
//
//        findTask.getAsync(task -> {
//            progressBar.setVisibility(View.VISIBLE);
//            if (task.isSuccess()) {
//                MongoCursor<Document> results = task.get();
//                if(!results.hasNext())
//                {
//                    /*mongoCollection.insertOne(
//                            new Document("userid", user.getId()).append("favoriteColor", "pink"))
//                            .getAsync(result -> {
//                                if (result.isSuccess()) {
//                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
//                                            + result.get().getInsertedId());
//                                    //goToSetupActivity();
//                                } else {
//                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
//                                }
//                            });*/
//                }
//                else
//                {
//                    Log.v("User", "successfully found the user");
//                    //getCourseData();
//                }
//                while (results.hasNext()) {
//                    //Log.v("EXAMPLE", results.next().toString());
//                    Document currentDoc = results.next();
//                    try {
//                        UserName.setText(currentDoc.getString("name"));
//                        PhoneNumber.setText(currentDoc.getString("phonenumber"));
//                        Email.setText(user.getProfile().getEmail());
//                        Log.v("Email","Hello" + user.getProfile().toString());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        Picasso.with(getApplicationContext()).load(currentDoc.getString("profilePicUrl")).into(profileImage);
//                        Log.v("ProfilePic",currentDoc.getString("profilePicUrl"));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    Log.v("User",currentDoc.getString("userid"));
//                }
//            } else {
//                Log.v("User","Failed to complete search");
//            }
//            progressBar.setVisibility(View.GONE);
//        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: update fields in backend
            }
        });


    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu.add(0);
        //menu.getItem(0).setIcon(getDrawable(R.drawable.edit_icon_light));
        menu.add("Edit");
        menu.getItem(0).setIcon(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.edit_icon));
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
