package com.sanchit.Upsilon;

import android.annotation.SuppressLint;
import android.app.Application;
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
import com.sanchit.Upsilon.ui.login.LoginActivity;
import com.sanchit.Upsilon.userData.UserLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class Upsilon extends Application {

    String API = "https://upsilonbackend.herokuapp.com";
    String Token = null;
    private RequestQueue queue;
    ArrayList<String> interests = new ArrayList<>();
    UserLocation userLocation = null;

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
        fetchUserLocation();
    }

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }

    public void fetchUserLocation() {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, API+"/userLocation",new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("FetchUserLocation", response.toString());
                        userLocation = new UserLocation();
                        try {
                            userLocation.setLatitude((Double) Double.parseDouble(response.get("latitude").toString()));
                            userLocation.setLongitude((Double) Double.parseDouble(response.get("longitude").toString()));
                            Log.v("UserLocationResponse",userLocation.toString());
                        } catch (JSONException e) {
                            userLocation = null;
                            e.printStackTrace();
                        }
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

//        JSONObject jsonBody = new JSONObject();
        fetchInterests();
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
    }
}
