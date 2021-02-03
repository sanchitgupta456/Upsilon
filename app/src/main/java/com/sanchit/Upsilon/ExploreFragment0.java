package com.sanchit.Upsilon;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CoursesAdapter1;
import com.sanchit.Upsilon.courseSearching.SearchQuery;
import com.sanchit.Upsilon.courseSearching.rankBy;

import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

    ArrayList<Course> list = new ArrayList<>();

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
//        llLoader.setVisibility(View.INVISIBLE);
        Log.d(TAG, "onCreateView: started. 102");

        app = new App(new AppConfiguration.Builder(appID).build());
        user = app.currentUser();
        userdata = user.getCustomData();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");
        findLocation();
        Log.d(TAG, "onCreateView: 109");
        userdata = user.getCustomData();
        Log.d(TAG, "onCreateView: 111");
        alter.setVisibility(View.GONE);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
                } else {
                    llLoader.setVisibility(View.VISIBLE);
                    getLocation();
//                    app = new App(new AppConfiguration.Builder(appID).build());
//                    user = app.currentUser();
//                    userdata = user.getCustomData();
                    Log.d(TAG, "onClick: tests...");
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
//        llLoader.setVisibility(View.VISIBLE);
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
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
        Document queryFilter  = new Document("userid", user.getId());

        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                int i = 0;
                while (results.hasNext()) {
                    //Log.v("EXAMPLE", results.next().toString());
                    Document currentDoc = results.next();
                    Document userLoc = (Document) currentDoc.get("userLocation");
                    Log.v("UserLocation", String.valueOf(currentDoc.get("userLocation")));
                    if(currentDoc.get("userLocation")!=null) {
                        searchQuery.searchForCourse(app, mongoDatabase, getContext(), adapter, recyclerView, 10, userLoc);
                        list = searchQuery.getSearchResultsList();
                        Log.d(TAG, "performSearch: list after search: size: " + list.size());
                        Log.v("COURSEDISTANCE", "START!");
                        for (int p = 0; p < list.size(); p++){
                            Log.v("COURSEDISTANCE", list.get(p).getCourseName());
                            Log.v("COURSEDISTANCE", Double.toString(searchQuery.getCourseDistance(list.get(p), userLoc)));
                        }
                    }
                    else
                    {
                        Snackbar.make(requireView(),"Please add Your Location in UserData to search courses near you",1).show();
                    }
                    if(!results.hasNext())
                    {
                        progressBar.setVisibility(View.GONE);
//                        llLoader.setVisibility(View.GONE);
                    }
                }
            } else {
//                llLoader.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.GONE);
                Log.v("User","Failed to complete search");
            }
        });
        list = searchQuery.getSearchResultsList();

        initRecyclerView(recyclerView,list);
    }

    public void initRecyclerView(RecyclerView recyclerView, ArrayList<Course> list) {
        Log.d(TAG, "initRecyclerView: now displaying " + recyclerView.getId());
        CoursesAdapter1 coursesAdapter1 = new CoursesAdapter1(list);
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
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));;
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
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
                        userLocation.append("lattitude",location.getLatitude());
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
//        llLoader.setVisibility(View.VISIBLE);
        Document queryFilter = new Document("userid", user.getId());
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                if (!results.hasNext()) {

                } else {
                    Document userdata = results.next();
                    userLocation = (Document) userdata.get("userLocation");
                    if(userLocation!=null) {
                        progressBar.setVisibility(View.GONE);
                        Log.v("ExploreFragment0","userLoc is not null");
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
                }
            } else {
                progressBar.setVisibility(View.GONE);
                Log.v("User", "Failed to complete search");
            }
        });
    }

    public void updateLocation(){
        Document queryFilter = new Document("userid", user.getId());
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                if (!results.hasNext()) {
                    
                    mongoCollection.insertOne(
                            new Document("userid", user.getId()).append("userLocation",userLocation))
                            .getAsync(result -> {
                                if (result.isSuccess()) {
                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                            + result.get().getInsertedId());
                                    llLoader.setVisibility(View.INVISIBLE);
                                    performSearch();
                                    alter.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    Log.d(TAG, "updateLocation: inserted");
                                } else {
                                    llLoader.setVisibility(View.INVISIBLE);
                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                }
                            });
                    Log.d(TAG, "updateLocation: This is some error?!");
                } else {
                    Document userdata = results.next();
                    userdata.append("userLocation",userLocation);

                    mongoCollection.updateOne(
                            new Document("userid", user.getId()), (userdata))
                            .getAsync(result -> {
                                if (result.isSuccess()) {
                                    llLoader.setVisibility(View.INVISIBLE);
                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                            + result.get().getModifiedCount());
                                    Log.d(TAG, "updateLocation: updated");
                                    performSearch();
                                    alter.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                } else {
                                    llLoader.setVisibility(View.INVISIBLE);
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
                llLoader.setVisibility(View.INVISIBLE);
                Log.v("User", "Failed to complete search");
            }
        });
        Log.d(TAG, "updateLocation: safe exit here");
    }
}