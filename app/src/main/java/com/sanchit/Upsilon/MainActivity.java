package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseFinal;
import com.sanchit.Upsilon.courseData.CoursesAdapter;
import com.sanchit.Upsilon.courseData.CoursesAdapter1;
import com.sanchit.Upsilon.courseSearching.SearchQuery;
import com.sanchit.Upsilon.courseSearching.rankBy;
import com.sanchit.Upsilon.notifications.NotifService;
import com.sanchit.Upsilon.notifications.UpsilonJobService;
import com.sanchit.Upsilon.ui.login.LoginActivity;
import com.sanchit.Upsilon.ui.login.SignUpActivity;
import com.squareup.picasso.Picasso;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

import static android.view.View.GONE;
import static com.sanchit.Upsilon.notifications.NotifChannel.CHANNEL_ID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private float x1,x2;
    boolean swapped = false;
    static final int MIN_DISTANCE = 500;
    DrawerLayout drawer;
    TextView explore;

    String appID = "upsilon-ityvn";
    private static final String TAG = "MainActivity";

    private Gson gson;
    private GsonBuilder gsonBuilder;
    private ArrayList<String> myCourses = new ArrayList<>();

    RelativeLayout main;
    RecyclerView recyclerView,recyclerView1,recyclerView2,recyclerViewSearchResults;
    CardView frame, frame1, frame2, alter;
    CoursesAdapter1 coursesAdapter;
    CoursesAdapter1 searchResultsAdapter;
    CoursesAdapter1 coursesAdapter1_1;
    CoursesAdapter coursesAdapter1;
    CoursesAdapter coursesAdapter2;
    ArrayList<CourseFinal> courseArrayList = new ArrayList<>();
    ArrayList<CourseFinal> courseArrayList1 = new ArrayList<>();
    ArrayList<CourseFinal> courseArrayList2 = new ArrayList<>();
    ArrayList<Course> searchResultsList = new ArrayList<>();
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    CircleImageView imageView;
    TextView tvEmail, tvName, tvPhone;
    Button btnEditProfile;
    ImageButton profileMoreToggle;
    LinearLayout profileMore;
    ActionBarDrawerToggle toggle;

    SearchView searchView;

    private ProgressBar progressBar;
    public static final List<String> TvShows  = new ArrayList<String>();
    public static final int[] TvShowImgs = {R.drawable.google, R.drawable.facebook};
    private String college;
    private String email;
    private Menu menu;
    //NOTIFS
    Intent serviceIntent;
    Intent mServiceIntent;
    private NotifService mSensorService;
    private UpsilonJobService upsilonJobService;
    Context ctx;
    public Context getCtx() {
        return ctx;
    }

    //SearchQuery Ranking method
    SearchQuery searchQuery = new SearchQuery();
    //User location
    Document userLoc;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    private RequestQueue queue;
    private String API ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        Intent intent1 = getIntent();
        email = intent1.getStringExtra("email");
        queue = Volley.newRequestQueue(getApplicationContext());
        API = ((Upsilon)this.getApplication()).getAPI();
        /*ctx = this;
        //startService(upsilonJobService);
        mSensorService = new NotifService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }*/

       // Intent i = new Intent(this, UpsilonJobService.class);
       // startService(i);

        //displayNotif();

        //scheduleJob();
        //createNotificationChannel();
        //displayNotif();

        progressBar = (ProgressBar)findViewById(R.id.loadingMain);
        recyclerViewSearchResults = (RecyclerView) findViewById(R.id.search_results_home);
        main = (RelativeLayout) findViewById(R.id.main);
        frame = (CardView) findViewById(R.id.ll);
        frame1 = (CardView) findViewById(R.id.frameCurrentCourseListView);
        frame2 = (CardView) findViewById(R.id.frameRecommendedCourses);
        alter = findViewById(R.id.alter);
        alter.setVisibility(GONE);
        /*
        if(courseArrayList.size()==0){
            frame.setVisibility(View.GONE);
        } else {
            frame.setVisibility(View.VISIBLE);
        }
        if(courseArrayList1.size()==0){
            frame1.setVisibility(View.GONE);
        } else {
            frame1.setVisibility(View.VISIBLE);
        }
        if(courseArrayList2.size()==0){
            frame2.setVisibility(View.GONE);
        } else {
            frame2.setVisibility(View.VISIBLE);
        }*/
//        app = new App(new AppConfiguration.Builder(appID)
//                .build());
//        User user = app.currentUser();

        if ( ((Upsilon)this.getApplication()).getToken() == null || ((Upsilon)this.getApplication()).getUser() == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        else
        {
//            if(email==null)
//            {
//                email = user.getProfile().getEmail();
//            }
//                app.getSync();
//            app.currentUser().getRefreshToken();
//            app.currentUser().getAccessToken();
        }
        //Toolbar toolbar = findViewById(R.id.toolbar);
 //     setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        drawer.closeDrawer(GravityCompat.START);
        View hView =  navigationView.getHeaderView(0);
        imageView = (CircleImageView) hView.findViewById(R.id.profilePhotoHeader);
//        profileMoreToggle = (ImageButton) hView.findViewById(R.id.btn_expand_collapse);
//        tvEmail = (TextView) hView.findViewById(R.id.email);
        tvName = (TextView) hView.findViewById(R.id.name);
//        tvPhone = (TextView) hView.findViewById(R.id.phone);
//        btnEditProfile = (Button) hView.findViewById(R.id.btn_edit_profile);
//        profileMore = (LinearLayout) hView.findViewById(R.id.more);
//        btnEditProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ProfileViewActivity.class);
//                startActivity(intent);
//            }
//        });
//        profileMoreToggle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.animate().rotationBy(180).start();
//                if(profileMore.getVisibility() == GONE)
//                    profileMore.setVisibility(View.VISIBLE);
//                else
//                    profileMore.setVisibility(GONE);
//            }
//        });
        /*
        getActionView().animate().rotationBy(180).start();
            LinearLayout header_more = navigationView.getHeaderView(0).findViewById(R.id.more);
            header_more.setVisibility(View.VISIBLE);
         */
        if(((Upsilon)this.getApplication()).getToken()==null || ((Upsilon)this.getApplication()).getUser() == null)
        {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            updateMenu();
            getCourseData();
//
//            Log.v("RefreshToken",app.currentUser().getRefreshToken().toString());
//            //operationPURGE(user);
//            //operationCalulateDistance();
//            mongoClient = user.getMongoClient("mongodb-atlas");
//            mongoDatabase = mongoClient.getDatabase("Upsilon");
//            MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
//
//            //Blank query to find every single course in db
//            Document queryFilter  = new Document("userid",user.getId());
//
//            RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
//
//            findTask.getAsync(task -> {
//                if (task.isSuccess()) {
//                    MongoCursor<Document> results = task.get();
//                    if(!results.hasNext())
//                    {
//                        mongoCollection.insertOne(
//                                new Document("userid", user.getId()).append("favoriteColor", "pink").append("email",email).append("teacherSetup",false))
//                                .getAsync(result -> {
//                                    if (result.isSuccess()) {
//                                        Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
//                                                + result.get().getInsertedId());
//                                        goToSetupActivity();
//                                    } else {
//                                        Snackbar.make(drawerLayout,"An error occured . Please signIn again",Snackbar.LENGTH_LONG).show();
//                                        Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
//                                    }
//                                });
//                    }
//                    else
//                    {
//                        Log.v("User", "successfully found the user");
////                        getCourseData();
//
//                    }
//                    while (results.hasNext()) {
//                        //Log.v("EXAMPLE", results.next().toString());
//                        Document currentDoc = results.next();
//                        /*if(currentDoc.get("profilePicture") ==null)
//                        {
//                            goToSetupActivity();
//                        }
//                        if(currentDoc.get("username") == null)
//                        {
//                            goToSetupActivity();
//                        }*/
//                        userLoc = (Document) currentDoc.get("userLocation");
//                        Log.v("User",currentDoc.getString("userid"));
//                        try {
//                            Picasso.with(getApplicationContext()).load(currentDoc.getString("profilePicUrl")).error(R.drawable.default_person_image).into(imageView);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            tvEmail.setText(user.getProfile().getEmail());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            tvName.setText(currentDoc.getString("name"));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            if(!currentDoc.getString("phonenumber").equals("0"))
//                            {
//                                tvPhone.setText(currentDoc.getString("phonenumber"));
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        //Log.v("ProfilePic",currentDoc.getString("profilePicUrl"));
//                    }
//                } else {
//                    Snackbar.make(drawerLayout,"An error occured . Please signIn again",Snackbar.LENGTH_LONG).show();
//                    Log.v("User","Failed to complete search");
//                }
//            });
//        }
        }
        explore = (TextView) findViewById(R.id.textExploreCoursesList);
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ExploreActivity.class);
                startActivity(intent);
            }
        });
    }
/*
    private void operationCalulateDistance() {
        final String[] parsedDistance = new String[1];
        final String[] response = new String[1];
        double lat1=19.0317,lon1=72.9049,lat2=19.8,lon2=75.26;
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String key="AIzaSyAp3s5yvUwCfKcOtBsCZNgXlFbubc1eN9Y";
                    URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric&mode=driving&key="+key);
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject)jsonParser.parse(
                            new InputStreamReader(in, "UTF-8"));
                    Log.v("Response", String.valueOf(jsonObject));
                    //JSONObject jsonObject = new JSONObject(response[0]);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distance = steps.getJSONObject("distance");
                    parsedDistance[0] =distance.getString("text");
                    //Log.v("Distance", parsedDistance[0]);
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
*/
    private void goToSetupActivity() {
        Intent intent = new Intent(MainActivity.this,UserDataSetupActivity.class);
        startActivity(intent);
    }

    public void getCourseData(){


        courseArrayList = new ArrayList<>();
        courseArrayList1 = new ArrayList<>();
        courseArrayList2 = new ArrayList<>();


// an authenticated user is required to access a MongoDB instance

            if (((Upsilon)this.getApplication()).getToken() != null && ((Upsilon)this.getApplication()).getUser() != null) {
                progressBar.setVisibility(View.VISIBLE);
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, API+"/myCourses",new JSONObject(),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("FetchMyCourses", response.toString());
                                    courseArrayList1 = new ArrayList<CourseFinal>();
                                    try {
                                        JSONArray jsonArray = (JSONArray) response.get("courses");
                                        Log.v("array",String.valueOf(jsonArray));
                                        for(int i=0;i<jsonArray.length();i++)
                                        {
                                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                            Gson gson= new Gson();
                                            CourseFinal course = gson.fromJson(jsonObject.toString(),CourseFinal.class);
                                            courseArrayList1.add(course);
//                                            coursesAdapter1.notifyDataSetChanged();
                                            frame1.setVisibility(View.VISIBLE);
                                            Log.v("course",String.valueOf(course.getCourseReviews()));
                                            tvName.setText(((Upsilon)getApplication()).getUser().getUsername());
                                        }
                                        progressBar.setVisibility(GONE);
                                        displayCoursesInRecycler();

//                                        initRecyclerView(recyclerView, list);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        progressBar.setVisibility(GONE);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @SuppressLint("LongLogTag")
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("ErrorFetchingMyCourses", error.toString());
                                    progressBar.setVisibility(GONE);
                                }
                            }
                    ){
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("token", ((Upsilon)getApplication()).getToken());
                            return params;
                        }
                    };
                    queue.add(jsonRequest);
                progressBar.setVisibility(View.VISIBLE);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("index",0);
                    jsonObject.put("filter","Rating");
                    JsonObjectRequest jsonRequest1 = new JsonObjectRequest(Request.Method.POST, API+"/paging",jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("FetchMyCourses", response.toString());
                                    courseArrayList = new ArrayList<CourseFinal>();
                                    try {
                                        JSONArray jsonArray = (JSONArray) response.get("courses");
                                        Log.v("array",String.valueOf(jsonArray));
                                        for(int i=0;i<jsonArray.length();i++)
                                        {
                                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                            Gson gson= new Gson();
                                            CourseFinal course = gson.fromJson(jsonObject.toString(),CourseFinal.class);
                                            courseArrayList.add(course);
//                                            coursesAdapter.notifyDataSetChanged();
                                            frame.setVisibility(View.VISIBLE);
                                            Log.v("course",String.valueOf(course.getCourseReviews()));

                                        }
                                        progressBar.setVisibility(GONE);
                                        displayCoursesInRecycler();

    //                                        initRecyclerView(recyclerView, list);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        progressBar.setVisibility(GONE);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @SuppressLint("LongLogTag")
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("ErrorFetchingMyCourses", error.toString());
                                    progressBar.setVisibility(GONE);
                                }
                            }
                    ){
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("token", ((Upsilon)getApplication()).getToken());
                            return params;
                        }
                    };
                    queue.add(jsonRequest1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                final User user = app.currentUser();
//                assert user != null;
//                MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");
//                MongoCollection<Document> mongoCollection2  = mongoDatabase.getCollection("UserData");
                //Blank query to find every single course in db
//                TODO: Modify query to look for user preferred course IDs

//                Document queryFilter2 = new Document("userid", user.getId());

//                RealmResultTask<MongoCursor<Document>> findTask2 = mongoCollection2.find(queryFilter2).iterator();

//                findTask2.getAsync(task2 -> {
//                    if (task2.isSuccess()) {
//                        MongoCursor<Document> results1 = task2.get();
//                        if (!results1.hasNext()) {
//                            Log.v("ViewCourse", "Couldnt Find The Tutor");
//                        } else {
//                            Log.v("User", "successfully found the Tutor");
//                        }
//                        while (results1.hasNext()) {
//                            //Log.v("EXAMPLE", results.next().toString());
//                            Document currentDoc1 = results1.next();
//                            myCourses = (ArrayList<String>) currentDoc1.get("myCourses");
//                            if(myCourses==null)
//                            {
//                                myCourses=new ArrayList<>();
//                            }
//                            college = currentDoc1.getString("college");
//
//                        }
//                    } else {
//                        Log.v("User", "Failed to complete search");
//                    }
//                });




//                Document queryFilter  = new Document();
//                //Document userdata = user.getCustomData();
//                // myCourses = (ArrayList<String>) userdata.get("myCourses");
//
//
//                RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
//
//                final int[] flag = {0};
//                findTask.getAsync(task -> {
//                    if (task.isSuccess()) {
//                        try {
//                            MongoCursor<Document> results = task.get();
//                            Log.v("COURSEHandler", "successfully found all courses:");
//                            while (results.hasNext()) {
//                                //Log.v("EXAMPLE", results.next().toString());
//                                try {
//                                    Document currentDoc = results.next();
//
//                                    //Log.v("IMPORTANT","Error:"+currentDoc.getString("nextLectureOn"));
//
//                                    if(currentDoc.getString("nextLectureOn")==null)
//                                    {
//                                        currentDoc.append("nextLectureOn","0");
//                                    }
//                                    currentDoc.toJson();
//                                    gsonBuilder = new GsonBuilder();
//                                    gson = gsonBuilder.create();
//
//                                    Course course = gson.fromJson(currentDoc.toJson(),Course.class);
//
//                                    flag[0] =0;
//                                    //Log.v("MyCourses", String.valueOf(myCourses));
//                                    try {
//                                        for(int i=0;i<myCourses.size();i++)
//                                        {
//                                            try {
//                                                Log.v("currentCourse", course.getCourseId() + myCourses.get(i));
//                                                if(myCourses.get(i).equals(course.getCourseId()))
//                                                {
//                                                    Log.v("CourseAdded","Added");
//                                                    courseArrayList1.add(course);
//                                                    coursesAdapter1.notifyDataSetChanged();
//                                                    frame1.setVisibility(View.VISIBLE);
//                                                    flag[0] =1;
//                                                    break;
//                                                }
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    if(flag[0] ==1)
//                                    {
//                                        continue;
//                                    }
//                                    if(!course.getTutorId().equals(user.getId())) {
//                                        courseArrayList.add(course);
//                                        coursesAdapter.notifyDataSetChanged();
//                                        frame.setVisibility(View.VISIBLE);
//                                        //courseArrayList2.add(course);
////                                        Document queryFilter1 = new Document("userid", course.getTutorId());
//
////                                        RealmResultTask<MongoCursor<Document>> findTask1 = mongoCollection2.find(queryFilter1).iterator();
//
////                                        findTask1.getAsync(task1 -> {
////                                            if (task1.isSuccess()) {
////                                                MongoCursor<Document> results1 = task1.get();
////                                                if (!results1.hasNext()) {
////                                                    //Log.v("ViewCourse", "Couldnt Find The Tutor");
////                                                } else {
////                                                    //Log.v("User", "successfully found the Tutor");
////                                                }
////                                                while (results1.hasNext()) {
////                                                    //Log.v("EXAMPLE", results.next().toString());
////                                                    Document currentDoc1 = results1.next();
////                                                    //Log.v("CourseBySenior", (String) currentDoc1.get("college"));
////                                                    //Log.v("CourseBySenior", (String)  college);
//                                                    try {
//                                                        if (course.getTutorCollege().equals(college)) {
//                                                            //Log.v("CourseBy","Hello");
//                                                            courseArrayList2.add(course);
//                                                            coursesAdapter2.notifyDataSetChanged();
//                                                            frame2.setVisibility(View.VISIBLE);
//                                                            //Log.v("CourseBySenior", String.valueOf(courseArrayList2));
//                                                        }
//                                                    } catch (Exception e) {
//                                                        e.printStackTrace();
//                                                    }
////                                                    //Log.v("User", currentDoc1.getString("userid"));
////                                                }
////                                            } else {
////                                                //Log.v("User", "Failed to complete search"+task1.getError().toString());
////                                                Credentials credentials = Credentials.jwt(app.currentUser().getRefreshToken());
////                                                //Log.v("RefreshToken","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJiYWFzX2RhdGEiOm51bGwsImJhYXNfZGV2aWNlX2lkIjoiNWZmMWMzOGFmZmRjZmJmNjRiOWU5YWFmIiwiYmFhc19kb21haW5faWQiOiI1Zjg0NmU3M2Y4MzM3YmYyMmI5NjI4YTYiLCJiYWFzX2lkIjoiNWZmMWMzOGFmZmRjZmJmNjRiOWU5YjFkIiwiYmFhc19pZGVudGl0eSI6eyJpZCI6IjVmOGQ3NDNjZWQ3M2VkMTZlYWZiZmFmNyIsInByb3ZpZGVyX3R5cGUiOiJsb2NhbC11c2VycGFzcyIsInByb3ZpZGVyX2lkIjoiNWY4ODg5MjlmNjlmZDllMjQxZjBiZjAxIn0sImV4cCI6MTYxNDg2Mzc1NCwiaWF0IjoxNjA5Njc5NzU0LCJzdGl0Y2hfZGF0YSI6bnVsbCwic3RpdGNoX2RldklkIjoiNWZmMWMzOGFmZmRjZmJmNjRiOWU5YWFmIiwic3RpdGNoX2RvbWFpbklkIjoiNWY4NDZlNzNmODMzN2JmMjJiOTYyOGE2Iiwic3RpdGNoX2lkIjoiNWZmMWMzOGFmZmRjZmJmNjRiOWU5YjFkIiwic3RpdGNoX2lkZW50Ijp7ImlkIjoiNWY4ZDc0M2NlZDczZWQxNmVhZmJmYWY3IiwicHJvdmlkZXJfdHlwZSI6ImxvY2FsLXVzZXJwYXNzIiwicHJvdmlkZXJfaWQiOiI1Zjg4ODkyOWY2OWZkOWUyNDFmMGJmMDEifSwic3ViIjoiNWY4ZDc0NDU1ODNiYjRhYmI3OGJjYzJhIiwidHlwIjoicmVmcmVzaCJ9.pNJzrvq60722wT2zeJlWVIDhEkcmAp_hDcG8g3YQsws");
////                                                app.loginAsync(credentials,it->{
////                                                    if(it.isSuccess())
////                                                    {
////                                                        //Log.v("Success","Authenticated");
////                                                    }
////                                                    else
////                                                    {
////                                                        Log.v("Error",it.getError().toString());
////                                                    }
////                                                });
////                                            }
////                                        });
//                                    }
//                                } catch (JsonSyntaxException e) {
//                                    e.printStackTrace();
//                                }
//                                if(!results.hasNext())
//                                {
//                                    progressBar.setVisibility(GONE);
//                                }
//                            }
//                            if(!results.hasNext())
//                            {
//                                progressBar.setVisibility(GONE);
//                                if(courseArrayList.size()==0)
//                                {
//                                    frame.setVisibility(View.VISIBLE);
//                                    Course nocourse = new Course();
//                                    nocourse.setCourseName("No Courses Found");
//                                    nocourse.setCourseMode("Online");
//                                    nocourse.setCourseImage("https://i.pinimg.com/originals/89/39/06/893906d9df7228cc36e1b3679a0d1dac.png");
//                                    courseArrayList.add(nocourse);
//                                    coursesAdapter.notifyDataSetChanged();
//                                }
//                            }
//                        } catch (Exception e) {
//                            progressBar.setVisibility(GONE);
//                            e.printStackTrace();
//                        }
//                    } else {
//                        Log.e("COURSEHandler", "failed to find courses with: ", task.getError());
//                        Credentials credentials = Credentials.jwt("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJiYWFzX2RhdGEiOm51bGwsImJhYXNfZGV2aWNlX2lkIjoiNWZmMWMzOGFmZmRjZmJmNjRiOWU5YWFmIiwiYmFhc19kb21haW5faWQiOiI1Zjg0NmU3M2Y4MzM3YmYyMmI5NjI4YTYiLCJiYWFzX2lkIjoiNWZmMWMzOGFmZmRjZmJmNjRiOWU5YjFkIiwiYmFhc19pZGVudGl0eSI6eyJpZCI6IjVmOGQ3NDNjZWQ3M2VkMTZlYWZiZmFmNyIsInByb3ZpZGVyX3R5cGUiOiJsb2NhbC11c2VycGFzcyIsInByb3ZpZGVyX2lkIjoiNWY4ODg5MjlmNjlmZDllMjQxZjBiZjAxIn0sImV4cCI6MTYxNDg2Mzc1NCwiaWF0IjoxNjA5Njc5NzU0LCJzdGl0Y2hfZGF0YSI6bnVsbCwic3RpdGNoX2RldklkIjoiNWZmMWMzOGFmZmRjZmJmNjRiOWU5YWFmIiwic3RpdGNoX2RvbWFpbklkIjoiNWY4NDZlNzNmODMzN2JmMjJiOTYyOGE2Iiwic3RpdGNoX2lkIjoiNWZmMWMzOGFmZmRjZmJmNjRiOWU5YjFkIiwic3RpdGNoX2lkZW50Ijp7ImlkIjoiNWY4ZDc0M2NlZDczZWQxNmVhZmJmYWY3IiwicHJvdmlkZXJfdHlwZSI6ImxvY2FsLXVzZXJwYXNzIiwicHJvdmlkZXJfaWQiOiI1Zjg4ODkyOWY2OWZkOWUyNDFmMGJmMDEifSwic3ViIjoiNWY4ZDc0NDU1ODNiYjRhYmI3OGJjYzJhIiwidHlwIjoicmVmcmVzaCJ9.pNJzrvq60722wT2zeJlWVIDhEkcmAp_hDcG8g3YQsws");
//                        app.loginAsync(credentials,it->{
//                            if(it.isSuccess())
//                            {
//                                progressBar.setVisibility(GONE);
//                                getCourseData();
//                                Log.v("Success","Authenticated");
//                            }
//                            else
//                            {
//                                Log.v("Error",it.getError().toString());
//                            }
//                        });
//                    }
//                });

                if(courseArrayList.size()==0){
                    frame.setVisibility(GONE);
                } else {
                    frame.setVisibility(View.VISIBLE);
                }
                if(courseArrayList1.size()==0){
                    frame1.setVisibility(GONE);
                } else {
                    frame1.setVisibility(View.VISIBLE);
                }
                if(courseArrayList2.size()==0){
                    frame2.setVisibility(GONE);
                } else {
                    frame2.setVisibility(View.VISIBLE);
                }
//                if(courseArrayList.size()==0 && courseArrayList1.size()==0 && courseArrayList2.size()==0) {
//                    alter.setVisibility(View.VISIBLE);
//                } else {
//                    alter.setVisibility(GONE);
//                }
//
//
//                MongoCollection<Document> mongoCollection1  = mongoDatabase.getCollection("UserData");
//
//                //Blank query to find every single course in db
//                Document queryFilter1  = new Document();
//                queryFilter1.append("userid",user.getId());
//
//                RealmResultTask<MongoCursor<Document>> findTask1 = mongoCollection1.find(queryFilter1).iterator();
//
//                findTask1.getAsync(task -> {
//                    if (task.isSuccess()) {
//                        MongoCursor<Document> results = task.get();
//                        Log.v("COURSEHandler", "successfully found all courses:");
//
//                        try {
//                            Document document = results.next();
//                            String url = document.getString("profilePicUrl");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        //Toast.makeText(MainActivity.this,url,Toast.LENGTH_LONG).show();
//                        //Log.v("User","Hi"+ url);
//                        //Picasso.with(getApplicationContext()).load(url).into(imageView);
//                    } else {
//                        Log.e("COURSEHandler", "failed to find courses with: ", task.getError());
//                    }
//                });
            }
            else {
                Toast.makeText(getApplicationContext(),"You have been Logged Out because of Inactivity",Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    Log.e(TAG, "Error logging into the Realm app. Make sure that anonymous authentication is enabled.");
            }

//        User user = app.currentUser();
    }

    public void displayCoursesInRecycler(){
        coursesAdapter = new CoursesAdapter1(courseArrayList,((Upsilon)getApplication()).getAPI() , ((Upsilon)getApplication()).getToken() , ((Upsilon)getApplication()).getUser());
        coursesAdapter1 = new CoursesAdapter(courseArrayList1,((Upsilon)getApplication()).getUser().get_Id() , ((Upsilon)getApplication()).getUser().getMyCourses());
        coursesAdapter2 = new CoursesAdapter(courseArrayList2,((Upsilon)getApplication()).getUser().get_Id(), ((Upsilon)getApplication()).getUser().getMyCourses());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView = (RecyclerView)findViewById(R.id.exploreCourseListView);
        recyclerView1 = (RecyclerView)findViewById(R.id.currentCourseListView);
        recyclerView2 = (RecyclerView) findViewById(R.id.recommendedCourseListView);
        frame = (CardView) findViewById(R.id.ll);
        frame1 = (CardView) findViewById(R.id.frameCurrentCourseListView);
        frame2 = (CardView) findViewById(R.id.frameRecommendedCourses);
        Log.d(TAG, "displayCoursesInRecycler: in here; commence debug test");
        if(courseArrayList.size()==0){
            frame.setVisibility(GONE);
            Log.d(TAG, "displayCoursesInRecycler: failure 3");
        } else {
            frame.setVisibility(View.VISIBLE);
            Log.d(TAG, "displayCoursesInRecycler: success 3");
        }
        if(courseArrayList1.size()==0){
            frame1.setVisibility(GONE);
            Log.d(TAG, "displayCoursesInRecycler: failure 1");
        } else {
            frame1.setVisibility(View.VISIBLE);
            Log.d(TAG, "displayCoursesInRecycler: success 1");
        }
        if(courseArrayList2.size()==0){
            frame2.setVisibility(GONE);
            Log.d(TAG, "displayCoursesInRecycler: failure 2");
        } else {
            frame2.setVisibility(View.VISIBLE);
            Log.d(TAG, "displayCoursesInRecycler: success 2");
        }
        Log.d(TAG, "displayCoursesInRecycler: end test");
        recyclerView.setLayoutManager(layoutManager);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(recyclerView1.getContext(), layoutManager1.getOrientation());
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(recyclerView2.getContext(), layoutManager2.getOrientation());

        //recyclerView1.addItemDecoration(dividerItemDecoration1);
        //recyclerView2.addItemDecoration(dividerItemDecoration2);
        recyclerView.setAdapter(coursesAdapter);
        recyclerView1.setAdapter(coursesAdapter1);
        recyclerView2.setAdapter(coursesAdapter2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.three_dot_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.search_home);
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                main.setVisibility(GONE);
                recyclerViewSearchResults.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                main.setVisibility(View.VISIBLE);
                recyclerViewSearchResults.setVisibility(GONE);
                return true;
            }
        });
        searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.getQuery() method should give you the query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.v("text",newText);
                performSearch(newText);
                //Log.e("onQueryTextChange", "called");
                /*if(!newText.equals("")) {

                    searchQuery.setQuery(newText);
                    searchQuery.setRankMethod(rankBy.PRICE);

                    searchForCourse(searchQuery, 5);
                    return false;
                }*/
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
//                searchQuery.setQuery(query);
//                searchQuery.setRankMethod(rankBy.RATING);
//                initRecyclerView(recyclerViewSearchResults, searchResultsList);
//                searchQuery.searchForCourse(app, mongoDatabase,MainActivity.this,  coursesAdapter1_1, recyclerViewSearchResults, 10, userLoc);
                return false;
            }
        });
        return true;
    }

    public void performSearch(String regex) {
//        progressBar.setVisibility(View.VISIBLE);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("index", 0);
            jsonBody.put("filter", "Rating");
            Gson gson = new Gson();
            jsonBody.put("tags", gson.toJson(new ArrayList<>()));
            jsonBody.put("regex", regex);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API + "/paging", jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", response.toString());
                            ArrayList<CourseFinal> list = new ArrayList<CourseFinal>();
                            try {
                                JSONArray jsonArray = (JSONArray) response.get("courses");
                                Log.v("array", String.valueOf(jsonArray));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    Gson gson = new Gson();
                                    CourseFinal course = gson.fromJson(jsonObject.toString(), CourseFinal.class);
                                    list.add(course);
                                    Log.v("course", String.valueOf(course.getCourseReviews()));
                                }
                                initRecyclerView(recyclerViewSearchResults, list);
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
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", ((Upsilon)getApplication()).getToken());
                    return params;
                }
            };
            queue.add(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

        @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        if(item.getItemId()==R.id.signOut)
        {
            signOut();
        }
        else if(item.getItemId()==R.id.refresh)
        {
            getCourseData();
        }
//        else if(item.getItemId()==R.id.btn_expand_collapse)
//        {
//            item.getActionView().animate().rotationBy(180).start();
//            LinearLayout header_more = navigationView.getHeaderView(0).findViewById(R.id.more);
//            header_more.setVisibility(View.VISIBLE);
//        }

        /*else if(item.getItemId()==R.id.search_home)
        {
            main.setVisibility(View.GONE);
            recyclerViewSearchResults.setVisibility(View.VISIBLE);
        }
        else if(item.getActionView()==searchView)
        {
            main.setVisibility(View.VISIBLE);
            recyclerViewSearchResults.setVisibility(View.GONE);
        }*/
        return true;
    }

    /*
    @Override
    public boolean onSearchRequested() {
        main.setVisibility(View.GONE);
        recyclerViewSearchResults.setVisibility(View.VISIBLE);
        return true;
    }*/

    private void signOut() {
//        User user = app.currentUser();
//        LoginManager.getInstance().logOut();
//        user.logOutAsync(new App.Callback<User>() {
//            @Override
//            public void onResult(App.Result<User> result) {
//                if(result.isSuccess())
//                {
//                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
//                }
//                else
//                {
//
//                }
//            }
//        });
        ((Upsilon)getApplication()).setToken(null);
        ((Upsilon)getApplication()).user.setUserLocation(null);
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.homeDrawerMenuItem1)
        {
            Intent intent = new Intent(MainActivity.this,AddCourseActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.homeDrawerMenuItem2)
        {
            Intent intent = new Intent(MainActivity.this,CoursesTaughtActivity.class);
            startActivity(intent);
        }
//        else if(id==R.id.btn_edit_profile)
//        {
//            Intent intent = new Intent(MainActivity.this,ProfileViewActivity.class);
//            startActivity(intent);
//        }
        else if(id==R.id.homeDrawerMenuItem3)
        {
            Intent intent = new Intent(MainActivity.this,ProfileViewActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.tc)
        {
            Intent intent = new Intent(MainActivity.this,Terms.class);
            startActivity(intent);
        }
        else if(id==R.id.faq)
        {
            Intent intent = new Intent(MainActivity.this,FAQ.class);
            startActivity(intent);
        }
        else if(id==R.id.contactUs)
        {
            Intent intent = new Intent(MainActivity.this,ContactUs.class);
            startActivity(intent);
        }
        else if(id==R.id.about)
        {
            Intent intent = new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.homeDrawerMenuItem4)
        {
            Intent intent = new Intent(MainActivity.this,UserDataSetupActivity.class);
            startActivity(intent);
        }
//        else if(id==R.id.homeDrawerMenuItem5)
//        {
//            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
//            startActivity(intent);
//        }
        else if(id==R.id.homeDrawerMenuItem6)
        {
            Intent intent = new Intent(MainActivity.this, WalletActivity.class);
            startActivity(intent);
        }
//        else if(id==R.id.homeDrawerMenuItem7)
//        {
//            Intent intent = new Intent(MainActivity.this, RegisteredStudentViewCourseAssignmentsFragment.class);
//            startActivity(intent);
//        }
        else if(id==R.id.homeDrawerMenuItem8)
        {
            Intent intent = new Intent(MainActivity.this, GetStartedAsTeacherActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(swapped){
     /*Make sure you don't swap twice,
since the dispatchTouchEvent might dispatch your touch event to this function again!*/
            swapped = false;
            return super.onTouchEvent(event);
        }
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (deltaX > MIN_DISTANCE)
                {
                    drawer.openDrawer(GravityCompat.START);
                    //you already swapped, set flag swapped = true
                    swapped = true;
                }
                else
                {
                    // not swapping
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        this.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    public void scheduleJob() {
        ComponentName componentName = new ComponentName(this, UpsilonJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("NotificationService", "Job scheduled");
        } else {
            Log.d("NotificationService", "Job scheduling failed");
        }
    }
    public void cancelJob() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d("NotificationService", "Job cancelled");
    }

    public void startService(UpsilonJobService upsilonJobService) {
        String input = "Upsilon";
        serviceIntent = new Intent(this, NotifService.class);
        serviceIntent.putExtra("inputExtra", input);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, NotifService.class);
        stopService(serviceIntent);
    }

    public void displayNotif(){
        Intent fullScreenIntent = new Intent(this, MainActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.lightlogo1)
                        .setContentTitle("Incoming call")
                        .setContentText("(919) 555-1234")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_CALL)

                        // Use a full-screen intent only for the highest-priority alerts where you
                        // have an associated activity that you would like to launch after the user
                        // interacts with the notification. Also, if your app targets Android 10
                        // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
                        // order for the platform to invoke this notification.
                        .setFullScreenIntent(fullScreenPendingIntent, true);

        Notification incomingCallNotification = notificationBuilder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(123, notificationBuilder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Upsilon";
            String description = "Helllo!!";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onDestroy() {
        cancelJob();
        Log.i("MAINACT", "onDestroy!");
        stopService(mServiceIntent);
        super.onDestroy();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    public void operationPURGE(User user){
        int j=0;
        while(j<100)
        {
            Log.v("PURGE", "THE PURGE BEGINS!");
            j++;
        }


//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("Upsilon");
//        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
//
//        //Blank query to find every single user in db
//        Document queryFilter  = new Document();
//
//        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
//
//        findTask.getAsync(task -> {
//            if (task.isSuccess()) {
//                MongoCursor<Document> results = task.get();
//                int i = 0;
//                while (results.hasNext()) {
//                    i++;
//                    //Log.v("EXAMPLE", results.next().toString());
//                    Document currentDoc = results.next();
//                    Document loc = new Document();
//                    loc.append("latitude", 19.84);
//                    loc.append("longitude", 75.26);
//                    currentDoc.append("userLocation", loc);
//                    Log.v("Purging",currentDoc.getString("userid"));
//                    mongoCollection.updateOne(new Document("userid", currentDoc.getString("userid")), currentDoc).getAsync(result ->
//                    {
//                        if(result.isSuccess())
//                        {
//                            Log.v("Success","success");
//                        }
//                        else
//                        {
//                            Log.v("Error",result.getError().toString());
//                        }
//                    });
//                    Log.v("PURGE", "Purged!");
//                    Log.v("PURGE", Integer.toString(i));
//                }
//                Log.v("PURGE", "THE PURGE WAS A SUCCESS!");
//            } else {
//                Log.v("User","Failed to complete search");
//            }
//        });
    }

    public void initRecyclerView(RecyclerView recyclerView, ArrayList<CourseFinal> list) {
        coursesAdapter1_1 = new CoursesAdapter1(list,((Upsilon)getApplication()).getAPI() , ((Upsilon)getApplication()).getToken() , ((Upsilon) getApplication()).getUser());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(coursesAdapter1_1);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }


    @Override
    protected void onResume() {
        updateMenu();
        getCourseData();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

//    @Override
//    protected void onRestart() {
//        updateMenu();
//        getCourseData();
//        super.onRestart();
//    }

    private void updateMenu()
    {
        try {
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, API+"/isTeacher",new JSONObject(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("FetchingisTeacher", response.toString());
                            try {
                                if(response.getBoolean("isTeacher"))
                                {
                                    navigationView.getMenu().clear();
                                    navigationView.inflateMenu(R.menu.home_drawer_menu);
                                }
                                else
                                {
                                    navigationView.getMenu().clear();
                                    navigationView.inflateMenu(R.menu.home_drawer_menu_1);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("ErrorFetchingisTeacher", error.toString());
                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", ((Upsilon)getApplication()).getToken());
                    return params;
                }
            };
            queue.add(jsonRequest);
//            User user = app.currentUser();
//            mongoClient = user.getMongoClient("mongodb-atlas");
//            mongoDatabase = mongoClient.getDatabase("Upsilon");
//            MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
//
//            //Blank query to find every single course in db
//            //TODO: Modify query to look for user preferred course IDs
//            Document queryFilter  = new Document("userid",user.getId());
//
//            RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
//
//            findTask.getAsync(task -> {
//                if (task.isSuccess()) {
//                    MongoCursor<Document> results = task.get();
//                    while (results.hasNext()) {
//                        //Log.v("EXAMPLE", results.next().toString());
//                        Document currentDoc = results.next();
//                            /*if(currentDoc.get("profilePicture") ==null)
//                            {
//                                goToSetupActivity();
//                            }
//                            if(currentDoc.get("username") == null)
//                            {
//                                goToSetupActivity();
//                            }*/
//                        try {
//                            if(currentDoc==null || currentDoc.get("teacherSetup")==null)
//                            {
//                                navigationView.getMenu().clear();
//                                navigationView.inflateMenu(R.menu.home_drawer_menu_1);
//                            }
//                            else if(currentDoc.get("teacherSetup").equals(true))
//                            {
//                                navigationView.getMenu().clear();
//                                navigationView.inflateMenu(R.menu.home_drawer_menu);
//                            }
//                            else
//                            {
//                                navigationView.getMenu().clear();
//                                navigationView.inflateMenu(R.menu.home_drawer_menu_1);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } else {
//                    Snackbar.make(drawerLayout,"An error occured . Please signIn again",Snackbar.LENGTH_LONG).show();
//                    Log.v("User","Failed to complete search");
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}