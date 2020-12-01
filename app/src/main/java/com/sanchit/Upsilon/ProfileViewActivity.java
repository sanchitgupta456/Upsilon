package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.bson.Document;

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

    TextView UserName,PhoneNumber,Email,NumberOfCoursesTaken,NumberOfCoursesTaught;
    CircleImageView profileImage;
    App app;
    String appID = "upsilon-ityvn";

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_user_profile);
        getSupportActionBar().setElevation(0);
        View bar = getSupportActionBar().getCustomView();
        ImageButton imageButton = (ImageButton) bar.findViewById(R.id.imgBtnBackProfile);
        ImageButton imageButton1 = (ImageButton) bar.findViewById(R.id.imgBtnEditProfile);
        UserName = (TextView) findViewById(R.id.profileName);
        PhoneNumber = (TextView) findViewById(R.id.profilePhone);
        Email = (TextView) findViewById(R.id.profileEmail);
        NumberOfCoursesTaken = (TextView) findViewById(R.id.profileNumCoursesTaken);
        NumberOfCoursesTaught = (TextView) findViewById(R.id.profileNumCoursesTaught);
        profileImage = (CircleImageView) findViewById(R.id.imgProfileImage);
        progressBar = (ProgressBar) findViewById(R.id.loadingUserProfile);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileViewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileViewActivity.this, UserDataSetupActivity1.class);
                startActivity(intent);
            }
        });
        app = new App(new AppConfiguration.Builder(appID)
                .build());

        User user = app.currentUser();
        Document queryfilter = new Document("userid",user.getId());

        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");

        //Blank query to find every single course in db
        //TODO: Modify query to look for user preferred course IDs
        Document queryFilter  = new Document("userid",user.getId());

        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

        findTask.getAsync(task -> {
            progressBar.setVisibility(View.VISIBLE);
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                if(!results.hasNext())
                {
                    /*mongoCollection.insertOne(
                            new Document("userid", user.getId()).append("favoriteColor", "pink"))
                            .getAsync(result -> {
                                if (result.isSuccess()) {
                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                            + result.get().getInsertedId());
                                    //goToSetupActivity();
                                } else {
                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                }
                            });*/
                }
                else
                {
                    Log.v("User", "successfully found the user");
                    //getCourseData();
                }
                while (results.hasNext()) {
                    //Log.v("EXAMPLE", results.next().toString());
                    Document currentDoc = results.next();
                    UserName.setText(currentDoc.getString("name"));
                    PhoneNumber.setText(currentDoc.getString("phonenumber"));
                    Email.setText(user.getProfile().getEmail());
                    Log.v("Email","Hello" + user.getProfile().toString());
                    Picasso.with(getApplicationContext()).load(currentDoc.getString("profilePicUrl")).into(profileImage);
                    Log.v("ProfilePic",currentDoc.getString("profilePicUrl"));
                    Log.v("User",currentDoc.getString("userid"));
                }
            } else {
                Log.v("User","Failed to complete search");
            }
            progressBar.setVisibility(View.GONE);
        });

    }
}
