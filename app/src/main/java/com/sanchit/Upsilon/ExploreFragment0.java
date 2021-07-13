package com.sanchit.Upsilon;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseFinal;
import com.sanchit.Upsilon.courseData.CoursesAdapter1;
import com.sanchit.Upsilon.courseLocationMap.MapsActivity;
import com.sanchit.Upsilon.courseSearching.SearchQuery;
import com.sanchit.Upsilon.courseSearching.rankBy;
import com.sanchit.Upsilon.userData.UserLocation;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

import static io.realm.Realm.getApplicationContext;

public class ExploreFragment0 extends Fragment {
    private static final String TAG = "Near Me";
    private static final int REQUEST_FINE_LOCATION = 1234;
    String appID = "upsilon-ityvn";
    private App app;
    private LinearLayoutManager linearLayoutManager;
    CoursesAdapter1 adapter;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    private User user;
    private Gson gson;
    private GsonBuilder gsonBuilder;
    Document userdata;
    RecyclerView recyclerView;
    CardView alter;
    LinearLayout ll, llLoader;
    SearchQuery searchQuery = new SearchQuery();
    Document userLocation;
    ProgressBar progressBar;

    ArrayList<CourseFinal> list = new ArrayList<>();
    private RequestQueue queue;
    private String API ;

    public rankBy sortCriteria = rankBy.LOC;

    String query = "";
    public ExploreFragment0(String string) {
        query = string;
    }

    public ExploreFragment0() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: started. 95");
        View view = inflater.inflate(R.layout.fragment_explore0, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.exploreList0);
        progressBar = (ProgressBar) view.findViewById(R.id.loadingExplore);
        alter = (CardView) view.findViewById(R.id.alter);
        ll = (LinearLayout) view.findViewById(R.id.linearLayoutSetupMaps);
        llLoader = (LinearLayout) view.findViewById(R.id.llLocationSetupProgress);
        queue = Volley.newRequestQueue(getApplicationContext());
        API = ((Upsilon)getActivity().getApplication()).getAPI();
//        llLoader.setVisibility(View.INVISIBLE);
        Log.d(TAG, "onCreateView: started. 102");

//        app = new App(new AppConfiguration.Builder(appID).build());
//        user = app.currentUser();
//        userdata = user.getCustomData();
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("Upsilon");
//        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");
        findLocation();
        Log.d(TAG, "onCreateView: 109");
//        userdata = user.getCustomData();
        Log.d(TAG, "onCreateView: 111");
//        alter.setVisibility(View.GONE);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
                } else {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
                    } else {
                        if(isLocationEnabled(getApplicationContext()))
                        {
                            llLoader.setVisibility(View.VISIBLE);
                            getLocation();
//                    app = new App(new AppConfiguration.Builder(appID).build());
//                    user = app.currentUser();
//                    userdata = user.getCustomData();
                            Log.d(TAG, "onClick: tests...");
                        }
                        else
                        {
                            enableLoc();
                        }

                    }

                }
            }
        });
        /*
        if(userdata.get("userLocation")!=null) {
            //Log.v("Searching", String.valueOf(userdata.get("userLocation")));
            //searchQuery.setRankMethod(sortCriteria);
            //searchForCourses(query);
            alter.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setVisibility(View.GONE);
            alter.setVisibility(View.VISIBLE);
        }*/

        return view;
    }

    public void searchForCourses(SearchQuery _searchQuery){
        this.query = _searchQuery.getKeywords();
        searchQuery.setQuery(query);
        searchQuery.setSelectedTags(_searchQuery.getSelectedTags());
        performSearch();
    }
    public void searchForCourses(String query){
        this.query = query;
        searchQuery.setQuery(query);
        performSearch();
    }
    public void performSearch() {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("index",0);
            jsonBody.put("filter","Distance");
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/paging",jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", response.toString());
                            list = new ArrayList<CourseFinal>();
                            try {
                                JSONArray jsonArray = (JSONArray) response.get("courses");
                                Log.v("array",String.valueOf(jsonArray));
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    Gson gson= new Gson();
                                    CourseFinal course = gson.fromJson(jsonObject.toString(),CourseFinal.class);
                                    list.add(course);
                                    Log.v("course",String.valueOf(course.getCourseReviews()));
                                }
                                initRecyclerView(recyclerView, list);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                                initRecyclerView(recyclerView, list);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error response", error.toString());
                            Log.v(TAG, "Fetch Courses FAILED!");
                            Log.e(TAG, error.toString());
                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", ((Upsilon)getActivity().getApplication()).getToken());
                    return params;
                }
            };
            queue.add(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        llLoader.setVisibility(View.VISIBLE);
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("Upsilon");
//        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
//        if(userdata.get("userLocation")!=null) {
//            recyclerView.setVisibility(View.VISIBLE);
//            alter.setVisibility(View.GONE);
//        }
//        else
//        {
//            recyclerView.setVisibility(View.GONE);
//            alter.setVisibility(View.VISIBLE);
//            return;
//        }

        //Blank query to find every single user in db
//        Document queryFilter  = new Document("userid", user.getId());
//
//        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
//
//        findTask.getAsync(task -> {
//            if (task.isSuccess()) {
//                MongoCursor<Document> results = task.get();
//                int i = 0;
//                while (results.hasNext()) {
//                    //Log.v("EXAMPLE", results.next().toString());
//                    Document currentDoc = results.next();
//                    Document userLoc = (Document) currentDoc.get("userLocation");
//                    Log.v("UserLocation", String.valueOf(currentDoc.get("userLocation")));
//                    if(currentDoc.get("userLocation")!=null && ((Document) currentDoc.get("userLocation")).get("lattitude")!=null) {
//                        searchQuery.searchForCourse(app, mongoDatabase, getContext(), adapter, recyclerView, 10, userLoc);
//                        list = new ArrayList<>();
//                        list.clear();
////                        list = searchQuery.getSearchResultsList();
//                        Log.d(TAG, "performSearch: list after search: size: " + list.size());
//                        Log.v("COURSEDISTANCE", "START!");
////                        for (int p = 0; p < list.size(); p++){
////                            Log.v("COURSEDISTANCE", list.get(p).getCourseName());
////                            Log.v("COURSEDISTANCE", Double.toString(searchQuery.getCourseDistance(list.get(p), userLoc)));
////                        }
//                    }
//                    else
//                    {
//                        Snackbar.make(requireView(),"Please add Your Location in UserData to search courses near you",1).show();
//                    }
//                    if(!results.hasNext())
//                    {
//                        progressBar.setVisibility(View.GONE);
////                        llLoader.setVisibility(View.GONE);
//                    }
//                }
//            } else {
////                llLoader.setVisibility(View.INVISIBLE);
//                progressBar.setVisibility(View.GONE);
//                Log.v("User","Failed to complete search");
//            }
//        });
////        list = searchQuery.getSearchResultsList();
//
//        initRecyclerView(recyclerView,list);
    }

    public void initRecyclerView(RecyclerView recyclerView, ArrayList<CourseFinal> list) {
        Log.d(TAG, "initRecyclerView: now displaying " + recyclerView.getId());
        CoursesAdapter1 coursesAdapter1 = new CoursesAdapter1(list,((Upsilon)getActivity().getApplication()).getAPI() , ((Upsilon)getActivity().getApplication()).getToken() , ((Upsilon) getActivity().getApplication()).getUser());
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(coursesAdapter1);
        Log.d(TAG, "initRecyclerView: display success! Displayed " + list.size() + " items");
    }

    //location
    private void getLocation() {
//        llLoader.setVisibility(View.VISIBLE);
        Log.d(TAG, "getLocation: I got permissions!");
        userLocation = new Document();
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());;
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                Log.v("Location", String.valueOf(location));
                if(location!=null)
                {
                    Log.v("Location","Location"+location.getLatitude());
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        /*
                        City.setText(addresses.get(0).getLocality());
                        viewModel.setCity(addresses.get(0).getLocality());
                        Pincode.setText(addresses.get(0).getPostalCode());
                        viewModel.setPincode(addresses.get(0).getPostalCode());*/
                        userLocation.append("latitude",location.getLatitude());
                        userLocation.append("longitude",location.getLongitude());
                        //viewModel.setUserLocation(userLocation);
                        //user.getCustomData().append("userLocation", userLocation);
                        updateLocation();
                        //Log.v("Location",addresses.get(0).getPostalCode()+" "+addresses.get(0).getLocality()+" "+addresses.get(0).getSubLocality());
                    } catch (IOException e) {
//                        llLoader.setVisibility(View.INVISIBLE);
                        e.printStackTrace();
                    }

                }
                else
                {
                    llLoader.setVisibility(View.INVISIBLE);
                    Log.v("Location","Error");
                }
            }
        });
    }

    public void findLocation(){
        progressBar.setVisibility(View.VISIBLE);
        if(((Upsilon)getActivity().getApplication()).getUser().getUserLocation() != null)
        {
                        progressBar.setVisibility(View.GONE);
                        Log.v("ExploreFragment0","userLoc is not null"+String.valueOf(((Upsilon)getActivity().getApplication()).user.getUserLocation()));
                        recyclerView.setVisibility(View.VISIBLE);
                        alter.setVisibility(View.GONE);
                        llLoader.setVisibility(View.INVISIBLE);
                        performSearch();
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            llLoader.setVisibility(View.INVISIBLE);
            Log.v("ExploreFragment0","userLoc is null");
            recyclerView.setVisibility(View.GONE);
            alter.setVisibility(View.VISIBLE);
        }
//        llLoader.setVisibility(View.VISIBLE);
//        Document queryFilter = new Document("userid", user.getId());
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("Upsilon");
//        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
//        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

//        findTask.getAsync(task -> {
//            if (task.isSuccess()) {
//                MongoCursor<Document> results = task.get();
//                if (!results.hasNext()) {
//
//                } else {
//                    Document userdata = results.next();
//                    userLocation = (Document) userdata.get("userLocation");
//                    if(userLocation!=null && userLocation.get("lattitude")!=null) {
//                        progressBar.setVisibility(View.GONE);
//                        Log.v("ExploreFragment0","userLoc is not null");
//                        recyclerView.setVisibility(View.VISIBLE);
//                        alter.setVisibility(View.GONE);
//                        llLoader.setVisibility(View.INVISIBLE);
//                        performSearch();
//                    }
//                    else
//                    {
//                        progressBar.setVisibility(View.GONE);
//                        llLoader.setVisibility(View.INVISIBLE);
//                        Log.v("ExploreFragment0","userLoc is null");
//                        recyclerView.setVisibility(View.GONE);
//                        alter.setVisibility(View.VISIBLE);
//                    }
//                }
//            } else {
//                progressBar.setVisibility(View.GONE);
//                Log.v("User", "Failed to complete search");
//            }
//        });
    }

    public void updateLocation(){
        Log.v("Location",String.valueOf(userLocation.get("latitude"))+String.valueOf(userLocation.get("longitude")));
        Map<String, String> mHeaders = new ArrayMap<String, String>();
        mHeaders.put("token",((Upsilon)this.getActivity().getApplication()).getToken());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude", userLocation.get("latitude"));
            jsonObject.put("longitude", userLocation.get("longitude"));

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/userLocation",jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("UpdatingUserLocation", response.toString());
                            llLoader.setVisibility(View.INVISIBLE);
//                                    performSearch();
                            alter.setVisibility(View.GONE);
                            ((Upsilon)getActivity().getApplication()).user.setUserLocation(new UserLocation(Double.parseDouble(userLocation.get("latitude").toString()),Double.parseDouble(userLocation.get("longitude").toString())));
                            ((Upsilon)getActivity().getApplication()).fetchProfile();
                            performSearch();
                                    recyclerView.setVisibility(View.VISIBLE);
                        }
                    },
                    new Response.ErrorListener() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("ErrorUpdatingUserLocation", error.toString());
                                                                llLoader.setVisibility(View.INVISIBLE);

                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", ((Upsilon)getActivity().getApplication()).getToken());
                    return params;
                }
            };
            queue.add(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Document queryFilter = new Document("userid", user.getId());
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("Upsilon");
//        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
//        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
//        findTask.getAsync(task -> {
//            if (task.isSuccess()) {
//                MongoCursor<Document> results = task.get();
//                if (!results.hasNext()) {
//
//                    mongoCollection.insertOne(
//                            new Document("userid", user.getId()).append("userLocation",userLocation))
//                            .getAsync(result -> {
//                                if (result.isSuccess()) {
//                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
//                                            + result.get().getInsertedId());
//                                    llLoader.setVisibility(View.INVISIBLE);
//                                    performSearch();
//                                    alter.setVisibility(View.GONE);
//                                    recyclerView.setVisibility(View.VISIBLE);
//                                    Log.d(TAG, "updateLocation: inserted");
//                                } else {
//                                    llLoader.setVisibility(View.INVISIBLE);
//                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
//                                }
//                            });
//                    Log.d(TAG, "updateLocation: This is some error?!");
//                } else {
//                    Document userdata = results.next();
//                    userdata.append("userLocation",userLocation);
//
//                    mongoCollection.updateOne(
//                            new Document("userid", user.getId()), (userdata))
//                            .getAsync(result -> {
//                                if (result.isSuccess()) {
//                                    llLoader.setVisibility(View.INVISIBLE);
//                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
//                                            + result.get().getModifiedCount());
//                                    Log.d(TAG, "updateLocation: updated");
//                                    performSearch();
//                                    alter.setVisibility(View.GONE);
//                                    recyclerView.setVisibility(View.VISIBLE);
//                                } else {
//                                    llLoader.setVisibility(View.INVISIBLE);
//                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
//                                }
//                            });
//                }
//                while (results.hasNext()) {
//                    //Log.v("EXAMPLE", results.next().toString());
//                    Document currentDoc = results.next();
//                    Log.v("User", currentDoc.getString("userid"));
//                }
//            } else {
//                llLoader.setVisibility(View.INVISIBLE);
//                Log.v("User", "Failed to complete search");
//            }
//        });
        Log.d(TAG, "updateLocation: safe exit here");
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    private void enableLoc() {

        GoogleApiClient googleApiClient = null;
        if (googleApiClient == null) {
            GoogleApiClient finalGoogleApiClient = googleApiClient;
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            finalGoogleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                private static final int REQUEST_LOCATION =  1235 ;

                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(getActivity(), REQUEST_LOCATION);

//                                finish();
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }
}