package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseReview;
import com.sanchit.Upsilon.courseData.CourseReviewAdapter;
import com.sanchit.Upsilon.courseData.CoursesAdapter;

import org.bson.Document;
import org.bson.types.BasicBSONList;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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
    private ImageView BackButton;
    private TextView ActionBarCourseName,CourseDescription,CourseDuration,CourseMode,CourseCost,CourseTutorName;
    String appID = "upsilon-ityvn";
    App app;
    Course course;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    RecyclerView reviewsRecyclerView;
    CourseReviewAdapter courseReviewAdapter;
    ArrayList<CourseReview> courseReviewsArrayList = new ArrayList<CourseReview>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.layout_custom_action_bar_view_course);
        ActionBarView = getSupportActionBar().getCustomView();
        BackButton = findViewById(R.id.action_bar_back_button);
        ActionBarCourseName = findViewById(R.id.action_bar_course_name);
        CourseName = (TextView) findViewById(R.id.textCourseCoverCard);
        CourseRating = (RatingBar) findViewById(R.id.courseRatings);
        CourseDescription = (TextView) findViewById(R.id.textCourseDescription);
        CourseDuration = (TextView) findViewById(R.id.view_course_duration);
        CourseCost = (TextView) findViewById(R.id.view_course_fees);
        CourseMode = (TextView) findViewById(R.id.view_course_mode);
        CourseTutorName = (TextView) findViewById(R.id.view_course_tutor_name);
        reviewsRecyclerView = (RecyclerView) findViewById(R.id.listCourseReviews);
        initialise();

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();

        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");

        Intent intent = getIntent();
        course = (Course)intent.getSerializableExtra("Course");

        Log.v("Course","Hello");
        Log.v("courseReview", String.valueOf(course.getCourseReviews().getClass()));

        getCourseReviews();

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
                    Log.v("User",currentDoc.getString("userid"));
                }
            } else {
                Log.v("User","Failed to complete search");
            }
        });

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CourseName.setText(course.getCourseName());
        ActionBarCourseName.setText(course.getCourseName());
        CourseRating.setStepSize(0.01f);
        CourseRating.setRating((float) course.getCourseRating());
        CourseDescription.setText(course.getCourseDescription());
        CourseMode.setText(course.getCourseMode());
        CourseCost.setText(course.getCourseFees());
        CourseDuration.setText(course.getCourseDuration() + course.getCourseDurationMeasure());

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

    public void initialise()
    {

        courseReviewAdapter = new CourseReviewAdapter(courseReviewsArrayList);

        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        reviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewsRecyclerView.setAdapter(courseReviewAdapter);
        courseReviewAdapter = new CourseReviewAdapter(courseReviewsArrayList);
    }


}
