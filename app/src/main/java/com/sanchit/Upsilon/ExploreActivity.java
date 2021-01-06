package com.sanchit.Upsilon;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

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
    private static final String TAG = "ExploreActivity";
    //vars
    //private RecyclerView recyclerViewFilterList;
    String appID = "upsilon-ityvn";
    private App app;
    private LinearLayoutManager linearLayoutManager;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    private User user;
    private Gson gson;
    private GsonBuilder gsonBuilder;
    private ProgressBar progressBar;
    private View bar;

    ArrayList<String> all_tags;
    ArrayList<Boolean> isChecked;
    //selected tags
    //tags are now a part of the searchQuery class
    ChipGroup chipGroup;

    //tabLayout
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;

    //fragments
    ExploreFragment0 fragment0;
    ExploreFragment1 fragment1;
    ExploreFragment2 fragment2;
    ExploreFragment3 fragment3;

    //SearchView
    SearchView searchView;
    //SearchQuery Ranking method
    SearchQuery searchQuery = new SearchQuery();
    //User location
    Document userLoc = new Document();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_courses);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.explore);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        bar = getSupportActionBar().getCustomView();
        /*ImageButton imageButton = (ImageButton) bar.findViewById(R.id.imgBtnBackExploreCourses);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExploreActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });*/

        progressBar = (ProgressBar) findViewById(R.id.loadingExplore);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragment0 = new ExploreFragment0();
        fragment1 = new ExploreFragment1();
        fragment2 = new ExploreFragment2();
        fragment3 = new ExploreFragment3();

        //recyclerViewFilterList = findViewById(R.id.filter_categories_list);
        chipGroup = (ChipGroup) findViewById(R.id.search_tags_group);
        app = new App(new AppConfiguration.Builder(appID).build());
        progressBar = findViewById(R.id.loadingExplore);

        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");
        initFiltersGroup();

        viewPager = (ViewPager)(findViewById(R.id.viewPager));
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.explore_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_explore).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery.setQuery(newText);
                searchForCourses(searchQuery);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery.setQuery(query);
                searchForCourses(searchQuery);
                return false;
            }

        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){

                }
            }
        });
        return true;
    }

    public void searchForCourses(SearchQuery searchQuery){
        //No need to update if query is same
        /*if (searchQuery.getKeywords().equals(query)){
            return;
        }*/
        switch (tabLayout.getSelectedTabPosition()) {
            case 0:
                fragment0.searchForCourses(searchQuery);
                break;
            case 1:
                fragment1.searchForCourses(searchQuery);
                break;
            case 2:
                fragment2.searchForCourses(searchQuery);
                break;
            case 3:
                fragment3.searchForCourses(searchQuery);
                break;
            default:
                break;
        }
        /*
        searchQuery.setQuery(query);
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
                    searchQuery.searchForCourse(app, mongoDatabase,ExploreActivity.this,  10, userLoc);
                    //init Recycler
                }
                Log.v("PURGE", "THE PURGE WAS A SUCCESS!");
            } else {
                Log.v("User","Failed to complete search");
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.topFree:
                break;
            case R.id.topRated:
                break;
            case R.id.nearYou:
                break;
            case R.id.topOnline:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //search

    /*
    public void initFilters() {
        getFilters();

        FilterAdapter filterAdapter = new FilterAdapter(all_tags, isChecked, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewFilterList.setAdapter(filterAdapter);
        recyclerViewFilterList.setLayoutManager(manager);

    }*/

    public void initFiltersGroup() {
        getFilters();

        LayoutInflater inflater = getLayoutInflater();
        for (int i = 0; i < all_tags.size(); i++) {
            Chip chip = (Chip) inflater.inflate(R.layout.chip_filter, chipGroup, false);
            chip.setText(all_tags.get(i));
            chip.setChecked(isChecked.get(i));

            //Callback for change in the selected tags.
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b) {
                        searchQuery.selectedTags.put(compoundButton.getText().toString(), true);
                        //Tags have changed. Search for them, update the results
                        Log.v("TAGS", compoundButton.getText().toString());
                        searchForCourses(searchQuery);
                    } else {
                        searchQuery.selectedTags.remove(compoundButton.getText().toString());
                        //Tags have changed. Search for them, update the results
                        Log.v("TAGS", compoundButton.getText().toString());
                        searchForCourses(searchQuery);
                    }
                }
            });
            chipGroup.addView(chip);
        }
    }

    public void getFilters() {
        all_tags = new ArrayList<>();
        isChecked = new ArrayList<>();
        String[] categories = getResources().getStringArray(R.array.categories);
        for (String tag : categories) {
            all_tags.add(tag);
            isChecked.add(false);
        }
    }

    public void initRecyclerView(RecyclerView recyclerView, ArrayList<Course> list) {
        Log.d(TAG, "initRecyclerView: now displaying " + recyclerView.getId());
        CoursesAdapter1 coursesAdapter1 = new CoursesAdapter1(list);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(coursesAdapter1);
        Log.d(TAG, "initRecyclerView: display success! Displayed " + list.size() + " items");
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter.clearFragments();
        adapter.addFragment(fragment0, "Near me");
        adapter.addFragment(fragment1, "Top Rated");
        adapter.addFragment(fragment2, "Top Free");
        adapter.addFragment(fragment3, "Top Online");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void clearFragments() {
            mFragmentList.clear();
            mFragmentTitleList.clear();
        }

    }
}
