package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sanchit.Upsilon.classData.ScheduledClass;
import com.sanchit.Upsilon.courseData.CourseFinal;
import com.sanchit.Upsilon.courseData.ResourceAdapter;
import com.sanchit.Upsilon.pdfUpload.UriUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ClassActivityTeacher extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ClassActivityTeacher";
    private CourseFinal course;
    private ScheduledClass scheduledClass;
    private TextInputLayout dateLayout, startTimeLayout, endTimeLayout;
    private TextInputEditText date;
    private TextInputEditText start_time;
    private TextInputEditText end_time;
//    private CardView alter, alter2;
//    private LinearLayout progressBarAlter;
//    private Button mark, markTrue;
    private RecyclerView recyclerView;
    ResourceAdapter adapter;

    private FloatingActionButton addVideo, addDoc, addImage;
    private Button updateChange;

    View dialogView;
    private SimpleDateFormat dateFormatter;

    private ArrayList<String> videos = new ArrayList<>();
    private ArrayList<String> docs = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private int startMonth;
    private int startYear;
    private int startDay;
    DatePickerDialog datePickerDialog;
    TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    private RequestQueue queue;
    private String API ;
    private static int RESULT_LOAD_IMAGE = 1,RESULT_LOAD_VIDEO = 2,RESULT_LOAD_DOCUMENT=3;
    private static final int WRITE_PERMISSION = 0x01;
    String picturePath,videoPath,documentPath;
    ArrayList<String> picturePaths = new ArrayList<>();
    ArrayList<String> introductoryImageUrls;
    ArrayList<String> videoPaths = new ArrayList<>();
    ArrayList<String> introductoryVideoUrls;
    ArrayList<String> documentPaths = new ArrayList<>();
    ArrayList<String> introductoryDocumentUrls;
    String id;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_teacher);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        queue = Volley.newRequestQueue(getApplicationContext());
        API = ((Upsilon)getApplication()).getAPI();

        Intent intent = getIntent();
        scheduledClass = (ScheduledClass) intent.getSerializableExtra("ScheduledClass");
        id = intent.getStringExtra("id");
        date = (TextInputEditText) findViewById(R.id.etDate);
        dateLayout = (TextInputLayout) findViewById(R.id.ll1);
        startTimeLayout = (TextInputLayout) findViewById(R.id.ll2);
        endTimeLayout = (TextInputLayout) findViewById(R.id.ll3);
        start_time = (TextInputEditText) findViewById(R.id.etStartTime);
        end_time = (TextInputEditText) findViewById(R.id.etEndTime);
        addVideo = (FloatingActionButton) findViewById(R.id.btnAddVideo);
        addDoc = (FloatingActionButton) findViewById(R.id.btnAddDoc);
        addImage = (FloatingActionButton) findViewById(R.id.btnAddImage);
        updateChange = (Button) findViewById(R.id.updateChange);
        recyclerView = (RecyclerView) findViewById(R.id.video_resources);

        setDateTimeField();

//        date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar calendar = Calendar.getInstance();
//                startDay = calendar.get(Calendar.DAY_OF_MONTH);
//                startMonth = calendar.get(Calendar.MONTH);
//                startYear = calendar.get(Calendar.YEAR);
//                datePickerDialog = new DatePickerDialog(
//                        getApplicationContext(), ClassActivityTeacher.this, startYear, startMonth, startDay);
//                datePickerDialog.show();
//            }
//        });
        View.OnClickListener dateListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        };
        View.OnClickListener startTimeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimePickerDialog.show();
            }
        };
        View.OnClickListener endTimeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTimePickerDialog.show();
            }
        };
        dateLayout.setStartIconOnClickListener(dateListener);
        dateLayout.setOnClickListener(dateListener);
        date.setOnClickListener(dateListener);
        date.setInputType(InputType.TYPE_NULL);
        dateLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    datePickerDialog.show();
                }
            }
        });
        startTimeLayout.setStartIconOnClickListener(startTimeListener);
        startTimeLayout.setOnClickListener(startTimeListener);
        start_time.setOnClickListener(startTimeListener);
        start_time.setInputType(InputType.TYPE_NULL);
        startTimeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    startTimePickerDialog.show();
                }
            }
        });
        endTimeLayout.setStartIconOnClickListener(endTimeListener);
        endTimeLayout.setOnClickListener(endTimeListener);
        end_time.setInputType(InputType.TYPE_NULL);
        end_time.setOnClickListener(endTimeListener);
        endTimeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    endTimePickerDialog.show();
                }
            }
        });

        //get the scheduled class in scheduledClass
        scheduledClass = (ScheduledClass) intent.getExtras().get("ScheduledClass");
        if(scheduledClass == null) {
            Log.d(TAG, "onCreate: scheduled class is null");
            return;
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(scheduledClass.getClassName());
        String dateString = scheduledClass.getMonth() + " " + scheduledClass.getDate();
        date.setText(dateString);
        start_time.setText(scheduledClass.getTime());
//        Log.v("scheduled",scheduledClass.getEndtime());
        end_time.setText(scheduledClass.getEndtime());

        addVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPaths = new ArrayList<>();
                Intent i = new Intent(
                        Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                i.setType("video/*");
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(i, RESULT_LOAD_VIDEO);
            }
        });
        addDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentPaths = new ArrayList<>();
                requestWritePermission();
                verifyStoragePermissions(ClassActivityTeacher.this);
//                Intent i = new Intent(
//                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, RESULT_LOAD_DOCUMENT);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                // Update with mime types
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

                startActivityForResult(intent, RESULT_LOAD_DOCUMENT);
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picturePaths = new ArrayList<>();
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        updateChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: update class information of date, start and end times
            }
        });

        adapter = new ResourceAdapter(videos, docs, images);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        getData();
        adapter.notifyDataSetChanged();
    }

    public void getData() {
        //TODO: fetch videos, docs and images
    }

    private void setDateTimeField() {
        dateLayout.setOnClickListener(this);
        date.setOnClickListener(this);
        dateLayout.setStartIconOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        startTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                StringBuilder timeString = new StringBuilder();
                timeString.append(hourOfDay).append(":");
                if(minute<10) timeString.append("0");
                timeString.append(minute);
                start_time.setText(timeString.toString());
            }
        },newCalendar.get(Calendar.HOUR), newCalendar.get(Calendar.MINUTE), true);

        endTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                StringBuilder timeString = new StringBuilder();
                timeString.append(hourOfDay).append(":");
                if(minute<10) timeString.append("0");
                timeString.append(minute);
                end_time.setText(timeString.toString());
            }
        },newCalendar.get(Calendar.HOUR), newCalendar.get(Calendar.MINUTE), true);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode==100)
            {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if(Environment.isExternalStorageManager())
                        {

                        }
                        else
                        {
                            verifyStoragePermissions(ClassActivityTeacher.this);
                        }
                    }

            }
            else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for (int i = 0; i < count; i++) {
                        Uri selectedImage = data.getClipData().getItemAt(i).getUri();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);
                        picturePaths.add(picturePath);
                        images.add(picturePath);
                        adapter.notifyDataSetChanged();
                        cursor.close();
                    }
                    Log.v("Images", String.valueOf(picturePaths));
                } else if (data.getData() != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    picturePaths.add(picturePath);
                    images.add(picturePath);
                    adapter.notifyDataSetChanged();
                    cursor.close();
                }
                for (int i = 0; i < picturePaths.size(); i++)
                {
                    String requestId = MediaManager.get().upload(picturePaths.get(i))
                            .unsigned("preset1")
                            .option("resource_type", "image")
                            .option("folder", "Upsilon/Courses/"+id+"/"+scheduledClass.getId()+"/Images")
                            .option("public_id", "Image"+id+scheduledClass.getId()+i+ UUID.randomUUID())
                            .callback(new UploadCallback() {
                                @Override
                                public void onStart(String requestId) {

                                }

                                @Override
                                public void onProgress(String requestId, long bytes, long totalBytes) {

                                }

                                @Override
                                public void onSuccess(String requestId, Map resultData) {
                                    try {
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("url",resultData.get("url").toString());
                                        jsonObject.put("id",scheduledClass.getId());
                                        jsonObject.put("courseId",id);
                                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/addImage",jsonObject,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        Log.d("UploadingResource", response.toString());

                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @SuppressLint("LongLogTag")
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.d("ErrorUploadingResource", error.toString());

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

                                }

                                @Override
                                public void onReschedule(String requestId, ErrorInfo error) {

                                }
                            }).dispatch();
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
                        videos.add(videoPath);
                        adapter.notifyDataSetChanged();
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
                    videos.add(videoPath);
                    adapter.notifyDataSetChanged();
                    cursor.close();
                }
                for (int i = 0; i < videoPaths.size(); i++)
                {
                    String requestId = MediaManager.get().upload(videoPaths.get(i))
                            .unsigned("preset1")
                            .option("resource_type", "video")
                            .option("folder", "Upsilon/Courses/"+id+"/"+scheduledClass.getId()+"/Videos")
                            .option("public_id", "Video"+id+scheduledClass.getId()+i+UUID.randomUUID())
                            .callback(new UploadCallback() {
                                @Override
                                public void onStart(String requestId) {

                                }

                                @Override
                                public void onProgress(String requestId, long bytes, long totalBytes) {

                                }

                                @Override
                                public void onSuccess(String requestId, Map resultData) {
                                    try {
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("url",resultData.get("url").toString());
                                        jsonObject.put("id",scheduledClass.getId());
                                        jsonObject.put("courseId",id);
                                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/addVideo",jsonObject,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        Log.d("UploadingResource", response.toString());

                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @SuppressLint("LongLogTag")
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.d("ErrorUploadingResource", error.toString());

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

                                }

                                @Override
                                public void onReschedule(String requestId, ErrorInfo error) {

                                }
                            }).dispatch();
                }
            }
            else if (requestCode == RESULT_LOAD_DOCUMENT && resultCode == RESULT_OK && null != data) {
                // Checking for selection multiple files or single.
                if(data.getClipData()!=null)
                {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for(int i = 0; i < count; i++) {
                        Uri selectedImage = data.getClipData().getItemAt(i).getUri();
                        String fullFilePath = UriUtils.getPathFromUri(this, selectedImage);
                        documentPaths.add(fullFilePath);
                        docs.add(fullFilePath);
                        adapter.notifyDataSetChanged();
                    }
                    Log.v("Videos", String.valueOf(documentPaths));
                }
                else if(data.getData()!=null) {
                    String fullFilePath = UriUtils.getPathFromUri(this, data.getData());
                    Log.v("path", String.valueOf(fullFilePath));
//                    documentPath = cursor.getString(columnIndex);
                    documentPaths.add(fullFilePath);
                    docs.add(fullFilePath);
                    adapter.notifyDataSetChanged();
                }
                Log.v("Paths", String.valueOf(documentPaths));
                for (int i = 0; i < documentPaths.size(); i++)
                {
                    String requestId = MediaManager.get().upload(documentPaths.get(i))
                            .unsigned("preset1")
                            .option("resource_type", "raw")
                            .option("folder", "Upsilon/Courses/"+id+"/"+scheduledClass.getId()+"/Documents")
                            .option("public_id", "Document"+id+scheduledClass.getId()+i+UUID.randomUUID())
                            .callback(new UploadCallback() {
                                @Override
                                public void onStart(String requestId) {

                                }

                                @Override
                                public void onProgress(String requestId, long bytes, long totalBytes) {

                                }

                                @Override
                                public void onSuccess(String requestId, Map resultData) {
                                    try {
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("url",resultData.get("url").toString());
                                        jsonObject.put("id",scheduledClass.getId());
                                        jsonObject.put("courseId",id);
                                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/addDocument",jsonObject,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        Log.d("UploadingResource", response.toString());

                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @SuppressLint("LongLogTag")
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.d("ErrorUploadingResource", error.toString());

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

                                }

                                @Override
                                public void onReschedule(String requestId, ErrorInfo error) {

                                }
                            }).dispatch();
                }

          }
//            adapter.notifyDataSetChanged();
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

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: clicked");
//        if(v == dateLayout) datePickerDialog.show();
//        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestWritePermission(){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
        }
    }

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(ClassActivityTeacher.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R)
        {
            if(Environment.isExternalStorageManager())
            {

            }
            else
            {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                    startActivityForResult(intent,100);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent,100);
                }
            }
        }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        else
        {
            Log.v("Permission","Has Permission");
        }
    }
}