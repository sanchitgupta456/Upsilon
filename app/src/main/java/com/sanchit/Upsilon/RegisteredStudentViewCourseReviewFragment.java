package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseReview;
import com.sanchit.Upsilon.ui.login.LoginActivity;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.BasicBSONList;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class RegisteredStudentViewCourseReviewFragment extends Fragment {

    String appID = "upsilon-ityvn";
    private Course course;
    private TextView textView;
    private RatingBar ratingBar;
    private EditText rating,review;
    private Button submitReview;
    private CourseReview courseReview;
    private User user;
    private App app;
    private String Review;
    private double Rating;
    private Gson gson;
    private GsonBuilder gsonBuilder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_active_course_review,null);
        course = (Course) getArguments().get("Course");
        textView = view.findViewById(R.id.textReviewTitleText);
        ratingBar = view.findViewById(R.id.rateCourse);
        //rating = view.findViewById(R.id.rateCourse);
        review = view.findViewById(R.id.courseReview);
        submitReview = view.findViewById(R.id.submitReview);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();
        if(user==null)
        {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
        else
        {
            BasicBSONList courseReviews = course.getCourseReviews();
            Log.v("courseReview", String.valueOf(courseReviews));
            if(courseReviews!=null)
            {
                for(int counter=0;counter<courseReviews.size();counter++)
                {
                    //Object courseReview = (Object) courseReviews.get(counter);
                    //CourseReview courseReview1 = (CourseReview) courseReviews.get(counter);
                    try {
                        LinkedHashMap courseReview1 = (LinkedHashMap) courseReviews.get(counter);
                        if(courseReview1.get("reviewAuthorId").equals(user.getId()))
                        {
                            //rating.setText(courseReview1.get("reviewRating").toString());
                            //TODO
                            ratingBar.setRating(Float.parseFloat(String.valueOf(courseReview1.get("reviewRating"))));
                            //rating.setText((int) Double.parseDouble(String.valueOf(4.5)));
                            review.setText(courseReview1.get("review").toString());
                            submitReview.setVisibility(View.INVISIBLE);
                            break;
                        }
                        //CourseReview courseReview2 = new CourseReview(courseReview1.get("review").toString(), (Double) courseReview1.get("reviewRating"),courseReview1.get("reviewAuthorId").toString());
                        //courseReviewsArrayList.add(courseReview2);
                        //courseReviewAdapter.notifyDataSetChanged();
                        Log.v("test", (String) courseReview1.get("review"));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Review = review.getText().toString();
                Rating = ratingBar.getRating();

                CourseReview courseReview = new CourseReview(Review,Rating,user.getId().toString());
                BasicBSONList courseReviews = course.getCourseReviews();
                if(courseReviews==null)
                {
                    courseReviews=new BasicBSONList();
                }

                courseReviews.add(courseReview);


                course.setCourseReviews(courseReviews);
                BsonDocument courseDoc = new BsonDocument();
                gsonBuilder = new GsonBuilder();
                gson = gsonBuilder.create();
                String object = gson.toJson(course,Course.class);

                courseDoc = BsonDocument.parse(object);

                MongoClient mongoClient;
                MongoDatabase mongoDatabase;

                mongoClient = user.getMongoClient("mongodb-atlas");
                mongoDatabase = mongoClient.getDatabase("Upsilon");
                MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");

                mongoCollection.findOneAndUpdate(new Document("courseId",course.getCourseId()), courseDoc).getAsync(result -> {
                    if(result.isSuccess())
                    {
                        submitReview.setVisibility(View.INVISIBLE);
                        Log.v("Review","Added Review Successfully");
                    }
                    else
                    {
                        Log.v("Review","Could not add Review"+result.getError().toString());
                    }
                });

            }
        });



        return view;
    }
}
