package com.sanchit.Upsilon;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.courseData.CourseFinal;
import com.sanchit.Upsilon.courseData.CoursesAdapter1;
import com.sanchit.Upsilon.courseSearching.SearchQuery;
import com.sanchit.Upsilon.courseSearching.rankBy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;

import static io.realm.Realm.getApplicationContext;

public class ExploreFragment2 extends Fragment {
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
    private boolean loading;
    SearchQuery searchQuery = new SearchQuery();
    private RequestQueue queue;
    private String API ;
    ArrayList<CourseFinal> list = new ArrayList<>();
    private boolean allLoaded = false;

    public rankBy sortCriteria = rankBy.PRICE;

    String query = "";
    ArrayList<String> selectedTags;
    String regex ="";
    public ExploreFragment2(String string) {
        query = string;
    }

    public ExploreFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore2, container, false);
        recyclerView = (RecyclerView)  view.findViewById(R.id.exploreList2);
        loading = false;
        queue = Volley.newRequestQueue(getApplicationContext());
        API = ((Upsilon)getActivity().getApplication()).getAPI();
        loadOnce();
//        app = new App(new AppConfiguration.Builder(appID).build());
//        user = app.currentUser();
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("Upsilon");
//        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");

//        searchQuery.setRankMethod(sortCriteria);
//        searchForCourses(query);
//        performSearch();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!loading) {
                    //list is the course list currently being displayed
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition()==list.size()-1){
                        loadMore();
//                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        return view;


    }

    public void loadOnce(){
        loading = true;
        list = new ArrayList<CourseFinal>();
        initRecyclerView(recyclerView, list);
        performSearch();
    }
    public void loadMore(){
        if(allLoaded)
        {
            return;
        }
        loading = true;
        performSearch();
    }

    public void initRecyclerView(RecyclerView recyclerView, ArrayList<CourseFinal> list) {
        Log.d(TAG, "initRecyclerView: now displaying " + recyclerView.getId());
        adapter = new CoursesAdapter1(list,((Upsilon)getActivity().getApplication()).getAPI() , ((Upsilon)getActivity().getApplication()).getToken() , ((Upsilon) getActivity().getApplication()).getUser());
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "initRecyclerView: display success! Displayed " + list.size() + " items");
    }
    public void searchForCourses(SearchQuery _searchQuery){
        this.query = _searchQuery.getKeywords();
        searchQuery.setQuery(query);
        searchQuery.setSelectedTags(_searchQuery.getSelectedTags());
        selectedTags = new ArrayList<>();
        regex = _searchQuery.getKeywords();
//        for(int i=0;i<_searchQuery.getSelectedTags().size();i++)
//        {
//            if(_searchQuery.getSelectedTags().get(i).booleanValue()==true)
//            {
//                selectedTags.add(_searchQuery.getSelectedTags().get(i).toString());
//            }
//        }
        for ( String key : _searchQuery.getSelectedTags().keySet() ) {
            selectedTags.add(key);
        }
        Log.v("Tags", String.valueOf(selectedTags));
        list.clear();
        loadOnce();
    }
//    public void searchForCourses(String query){
//        this.query = query;
//        searchQuery.setQuery(query);
//        performSearch(selectedTags);
//    }
    public void performSearch() {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("index",Integer.parseInt(String.valueOf(list.size()/5)));
            jsonBody.put("filter","Fees");
            Gson gson = new Gson();
            jsonBody.put("tags",gson.toJson(selectedTags));
            jsonBody.put("regex",regex);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/paging",jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", response.toString());
//                            list = new ArrayList<CourseFinal>();
                            try {
                                JSONArray jsonArray = (JSONArray) response.get("courses");
                                if(jsonArray.length()==0)
                                {
                                    allLoaded=true;
                                }
                                Log.v("array",String.valueOf(jsonArray));
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    Gson gson= new Gson();
                                    CourseFinal course = gson.fromJson(jsonObject.toString(),CourseFinal.class);
                                    list.add(course);
                                    adapter.notifyDataSetChanged();
                                    Log.v("course",String.valueOf(course.getCourseReviews()));
                                }
//                                initRecyclerView(recyclerView, list);
//                                initRecyclerView(recyclerView, list);
                                loading = false;
                            } catch (JSONException e) {
                                loading = false;
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
//        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
//
//        //Blank query to find every single user in db
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
//                    if(userLoc!=null) {
//                        searchQuery.searchForCourse(app, mongoDatabase, getContext(), adapter, recyclerView, 10, userLoc);
//                    }
//                    else
//                    {
//                        Snackbar.make(getView(),"Please setup your location to view courses near you",Snackbar.LENGTH_LONG).show();
//                    }
//                }
//            } else {
//                Log.v("User","Failed to complete search");
//            }
//        });
////        list = searchQuery.getSearchResultsList();
//        initRecyclerView(recyclerView,list);
    }
}