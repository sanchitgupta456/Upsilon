package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sanchit.Upsilon.classData.ScheduledClass;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.IntroductoryContentAdapter;
import com.sanchit.Upsilon.courseData.VideoResourceAdapter;
import com.sanchit.Upsilon.pdfUpload.pdfPlayground;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static io.realm.Realm.getApplicationContext;

public class ClassActivityTeacher extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ClassActivityTeacher";
    private Course course;
    private ScheduledClass scheduledClass;
    private TextInputLayout dateLayout, startTimeLayout, endTimeLayout;
    private TextInputEditText date;
    private TextInputEditText start_time;
    private TextInputEditText end_time;
//    private CardView alter, alter2;
//    private LinearLayout progressBarAlter;
//    private Button mark, markTrue;
    private RecyclerView recyclerView;

    private FloatingActionButton addVideo, addDoc, addImage;
    private Button updateChange;

    View dialogView;
    private SimpleDateFormat dateFormatter;

    private ArrayList<String> videos = new ArrayList<>();
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
    ArrayList<String> picturePaths;
    ArrayList<String> introductoryImageUrls;
    ArrayList<String> videoPaths;
    ArrayList<String> introductoryVideoUrls;
    ArrayList<String> documentPaths;
    ArrayList<String> introductoryDocumentUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_teacher);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        queue = Volley.newRequestQueue(getApplicationContext());
        API = ((Upsilon)getApplication()).getAPI();

        Intent intent = getIntent();

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
        Log.v("scheduled",scheduledClass.getEndtime());
        end_time.setText(scheduledClass.getEndtime());

        addVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(Intent.createChooser(intent, "Select Document"), RESULT_LOAD_DOCUMENT);
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        VideoResourceAdapter adapter = new VideoResourceAdapter(videos);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        getVideos();
        adapter.notifyDataSetChanged();
    }

    public void getVideos() {
        //TODO: fetch videos
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
                    }
                }else{ Uri uri = data.getData();
                    Log.v("Documents", String.valueOf(documentPaths));
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
}