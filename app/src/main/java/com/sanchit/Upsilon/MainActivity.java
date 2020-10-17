package com.sanchit.Upsilon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Realm.init(this); // `this` is a Context, typically an Application or Activity
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
        });*/
    }
}