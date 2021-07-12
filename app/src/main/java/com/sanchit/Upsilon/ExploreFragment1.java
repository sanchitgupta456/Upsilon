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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseFinal;
import com.sanchit.Upsilon.courseData.CoursesAdapter1;
import com.sanchit.Upsilon.courseSearching.SearchQuery;
import com.sanchit.Upsilon.courseSearching.rankBy;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

import static io.realm.Realm.getApplicationContext;

public class ExploreFragment1 extends Fragment {
    private static final String TAG = "Top Rated";
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
    private RequestQueue queue;
    private String API ;

    ArrayList<Course> list = new ArrayList<>();

    public rankBy sortCriteria = rankBy.RATING;

    String query = "";

    public ExploreFragment1(String string) {
        query = string;
    }

    public ExploreFragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore1, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.exploreList1);
        queue = Volley.newRequestQueue(getApplicationContext());
        API = ((Upsilon)getActivity().getApplication()).getAPI();

//        app = new App(new AppConfiguration.Builder(appID).build());
//        user = app.currentUser();
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("Upsilon");
//        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("CourseData");

//        searchQuery.setRankMethod(sortCriteria);
//        searchForCourses(query);
        performSearch();
        return view;

    }

    public void searchForCourses(SearchQuery _searchQuery) {
        this.query = _searchQuery.getKeywords();
        searchQuery.setQuery(query);
        searchQuery.setSelectedTags(_searchQuery.getSelectedTags());
        performSearch();
    }

    public void searchForCourses(String query) {
        this.query = query;
        searchQuery.setQuery(query);
        performSearch();
    }

    public void performSearch() {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("index",0);
            jsonBody.put("filter","Rating");
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/paging",jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", response.toString());
                            list = new ArrayList<Course>();
                            try {
                                JSONArray jsonArray = (JSONArray) response.get("courses");
                                Log.v("array",String.valueOf(jsonArray));
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    Gson gson= new Gson();
                                    CourseFinal course = gson.fromJson(jsonObject.toString(),CourseFinal.class);
                                    Log.v("course",String.valueOf(course.getCourseReviews()));
                                }
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
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("Upsilon");
//        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");
//
//        //Blank query to find every single user in db
//        Document queryFilter = new Document("userid", user.getId());
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
//                    if(userLoc!=null) {
//                        searchQuery.searchForCourse(app, mongoDatabase, getContext(), adapter, recyclerView, 10, userLoc);
//                    }
//                    else
//                    {
//                        Snackbar.make(getView(),"Please setup your location to view courses near you",Snackbar.LENGTH_LONG).show();
//                    }
//                }
//            } else {
//                Log.v("User", "Failed to complete search");
//            }
//        });
//        list = searchQuery.getSearchResultsList();
//        initRecyclerView(recyclerView, list);
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