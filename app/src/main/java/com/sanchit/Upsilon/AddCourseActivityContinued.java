package com.sanchit.Upsilon;

import android.Manifest;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

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
    LinearLayout addImages,addVideos,addDocuments;
    String picturePath,videoPath,documentPath;
    ArrayList<String> picturePaths;
    ArrayList<String> introductoryImageUrls;
    ArrayList<String> videoPaths;
    ArrayList<String> introductoryVideoUrls;
    ArrayList<String> documentPaths;
    ArrayList<String> introductoryDocumentUrls;
    RecyclerView recyclerView;
    String id="5fad2a5a9f30789191ea7d15";
    ImageButton imageButtonBack, imageButtonProceed;
    private static int RESULT_LOAD_IMAGE = 1,RESULT_LOAD_VIDEO = 2,RESULT_LOAD_DOCUMENT=3;
    private static final int WRITE_PERMISSION = 0x01;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_contd);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_add_course_contd);
        getSupportActionBar().setElevation(10);
        addImages = (LinearLayout) findViewById(R.id.add_images_introductory);
        addVideos = (LinearLayout) findViewById(R.id.add_video_introductory);
        addDocuments = (LinearLayout) findViewById(R.id.add_documents_introductory);
        recyclerView =(RecyclerView) findViewById(R.id.listIntroductoryMaterial);
        imageButtonBack = (ImageButton) findViewById(R.id.imgBtnBackAddCourseContd);
        imageButtonProceed = (ImageButton) findViewById(R.id.imgBtnProceedAddCourseContd);
        Intent intent = getIntent();
        id = intent.getStringExtra("InsertedDocument");
        ObjectId _id = new ObjectId(id);
        Log.v("Continued", String.valueOf(_id));
        picturePaths = new ArrayList<>();
        introductoryImageUrls = new ArrayList<>();
        videoPaths = new ArrayList<>();
        introductoryVideoUrls = new ArrayList<>();
        documentPaths = new ArrayList<>();
        introductoryVideoUrls = new ArrayList<>();
        nextButton = (Button) findViewById(R.id.btnAddCourse);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<org.bson.Document> mongoCollection  = mongoDatabase.getCollection("CourseData");

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
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCourseActivityContinued.this, AddCourseActivity.class);
                startActivity(intent);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(picturePaths.size()==0 && videoPaths.size()==0 && documentPaths.size()==0)
                {
                    Document queryFilter  = new Document("_id",_id);

                    RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                    findTask.getAsync(task -> {
                        if (task.isSuccess()) {
                            MongoCursor<Document> results = task.get();
                            if(!results.hasNext())
                            {
                                                    /*mongoCollection.insertOne(
                                                            new Document("userid", user.getId()).append("profilePicCounter",0).append("favoriteColor", "pink").append("profilePicUrl",resultData.get("url").toString()))
                                                            .getAsync(result -> {
                                                                if (result.isSuccess()) {
                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                                            + result.get().getInsertedId());
                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                                    //startActivity(intent);
                                                                } else {
                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                                                }
                                                            });*/
                            }
                            else
                            {
                                Document userdata = results.next();
                                userdata.append("IntroductoryContentImages",introductoryImageUrls);
                                userdata.append("IntroductoryImageCounter", 0);
                                userdata.append("courseId",_id.toString());
                                Log.v("AddedCourseId", String.valueOf(_id));

                                mongoCollection.updateOne(
                                        new Document("_id",_id),(userdata))
                                        .getAsync(result -> {
                                            if (result.isSuccess()) {
                                                Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                        + result.get().getModifiedCount());
                                                //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                //startActivity(intent);
                                            } else {
                                                Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                            }
                                        });
                            }
                            while (results.hasNext()) {
                                //Log.v("EXAMPLE", results.next().toString());
                                Document currentDoc = results.next();
                                Log.v("User",currentDoc.getString("userid"));
                            }
                        } else {
                            Log.v("User","Failed to complete search");
                        }
                    });
                }

                int counter=0;
                for (counter=0;counter<picturePaths.size();counter++)
                {
                    int finalCounter = counter;
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
                                    if(finalCounter ==picturePaths.size()-1)
                                    {
                                        Document queryFilter  = new Document("_id",_id);

                                        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                                        findTask.getAsync(task -> {
                                            if (task.isSuccess()) {
                                                MongoCursor<Document> results = task.get();
                                                if(!results.hasNext())
                                                {
                                                    /*mongoCollection.insertOne(
                                                            new Document("userid", user.getId()).append("profilePicCounter",0).append("favoriteColor", "pink").append("profilePicUrl",resultData.get("url").toString()))
                                                            .getAsync(result -> {
                                                                if (result.isSuccess()) {
                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                                            + result.get().getInsertedId());
                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                                    //startActivity(intent);
                                                                } else {
                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                                                }
                                                            });*/
                                                }
                                                else
                                                {
                                                    Document userdata = results.next();
                                                    userdata.append("IntroductoryContentImages",introductoryImageUrls);
                                                    userdata.append("IntroductoryImageCounter", finalCounter +1);
                                                    userdata.append("courseId",_id.toString());
                                                    Log.v("AddedCourseId", String.valueOf(_id));

                                                    mongoCollection.updateOne(
                                                            new Document("_id",_id),(userdata))
                                                            .getAsync(result -> {
                                                                if (result.isSuccess()) {
                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                                            + result.get().getModifiedCount());
                                                                    if(videoPaths.size()==0)
                                                                    {
                                                                        startActivity(new Intent(AddCourseActivityContinued.this,MainActivity.class));
                                                                    }
                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                                    //startActivity(intent);
                                                                } else {
                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                                                }
                                                            });
                                                }
                                                while (results.hasNext()) {
                                                    //Log.v("EXAMPLE", results.next().toString());
                                                    Document currentDoc = results.next();
                                                    Log.v("User",currentDoc.getString("userid"));
                                                }
                                            } else {
                                                Log.v("User","Failed to complete search");
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onError(String requestId, ErrorInfo error) {

                                }

                                @Override
                                public void onReschedule(String requestId, ErrorInfo error) {

                                }
                            }).dispatch();

                }

                counter=0;
                for (counter=0;counter<videoPaths.size();counter++)
                {
                    int finalCounter = counter;
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
                                    if(finalCounter ==videoPaths.size()-1)
                                    {
                                        Document queryFilter  = new Document("_id",_id);

                                        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                                        findTask.getAsync(task -> {
                                            if (task.isSuccess()) {
                                                MongoCursor<Document> results = task.get();
                                                if(!results.hasNext())
                                                {
                                                    /*mongoCollection.insertOne(
                                                            new Document("userid", user.getId()).append("profilePicCounter",0).append("favoriteColor", "pink").append("profilePicUrl",resultData.get("url").toString()))
                                                            .getAsync(result -> {
                                                                if (result.isSuccess()) {
                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                                            + result.get().getInsertedId());
                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                                    //startActivity(intent);
                                                                } else {
                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                                                }
                                                            });*/
                                                }
                                                else
                                                {
                                                    Document userdata = results.next();
                                                    userdata.append("IntroductoryContentVideos",introductoryVideoUrls);
                                                    userdata.append("IntroductoryVideoCounter", finalCounter+1);

                                                    mongoCollection.updateOne(
                                                            new Document("_id",_id),(userdata))
                                                            .getAsync(result -> {
                                                                if (result.isSuccess()) {
                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                                            + result.get().getModifiedCount());
                                                                    startActivity(new Intent(AddCourseActivityContinued.this,MainActivity.class));
                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                                    //startActivity(intent);
                                                                } else {
                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                                                }
                                                            });
                                                }
                                                while (results.hasNext()) {
                                                    //Log.v("EXAMPLE", results.next().toString());
                                                    Document currentDoc = results.next();
                                                    Log.v("User",currentDoc.getString("userid"));
                                                }
                                            } else {
                                                Log.v("User","Failed to complete search");
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onError(String requestId, ErrorInfo error) {

                                }

                                @Override
                                public void onReschedule(String requestId, ErrorInfo error) {

                                }
                            }).dispatch();

                }

                counter=0;
                for (counter=0;counter<documentPaths.size();counter++)
                {
                    int finalCounter = counter;
                    String requestId = MediaManager.get().upload(documentPaths.get(counter))
                            .unsigned("preset1")
                            .option("resource_type", "document")
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
                                    if(finalCounter ==documentPaths.size()-1)
                                    {
                                        Document queryFilter  = new Document("_id",_id);

                                        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                                        findTask.getAsync(task -> {
                                            if (task.isSuccess()) {
                                                MongoCursor<Document> results = task.get();
                                                if(!results.hasNext())
                                                {
                                                    /*mongoCollection.insertOne(
                                                            new Document("userid", user.getId()).append("profilePicCounter",0).append("favoriteColor", "pink").append("profilePicUrl",resultData.get("url").toString()))
                                                            .getAsync(result -> {
                                                                if (result.isSuccess()) {
                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                                            + result.get().getInsertedId());
                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                                    //startActivity(intent);
                                                                } else {
                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                                                }
                                                            });*/
                                                }
                                                else
                                                {
                                                    Document userdata = results.next();
                                                    userdata.append("IntroductoryContentDocuments",introductoryDocumentUrls);
                                                    userdata.append("IntroductoryDocumentCounter", finalCounter+1);

                                                    mongoCollection.updateOne(
                                                            new Document("_id",_id),(userdata))
                                                            .getAsync(result -> {
                                                                if (result.isSuccess()) {
                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                                            + result.get().getModifiedCount());
                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                                    //startActivity(intent);
                                                                } else {
                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                                                }
                                                            });
                                                }
                                                while (results.hasNext()) {
                                                    //Log.v("EXAMPLE", results.next().toString());
                                                    Document currentDoc = results.next();
                                                    Log.v("User",currentDoc.getString("userid"));
                                                }
                                            } else {
                                                Log.v("User","Failed to complete search");
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onError(String requestId, ErrorInfo error) {

                                }

                                @Override
                                public void onReschedule(String requestId, ErrorInfo error) {

                                }
                            }).dispatch();

                }

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
        });
        imageButtonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(picturePaths.size()==0 && videoPaths.size()==0 && documentPaths.size()==0)
                {
                    Document queryFilter  = new Document("_id",_id);

                    RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                    findTask.getAsync(task -> {
                        if (task.isSuccess()) {
                            MongoCursor<Document> results = task.get();
                            if(!results.hasNext())
                            {
                                                    /*mongoCollection.insertOne(
                                                            new Document("userid", user.getId()).append("profilePicCounter",0).append("favoriteColor", "pink").append("profilePicUrl",resultData.get("url").toString()))
                                                            .getAsync(result -> {
                                                                if (result.isSuccess()) {
                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                                            + result.get().getInsertedId());
                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                                    //startActivity(intent);
                                                                } else {
                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                                                }
                                                            });*/
                            }
                            else
                            {
                                Document userdata = results.next();
                                userdata.append("IntroductoryContentImages",introductoryImageUrls);
                                userdata.append("IntroductoryImageCounter", 0);
                                userdata.append("courseId",_id.toString());
                                Log.v("AddedCourseId", String.valueOf(_id));

                                mongoCollection.updateOne(
                                        new Document("_id",_id),(userdata))
                                        .getAsync(result -> {
                                            if (result.isSuccess()) {
                                                Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                        + result.get().getModifiedCount());
                                                //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                //startActivity(intent);
                                            } else {
                                                Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                            }
                                        });
                            }
                            while (results.hasNext()) {
                                //Log.v("EXAMPLE", results.next().toString());
                                Document currentDoc = results.next();
                                Log.v("User",currentDoc.getString("userid"));
                            }
                        } else {
                            Log.v("User","Failed to complete search");
                        }
                    });
                }

                int counter=0;
                for (counter=0;counter<picturePaths.size();counter++)
                {
                    int finalCounter = counter;
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
                                    if(finalCounter ==picturePaths.size()-1)
                                    {
                                        Document queryFilter  = new Document("_id",_id);

                                        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                                        findTask.getAsync(task -> {
                                            if (task.isSuccess()) {
                                                MongoCursor<Document> results = task.get();
                                                if(!results.hasNext())
                                                {
                                                    /*mongoCollection.insertOne(
                                                            new Document("userid", user.getId()).append("profilePicCounter",0).append("favoriteColor", "pink").append("profilePicUrl",resultData.get("url").toString()))
                                                            .getAsync(result -> {
                                                                if (result.isSuccess()) {
                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                                            + result.get().getInsertedId());
                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                                    //startActivity(intent);
                                                                } else {
                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                                                }
                                                            });*/
                                                }
                                                else
                                                {
                                                    Document userdata = results.next();
                                                    userdata.append("IntroductoryContentImages",introductoryImageUrls);
                                                    userdata.append("IntroductoryImageCounter", finalCounter +1);
                                                    userdata.append("courseId",_id.toString());
                                                    Log.v("AddedCourseId", String.valueOf(_id));

                                                    mongoCollection.updateOne(
                                                            new Document("_id",_id),(userdata))
                                                            .getAsync(result -> {
                                                                if (result.isSuccess()) {
                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                                            + result.get().getModifiedCount());
                                                                    if(videoPaths.size()==0)
                                                                    {
                                                                        startActivity(new Intent(AddCourseActivityContinued.this,MainActivity.class));
                                                                    }
                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                                    //startActivity(intent);
                                                                } else {
                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                                                }
                                                            });
                                                }
                                                while (results.hasNext()) {
                                                    //Log.v("EXAMPLE", results.next().toString());
                                                    Document currentDoc = results.next();
                                                    Log.v("User",currentDoc.getString("userid"));
                                                }
                                            } else {
                                                Log.v("User","Failed to complete search");
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onError(String requestId, ErrorInfo error) {

                                }

                                @Override
                                public void onReschedule(String requestId, ErrorInfo error) {

                                }
                            }).dispatch();

                }

                counter=0;
                for (counter=0;counter<videoPaths.size();counter++)
                {
                    int finalCounter = counter;
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
                                    if(finalCounter ==videoPaths.size()-1)
                                    {
                                        Document queryFilter  = new Document("_id",_id);

                                        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                                        findTask.getAsync(task -> {
                                            if (task.isSuccess()) {
                                                MongoCursor<Document> results = task.get();
                                                if(!results.hasNext())
                                                {
                                                    /*mongoCollection.insertOne(
                                                            new Document("userid", user.getId()).append("profilePicCounter",0).append("favoriteColor", "pink").append("profilePicUrl",resultData.get("url").toString()))
                                                            .getAsync(result -> {
                                                                if (result.isSuccess()) {
                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                                            + result.get().getInsertedId());
                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                                    //startActivity(intent);
                                                                } else {
                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                                                }
                                                            });*/
                                                }
                                                else
                                                {
                                                    Document userdata = results.next();
                                                    userdata.append("IntroductoryContentVideos",introductoryVideoUrls);
                                                    userdata.append("IntroductoryVideoCounter", finalCounter+1);

                                                    mongoCollection.updateOne(
                                                            new Document("_id",_id),(userdata))
                                                            .getAsync(result -> {
                                                                if (result.isSuccess()) {
                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                                            + result.get().getModifiedCount());
                                                                    startActivity(new Intent(AddCourseActivityContinued.this,MainActivity.class));
                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                                    //startActivity(intent);
                                                                } else {
                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                                                }
                                                            });
                                                }
                                                while (results.hasNext()) {
                                                    //Log.v("EXAMPLE", results.next().toString());
                                                    Document currentDoc = results.next();
                                                    Log.v("User",currentDoc.getString("userid"));
                                                }
                                            } else {
                                                Log.v("User","Failed to complete search");
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onError(String requestId, ErrorInfo error) {

                                }

                                @Override
                                public void onReschedule(String requestId, ErrorInfo error) {

                                }
                            }).dispatch();

                }

                counter=0;
                for (counter=0;counter<documentPaths.size();counter++)
                {
                    int finalCounter = counter;
                    String requestId = MediaManager.get().upload(documentPaths.get(counter))
                            .unsigned("preset1")
                            .option("resource_type", "document")
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
                                    if(finalCounter ==documentPaths.size()-1)
                                    {
                                        Document queryFilter  = new Document("_id",_id);

                                        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                                        findTask.getAsync(task -> {
                                            if (task.isSuccess()) {
                                                MongoCursor<Document> results = task.get();
                                                if(!results.hasNext())
                                                {
                                                    /*mongoCollection.insertOne(
                                                            new Document("userid", user.getId()).append("profilePicCounter",0).append("favoriteColor", "pink").append("profilePicUrl",resultData.get("url").toString()))
                                                            .getAsync(result -> {
                                                                if (result.isSuccess()) {
                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                                            + result.get().getInsertedId());
                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                                    //startActivity(intent);
                                                                } else {
                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                                                }
                                                            });*/
                                                }
                                                else
                                                {
                                                    Document userdata = results.next();
                                                    userdata.append("IntroductoryContentDocuments",introductoryDocumentUrls);
                                                    userdata.append("IntroductoryDocumentCounter", finalCounter+1);

                                                    mongoCollection.updateOne(
                                                            new Document("_id",_id),(userdata))
                                                            .getAsync(result -> {
                                                                if (result.isSuccess()) {
                                                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                                            + result.get().getModifiedCount());
                                                                    //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                                    //startActivity(intent);
                                                                } else {
                                                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                                                }
                                                            });
                                                }
                                                while (results.hasNext()) {
                                                    //Log.v("EXAMPLE", results.next().toString());
                                                    Document currentDoc = results.next();
                                                    Log.v("User",currentDoc.getString("userid"));
                                                }
                                            } else {
                                                Log.v("User","Failed to complete search");
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onError(String requestId, ErrorInfo error) {

                                }

                                @Override
                                public void onReschedule(String requestId, ErrorInfo error) {

                                }
                            }).dispatch();

                }

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
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == WRITE_PERMISSION){
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
}
