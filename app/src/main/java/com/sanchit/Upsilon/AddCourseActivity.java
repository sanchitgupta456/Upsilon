package com.sanchit.Upsilon;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class AddCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "AddCourseActivity";

    String appID = "upsilon-ityvn";
    EditText CourseName,CourseDescription,CourseDuration,NumberOfBatches,CourseFees;
    String courseName,courseDescription,courseDuration,numOfBatches,mode,courseDurationMeasure;
    int fees;
    Button nextButton, addCategory;
    RadioButton Group,Individual,Free,Paid;
    ToggleButton offline_online;
    //Spinner spinner;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    ArrayList courseReviews;
    ImageView CourseImage;
    String CourseImageUrl;
    //RecyclerView courseCategories;
    TextView tvCourseCategoriesDisplay;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int WRITE_PERMISSION = 0x01;
    private ProgressBar progressBar;
    View bar;

    private String[] categories;
    private boolean[] isCheckedCategories;
    private ArrayList<Integer> selected_categories = new ArrayList<>();
    private ArrayList<String> categories_chosen = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(R.string.add_course_details);
        bar = (View) getSupportActionBar().getCustomView();
        CourseName = (EditText) findViewById(R.id.add_course_name);
        CourseDescription = (EditText) findViewById(R.id.add_course_description);
        addCategory = (Button) findViewById(R.id.add_course_category);
        //courseCategories = (RecyclerView) findViewById(R.id.categories_list_add_course);
        tvCourseCategoriesDisplay = (TextView) findViewById(R.id.textCategoriesSelected);
        CourseDuration = (EditText) findViewById(R.id.add_course_duration);
        //NumberOfBatches = (EditText) findViewById(R.id.add_course_num_batches);
        nextButton = (Button) findViewById(R.id.btnNext);
        offline_online = (ToggleButton) findViewById(R.id.add_course_mode);
        CourseImage = (ImageView) findViewById(R.id.imgAddCourseImage);
        CourseFees = (EditText) findViewById(R.id.course_fee);
        Free = (RadioButton) findViewById(R.id.add_course_free);
        Paid = (RadioButton) findViewById(R.id.add_course_paid);
        progressBar = (ProgressBar) findViewById(R.id.loadingAddCourse);

        //spinner = (Spinner) findViewById(R.id.courseDurationMeasureSpinner);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");
        MongoCollection<Document> mongoCollection1  = mongoDatabase.getCollection("ForumData");

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

        Paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseFees.setVisibility(View.VISIBLE);
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
                //numOfBatches = NumberOfBatches.getText().toString();

                if(Paid.isChecked())
                {
                    fees = Integer.parseInt(CourseFees.getText().toString());
                }

                //courseReviews = new ArrayList<CourseReview>();
                if(offline_online.isChecked())
                {
                    mode="Online";
                }
                else {
                    mode = "Offline";
                }
                //Object object = new CourseReview("Hi",5,"Hello");
                courseReviews = new ArrayList();
                Document test = new Document().append("review","A Step for bringing Knowledge down the years of College").append("reviewRating",5).append("reviewAuthorId",user.getId());
                courseReviews.add(test);
                //courseReviews.add(2,object);
                //courseReviews.put("hello",object);
                /*course.setRatingAuthorId("h");
                course.setReview("fd");
                course.setReviewRating(1.23);
                courseReviews.add(course);*/
                Document courseDetails = new Document();

                courseDetails.append("_partitionkey","_partitionKey");
                courseDetails.append("courseName",courseName);
                courseDetails.append("tutorId",user.getId().toString());
                courseDetails.append("courseDescription",courseDescription);
                courseDetails.append("coursePreReq","");
                courseDetails.append("courseRating",5);
                courseDetails.append("courseMode",mode);
                courseDetails.append("courseFees",fees);
                courseDetails.append("courseImage","balh");
                courseDetails.append("instructorLocation","Here");
                //courseDetails.append("courseDurationMeasure","hours");
                courseDetails.append("numberOfStudentsEnrolled",0);
                courseDetails.append("courseDuration",courseDuration);
                //courseDetails.append("numberOfBatches",numOfBatches);
                courseDetails.append("courseReviews",courseReviews);
                courseDetails.append("courseImageCounter",0);

                /*Intent intent = new Intent(AddCourseActivity.this,AddCourseActivityContinued.class);
                intent.putExtra("courseDetails",courseDetails.toJson().toString());
                startActivity(intent);*/
                mongoCollection.insertOne(courseDetails).getAsync(result -> {
                    if(result.isSuccess())
                    {
                        String id= String.valueOf(result.get().getInsertedId().asObjectId().getValue());
                        Log.v("Added Course",id);
                                       String requestId = MediaManager.get().upload(CourseImageUrl)
                        .unsigned("preset1")
                        .option("resource_type", "image")
                        .option("folder", "Upsilon/Courses/".concat(id))
                        .option("public_id", "CourseImage "+0)
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

                                mongoCollection1.insertOne(new Document("courseId",result.get().getInsertedId())).getAsync(result2 -> {
                                    if(result2.isSuccess())
                                    {
                                        Log.v("Course","Successfully Created Forum");
                                    }
                                    else
                                    {
                                        Log.v("Course",result2.getError().toString());
                                    }
                                });

                                courseDetails.append("courseImage",resultData.get("url").toString());
                                mongoCollection.updateOne(new Document("_id",result.get().getInsertedId()),courseDetails).getAsync(result1 -> {
                                    if(result1.isSuccess())
                                    {
                                        progressBar.setProgress(100);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                + result1.get().getModifiedCount());
                                        Log.v("AddCourse","Updated Image Successfully");
                                        Intent intent = new Intent(AddCourseActivity.this,AddCourseActivityContinued.class);
                                        intent.putExtra("InsertedDocument", result.get().getInsertedId().asObjectId().getValue().toString());
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Log.v("AddCourse",result1.getError().toString());
                                    }
                                });
                            }

                            @Override
                            public void onError(String requestId, ErrorInfo error) {

                            }

                            @Override
                            public void onReschedule(String requestId, ErrorInfo error) {

                            }
                        })
                        .dispatch();
                        //Toast.makeText(getApplicationContext(),"Successfully Added The Course",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Log.v("User",result.getError().toString());
                    }
                });
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
            if(b){
                if(!selected_categories.contains(i)){
                    selected_categories.add(i);
                }
                else if(selected_categories.contains(i)){
                    selected_categories.remove((Integer)i);
                }
            }
            else if(selected_categories.contains(i)){
                selected_categories.remove((Integer)i);
            }
            isCheckedCategories[i] = b;
        };
        builder.setTitle("Choose Categories").setMultiChoiceItems(categories, isCheckedCategories, listener).setPositiveButton("OK", (dialogInterface, i) -> {
            categories_chosen.clear();
            if (selected_categories.size() > 0) {
                tvCourseCategoriesDisplay.setVisibility(View.VISIBLE);
                StringBuilder string = new StringBuilder();
                for (int i1 = 0; i1 < selected_categories.size(); i1++) {
                    categories_chosen.add(categories[(selected_categories.get(i1))]);
                    if (i1 != 0) {
                        string.append("\n");
                    }
                    string.append(categories[(selected_categories.get(i1))]);
                }
                tvCourseCategoriesDisplay.setText(string);
            } else {
                tvCourseCategoriesDisplay.setVisibility(View.GONE);
            }

        }).setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss()).setNeutralButton("Clear", (dialogInterface, i) -> {
            selected_categories.clear();
            categories_chosen.clear();
            tvCourseCategoriesDisplay.setText("");
            tvCourseCategoriesDisplay.setVisibility(View.GONE);
            for (int i1 = 0; i1 < categories.length; i1++) isCheckedCategories[i1] = false;
        }).setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void getCategories() {
        categories = getResources().getStringArray(R.array.categories);
        //categories = new String[]{"Science", "Arts"};
    }
}
