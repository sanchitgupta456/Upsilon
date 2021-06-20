package com.sanchit.Upsilon.pdfUpload;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.sanchit.Upsilon.R;
import com.sanchit.Upsilon.courseData.Course;

import org.bson.Document;

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

public class pdfPlayground extends AppCompatActivity {

    Button selectPdf;
    EditText pdfName;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int WRITE_PERMISSION = 0x01;
    private static final int REQUEST_CODE = 0;

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    String appID = "upsilon-ityvn";

    //TODO: get the course object from the activity
    Course course = null;
    ArrayList<String> myCourses;

    public enum Type{
        MAT,
        PAP,
        SOLN
    }

    //Pdf details
    Type pdfType = Type.MAT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_playground);

        selectPdf = (Button) findViewById(R.id.pdfSelect);
        pdfName = (EditText) findViewById(R.id.pdfName);

        selectPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestWritePermission();
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                // Update with mime types
                intent.setType("*/*");

                // Update with additional mime types here using a String[].
                //intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

                // Only pick openable and local files. Theoretically we could pull files from google drive
                // or other applications that have networked files, but that's unnecessary for this example.
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the user doesn't pick a file just return
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE || resultCode != RESULT_OK) {
            return;
        }
        // Import the file
        String fullFilePath = UriUtils.getPathFromUri(this, data.getData());
        if (fullFilePath == null){
            Log.v("PDF_UPLOADER", "Filepath is NULL!");
        }
        else {
            Log.v("PDF_UPLOADER", fullFilePath);
        }

        checkNumPdfsAndUpload(pdfType, fullFilePath);
    }

    void checkNumPdfsAndUpload(Type pdfType, String filepath){
        Log.v("PDF_UPLOADER", "Fetching numPdfs");
        App app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();
        Log.v("Id",course.getTutorId());
        Log.v("Id",user.getId());
        if(course.getTutorId().equals(user.getId()))
        {
            Log.v("Id","matched");

            mongoClient = user.getMongoClient("mongodb-atlas");
            mongoDatabase = mongoClient.getDatabase("Upsilon");
            MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");

            Document queryFilter  = new Document("userid",user.getId());

            RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

            findTask.getAsync(task -> {
                if (task.isSuccess()) {
                    MongoCursor<Document> results = task.get();
                    if(results.hasNext())
                    {
                        try {
                            Document currentDoc = results.next();
                            myCourses = (ArrayList<String>) currentDoc.get("myCourses");
                            int i=0;
                            for(i=0;i<myCourses.size();i++)
                            {
                                if(((String)myCourses.get(i)).equals((String)course.getCourseId()))
                                {
                                    ArrayList<Integer> pdfs = (ArrayList<Integer>) currentDoc.get("numPdfs");
                                    int numPdfs = pdfs.get(pdfType.ordinal());
                                    String courseID = (String)course.getCourseId();
                                    String cloudPath = "Upsilon/Courses/".concat(courseID);
                                    if (pdfType == Type.MAT){
                                        cloudPath.concat("/materials");
                                    }
                                    else if (pdfType == Type.PAP) {
                                        cloudPath.concat("/papers");
                                    }
                                    else if (pdfType == Type.SOLN){
                                        cloudPath.concat("/solns");
                                    }
                                    else{
                                        Log.v("PDF_UPLOADER", "pdfType assignment exception detected");
                                    }

                                    String fileName = pdfName.getText().toString();
                                    if (fileName.length() == 0){
                                        fileName = filepath.substring(filepath.lastIndexOf("/") + 1);
                                    }

                                    String requestId = MediaManager.get().upload(filepath)
                                            .unsigned("preset1")
                                            .option("resource_type", "image")
                                            .option("folder", cloudPath)
                                            .option("public_id", fileName)
                                            .callback(new UploadCallback() {
                                                @Override
                                                public void onStart(String requestId) {
                                                }

                                                @RequiresApi(api = Build.VERSION_CODES.N)
                                                @Override
                                                public void onProgress(String requestId, long bytes, long totalBytes) {
                                                    //TODO : @vedant add progressbar UI animation here
                                                }

                                                @Override
                                                public void onSuccess(String requestId, Map resultData) {
                                                    Log.v("PDF_UPLOADER", "File uploaded successfully");
                                                }

                                                @Override
                                                public void onError(String requestId, ErrorInfo error) {
                                                    Log.v("PDF_UPLOADER", "Something went wrong while uploading the file");
                                                }

                                                @Override
                                                public void onReschedule(String requestId, ErrorInfo error) {

                                                }
                                            })
                                            .dispatch();
                                }
                            }
                            Log.v("User", "successfully found the user");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.v("User","Failed to complete search");
                }
            });


        }
        else{
            Log.v("PDF_UPLOADER", "Attempt to modify the data of a course which is not owned by user!");
        }
    }

    private void requestWritePermission(){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
        }
    }
}