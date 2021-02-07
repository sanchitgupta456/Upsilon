package com.sanchit.Upsilon.courseSearching;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CoursesAdapter1;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.regex.Pattern;

import io.realm.mongodb.App;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.FindIterable;
import io.realm.mongodb.mongo.iterable.MongoCursor;



public class SearchQuery {
    private static final String TAG = "SearchQuery debugger";
    String keywords = "";

    public HashMap<String, Boolean> getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(HashMap<String, Boolean> selectedTags) {
        this.selectedTags = selectedTags;
    }

    public HashMap<String, Boolean> selectedTags = new HashMap<String, Boolean>();

    rankBy rank = rankBy.LOC;

    ArrayList<Course> searchResultsList = new ArrayList<>();

    GsonBuilder gsonBuilder;
    Gson gson;

    public ArrayList<Course> getSearchResultsList() {
        return searchResultsList;
    }

    public SearchQuery(){
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
    }

    public String getKeywords() {return keywords;}

    public rankBy getRankMethod() {return rank;}

    public void setRankMethod(rankBy method) {rank = method;}

    public void setQuery(String query) {keywords = query;}
    PriorityQueue<Document> searchResultsByLocation;

    public ArrayList<Course> searchForCourse(App app, MongoDatabase mongoDatabase, Context context, double radius, Document userLoc){
        if (app.currentUser()!=null) {
            Log.v("courseSearch", keywords);
            final User user = app.currentUser();
            assert user != null;
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("CourseData");
            searchResultsList.clear();

            //Blank query to find every single course in db
            Document regQuery = new Document();

            if (!keywords.equals("")){
                regQuery.append("$regex", "(?)" + Pattern.quote(keywords));
                regQuery.append("$options", "i");
            }

            Document queryFilter  = new Document();

            if (!keywords.equals("")) {
                queryFilter.append("courseName", regQuery);
            }

            Document sortingMethod = new Document();


            if (rank == rankBy.LOC){
            }
            else if (rank == rankBy.RATING){
                sortingMethod.append("courseRating", -1);
            }
            else if (rank == rankBy.PRICE){
                sortingMethod.append("courseFees", 1);
            }
            else if (rank == rankBy.ONLINE_ONLY_RATING){
                queryFilter.append("courseMode", "Online");
                sortingMethod.append("courseRating", -1);
            }
            else{
                sortingMethod.append("courseRating", -1);
            }

            FindIterable<Document> queryResultIterable = mongoCollection.find(queryFilter);
            queryResultIterable.sort(sortingMethod);
            RealmResultTask<MongoCursor<Document>> findCourses = queryResultIterable.iterator();

            searchResultsByLocation = new
                    PriorityQueue<Document>(5, new LocationSorter());

            findCourses.getAsync(task -> {
                if (task.isSuccess()) {
                    MongoCursor<Document> results = task.get();
                    Log.v("COURSEHandler", "successfully found all courses:");
                    while (results.hasNext()) {
                        Document document = results.next();

                        Course course = gson.fromJson(document.toJson(),Course.class);

                        String name = document.getString("courseName");
                        String info = "NULL";
                        if (rank == rankBy.LOC){
                            if (!document.getString("courseMode").equals("Online")) {
                                Log.v("Distance","Calling Function");
                                double courseDist = calculateDistance((Document) document.get("courseLocation"), userLoc);
                                Log.v("LocationSearch", document.getString("courseName").concat(" ").concat(Double.toString(courseDist)));
                                document.append("courseDistance", courseDist);
                                info = document.getDouble("courseDistance").toString();
                                searchResultsByLocation.add(document);
                            }
                        }else{
                            searchResultsList.add(course);
                        }
                        if (rank == rankBy.RATING){
                            //info = document.getDouble("courseRating").toString();
                        }
                        else if (rank == rankBy.PRICE){
                            info = document.getInteger("courseFees").toString();
                        }
                        else{
                            //info = document.getDouble("courseRating").toString();
                        }
                        Log.v("CourseSearch",name);
                        Log.v("CourseSearch", info);
                    }
                    if (rank == rankBy.LOC) {
                        //NOTE: printing by distance:
                        Log.v("CourseSearch", "LocationSort");
                        while (!searchResultsByLocation.isEmpty()) {
                            Document d = searchResultsByLocation.poll();
                            Course course = gson.fromJson(d.toJson(), Course.class);
                            searchResultsList.add(course);
                            String name = d.getString("courseName");
                            String info = d.getDouble("courseDistance").toString();
                            Log.v("CourseSearch", name);
                            Log.v("CourseSearch", info);
                        }
                    }
                    //showSearchResults(context, courseAdapter, recyclerView);

                } else {
                    Log.e("COURSESearch", "failed to find courses with: ", task.getError());
                }
            });
            Log.d(TAG, "searchForCourse: Success!");
            return searchResultsList;
        }
        return null;
    }

    public double calculateDistance(Document courseLoc, Document userLoc){

        double lat1 = 0,lon1 = 0,lat2 = 0,lon2 = 0;
        try {
            lat1 = courseLoc.getDouble("latitude");
            lon2 = userLoc.getDouble("longitude");
            lat2 = userLoc.getDouble("lattitude");
            lon1 = courseLoc.getDouble("longitude");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v("Distance","calculating");
        double R = 6378;
        double dLat = Math.PI*Math.abs(lat2-lat1)/180;
        double dLon = Math.PI*Math.abs(lon2-lon1)/180;
        Log.v("calcDist", Double.toString(lat1).concat(" ").concat(Double.toString(lon1)).concat(" ").concat(Double.toString(lat2)).concat(" ").concat(Double.toString(lon2)));
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(Math.PI*(lat1)/180) * Math.cos(Math.PI*(lat2)/180) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        // Distance in km
        return R * c;
    }

    public void searchForCourse(App app, MongoDatabase mongoDatabase, Context context, CoursesAdapter1 coursesAdapter1, RecyclerView recyclerView, double radius, Document userLoc){
        //assert if the user is signed in
        if (app.currentUser()!=null) {
            Log.v("courseSearch", keywords);
            final User user = app.currentUser();
            assert user != null;
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("CourseData");
            searchResultsList.clear();
            searchResultsList = new ArrayList<>();

            //Blank query to find every single course in db
            Document queryFilter  = new Document();
            //Regex Query
            Document regQuery = new Document();

            //If there are no keywords, no need to add a regex filter
            //because apparently blank queries mess regex up
            if (!keywords.equals("")){
                regQuery.append("$regex", "(?)" + Pattern.quote(keywords));
                regQuery.append("$options", "i");
                queryFilter.append("courseName", regQuery);
            }

            //Sorting method filter
            Document sortingMethod = new Document();

            //Server side ranking filters
            if (rank == rankBy.LOC){
                //Handled client side, so no need of any
                //server side sorting
            }
            else if (rank == rankBy.RATING){
                sortingMethod.append("courseRating", -1);
            }
            else if (rank == rankBy.PRICE){
                sortingMethod.append("courseFees", 1);
                sortingMethod.append("courseRating", -1);
            }
            else if (rank == rankBy.ONLINE_ONLY_RATING){
                queryFilter.append("courseMode", "Online");
                sortingMethod.append("courseRating", -1);
            }
            else{
                sortingMethod.append("courseRating", -1);
            }

            //Make the query
            FindIterable<Document> queryResultIterable = mongoCollection.find(queryFilter);
            queryResultIterable.sort(sortingMethod);
            RealmResultTask<MongoCursor<Document>> findCourses = queryResultIterable.iterator();

            //Location based uses a custom comparator priority queue to rank the courses
            searchResultsByLocation = new
                    PriorityQueue<Document>(5, new LocationSorter());

            //Results get async task
            findCourses.getAsync(task -> {
                if (task.isSuccess()) {
                    MongoCursor<Document> results = task.get();
                    Log.v("COURSEHandler", "successfully found all courses:");
                    searchResultsList.clear();
                    searchResultsByLocation = new
                            PriorityQueue<Document>(5, new LocationSorter());
                    while (results.hasNext()) {
                        Document document = results.next();
                        //Cast the JSON data into the course class structure
                        Course course = gson.fromJson(document.toJson(),Course.class);

                        //Log the course name
                        String name = course.getCourseName();
                        Log.v("CourseSearch",name);

                        //Populate the results
                        if (rank == rankBy.LOC){
                            //Online courses are not considered for distance ranking
                            if (!document.getString("courseMode").equals("Online")) {
                                if (categoryCheck(course.getCourseCategories())) {
                                    Log.v("Distance", "Calling Function");
                                    if (document.get("courseLocation") != null) {
                                        double courseDist = calculateDistance((Document) document.get("courseLocation"), userLoc);
                                        //course.setDistance(courseDist);
                                        Log.v("LocationSearch", document.getString("courseName").concat(" ").concat(Double.toString(courseDist)));
                                        document.append("courseDistance", courseDist);
                                        searchResultsByLocation.add(document);
                                    } else
                                    {
                                        Log.v("NotFound", String.valueOf(document));
                                    }
                                }
                            }
                        }else{
                            //else, it doesn't matter if its online or offline
                            if (categoryCheck(course.getCourseCategories())) {
                                searchResultsList.add(course);
                            }
                        }
                    }

                    //Logging results for location based ranking
                    //This also adds the sorted results to the recyclerview container
                    if (rank == rankBy.LOC) {
                        //NOTE: printing by distance:
                        Log.v("CourseSearch", "LocationSort");
                        while (!searchResultsByLocation.isEmpty()) {
                            Document d = searchResultsByLocation.poll();
                            Course course = gson.fromJson(d.toJson(), Course.class);
                            searchResultsList.add(course);
                            String name = d.getString("courseName");
                            String info = d.getDouble("courseDistance").toString();
                            Log.v("CourseSearch", name);
                            Log.v("CourseSearch", info);
                        }
                    }

                    for (int p = 0; p < searchResultsList.size(); p++){
                        Log.v("COURSEDISTANCE", searchResultsList.get(p).getCourseName());
                        searchResultsList.get(p).setDistanceFromUserLoc(userLoc);
                        Log.v("COURSEDISTANCE", Double.toString(getCourseDistance(searchResultsList.get(p), userLoc)));
                    }

                    //Shows the results in recyclerview
                    //assert coursesAdapter1 != null;
                    //assert recyclerView != null;
                    showSearchResults(context, coursesAdapter1, recyclerView);

                } else {
                    Log.e("COURSESearch", "failed to find courses with: ", task.getError());
                }
            });
        }
    }

    public void showSearchResults(Context context, CoursesAdapter1 courseAdapter, RecyclerView recyclerView) {
        courseAdapter = new CoursesAdapter1(searchResultsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(courseAdapter);
    }

    //Checks if the categories match
    //If there are no selected categories, every course
    //will appear in the results
    public boolean categoryCheck(ArrayList<String> categories) {
        if(selectedTags == null) {
            Log.d(TAG, "categoryCheck: selectedTags is now null!");
            return true;
        }
        if (selectedTags.size() == 0){
            Log.d(TAG, "categoryCheck: No Tags Attached!");
            return true;
        }
        if (categories == null) {
            Log.d(TAG, "categoryCheck: Tag mismatch: Course Categories Are NULL!");
            return false;
        }
        for (int i = 0; i < categories.size(); i++){
            if (selectedTags.containsKey(categories.get(i))){
                Log.v("CategoryCheck", "Tag match!");
                return true;
            }
        }
        Log.v("CategoryCheck", "No tags match!");
        return false;
    }

    public double getCourseDistance(Course course, Document userLoc){
        try {
            return calculateDistance((Document) course.getCourseLocation(), userLoc);
        }
        catch(Exception e){
            Log.v("DISTANCE CALC | ", "Something went wrong! Most probably an invalid course was passed!");
            return -1;
        }
    }
}
