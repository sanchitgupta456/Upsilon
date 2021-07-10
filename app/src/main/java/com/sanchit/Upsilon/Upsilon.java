package com.sanchit.Upsilon;

import android.app.Application;

import com.cloudinary.android.MediaManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class Upsilon extends Application {

    String API = "http://192.168.0.107:3000";
    String Token = null;

    public Upsilon() {
        // this method fires only once per application start.
        // getApplicationContext returns null here

    }

    @Override
    public void onCreate() {
        super.onCreate();
        // this method fires once as well as constructor
        // but also application has context here
        Realm.init(getApplicationContext()); // initialize Realm, required before interacting with SDK
        Map config = new HashMap();
        config.put("cloud_name", "upsilon175");
        MediaManager.init(getApplicationContext(), config);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public String getAPI()
    {
        return API;
    }

    public String getToken()
    {
        return Token;
    }

    public void setToken(String token)
    {
        Token = token;
    }
}
