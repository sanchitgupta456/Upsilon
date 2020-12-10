package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseReview;
import com.sanchit.Upsilon.courseData.CourseReviewAdapter;
import com.sanchit.Upsilon.courseData.CoursesAdapter;
import com.sanchit.Upsilon.courseData.IntroductoryContentAdapter;
import com.squareup.picasso.Picasso;

import org.bson.Document;
import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class ViewCourseActivity extends AppCompatActivity {

    private TextView CourseName;
    private RatingBar CourseRating;
    private String courseName;
    private View ActionBarView;
    private ImageView CourseImage;
    private TextView ActionBarCourseName,CourseDescription,CourseDuration,CourseMode,CourseCost,CourseTutorName;
    private ImageButton CourseLocation;
    private Button RegisterButton;
    String appID = "upsilon-ityvn";
    App app;
    Course course;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    RecyclerView reviewsRecyclerView,introductoryRecyclerView;
    CourseReviewAdapter courseReviewAdapter;
    IntroductoryContentAdapter courseIntroductoryMaterialAdapter;
    ArrayList<CourseReview> courseReviewsArrayList = new ArrayList<CourseReview>();
    ArrayList<String> introductoryImages = new ArrayList<String>();
    ArrayList<String> introductoryVideos = new ArrayList<String>();
    ArrayList<String> myCourses = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        ActionBarView = getSupportActionBar().getCustomView();
        CourseName = (TextView) findViewById(R.id.textCourseCoverCard);
        CourseImage = (ImageView) findViewById(R.id.imageCourseCoverCard);
        CourseRating = (RatingBar) findViewById(R.id.courseRatings);
        CourseDescription = (TextView) findViewById(R.id.textCourseDescription);
        CourseDuration = (TextView) findViewById(R.id.view_course_duration);
        CourseCost = (TextView) findViewById(R.id.view_course_fees);
        CourseMode = (TextView) findViewById(R.id.view_course_mode);
        CourseTutorName = (TextView) findViewById(R.id.view_course_tutor_name);
        reviewsRecyclerView = (RecyclerView) findViewById(R.id.listCourseReviews);
        introductoryRecyclerView = (RecyclerView) findViewById(R.id.listIntroductoryMaterial);
        CourseLocation = (ImageButton) findViewById(R.id.location_view_course);
        RegisterButton = (Button) findViewById(R.id.view_course_register);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();

        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");

        Intent intent = getIntent();
        course = (Course)intent.getSerializableExtra("Course");

        if(course.getTutorId().equals(user.getId()))
        {
            RegisterButton.setVisibility(View.INVISIBLE);
        }
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseExoPlayer(courseIntroductoryMaterialAdapter.exoPlayer);
                Intent intent = new Intent(ViewCourseActivity.this,RegisterCourseActivity.class);
                String id = course.getCourseId();
                Log.v("ViewCourse", String.valueOf(course.getCourseId()));
                intent.putExtra("course",course);
                startActivity(intent);
            }
        });

        initialise();

        Log.v("Course","Hello");
        Log.v("courseReview", String.valueOf(course.getCourseReviews().getClass()));

        getCourseReviews();
        getCourseIntroductoryContent();

        Document queryFilter  = new Document("userid",course.getTutorId());

        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                if(!results.hasNext())
                {
                    Log.v("ViewCourse","Couldnt Find The Tutor");
                }
                else
                {
                    Log.v("User", "successfully found the Tutor");

                }
                while (results.hasNext()) {
                    //Log.v("EXAMPLE", results.next().toString());
                    Document currentDoc = results.next();
                    CourseTutorName.setText("Course By "+ currentDoc.getString("name"));
                    myCourses = (ArrayList<String>) currentDoc.get("myCourses");
                    if(myCourses==null)
                    {

                    }
                    else
                    {
                        for(int i=0;i<myCourses.size();i++)
                        {
                            if(myCourses.get(i).equals(course.getCourseId()))
                            {
                                RegisterButton.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    Log.v("User",currentDoc.getString("userid"));
                }
            } else {
                Log.v("User","Failed to complete search");
            }
        });

        /*BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseExoPlayer(courseIntroductoryMaterialAdapter.exoPlayer);
                finish();
            }
        });*/

        CourseName.setText(course.getCourseName());
        Objects.requireNonNull(getSupportActionBar()).setTitle(course.getCourseName());
        CourseRating.setStepSize(0.01f);
        CourseRating.setRating((float) course.getCourseRating());
        CourseDescription.setText(course.getCourseDescription());
        CourseMode.setText(course.getCourseMode());
        if(course.getCourseMode().equals("Online"))
        {
            CourseLocation.setVisibility(View.INVISIBLE);
        }
        CourseCost.setText("Rs. " +String.valueOf(course.getCourseFees()));
        CourseDuration.setText(course.getCourseDuration() + " Class Hours");
        Picasso.with(getApplicationContext()).load(course.getCourseImage()).fit().centerInside().into(CourseImage);

        //Toast.makeText(getApplicationContext(), "Course Rating"+(double) course.getCourseRating(),Toast.LENGTH_LONG).show();
    }

    public void getCourseReviews()
    {
        BasicBSONList courseReviews = course.getCourseReviews();
        for(int counter=0;counter<courseReviews.size();counter++)
        {
            Object courseReview = (Object) courseReviews.get(counter);
            LinkedHashMap courseReview1 = (LinkedHashMap) courseReview;
            CourseReview courseReview2 = new CourseReview(courseReview1.get("review").toString(), (Double) courseReview1.get("reviewRating"),courseReview1.get("reviewAuthorId").toString());
            courseReviewsArrayList.add(courseReview2);
            courseReviewAdapter.notifyDataSetChanged();
            Log.v("test", (String) courseReview1.get("review"));
        }
        Log.v("courseReview", String.valueOf(course.getCourseReviews().getClass()));
        Log.v("courseReviews", String.valueOf(course.getCourseReviews()));
    }

    public void getCourseIntroductoryContent()
    {

        introductoryRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        introductoryRecyclerView.setItemAnimator(new DefaultItemAnimator());

            introductoryImages = course.getIntroductoryContentImages();
            introductoryVideos = course.getIntroductoryContentVideos();
            //introductoryImages.add("http://res.cloudinary.com/upsilon175/image/upload/v1605196689/Upsilon/Courses/5fad2ca3600686e14bc0950b/IntroductoryContent/Images/IntroductoryImage0.jpg");
            //Log.v("introductoryImages", String.valueOf(introductoryImages));

        courseIntroductoryMaterialAdapter = new IntroductoryContentAdapter(introductoryImages,introductoryVideos);
        introductoryRecyclerView.setAdapter(courseIntroductoryMaterialAdapter);
        courseIntroductoryMaterialAdapter.notifyDataSetChanged();
    }

    public void initialise()
    {

        courseReviewAdapter = new CourseReviewAdapter(courseReviewsArrayList);

        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        reviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewsRecyclerView.setAdapter(courseReviewAdapter);
        courseReviewAdapter = new CourseReviewAdapter(courseReviewsArrayList);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(introductoryImages!=null)
        {
            introductoryImages.clear();
        }
        if(introductoryVideos!=null)
        {
            introductoryVideos.clear();
        }
        courseIntroductoryMaterialAdapter.notifyDataSetChanged();
        releaseExoPlayer(courseIntroductoryMaterialAdapter.exoPlayer);
    }

    public static void releaseExoPlayer(SimpleExoPlayer exoPlayer) {

        if (exoPlayer != null) {
            exoPlayer.release();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (ActionBarView==item.getActionView()){
            releaseExoPlayer(courseIntroductoryMaterialAdapter.exoPlayer);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
