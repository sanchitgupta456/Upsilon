package com.sanchit.Upsilon;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sanchit.Upsilon.courseData.IntroductoryContentAdapter;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class AddCourseActivityContinued extends AppCompatActivity {

    private Document courseDetails;
    String appID = "upsilon-ityvn";
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    Button nextButton;
    JSONObject json;
    String insertedId;
    FloatingActionButton addImages,addVideos,addDocuments;
    String picturePath,videoPath,documentPath;
    ArrayList<String> picturePaths;
    ArrayList<String> introductoryImageUrls;
    ArrayList<String> videoPaths;
    ArrayList<String> introductoryVideoUrls;
    ArrayList<String> documentPaths;
    ArrayList<String> introductoryDocumentUrls;
    RecyclerView recyclerView;
    IntroductoryContentAdapter adapter;
    FrameLayout frameLayout;
    String id="5fad2a5a9f30789191ea7d15";
    private static int RESULT_LOAD_IMAGE = 1,RESULT_LOAD_VIDEO = 2,RESULT_LOAD_DOCUMENT=3;
    private static final int WRITE_PERMISSION = 0x01;
    View bar;
    private int fees;
    private RequestQueue queue;
    private String API ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_contd);
        queue = Volley.newRequestQueue(getApplicationContext());
        API = ((Upsilon)this.getApplication()).getAPI();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(R.string.add_introductory_content);
        bar = (View) getSupportActionBar().getCustomView();
        addImages = (FloatingActionButton) findViewById(R.id.add_images_introductory);
        addVideos = (FloatingActionButton) findViewById(R.id.add_video_introductory);
        addDocuments = (FloatingActionButton) findViewById(R.id.add_documents_introductory);
        recyclerView =(RecyclerView) findViewById(R.id.display_introductory_content);
        Intent intent = getIntent();
        id = intent.getStringExtra("InsertedDocument");
        fees = intent.getIntExtra("fees",0);
//        ObjectId _id = new ObjectId(id);
//        Log.v("Continued", String.valueOf(_id));
        picturePaths = new ArrayList<>();
        introductoryImageUrls = new ArrayList<>();
        videoPaths = new ArrayList<>();
        introductoryVideoUrls = new ArrayList<>();
        documentPaths = new ArrayList<>();
        introductoryVideoUrls = new ArrayList<>();
        nextButton = (Button) findViewById(R.id.btnAddCourse);
        frameLayout = (FrameLayout) findViewById(R.id.frameLoading);
        frameLayout.setVisibility(View.GONE);
        adapter = new IntroductoryContentAdapter(picturePaths, videoPaths);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.HORIZONTAL, false));
        adapter.notifyDataSetChanged();

//        app = new App(new AppConfiguration.Builder(appID)
//                .build());
//        user = app.currentUser();
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("Upsilon");
//        MongoCollection<org.bson.Document> mongoCollection  = mongoDatabase.getCollection("CourseData");
//        MongoCollection<org.bson.Document> mongoCollection1  = mongoDatabase.getCollection("TeacherPaymentData");

        addImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        addVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                i.setType("video/*");
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(i, RESULT_LOAD_VIDEO);
            }
        });

        addDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(
                        Intent.ACTION_PICK);
                i.setType("application/pdf");
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(i, RESULT_LOAD_DOCUMENT);*/

                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_DOCUMENT);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.VISIBLE);
                if(picturePaths.size()==0 && videoPaths.size()==0 && documentPaths.size()==0)
                {
                    Intent intent1 = new Intent(AddCourseActivityContinued.this, MainActivity.class);
                    startActivity(intent1);
                }
                else
                {
                    final int[] count = {0};
                    int counter=0;
                    while(counter<picturePaths.size())
                    {
                        String requestId = MediaManager.get().upload(picturePaths.get(counter))
                                .unsigned("preset1")
                                .option("resource_type", "image")
                                .option("folder", "Upsilon/Courses/"+id+"/IntroductoryContent/Images")
                                .option("public_id", "IntroductoryImage"+counter)
                                .callback(new UploadCallback() {
                                    @Override
                                    public void onStart(String requestId) {

                                    }

                                    @Override
                                    public void onProgress(String requestId, long bytes, long totalBytes) {

                                    }

                                    @Override
                                    public void onSuccess(String requestId, Map resultData) {
                                        introductoryImageUrls.add(resultData.get("url").toString());
                                        Log.v("Urls",introductoryImageUrls.toString());
                                        try {
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("url",resultData.get("url").toString());
                                            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/addIntroductoryImage",jsonObject,
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            Log.d("BecomingTeacher", response.toString());
                                                            count[0]++;
                                                            if(count[0] ==picturePaths.size()+videoPaths.size()+documentPaths.size())
                                                            {
                                                                Intent intent1 = new Intent(AddCourseActivityContinued.this, MainActivity.class);
                                                                startActivity(intent1);
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @SuppressLint("LongLogTag")
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Log.d("ErrorBecomingTeacher", error.toString());
                                                            count[0]++;
                                                            if(count[0] ==picturePaths.size()+videoPaths.size()+documentPaths.size())
                                                            {
                                                                Intent intent1 = new Intent(AddCourseActivityContinued.this, MainActivity.class);
                                                                startActivity(intent1);
                                                            }
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
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }

                                    @Override
                                    public void onError(String requestId, ErrorInfo error) {
                                        count[0]++;
                                        if(count[0] ==picturePaths.size()+videoPaths.size()+documentPaths.size())
                                        {
                                            Intent intent1 = new Intent(AddCourseActivityContinued.this, MainActivity.class);
                                            startActivity(intent1);
                                        }
                                    }

                                    @Override
                                    public void onReschedule(String requestId, ErrorInfo error) {

                                    }
                                }).dispatch();
                        counter++;
                    }
                    counter=0;
                    while(counter<videoPaths.size())
                    {
                        String requestId = MediaManager.get().upload(videoPaths.get(counter))
                                .unsigned("preset1")
                                .option("resource_type", "video")
                                .option("folder", "Upsilon/Courses/"+id+"/IntroductoryContent/Videos")
                                .option("public_id", "IntroductoryVideo"+counter)
                                .callback(new UploadCallback() {
                                    @Override
                                    public void onStart(String requestId) {

                                    }

                                    @Override
                                    public void onProgress(String requestId, long bytes, long totalBytes) {

                                    }

                                    @Override
                                    public void onSuccess(String requestId, Map resultData) {
                                        introductoryVideoUrls.add(resultData.get("url").toString());
                                        Log.v("Urls",introductoryVideoUrls.toString());
                                        try {
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("url",resultData.get("url").toString());
                                            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/addIntroductoryVideo",jsonObject,
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            Log.d("BecomingTeacher", response.toString());
                                                            count[0]++;
                                                            if(count[0] ==picturePaths.size()+videoPaths.size()+documentPaths.size())
                                                            {
                                                                Intent intent1 = new Intent(AddCourseActivityContinued.this, MainActivity.class);
                                                                startActivity(intent1);
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @SuppressLint("LongLogTag")
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Log.d("ErrorBecomingTeacher", error.toString());
                                                            count[0]++;
                                                            if(count[0] ==picturePaths.size()+videoPaths.size()+documentPaths.size())
                                                            {
                                                                Intent intent1 = new Intent(AddCourseActivityContinued.this, MainActivity.class);
                                                                startActivity(intent1);
                                                            }
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
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }

                                    @Override
                                    public void onError(String requestId, ErrorInfo error) {
                                        count[0]++;
                                        if(count[0] ==picturePaths.size()+videoPaths.size()+documentPaths.size())
                                        {
                                            Intent intent1 = new Intent(AddCourseActivityContinued.this, MainActivity.class);
                                            startActivity(intent1);
                                        }
                                    }

                                    @Override
                                    public void onReschedule(String requestId, ErrorInfo error) {

                                    }
                                }).dispatch();
                        counter++;
                    }
                    counter=0;
                    while(counter<documentPaths.size())
                    {
                        String requestId = MediaManager.get().upload(documentPaths.get(counter))
                                .unsigned("preset1")
                                .option("resource_type", "raw")
                                .option("folder", "Upsilon/Courses/"+id+"/IntroductoryContent/Documents")
                                .option("public_id", "IntroductoryDocument"+counter)
                                .callback(new UploadCallback() {
                                    @Override
                                    public void onStart(String requestId) {

                                    }

                                    @Override
                                    public void onProgress(String requestId, long bytes, long totalBytes) {

                                    }

                                    @Override
                                    public void onSuccess(String requestId, Map resultData) {
                                        introductoryDocumentUrls.add(resultData.get("url").toString());
                                        Log.v("Urls",introductoryDocumentUrls.toString());
                                        try {
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("url",resultData.get("url").toString());
                                            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/addintroductoryDocument",jsonObject,
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            Log.d("BecomingTeacher", response.toString());
                                                            count[0]++;
                                                            if(count[0] ==picturePaths.size()+videoPaths.size()+documentPaths.size())
                                                            {
                                                                Intent intent1 = new Intent(AddCourseActivityContinued.this, MainActivity.class);
                                                                startActivity(intent1);
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @SuppressLint("LongLogTag")
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Log.d("ErrorBecomingTeacher", error.toString());
                                                            count[0]++;
                                                            if(count[0] ==picturePaths.size()+videoPaths.size()+documentPaths.size())
                                                            {
                                                                Intent intent1 = new Intent(AddCourseActivityContinued.this, MainActivity.class);
                                                                startActivity(intent1);
                                                            }
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
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }

                                    @Override
                                    public void onError(String requestId, ErrorInfo error) {
                                        count[0]++;
                                        if(count[0] ==picturePaths.size()+videoPaths.size()+documentPaths.size())
                                        {
                                            Intent intent1 = new Intent(AddCourseActivityContinued.this, MainActivity.class);
                                            startActivity(intent1);
                                        }
                                    }

                                    @Override
                                    public void onReschedule(String requestId, ErrorInfo error) {

                                    }
                                }).dispatch();
                        counter++;
                    }

//                    counter=0;
//                    for (counter=0;counter<videoPaths.size();counter++)
//                    {
//                        int finalCounter = counter;
//                        String requestId = MediaManager.get().upload(videoPaths.get(counter))
//                                .unsigned("preset1")
//                                .option("resource_type", "video")
//                                .option("folder", "Upsilon/Courses/"+id+"/IntroductoryContent/Videos")
//                                .option("public_id", "IntroductoryVideo"+counter)
//                                .callback(new UploadCallback() {
//                                    @Override
//                                    public void onStart(String requestId) {
//
//                                    }
//
//                                    @Override
//                                    public void onProgress(String requestId, long bytes, long totalBytes) {
//
//                                    }
//
//                                    @Override
//                                    public void onSuccess(String requestId, Map resultData) {
//                                        introductoryVideoUrls.add(resultData.get("url").toString());
//                                        Log.v("Urls",introductoryVideoUrls.toString());
//                                    if(finalCounter ==videoPaths.size()-1)
//                                    {
//                                        Document queryFilter  = new Document("_id",_id);
//
//                                        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
//                                        RealmResultTask<MongoCursor<Document>> findTask1 = mongoCollection1.find(new Document("userid",user.getId())).iterator();
//
//
//                                        findTask.getAsync(task -> {
//                                            if (task.isSuccess()) {
//                                                MongoCursor<Document> results = task.get();
//                                                if(!results.hasNext())
//                                                {
//                                                    /*mongoCollection.insertOne(
//                                                            new Document("userid", user.getId()).append("profilePicCounter",0).append("favoriteColor", "pink").append("profilePicUrl",resultData.get("url").toString()))
//                                                            .getAsync(result -> {
//                                                                if (result.isSuccess()) {
//                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
//                                                                            + result.get().getInsertedId());
//                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
//                                                                    //startActivity(intent);
//                                                                } else {
//                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
//                                                                }
//                                                            });*/
//                                                }
//                                                else
//                                                {
//                                                    Document userdata = results.next();
//                                                    userdata.append("IntroductoryContentVideos",introductoryVideoUrls);
//                                                    userdata.append("IntroductoryVideoCounter", finalCounter+1);
//
//                                                    mongoCollection.updateOne(
//                                                            new Document("_id",_id),(userdata))
//                                                            .getAsync(result -> {
//                                                                if (result.isSuccess()) {
//                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
//                                                                            + result.get().getModifiedCount());
//                                                                    if(fees>0) {
//                                                                        findTask1.getAsync(task1 -> {
//                                                                            if (task1.isSuccess()) {
//                                                                                MongoCursor<Document> results1 = task1.get();
//                                                                                if (!results1.hasNext()) {
//                                                                                    Intent intent1 = new Intent(AddCourseActivityContinued.this, AddCoursePayment.class);
//                                                                                    intent1.putExtra("id", _id);
//                                                                                    startActivity(intent1);
//                                                                                } else {
//                                                                                    startActivity(new Intent(AddCourseActivityContinued.this, MainActivity.class));
//                                                                                }
//                                                                            }
//                                                                        });
//                                                                    }
//                                                                    else
//                                                                    {
//                                                                        startActivity(new Intent(AddCourseActivityContinued.this, MainActivity.class));
//                                                                    }
//                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
//                                                                    //startActivity(intent);
//                                                                } else {
//                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
//                                                                }
//                                                            });
//                                                }
//                                                while (results.hasNext()) {
//                                                    //Log.v("EXAMPLE", results.next().toString());
//                                                    Document currentDoc = results.next();
//                                                    Log.v("User",currentDoc.getString("userid"));
//                                                }
//                                            } else {
//                                                Log.v("User","Failed to complete search");
//                                            }
//                                        });
//                                    }
//                                    }
//
//                                    @Override
//                                    public void onError(String requestId, ErrorInfo error) {
//
//                                    }
//
//                                    @Override
//                                    public void onReschedule(String requestId, ErrorInfo error) {
//
//                                    }
//                                }).dispatch();
//
//                    }
//
//                    counter=0;
//                    for (counter=0;counter<documentPaths.size();counter++)
//                    {
//                        int finalCounter = counter;
//                        String requestId = MediaManager.get().upload(documentPaths.get(counter))
//                                .unsigned("preset1")
//                                .option("resource_type", "document")
//                                .option("folder", "Upsilon/Courses/"+id+"/IntroductoryContent/Documents")
//                                .option("public_id", "IntroductoryDocument"+counter)
//                                .callback(new UploadCallback() {
//                                    @Override
//                                    public void onStart(String requestId) {
//
//                                    }
//
//                                    @Override
//                                    public void onProgress(String requestId, long bytes, long totalBytes) {
//
//                                    }
//
//                                    @Override
//                                    public void onSuccess(String requestId, Map resultData) {
//                                        introductoryDocumentUrls.add(resultData.get("url").toString());
//                                        Log.v("Urls",introductoryDocumentUrls.toString());
//                                    if(finalCounter ==documentPaths.size()-1)
//                                    {
//                                        Document queryFilter  = new Document("_id",_id);
//
//                                        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
//
//                                        findTask.getAsync(task -> {
//                                            if (task.isSuccess()) {
//                                                MongoCursor<Document> results = task.get();
//                                                if(!results.hasNext())
//                                                {
//                                                    /*mongoCollection.insertOne(
//                                                            new Document("userid", user.getId()).append("profilePicCounter",0).append("favoriteColor", "pink").append("profilePicUrl",resultData.get("url").toString()))
//                                                            .getAsync(result -> {
//                                                                if (result.isSuccess()) {
//                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
//                                                                            + result.get().getInsertedId());
//                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
//                                                                    //startActivity(intent);
//                                                                } else {
//                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
//                                                                }
//                                                            });*/
//                                                }
//                                                else
//                                                {
//                                                    Document userdata = results.next();
//                                                    userdata.append("IntroductoryContentDocuments",introductoryDocumentUrls);
//                                                    userdata.append("IntroductoryDocumentCounter", finalCounter+1);
//
//                                                    mongoCollection.updateOne(
//                                                            new Document("_id",_id),(userdata))
//                                                            .getAsync(result -> {
//                                                                if (result.isSuccess()) {
//                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
//                                                                            + result.get().getModifiedCount());
//                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
//                                                                    //startActivity(intent);
//                                                                } else {
//                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
//                                                                }
//                                                            });
//                                                }
//                                                while (results.hasNext()) {
//                                                    //Log.v("EXAMPLE", results.next().toString());
//                                                    Document currentDoc = results.next();
//                                                    Log.v("User",currentDoc.getString("userid"));
//                                                }
//                                            } else {
//                                                Log.v("User","Failed to complete search");
//                                            }
//                                        });
//                                    }
//                                    }
//
//                                    @Override
//                                    public void onError(String requestId, ErrorInfo error) {
//
//                                    }
//
//                                    @Override
//                                    public void onReschedule(String requestId, ErrorInfo error) {
//
//                                    }
//                                }).dispatch();
//
//                    }

                /*mongoCollection.updateOne(new Document("_id",insertedId),()).getAsync(result -> {
                    if(result.isSuccess())
                    {
                        App.Result result1 = result;
                        //Log.v("AddCourse", String.valueOf(result)+ result.get().getInsertedId());
                        Intent intent = new Intent(AddCourseActivityContinued.this,MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Successfully Added The Course", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Log.v("User",result.getError().toString());
                    }
                });*/
                }


            }
        });
    }

    // To handle when an image is selected from the browser, add the following to your Activity
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
                if(data.getClipData()!=null)
                {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for(int i = 0; i < count; i++) {
                        Uri selectedImage = data.getClipData().getItemAt(i).getUri();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);
                        picturePaths.add(picturePath);
                        cursor.close();
                    }
                    Log.v("Images", String.valueOf(picturePaths));
                }
                else if(data.getData()!=null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    picturePaths.add(picturePath);
                    cursor.close();
                    // String picturePath contains the path of selected Image
                    //ImageView imageView = (ImageView) findViewById(R.id.profilePhoto);
                    //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
            }
            else if (requestCode == RESULT_LOAD_VIDEO && resultCode == RESULT_OK && null != data) {
                if(data.getClipData()!=null)
                {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for(int i = 0; i < count; i++) {
                        Uri selectedImage = data.getClipData().getItemAt(i).getUri();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        videoPath = cursor.getString(columnIndex);
                        videoPaths.add(videoPath);
                        cursor.close();
                    }
                    Log.v("Videos", String.valueOf(videoPaths));
                }
                else if(data.getData()!=null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    videoPath = cursor.getString(columnIndex);
                    videoPaths.add(videoPath);
                    cursor.close();
                    // String picturePath contains the path of selected Image
                    //ImageView imageView = (ImageView) findViewById(R.id.profilePhoto);
                    //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
            }
            else if (requestCode == RESULT_LOAD_DOCUMENT && resultCode == RESULT_OK && null != data) {
                    // Checking for selection multiple files or single.
                    if (data.getClipData() != null){

                        // Getting the length of data and logging up the logs using index
                        for (int index = 0; index < data.getClipData().getItemCount(); index++) {

                            // Getting the URIs of the selected files and logging them into logcat at debug level
                            /*Uri selectedImage = data.getClipData().getItemAt(index).getUri();
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            documentPath = cursor.getString(columnIndex);
                            documentPaths.add(documentPath);
                            cursor.close();*/

                            Log.v("Documents", String.valueOf(documentPaths));
                            //Log.d("filesUri [" + uri + "] : ", String.valueOf(uri) );
                        }
                    }else{

                        // Getting the URI of the selected file and logging into logcat at debug level
                        Uri uri = data.getData();
                        //Log.d("fileUri: ", String.valueOf(uri));
                        Log.v("Documents", String.valueOf(documentPaths));
                    }

                /*if(data.getClipData()!=null)
                {

                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for(int i = 0; i < count; i++) {
                        Uri selectedImage = data.getClipData().getItemAt(i).getUri();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        documentPath = cursor.getString(columnIndex);
                        documentPaths.add(documentPath);
                        cursor.close();
                    }
                    Log.v("Documents","Hello"+ String.valueOf(documentPaths));
                }
                else if(data.getData()!=null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    documentPath = cursor.getString(columnIndex);
                    cursor.close();
                    // String picturePath contains the path of selected Image
                    //ImageView imageView = (ImageView) findViewById(R.id.profilePhoto);
                    //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }*/
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getActionView()==bar) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
