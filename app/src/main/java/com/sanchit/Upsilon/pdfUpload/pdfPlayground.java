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

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.sanchit.Upsilon.AddCourseActivity;
import com.sanchit.Upsilon.AddCourseActivityContinued;
import com.sanchit.Upsilon.R;

import org.bson.Document;

import java.util.Map;

public class pdfPlayground extends AppCompatActivity {

    Button selectPdf;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int WRITE_PERMISSION = 0x01;
    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_playground);

        selectPdf = (Button) findViewById(R.id.pdfSelect);

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

        String courseID = "602184b9c18652b70cc3ee4b";

        String requestId = MediaManager.get().upload(fullFilePath)
                .unsigned("preset1")
                .option("resource_type", "image")
                .option("folder", "Upsilon/Courses/".concat(courseID))
                .option("public_id", "CoursePdf " + 0)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        //TODO : @vedaant add progressbar UI animation here
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

    private void requestWritePermission(){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
        }
    }
}