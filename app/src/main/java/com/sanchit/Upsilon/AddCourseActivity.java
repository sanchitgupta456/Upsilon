package com.sanchit.Upsilon;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseLocation;
import com.sanchit.Upsilon.courseLocationMap.MapsActivity;
import com.sanchit.Upsilon.ui.login.LoginActivity;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class AddCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "AddCourseActivity";

    String appID = "upsilon-ityvn";
    TextInputEditText CourseName,CourseDescription,CourseDuration,CourseFees;
    String courseName,courseDescription,courseDuration,mode,courseDurationMeasure;
    ChipGroup group;
    int fees;
    Button nextButton, addCategory, addLocation;
    RadioButton Group,Individual,Free,Paid;
    ToggleButton offline_online;
    private final int REQUEST_FINE_LOCATION = 1234;
    //Spinner spinner;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    ArrayList courseReviews;
    ImageView CourseImage;
    String CourseImageUrl;
    //RecyclerView courseCategories;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int WRITE_PERMISSION = 0x01;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    private final String defaultUrl = "https://res.cloudinary.com/upsilon175/image/upload/v1626337087/lightlogo2_g4olr1.png";


    private ProgressBar progressBar;
    View bar;

    String[] categories;
    boolean[] isCheckedCategories;
    ArrayList<String> categories_chosen = new ArrayList<>();
    Double latitude,longitude;
    CourseLocation courseLocation = new CourseLocation();
    private String college;

    private RequestQueue queue;
    private String API ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        queue = Volley.newRequestQueue(getApplicationContext());
        API = ((Upsilon)this.getApplication()).getAPI();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(R.string.add_course_details);
        bar = (View) getSupportActionBar().getCustomView();
        CourseName = (TextInputEditText) findViewById(R.id.add_course_name);
        CourseDescription = (TextInputEditText) findViewById(R.id.add_course_description);
        addCategory = (Button) findViewById(R.id.add_course_category);
        addLocation = (Button) findViewById(R.id.btnCourseLocation);
        //courseCategories = (RecyclerView) findViewById(R.id.categories_list_add_course);
        group = (ChipGroup) findViewById(R.id.selectedCategoriesDisplayGroup);
        group.removeAllViews();
        CourseDuration = (TextInputEditText) findViewById(R.id.add_course_duration);
        //NumberOfBatches = (EditText) findViewById(R.id.add_course_num_batches);
        nextButton = (Button) findViewById(R.id.btnNext);
        offline_online = (ToggleButton) findViewById(R.id.add_course_mode);
        CourseImage = (ImageView) findViewById(R.id.imgAddCourseImage);
        CourseFees = (TextInputEditText) findViewById(R.id.course_fee);
        Free = (RadioButton) findViewById(R.id.add_course_free);
        Paid = (RadioButton) findViewById(R.id.add_course_paid);
        progressBar = (ProgressBar) findViewById(R.id.loadingAddCourse);

        //spinner = (Spinner) findViewById(R.id.courseDurationMeasureSpinner);

//        app = new App(new AppConfiguration.Builder(appID)
//                .build());
//        user = app.currentUser();
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("Upsilon");
//        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");
//        MongoCollection<Document> mongoCollection1  = mongoDatabase.getCollection("ForumData");
//        MongoCollection<Document> mongoCollection2  = mongoDatabase.getCollection("UserData");
//
//        mongoCollection2.findOne(new Document("userid",user.getId())).getAsync(result -> {
//            if(result.isSuccess())
//            {
//                Document userdata = result.get();
//                college = userdata.getString("college");
//            }
//            else
//            {
//
//            }
//        });



        CourseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestWritePermission();
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        Free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseFees.setVisibility(View.INVISIBLE);
                fees=0;
            }
        });

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddCourseActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddCourseActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
                } else {
                    if(isLocationEnabled(getApplicationContext()))
                    {
                        Intent intent = new Intent(AddCourseActivity.this, MapsActivity.class);
                        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
                    }
                    else
                    {
                        enableLoc();
                    }

                }
            }
        });

        Paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseFees.setVisibility(View.VISIBLE);
            }
        });

        offline_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addLocation.getVisibility()== View.VISIBLE)
                {
                    addLocation.setVisibility(View.GONE);
                }
                else
                {
                    addLocation.setVisibility(View.VISIBLE);
                }
            }
        });

        getCategories();
        isCheckedCategories = new boolean[categories.length];
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        //TODO: addLocation functionality (addLocation is a button)
        //String[] measureOfTime = {"minutes","hours","days","weeks","months","years"};

        //ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_expandable_list_item_1,measureOfTime);
        //spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener(this);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(0);
                progressBar.setIndeterminate(false);
                nextButton.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                courseName = CourseName.getText().toString();
                courseDescription = CourseDescription.getText().toString();
                courseDuration = CourseDuration.getText().toString();

                if(courseName.isEmpty())
                {
                    Animation shake = AnimationUtils.loadAnimation(AddCourseActivity.this, R.anim.shake);
                    CourseName.startAnimation(shake);
                    CourseName.setError("Please Enter a Valid Course Name");
                    CourseName.requestFocus();
                    nextButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                }
                else if(courseDescription.isEmpty())
                {
                    Animation shake = AnimationUtils.loadAnimation(AddCourseActivity.this, R.anim.shake);
                    CourseDescription.startAnimation(shake);
                    CourseDescription.setError("Please Enter a Valid Course Description");
                    CourseDescription.requestFocus();
                    nextButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else if(courseDuration.isEmpty())
                {
                    Animation shake = AnimationUtils.loadAnimation(AddCourseActivity.this, R.anim.shake);
                    CourseDuration.startAnimation(shake);
                    CourseDuration.setError("Please Enter a Valid Course Duration");
                    CourseDuration.requestFocus();
                    nextButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else if(categories_chosen.isEmpty())
                {
                    Snackbar.make(findViewById(android.R.id.content),"Please choose atleast one category",Snackbar.LENGTH_LONG).show();
                    nextButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else {
                    //numOfBatches = NumberOfBatches.getText().toString();

                    if (Paid.isChecked()) {
                        fees = Integer.parseInt(CourseFees.getText().toString());
                    }

                    //courseReviews = new ArrayList<CourseReview>();
                    if (offline_online.isChecked()) {
                        mode = "Online";
                    } else {
                        mode = "Offline";
                    }
                    //Object object = new CourseReview("Hi",5,"Hello");
//                    courseReviews = new ArrayList();
//                    Document test = new Document().append("review", "A Step for bringing Knowledge down the years of College").append("reviewRating", 5).append("reviewAuthorId", user.getId());
//                    courseReviews.add(test);
                    //courseReviews.add(2,object);
                    //courseReviews.put("hello",object);
                /*course.setRatingAuthorId("h");
                course.setReview("fd");
                course.setReviewRating(1.23);
                courseReviews.add(course);*/
//                    Document courseDetails = new Document();
//                    Double rating =5.0;
//                    courseDetails.append("_partitionkey", "_partitionKey");
//                    courseDetails.append("courseName", courseName);
//                    courseDetails.append("tutorId", user.getId());
//                    courseDetails.append("courseDescription", courseDescription);
//                    courseDetails.append("coursePreReq", "");
//                    courseDetails.append("courseRating", rating);
//                    courseDetails.append("courseMode", mode);
//                    courseDetails.append("courseFees", fees);
//                    courseDetails.append("courseImage", "balh");
//                    courseDetails.append("instructorLocation", "Here");
//                    //courseDetails.append("courseDurationMeasure","hours");
//                    courseDetails.append("numberOfStudentsEnrolled", 0);
//                    courseDetails.append("courseDuration", courseDuration);
//                    //courseDetails.append("numberOfBatches",numOfBatches);
//                    courseDetails.append("courseReviews", courseReviews);
//                    courseDetails.append("courseImageCounter", 0);
//                    courseDetails.append("courseLocation", courseLocation);
//                    courseDetails.append("numberOfReviews", 0);
//                    courseDetails.append("courseCategories", categories_chosen);
//                    courseDetails.append("registrationsOpen",true);
//                    courseDetails.append("tutorCollege",college);
                    try {
                                String requestId = MediaManager.get().upload(CourseImageUrl)
                                        .unsigned("preset1")
                                        .option("resource_type", "image")
                                        .option("folder", "Upsilon/Courses/".concat(((Upsilon)getApplication()).getUser().get_Id()).concat(UUID.randomUUID().toString()))
                                        .option("public_id", "CourseImage " + 0)
                                        .callback(new UploadCallback() {
                                            @Override
                                            public void onStart(String requestId) {
                                            }

                                            @RequiresApi(api = Build.VERSION_CODES.N)
                                            @Override
                                            public void onProgress(String requestId, long bytes, long totalBytes) {
                                                progressBar.setProgress(Math.toIntExact((bytes / totalBytes) * 100));
                                            }

                                            @Override
                                            public void onSuccess(String requestId, Map resultData) {
                                                try {
                                                    Gson gson = new Gson();
                                                    JSONObject jsonObject= new JSONObject();
                                                    jsonObject.put("courseCategories", JSONArray.toJSONString(categories_chosen));
                                                    jsonObject.put("courseDescription",courseDescription);
                                                    jsonObject.put("courseMode",mode);
                                                    jsonObject.put("courseName",courseName);
                                                    jsonObject.put("coursePreReq",null);
                                                    jsonObject.put("numberOfBatches",1);
                                                    jsonObject.put("courseDuration",courseDuration);
                                                    jsonObject.put("courseFees",fees);
                                                    jsonObject.put("courseImage",resultData.get("url"));
                                                    jsonObject.put("courseLocation",gson.toJson(courseLocation));

                                                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API + "/createCourse", jsonObject,
                                                            new Response.Listener<JSONObject>() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    try {
                                                                        Log.d("Created Course", String.valueOf(response.get("_id")));
                                                                        Intent intent = new Intent(AddCourseActivity.this, AddCourseActivityContinued.class);
                                                                        intent.putExtra("InsertedDocument", String.valueOf(response.get("_id")));
                                                                        intent.putExtra("fees", fees);
                                                                        startActivity(intent);
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }

                                                                }
                                                            },
                                                            new Response.ErrorListener() {
                                                                @SuppressLint("LongLogTag")
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    Log.d("ErrorCreatingCourse", error.toString());
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

                                            @Override
                                            public void onError(String requestId, ErrorInfo error) {
                                                try {
                                                    Gson gson = new Gson();
                                                    JSONObject jsonObject= new JSONObject();
                                                    jsonObject.put("courseCategories", JSONArray.toJSONString(categories_chosen));
                                                    jsonObject.put("courseDescription",courseDescription);
                                                    jsonObject.put("courseMode",mode);
                                                    jsonObject.put("courseName",courseName);
                                                    jsonObject.put("coursePreReq",null);
                                                    jsonObject.put("numberOfBatches",1);
                                                    jsonObject.put("courseDuration",courseDuration);
                                                    jsonObject.put("courseFees",fees);
                                                    jsonObject.put("courseImage",defaultUrl);
                                                    jsonObject.put("courseLocation",gson.toJson(courseLocation));

                                                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API + "/createCourse", jsonObject,
                                                            new Response.Listener<JSONObject>() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    try {
                                                                        Log.d("Created Course", String.valueOf(response.get("_id")));
                                                                        Intent intent = new Intent(AddCourseActivity.this, AddCourseActivityContinued.class);
                                                                        intent.putExtra("InsertedDocument", String.valueOf(response.get("_id")));
                                                                        intent.putExtra("fees", fees);
                                                                        startActivity(intent);
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }

                                                                }
                                                            },
                                                            new Response.ErrorListener() {
                                                                @SuppressLint("LongLogTag")
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    Log.d("ErrorCreatingCourse", error.toString());
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

                                            @Override
                                            public void onReschedule(String requestId, ErrorInfo error) {

                                            }
                                        })
                                        .dispatch();
                            } catch (Exception e) {
                                e.printStackTrace();
                        try {
                            Gson gson = new Gson();
                            JSONObject jsonObject= new JSONObject();
                            jsonObject.put("courseCategories", JSONArray.toJSONString(categories_chosen));
                            jsonObject.put("courseDescription",courseDescription);
                            jsonObject.put("courseMode",mode);
                            jsonObject.put("courseName",courseName);
                            jsonObject.put("coursePreReq",null);
                            jsonObject.put("numberOfBatches",1);
                            jsonObject.put("courseDuration",courseDuration);
                            jsonObject.put("courseFees",fees);
                            jsonObject.put("courseImage",defaultUrl);
                            jsonObject.put("courseLocation",gson.toJson(courseLocation));

                            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API + "/createCourse", jsonObject,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                Log.d("Created Course", String.valueOf(response.get("_id")));
                                                Intent intent = new Intent(AddCourseActivity.this, AddCourseActivityContinued.class);
                                                intent.putExtra("InsertedDocument", String.valueOf(response.get("_id")));
                                                intent.putExtra("fees", fees);
                                                startActivity(intent);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @SuppressLint("LongLogTag")
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("ErrorCreatingCourse", error.toString());
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
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }

                /*Intent intent = new Intent(AddCourseActivity.this,AddCourseActivityContinued.class);
                intent.putExtra("courseDetails",courseDetails.toJson().toString());
                startActivity(intent);*/
//                    mongoCollection.insertOne(courseDetails).getAsync(result -> {
//                        if (result.isSuccess()) {
//                            String id = String.valueOf(result.get().getInsertedId().asObjectId().getValue());
//                            Log.v("Added Course", id);
//                            try {
//                                String requestId = MediaManager.get().upload(CourseImageUrl)
//                                        .unsigned("preset1")
//                                        .option("resource_type", "image")
//                                        .option("folder", "Upsilon/Courses/".concat(id))
//                                        .option("public_id", "CourseImage " + 0)
//                                        .callback(new UploadCallback() {
//                                            @Override
//                                            public void onStart(String requestId) {
//                                            }
//
//                                            @RequiresApi(api = Build.VERSION_CODES.N)
//                                            @Override
//                                            public void onProgress(String requestId, long bytes, long totalBytes) {
//                                                progressBar.setProgress(Math.toIntExact((bytes / totalBytes) * 100));
//                                            }
//
//                                            @Override
//                                            public void onSuccess(String requestId, Map resultData) {
//
//                                                mongoCollection1.insertOne(new Document("courseId", result.get().getInsertedId())).getAsync(result2 -> {
//                                                    if (result2.isSuccess()) {
//                                                        Log.v("Course", "Successfully Created Forum");
//                                                    } else {
//                                                        Log.v("Course", result2.getError().toString());
//                                                    }
//                                                });
//
//                                                courseDetails.append("courseImage", resultData.get("url").toString());
//                                                mongoCollection.updateOne(new Document("_id", result.get().getInsertedId()), courseDetails).getAsync(result1 -> {
//                                                    if (result1.isSuccess()) {
//                                                        progressBar.setProgress(100);
//                                                        progressBar.setVisibility(View.INVISIBLE);
//                                                        Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
//                                                                + result1.get().getModifiedCount());
//                                                        Log.v("AddCourse", "Updated Image Successfully");
//                                                        Intent intent = new Intent(AddCourseActivity.this, AddCourseActivityContinued.class);
//                                                        intent.putExtra("InsertedDocument", result.get().getInsertedId().asObjectId().getValue().toString());
//                                                        intent.putExtra("fees", fees);
//                                                        startActivity(intent);
//                                                    } else {
//                                                        Log.v("AddCourse", result1.getError().toString());
//                                                    }
//                                                });
//                                            }
//
//                                            @Override
//                                            public void onError(String requestId, ErrorInfo error) {
//
//                                            }
//
//                                            @Override
//                                            public void onReschedule(String requestId, ErrorInfo error) {
//
//                                            }
//                                        })
//                                        .dispatch();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            //Toast.makeText(getApplicationContext(),"Successfully Added The Course",Toast.LENGTH_LONG).show();
//                        } else {
//                            Log.v("User", result.getError().toString());
//                        }
//                    });
                    }
            }
        });
    }
    // Defining the Callback methods here
    public void onItemSelected(AdapterView parent, View view, int pos,
                               long id) {

        //courseDurationMeasure = spinner.getItemAtPosition(pos).toString();

        //Toast.makeText(getApplicationContext(),
          //      spinner.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG)
            //    .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getActionView()==bar) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                CourseImageUrl = cursor.getString(columnIndex);
                cursor.close();
                // String picturePath contains the path of selected Image
                ImageView imageView = (ImageView) findViewById(R.id.imgAddCourseImage);
                imageView.setImageBitmap(BitmapFactory.decodeFile(CourseImageUrl));
            }
            else if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
                // Get String data from Intent
                latitude = data.getDoubleExtra("latitude",0);
                longitude = data.getDoubleExtra("longitude",0);
                Log.v("CourseLocationSet", String.valueOf(latitude+longitude));
                courseLocation.setLatitude(latitude);
                courseLocation.setLongitude(longitude);
            }
            else if (requestCode == 1235) {
                Intent intent = new Intent(AddCourseActivity.this, MapsActivity.class);
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Log.d("Hello", "Write Permission Failed");
                Toast.makeText(this, "You must allow permission write external storage to your mobile device.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void requestWritePermission(){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
        }
    }

    @Override
    public void onBackPressed() {

    }

    public void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnMultiChoiceClickListener listener = (dialogInterface, i, b) -> {
            /*if(b){
                if(!selected_categories.contains(i)){
                    selected_categories.add(i);
                }
                else if(selected_categories.contains(i)){
                    selected_categories.remove((Integer)i);
                }
            }
            else if(selected_categories.contains(i)){
                selected_categories.remove((Integer)i);
            }*/
            isCheckedCategories[i] = b;
        };
        builder.setTitle("Choose Categories").setMultiChoiceItems(categories, isCheckedCategories, listener).setPositiveButton("OK", (dialogInterface, i) -> {
            categories_chosen.clear();
            group.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            for (int i1 = 0; i1 < categories.length; i1++) {
                if (isCheckedCategories[i1]) {
                    categories_chosen.add(categories[i1]);
                    Chip chip = (Chip) inflater.inflate(R.layout.chip_entry, group, false);
                    chip.setText(categories[i1]);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            categories_chosen.remove(group.indexOfChild(view));
                            group.removeView(view);
                        }
                    });
                    group.addView(chip);
                }
            }
            for (int i1 = 0; i1 < categories.length; i1++) isCheckedCategories[i1] = false;
        }).setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss()).setNeutralButton("Clear", (dialogInterface, i) -> {
            categories_chosen.clear();
            for (int i1 = 0; i1 < categories.length; i1++) isCheckedCategories[i1] = false;
            group.removeAllViews();
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void getCategories() {
        categories = getResources().getStringArray(R.array.categories);
        //categories = new String[]{"Science", "Arts"};
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
            googleApiClient = new GoogleApiClient.Builder(AddCourseActivity.this)
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
                                status.startResolutionForResult(AddCourseActivity.this, REQUEST_LOCATION);

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

}
