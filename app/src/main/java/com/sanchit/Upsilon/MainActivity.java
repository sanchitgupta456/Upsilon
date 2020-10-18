package com.sanchit.Upsilon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class MainActivity extends AppCompatActivity {


    private ArrayList<String> CourseNames = new ArrayList<>(); //testing
    private ArrayList<String> ImageUrls = new ArrayList<>(); //testing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this); // `this` is a Context, typically an Application or Activity
        String appID = "upsilon-ityvn"; // replace this with your App ID
        final App app = new App(new AppConfiguration.Builder(appID)
                .build());

        Credentials credentials = Credentials.anonymous();

        app.loginAsync(credentials, new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> it) {
                if (it.isSuccess()) {
                    Log.v("QUICKSTART", "Successfully authenticated anonymously.");
                    User user = app.currentUser();

                    // interact with MongoDB Realm via user object here

                } else {
                    Log.e("QUICKSTART", "Failed to log in. Error: " + it.getError().toString());
                }
            }
        });

        //getImages(); :testing recycler views for home page. to use this on beta, uncomment getImages() and initRecyclerView()
    }
    //testing recycler view. when design of home page is finalized, this is to be removed.
    /*
    private void getImages(){


        CourseNames.add("GOOGLE");
        CourseNames.add("JAVA");
        CourseNames.add("FACEBOOK");
        CourseNames.add("JAVA");
        CourseNames.add("JAVA");
        CourseNames.add("JAVA");
        ImageUrls.add("drawable://" + R.drawable.google);
        ImageUrls.add("drawable://" + R.drawable.ic_launcher_foreground);
        ImageUrls.add("drawable://" + R.drawable.facebook);
        ImageUrls.add("drawable://" + R.drawable.btn_rounded_rect);
        ImageUrls.add("drawable://" + R.drawable.btn_rounded_rect);
        ImageUrls.add("drawable://" + R.drawable.btn_rounded_rect);

        initRecyclerView();

    }*/
    //testing recycler view. when design of home page is finalized, this is to be removed.
    /*
    private void initRecyclerView(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.currentCourseListView);
        RecyclerView recyclerView2= (RecyclerView) findViewById(R.id.recommendedCourseListView);
        RecyclerView recyclerView3= (RecyclerView) findViewById(R.id.exploreCourseListView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView3.setLayoutManager(layoutManager3);
        CourseListAdaptorHome adapter = new CourseListAdaptorHome(this, CourseNames, ImageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView2.setAdapter(adapter);
        recyclerView3.setAdapter(adapter);
    }*/
}