package com.sanchit.Upsilon;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseAdapter2;
import com.sanchit.Upsilon.courseData.CoursesAdapter1;
import com.sanchit.Upsilon.courseSearching.LocationSorter;
import com.sanchit.Upsilon.courseSearching.SearchQuery;
import com.sanchit.Upsilon.courseSearching.rankBy;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.regex.Pattern;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.FindIterable;
import io.realm.mongodb.mongo.iterable.MongoCursor;

import static android.view.View.GONE;

public class ExploreActivity extends AppCompatActivity {
    //vars
    private RecyclerView recyclerView, recyclerViewFilterList;
    ArrayList<Course> courseArrayList;
    String appID = "upsilon-ityvn";
    private static final String TAG = "MainActivity";
    private CoursesAdapter1 courseAdapter;
    private App app;
    private LinearLayoutManager linearLayoutManager;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    private User user;
    private Gson gson;
    private GsonBuilder gsonBuilder;
    private ProgressBar progressBar;
    private View bar;

    ArrayList<String> tags;
    ArrayList<Boolean> isChecked;

    //SearchQuery Ranking method
    SearchQuery searchQuery = new SearchQuery();
    ArrayList<Course> searchResultsList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_courses);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.explore);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(10);
        bar = getSupportActionBar().getCustomView();
        /*ImageButton imageButton = (ImageButton) bar.findViewById(R.id.imgBtnBackExploreCourses);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExploreActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });*/
        recyclerView = findViewById(R.id.exploreList);
        recyclerViewFilterList = findViewById(R.id.filter_categories_list);
        courseArrayList = new ArrayList<>();
        app = new App(new AppConfiguration.Builder(appID).build());
        courseAdapter = new CoursesAdapter1(courseArrayList);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
        progressBar = findViewById(R.id.loadingExplore);

        repopulateAll();

    }

    public void repopulateAll(){
        courseArrayList.clear();
        user = app.currentUser();

        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");

        Document queryFilter  = new Document();

        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

        //final int[] flag = {0};
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                Log.v("COURSEHandler", "successfully found all courses:");
                while (results.hasNext()) {
                    progressBar.setVisibility(View.VISIBLE);
                    //Log.v("EXAMPLE", results.next().toString());
                    Document currentDoc = results.next();

                    //Log.v("IMPORTANT","Error:"+currentDoc.getString("nextLectureOn"));

                    if(currentDoc.getString("nextLectureOn")==null)
                    {
                        currentDoc.append("nextLectureOn","0");
                    }
                    currentDoc.toJson();
                    gsonBuilder = new GsonBuilder();
                    gson = gsonBuilder.create();

                    try {
                        Course course = gson.fromJson(currentDoc.toJson(),Course.class);

                        courseArrayList.add(course);
                        courseAdapter.notifyDataSetChanged();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    if(!results.hasNext())
                    {
                        progressBar.setVisibility(GONE);
                    }
                }
            } else {
                Log.e("COURSEHandler", "failed to find courses with: ", task.getError());
            }
        });
        initFilters();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.explore_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_explore).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery.setQuery(query);
                searchQuery.setRankMethod(rankBy.LOC);
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
                            searchQuery.searchForCourse(app, mongoDatabase,ExploreActivity.this, courseAdapter, recyclerView,  10, userLoc);
                        }
                        Log.v("PURGE", "THE PURGE WAS A SUCCESS!");
                    } else {
                        Log.v("User","Failed to complete search");
                    }
                });
                return false;
            }

        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    //TODO: get all courses
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getActionView()==bar){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //search

    public void initFilters() {
        getFilters();

        FilterAdapter filterAdapter = new FilterAdapter(tags, isChecked, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewFilterList.setAdapter(filterAdapter);
        recyclerViewFilterList.setLayoutManager(manager);

    }

    public void getFilters() {
        tags = new ArrayList<>();
        isChecked = new ArrayList<>();
        tags.add("Computer Science");
        tags.add("Arts");
        tags.add("Languages");
        tags.add("Co-curricular");
        tags.add("Music");
        isChecked.add(false);
        isChecked.add(false);
        isChecked.add(false);
        isChecked.add(false);
        isChecked.add(false);
    }
}
