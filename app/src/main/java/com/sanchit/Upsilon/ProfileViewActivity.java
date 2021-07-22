package com.sanchit.Upsilon;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.sanchit.Upsilon.courseData.CourseFinal;
import com.sanchit.Upsilon.courseData.CourseLocation;
import com.sanchit.Upsilon.courseLocationMap.MapsActivity;
import com.sanchit.Upsilon.userData.UserLocation;
import com.squareup.picasso.Picasso;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

import static android.view.View.GONE;
import static io.realm.Realm.getApplicationContext;

public class ProfileViewActivity extends AppCompatActivity {

    private static final String TAG = "ProfileViewActivity";
    TextInputEditText UserName,PhoneNumber,Email;
    CircleImageView profileImage;
    App app;
    String appID = "upsilon-ityvn";
    MaterialButton update;

    LinearLayout locMain, locAlter;
    TextView viewLoc;
    MaterialButton changeLoc, setLoc;

    MaterialButton btnCoursesTaught, btnCoursesEnrolled;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    private final int REQUEST_FINE_LOCATION = 1234;
    Double latitude,longitude;
    CourseLocation courseLocation = new CourseLocation();
    UserLocation userLocation = new UserLocation();

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    private ProgressBar progressBar;
    private RequestQueue queue;
    private String API ;

//    private View bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.your_profile);
//        bar = getSupportActionBar().getCustomView();
        UserName = (TextInputEditText) findViewById(R.id.editTextUserName);
        PhoneNumber = (TextInputEditText) findViewById(R.id.editTextPhoneNumber);
        Email = (TextInputEditText) findViewById(R.id.editTextEmailId);
        update = (MaterialButton) findViewById(R.id.updateChange);
        locMain = findViewById(R.id.loc_main);
        locAlter = findViewById(R.id.alter_loc);
        viewLoc = findViewById(R.id.view_location);
        changeLoc = findViewById(R.id.changeLocation);
        setLoc = findViewById(R.id.setupLocation);
        btnCoursesEnrolled = findViewById(R.id.courses_enrolled);
        btnCoursesTaught = findViewById(R.id.courses_taught);
        queue = Volley.newRequestQueue(getApplicationContext());
        API = ((Upsilon)this.getApplication()).getAPI();

//        NumberOfCoursesTaken = (TextView) findViewById(R.id.profileNumCoursesTaken);
//        NumberOfCoursesTaught = (TextView) findViewById(R.id.profileNumCoursesTaught);
        profileImage = (CircleImageView) findViewById(R.id.imgProfileImage);
        progressBar = (ProgressBar) findViewById(R.id.loadingUserProfile);

        UserName.setText(((Upsilon)getApplication()).getUser().getUsername());
        Email.setText(((Upsilon)getApplication()).getUser().getEmail());
        PhoneNumber.setText(((Upsilon)getApplication()).getUser().getPhone());
        Glide.with(getApplicationContext()).load(((Upsilon)getApplication()).getUser().getImg()).into(profileImage);
//        app = new App(new AppConfiguration.Builder(appID)
//                .build());
//
//        User user = app.currentUser();
//        Document queryfilter = new Document("userid",user.getId());
//
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("Upsilon");
//        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
//
//        //Blank query to find every single course in db
//        Document queryFilter  = new Document("userid",user.getId());
//
//        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
//
//        findTask.getAsync(task -> {
//            progressBar.setVisibility(View.VISIBLE);
//            if (task.isSuccess()) {
//                MongoCursor<Document> results = task.get();
//                if(!results.hasNext())
//                {
//                    /*mongoCollection.insertOne(
//                            new Document("userid", user.getId()).append("favoriteColor", "pink"))
//                            .getAsync(result -> {
//                                if (result.isSuccess()) {
//                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
//                                            + result.get().getInsertedId());
//                                    //goToSetupActivity();
//                                } else {
//                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
//                                }
//                            });*/
//                }
//                else
//                {
//                    Log.v("User", "successfully found the user");
//                    //getCourseData();
//                }
//                while (results.hasNext()) {
//                    //Log.v("EXAMPLE", results.next().toString());
//                    Document currentDoc = results.next();
//                    try {
//                        UserName.setText(currentDoc.getString("name"));
//                        PhoneNumber.setText(currentDoc.getString("phonenumber"));
//                        Email.setText(user.getProfile().getEmail());
//                        Log.v("Email","Hello" + user.getProfile().toString());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        Picasso.with(getApplicationContext()).load(currentDoc.getString("profilePicUrl")).into(profileImage);
//                        Log.v("ProfilePic",currentDoc.getString("profilePicUrl"));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    Log.v("User",currentDoc.getString("userid"));
//                }
//            } else {
//                Log.v("User","Failed to complete search");
//            }
//            progressBar.setVisibility(View.GONE);
//        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/myCourses",new JSONObject(),
//                        new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                Log.d("FetchMyCourses", response.toString());
//
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @SuppressLint("LongLogTag")
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.d("ErrorFetchingMyCourses", error.toString());
//                                progressBar.setVisibility(GONE);
//                            }
//                        }
//                ){
//                    @Override
//                    public Map<String, String> getHeaders() {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("token", ((Upsilon)getApplication()).getToken());
//                        return params;
//                    }
//                };
//                queue.add(jsonRequest);
            }
        });

        if(((Upsilon)getApplication()).getUser().getUserLocation()!=null && ((Upsilon)getApplication()).getUser().getUserLocation().getLatitude()!=null)
        {
            locMain.setVisibility(View.VISIBLE);
            locAlter.setVisibility(View.GONE);
        }
        else
        {
            locMain.setVisibility(View.GONE);
            locAlter.setVisibility(View.VISIBLE);
        }

        //TODO: for all item clicks, alter visibility of progress bar to indicate process being done
        changeLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: show map; allow to choose a location. Current location as default.
                if (ContextCompat.checkSelfPermission(ProfileViewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ProfileViewActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
                } else {
                    if(isLocationEnabled(getApplicationContext()))
                    {
                        Intent intent = new Intent(ProfileViewActivity.this, MapsActivity.class);
                        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
                    }
                    else
                    {
                        enableLoc();
                    }

                }
            }
        });

        setLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: ask for permission for location, of not given. Enable location services.
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ProfileViewActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
                } else {
                    if (ContextCompat.checkSelfPermission(ProfileViewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProfileViewActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
                    } else {
                        if(isLocationEnabled(getApplicationContext()))
                        {
                            getLocation();
//                    app = new App(new AppConfiguration.Builder(appID).build());
//                    user = app.currentUser();
//                    userdata = user.getCustomData();
                            Log.d(TAG, "onClick: tests...");
                        }
                        else
                        {
                            enableLoc();
                        }

                    }

                }
            }
        });

        if(((Upsilon)getApplication()).getUser().getRole().equals("teacher"))
        {

        }
        else
        {
            btnCoursesTaught.setText("Get Started As Teacher");
        }

        btnCoursesTaught.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((Upsilon)getApplication()).getUser().getRole().equals("teacher"))
                {
                    Intent intent = new Intent(getApplicationContext(), CoursesTaughtActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), GetStartedAsTeacherActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnCoursesEnrolled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                //TODO: go to a page where all courses learnt by you are displayed, current and past
            }
        });

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu.add(0);
        //menu.getItem(0).setIcon(getDrawable(R.drawable.edit_icon_light));
        menu.add("Edit");
        menu.getItem(0).setIcon(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.edit_icon));
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    private void enableLoc() {

        GoogleApiClient googleApiClient = null;
        if (googleApiClient == null) {
            GoogleApiClient finalGoogleApiClient = googleApiClient;
            googleApiClient = new GoogleApiClient.Builder(ProfileViewActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            finalGoogleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                private static final int REQUEST_LOCATION =  1235 ;

                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(ProfileViewActivity.this, REQUEST_LOCATION);

//                                finish();
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            /*if (requestCode == 1) {

                // currImageURI is the global variable I'm using to hold the content:// URI of the image
                Uri currImageURI = data.getData();
                Bundle bundle = data.getExtras();
                profilepic.setImageURI(currImageURI);
                Profilepicpath = getRealPathFromURI(currImageURI);

            }*/
            if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
                // Get String data from Intent
                latitude = data.getDoubleExtra("latitude",0);
                longitude = data.getDoubleExtra("longitude",0);
                Log.v("CourseLocationSet", String.valueOf(latitude+longitude));
                courseLocation.setLatitude(latitude);
                courseLocation.setLongitude(longitude);
                Log.v("GotIt",latitude.toString()+longitude.toString()+"yeah");
                updateLocation(latitude,longitude);
            }
            else if (requestCode == 1235) {
                Intent intent = new Intent(ProfileViewActivity.this, MapsActivity.class);
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            }
        }

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == WRITE_PERMISSION) {
//            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                Log.d("Hello", "Write Permission Failed");
//                Toast.makeText(this, "You must allow permission write external storage to your mobile device.", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//    }

    private void getLocation() {
//        llLoader.setVisibility(View.VISIBLE);
        Log.d(TAG, "getLocation: I got permissions!");
        userLocation = new UserLocation();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());;
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                Log.v("Location", String.valueOf(location));
                if(location!=null)
                {
                    Log.v("Location","Location"+location.getLatitude());
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        /*
                        City.setText(addresses.get(0).getLocality());
                        viewModel.setCity(addresses.get(0).getLocality());
                        Pincode.setText(addresses.get(0).getPostalCode());
                        viewModel.setPincode(addresses.get(0).getPostalCode());*/
                        userLocation.setLatitude(location.getLatitude());
                        userLocation.setLongitude(location.getLongitude());
                        //viewModel.setUserLocation(userLocation);
                        //user.getCustomData().append("userLocation", userLocation);
                        updateLocation(location.getLatitude(),location.getLongitude());
                        //Log.v("Location",addresses.get(0).getPostalCode()+" "+addresses.get(0).getLocality()+" "+addresses.get(0).getSubLocality());
                    } catch (IOException e) {
//                        llLoader.setVisibility(View.INVISIBLE);
                        e.printStackTrace();
                    }

                }
                else
                {
                    Log.v("Location","Error");
                }
            }
        });
    }

    public void updateLocation(double latitude,double longitude) {
//        Log.v("Location",String.valueOf(userLocation.get("latitude"))+String.valueOf(userLocation.get("longitude")));
        Map<String, String> mHeaders = new ArrayMap<String, String>();
        mHeaders.put("token", ((Upsilon) getApplication()).getToken());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API + "/userLocation", jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("UpdatingUserLocation", response.toString());
//                            llLoader.setVisibility(View.INVISIBLE);
//                                    performSearch();
//                            alter.setVisibility(View.GONE);
                            ((Upsilon) getApplication()).user.setUserLocation(new UserLocation(latitude,longitude));
                            ((Upsilon) getApplication()).fetchProfile();
                            if(((Upsilon)getApplication()).getUser().getUserLocation()!=null && ((Upsilon)getApplication()).getUser().getUserLocation().getLatitude()!=null)
                            {
                                locMain.setVisibility(View.VISIBLE);
                                locAlter.setVisibility(View.GONE);
                            }
                            else
                            {
                                locMain.setVisibility(View.GONE);
                                locAlter.setVisibility(View.VISIBLE);
                            }
//                            loadOnce();
//                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    },
                    new Response.ErrorListener() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("ErrorUpdatingUserLocation", error.toString());
//                            llLoader.setVisibility(View.INVISIBLE);

                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", ((Upsilon) getApplication()).getToken());
                    return params;
                }
            };
            queue.add(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
