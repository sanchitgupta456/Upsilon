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

public class SearchQuery {
    String keywords = "";

    rankBy rank = rankBy.RATING;

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

    public ArrayList<Course> searchForCourse(App app, MongoDatabase mongoDatabase, Context context, CoursesAdapter1 courseAdapter, RecyclerView recyclerView, double radius){
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
                        double courseDist = calculateDistance(document.get("courseLocation"), user);

                        document.append("courseDistance", courseDist);

                        Course course = gson.fromJson(document.toJson(),Course.class);
                        searchResultsList.add(course);
                        String name = document.getString("courseName");
                        String info = "NULL";
                        if (rank == rankBy.LOC){
                            info = document.getDouble("courseDistance").toString();
                            searchResultsByLocation.add(document);
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

                        showSearchResults(context, courseAdapter, recyclerView);
                        Log.v("CourseSearch",name);
                        Log.v("CourseSearch", info);
                    }

                } else {
                    Log.e("COURSESearch", "failed to find courses with: ", task.getError());
                }
            });

            //NOTE: printing by distance:
            /*
            Log.v("CourseSearch","LocationSort");
            while (!searchResultsByLocation.isEmpty()){
                Document d = searchResultsByLocation.peek();
                String name = d.getString("courseName");
                String info = d.getDouble("courseDistance").toString();
                Log.v("CourseSearch",name);
                Log.v("CourseSearch", info);
            }
            user.getId();
            */
        }
        return searchResultsList;
    }

    public double calculateDistance(Object courseLoc, User user){
        /*
        MongoCollection<Document> mongoCollection2  = mongoDatabase.getCollection("UserData");
        //Blank query to find every single course in db

        Document queryFilter2 = new Document("userid", user.getId());

        RealmResultTask<MongoCursor<Document>> findTask2 = mongoCollection2.find(queryFilter2).iterator();

        findTask2.getAsync(task2 -> {
            if (task2.isSuccess()) {
                MongoCursor<Document> results1 = task2.get();
                if (!results1.hasNext()) {
                    Log.v("ViewCourse", "Couldnt Find The Tutor");
                } else {
                    Log.v("User", "successfully found the Tutor");
                }
                while (results1.hasNext()) {
                    //Log.v("EXAMPLE", results.next().toString());
                    Document currentDoc1 = results1.next();

                }
            } else {
                Log.v("User", "Failed to complete search");
            }
        });
        */
        double lat1 = 0,lon1 = 0,lat2 = 0,lon2 = 0;
        double R = 6371;
        double dLat = Math.PI*(lat2-lat1)/180;
        double dLon = Math.PI*(lon2-lon1)/180;

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
