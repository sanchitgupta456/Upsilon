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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.regex.Pattern;

import io.realm.mongodb.App;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.FindIterable;
import io.realm.mongodb.mongo.iterable.MongoCursor;

import static android.view.View.GONE;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class SearchQuery {
    String keywords = "";

    rankBy rank = rankBy.LOC;

    ArrayList<Course> searchResultsList = new ArrayList<>();

    GsonBuilder gsonBuilder;
    Gson gson;

    public SearchQuery(){
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
    }

    public String getKeywords() {return keywords;}

    public rankBy getRankMethod() {return rank;}

    public void setRankMethod(rankBy method) {rank = method;}

    public void setQuery(String query) {keywords = query;}

    public ArrayList<Course> searchForCourse(App app, MongoDatabase mongoDatabase, Context context, CoursesAdapter1 courseAdapter, RecyclerView recyclerView, double radius, Document userLoc){
        if (app.currentUser()!=null) {
            Log.v("courseSearch", keywords);
            final User user = app.currentUser();
            assert user != null;
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("CourseData");
            searchResultsList.clear();

            //Blank query to find every single course in db
            Document regQuery = new Document();
            regQuery.append("$regex", "(?)" + Pattern.quote(keywords));
            regQuery.append("$options", "i");

            Document queryFilter  = new Document();
            queryFilter.append("courseName", regQuery);

            Document sortingMethod = new Document();


            if (rank == rankBy.LOC){
                sortingMethod.append("courseRating", 1);
            }
            else if (rank == rankBy.RATING){
                sortingMethod.append("courseRating", -1);
            }
            else if (rank == rankBy.PRICE){
                sortingMethod.append("courseFees", 1);
            }
            else{
                sortingMethod.append("courseRating", -1);
            }

            FindIterable<Document> queryResultIterable = mongoCollection.find(queryFilter);
            queryResultIterable.sort(sortingMethod);
            RealmResultTask<MongoCursor<Document>> findCourses = queryResultIterable.iterator();

            PriorityQueue<Document> searchResultsByLocation = new
                    PriorityQueue<Document>(5, new LocationSorter());

            findCourses.getAsync(task -> {
                if (task.isSuccess()) {
                    MongoCursor<Document> results = task.get();
                    Log.v("COURSEHandler", "successfully found all courses:");
                    while (results.hasNext()) {
                        Document document = results.next();

                        //TODO: implement course distance calculation

                        Course course = gson.fromJson(document.toJson(),Course.class);
                        searchResultsList.add(course);
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
                        }
                        else if (rank == rankBy.RATING){
                            info = document.getDouble("courseRating").toString();
                        }
                        else if (rank == rankBy.PRICE){
                            info = document.getInteger("courseFees").toString();
                        }
                        else{
                            info = document.getDouble("courseRating").toString();
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
                    showSearchResults(context, courseAdapter, recyclerView);

                } else {
                    Log.e("COURSESearch", "failed to find courses with: ", task.getError());
                }
            });
        }
        return searchResultsList;
    }

    public double calculateDistance(Document courseLoc, Document userLoc){
        double lat1 = courseLoc.getDouble("latitude"),lon1 = courseLoc.getDouble("longitude"),lat2 = userLoc.getDouble("latitude"),lon2 = userLoc.getDouble("longitude");
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

    public void showSearchResults(Context context, CoursesAdapter1 courseAdapter, RecyclerView recyclerView) {
        courseAdapter = new CoursesAdapter1(searchResultsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(courseAdapter);
    }
}
