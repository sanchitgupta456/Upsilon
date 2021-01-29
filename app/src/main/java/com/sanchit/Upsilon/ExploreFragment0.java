package com.sanchit.Upsilon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CoursesAdapter1;
import com.sanchit.Upsilon.courseSearching.SearchQuery;
import com.sanchit.Upsilon.courseSearching.rankBy;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class ExploreFragment0 extends Fragment {
    private static final String TAG = "Near Me";
    String appID = "upsilon-ityvn";
    private App app;
    private LinearLayoutManager linearLayoutManager;
    CoursesAdapter1 adapter;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    private User user;
    private Gson gson;
    private GsonBuilder gsonBuilder;
    RecyclerView recyclerView;
    SearchQuery searchQuery = new SearchQuery();

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
        View view = inflater.inflate(R.layout.fragment_explore0, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.exploreList0);

        app = new App(new AppConfiguration.Builder(appID).build());
        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");
        Document userdata = user.getCustomData();
        if(userdata.get("userLocation")!=null) {
            Log.v("Searching", String.valueOf(userdata.get("userLocation")));
            searchQuery.setRankMethod(sortCriteria);
            searchForCourses(query);
        }
        else
        {
            recyclerView.setVisibility(View.INVISIBLE);
            Snackbar.make(getView(),"Blah",Snackbar.LENGTH_LONG).show();
        }

        return view;
    }

    public void searchForCourses(SearchQuery _searchQuery){
        this.query = _searchQuery.getKeywords();
        searchQuery.setQuery(query);
        searchQuery.setSelectedTags(_searchQuery.getSelectedTags());
        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        performSearch();
        progressBar.setVisibility(View.GONE);
    }
    public void searchForCourses(String query){
        this.query = query;
        searchQuery.setQuery(query);
        performSearch();
    }
    public void performSearch() {
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");

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
                        Log.v("COURSEDISTANCE", "START!");
                        for (int p = 0; p < list.size(); p++){
                            Log.v("COURSEDISTANCE", list.get(p).getCourseName());
                            Log.v("COURSEDISTANCE", Double.toString(searchQuery.getCourseDistance(list.get(p), userLoc)));
                        }
                    }
                    else
                    {
                        Snackbar.make(getView(),"Please add Your Location in UserData to search courses near you",1).show();
                    }
                }
            } else {
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
}