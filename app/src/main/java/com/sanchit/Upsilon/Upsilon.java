package com.sanchit.Upsilon;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.android.MediaManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sanchit.Upsilon.courseData.CourseFinal;
import com.sanchit.Upsilon.ui.login.LoginActivity;
import com.sanchit.Upsilon.userData.User;
import com.sanchit.Upsilon.userData.UserLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class Upsilon extends Application {

//    String API = "http://192.168.0.107:3000";
    String API = "https://upsilonbackend.herokuapp.com";
    String Token = null;
    private RequestQueue queue;
    ArrayList<String> interests = new ArrayList<>();
    User user;
    public static final String PREFS_NAME = "MyPrefsFile";


    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

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
        queue = Volley.newRequestQueue(getApplicationContext());
        startup();
        if(Token!=null)
        {
            initialise();
        }
    }

    public void initialise() {
        Gson gson = new Gson();
        saveToken();
        fetchProfile();
    }

    private void saveToken() {
        getApplicationContext().getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                .edit()
                .putString("token", Token)
                .apply();
    }

    public void fetchProfile() {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, API+"/me",new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("FetchUser", response.toString());
                        try {
                            user = new User();
                            Gson gson= new Gson();
                            getApplicationContext().getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                                    .edit()
                                    .putString("myuser", String.valueOf(response))
                                    .apply();
                            User user = gson.fromJson(String.valueOf(response),User.class);
                            setUser(user);
                        } catch (JsonSyntaxException e) {
                            fetchProfile();
                            e.printStackTrace();
                        }
//                        Log.v("User",user.getUserLocation().getLatitude().toString());
                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ErrorFetchingUserLocation", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", Token);
                return params;
            }
        };
        queue.add(jsonRequest);
    }

    private void startup() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        Token = pref.getString("token", null);
        Gson gson = new Gson();
        String userstr = pref.getString("myuser",null);
        if(userstr!=null)
        {
            Log.v("User",userstr);
            user = gson.fromJson(userstr,User.class);
        }
//        JSONObject jsonBody = new JSONObject();
        fetchInterests();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void fetchInterests() {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, API+"/interests",new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("FetchInterests", response.toString());
                        try {
                            JSONArray jsonArray = (JSONArray) (response.get("interests"));
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                interests.add((String) jsonArray.get(i));
                            }
                            Log.v("Fetched", String.valueOf(interests));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ErrorFetchingInterests", error.toString());
                    }
                }
        );
        queue.add(jsonRequest);
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
        saveToken();
    }
}
