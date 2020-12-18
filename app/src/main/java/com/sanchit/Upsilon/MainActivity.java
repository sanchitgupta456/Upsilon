package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CoursesAdapter;
import com.sanchit.Upsilon.courseData.CoursesAdapter1;
import com.sanchit.Upsilon.notifications.NotifService;
import com.sanchit.Upsilon.notifications.UpsilonJobService;
import com.sanchit.Upsilon.ui.login.LoginActivity;
import com.squareup.picasso.Picasso;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

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
    private ArrayList<String> myCourses;

    RecyclerView recyclerView,recyclerView1,recyclerView2;
    CoursesAdapter1 coursesAdapter;
    CoursesAdapter coursesAdapter1;
    CoursesAdapter coursesAdapter2;
    ArrayList<Course> courseArrayList = new ArrayList<Course>();
    ArrayList<Course> courseArrayList1 = new ArrayList<>();
    ArrayList<Course> courseArrayList2 = new ArrayList<>();
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    CircleImageView imageView;
    ActionBarDrawerToggle toggle;

    private ProgressBar progressBar;
    public static final List<String> TvShows  = new ArrayList<String>();
    public static final int[] TvShowImgs = {R.drawable.google, R.drawable.facebook};
    private String college;

    //NOTIFS
    Intent serviceIntent;
    Intent mServiceIntent;
    private NotifService mSensorService;
    private UpsilonJobService upsilonJobService;
    Context ctx;
    public Context getCtx() {
        return ctx;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

        progressBar = findViewById(R.id.loadingMain);
        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        else
        {
            app.getSync();
        }
        //Toolbar toolbar = findViewById(R.id.toolbar);
 //        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        drawer.closeDrawer(GravityCompat.START);
        View hView =  navigationView.getHeaderView(0);
        imageView = (CircleImageView) hView.findViewById(R.id.profilePhotoHeader);
        if(user==null)
        {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else
        {
            mongoClient = user.getMongoClient("mongodb-atlas");
            mongoDatabase = mongoClient.getDatabase("Upsilon");
            MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");

            //Blank query to find every single course in db
            //TODO: Modify query to look for user preferred course IDs
            Document queryFilter  = new Document("userid",user.getId());

            RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

            findTask.getAsync(task -> {
                if (task.isSuccess()) {
                    MongoCursor<Document> results = task.get();
                    if(!results.hasNext())
                    {
                        mongoCollection.insertOne(
                                new Document("userid", user.getId()).append("favoriteColor", "pink"))
                                .getAsync(result -> {
                                    if (result.isSuccess()) {
                                        Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                + result.get().getInsertedId());
                                        goToSetupActivity();
                                    } else {
                                        Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                    }
                                });
                    }
                    else
                    {
                        Log.v("User", "successfully found the user");
                        getCourseData();
                    }
                    while (results.hasNext()) {
                        //Log.v("EXAMPLE", results.next().toString());
                        Document currentDoc = results.next();
                        Log.v("User",currentDoc.getString("userid"));
                        Picasso.with(getApplicationContext()).load(currentDoc.getString("profilePicUrl")).error(R.drawable.default_person_image).into(imageView);
                        Log.v("ProfilePic",currentDoc.getString("profilePicUrl"));
                    }
                } else {
                   Log.v("User","Failed to complete search");
                }
            });
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

    private void goToSetupActivity() {
        Intent intent = new Intent(MainActivity.this,UserDataSetupActivity1.class);
        startActivity(intent);
    }

    public void getCourseData(){

        progressBar.setVisibility(View.VISIBLE);
        courseArrayList=new ArrayList<>();
        courseArrayList1=new ArrayList<>();
        courseArrayList2=new ArrayList<>();



            displayCoursesInRecycler();
// an authenticated user is required to access a MongoDB instance

            if (app.currentUser()!=null) {
                final User user = app.currentUser();
                assert user != null;
                MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");
                MongoCollection<Document> mongoCollection2  = mongoDatabase.getCollection("UserData");
                //Blank query to find every single course in db
                //TODO: Modify query to look for user preferred course IDs

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
                            myCourses = (ArrayList<String>) currentDoc1.get("myCourses");
                            college = currentDoc1.getString("college");
                        }
                    } else {
                        Log.v("User", "Failed to complete search");
                    }
                });


                Document queryFilter  = new Document();
                //Document userdata = user.getCustomData();
                // myCourses = (ArrayList<String>) userdata.get("myCourses");
                if(myCourses==null)
                {
                    myCourses=new ArrayList<>();
                }

                RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                final int[] flag = {0};
                findTask.getAsync(task -> {
                    if (task.isSuccess()) {
                        try {
                            MongoCursor<Document> results = task.get();
                            Log.v("COURSEHandler", "successfully found all courses:");
                            while (results.hasNext()) {
                                //Log.v("EXAMPLE", results.next().toString());
                                try {
                                    Document currentDoc = results.next();

                                    //Log.v("IMPORTANT","Error:"+currentDoc.getString("nextLectureOn"));

                                    if(currentDoc.getString("nextLectureOn")==null)
                                    {
                                        currentDoc.append("nextLectureOn","0");
                                    }
                                    currentDoc.toJson();
                                    gsonBuilder = new GsonBuilder();
                                    gson = gsonBuilder.create();

                                    Course course = gson.fromJson(currentDoc.toJson(),Course.class);

                                    flag[0] =0;
                                    //Log.v("MyCourses", String.valueOf(myCourses));
                                    for(int i=0;i<myCourses.size();i++)
                                    {
                                        try {
                                            Log.v("currentCourse", course.getCourseId() + myCourses.get(i));
                                            if(myCourses.get(i).equals(course.getCourseId()))
                                            {
                                                Log.v("CourseAdded","Added");
                                                courseArrayList1.add(course);
                                                coursesAdapter1.notifyDataSetChanged();
                                                flag[0] =1;
                                                break;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if(flag[0] ==1)
                                    {
                                        continue;
                                    }
                                    if(!course.getTutorId().equals(user.getId())) {
                                        courseArrayList.add(course);
                                        coursesAdapter.notifyDataSetChanged();
                                        //courseArrayList2.add(course);
                                        Document queryFilter1 = new Document("userid", course.getTutorId());

                                        RealmResultTask<MongoCursor<Document>> findTask1 = mongoCollection2.find(queryFilter1).iterator();

                                        findTask1.getAsync(task1 -> {
                                            if (task1.isSuccess()) {
                                                MongoCursor<Document> results1 = task1.get();
                                                if (!results1.hasNext()) {
                                                    Log.v("ViewCourse", "Couldnt Find The Tutor");
                                                } else {
                                                    Log.v("User", "successfully found the Tutor");
                                                }
                                                while (results1.hasNext()) {
                                                    //Log.v("EXAMPLE", results.next().toString());
                                                    Document currentDoc1 = results1.next();
                                                    Log.v("CourseBySenior", (String) currentDoc1.get("college"));
                                                    Log.v("CourseBySenior", (String)  college);

                                                    if (currentDoc1.getString("college").equals(college)) {
                                                        Log.v("CourseBy","Hello");
                                                        courseArrayList2.add(course);
                                                        coursesAdapter2.notifyDataSetChanged();
                                                        Log.v("CoursesySeniors", String.valueOf(courseArrayList2));
                                                    }
                                                    Log.v("User", currentDoc1.getString("userid"));
                                                }
                                            } else {
                                                Log.v("User", "Failed to complete search");
                                            }
                                        });
                                    }
                                } catch (JsonSyntaxException e) {
                                    e.printStackTrace();
                                }
                                if(!results.hasNext())
                                {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("COURSEHandler", "failed to find courses with: ", task.getError());
                    }
                });


                MongoCollection<Document> mongoCollection1  = mongoDatabase.getCollection("UserData");

                //Blank query to find every single course in db
                Document queryFilter1  = new Document();
                queryFilter1.append("userid",user.getId());

                RealmResultTask<MongoCursor<Document>> findTask1 = mongoCollection1.find(queryFilter1).iterator();

                findTask1.getAsync(task -> {
                    if (task.isSuccess()) {
                        MongoCursor<Document> results = task.get();
                        Log.v("COURSEHandler", "successfully found all courses:");
                        Document document = results.next();
                        String url = document.getString("profilePicUrl");
                        //Toast.makeText(MainActivity.this,url,Toast.LENGTH_LONG).show();
                        //Log.v("User","Hi"+ url);
                        //Picasso.with(getApplicationContext()).load(url).into(imageView);
                    } else {
                        Log.e("COURSEHandler", "failed to find courses with: ", task.getError());
                    }
                });
            }
            else {
                    Log.e(TAG, "Error logging into the Realm app. Make sure that anonymous authentication is enabled.");
                }

        User user = app.currentUser();
    }

    public void displayCoursesInRecycler(){
        coursesAdapter = new CoursesAdapter1(courseArrayList);
        coursesAdapter1 = new CoursesAdapter(courseArrayList1);
        coursesAdapter2 = new CoursesAdapter(courseArrayList2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView = (RecyclerView)findViewById(R.id.exploreCourseListView);
        recyclerView1 = (RecyclerView)findViewById(R.id.currentCourseListView);
        recyclerView2 = (RecyclerView) findViewById(R.id.recommendedCourseListView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(recyclerView.getContext(), layoutManager1.getOrientation());
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(recyclerView.getContext(), layoutManager2.getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView1.addItemDecoration(dividerItemDecoration1);
        recyclerView2.addItemDecoration(dividerItemDecoration2);
        recyclerView.setAdapter(coursesAdapter);
        recyclerView1.setAdapter(coursesAdapter1);
        recyclerView2.setAdapter(coursesAdapter2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.three_dot_menu, menu);
        return true;
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
        return true;
    }

    private void signOut() {
        User user = app.currentUser();
        LoginManager.getInstance().logOut();
        user.logOutAsync(new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if(result.isSuccess())
                {
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
                else
                {

                }
            }
        });
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
        else if(id==R.id.homeDrawerMenuItem3)
        {
            Intent intent = new Intent(MainActivity.this,ProfileViewActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.tc)
        {
            Intent intent = new Intent(MainActivity.this,Terms.class);
            //Toast toast = Toast.makeText(getApplicationContext(), "TC Clicked", Toast.LENGTH_LONG);
            //toast.show();
            startActivity(intent);
        }
        else if(id==R.id.faq)
        {
            Intent intent = new Intent(MainActivity.this,FAQ.class);
            //Toast toast = Toast.makeText(getApplicationContext(), "TC Clicked", Toast.LENGTH_LONG);
            //toast.show();
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
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    //Search

    void searchForCourse(String query){
        if (app.currentUser()!=null) {
            final User user = app.currentUser();
            assert user != null;
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("CourseData");

            //Blank query to find every single course in db
            Document queryFilter  = new Document();
            queryFilter.append("courseName", query);

            RealmResultTask<MongoCursor<Document>> findCourses = mongoCollection.find(queryFilter).iterator();

            findCourses.getAsync(task -> {
                if (task.isSuccess()) {
                    MongoCursor<Document> results = task.get();
                    Log.v("COURSEHandler", "successfully found all courses:");
                    Document document = results.next();
                    String name = document.getString("courseName");
                    //Toast.makeText(MainActivity.this,url,Toast.LENGTH_LONG).show();
                    Log.v("CourseSearch",name);
                    //Picasso.with(getApplicationContext()).load(url).into(imageView);
                } else {
                    Log.e("COURSESearch", "failed to find courses with: ", task.getError());
                }
            });
        }
    }
}