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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseFinal;
import com.sanchit.Upsilon.courseData.CourseReview;
import com.sanchit.Upsilon.ui.login.LoginActivity;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.BasicBSONList;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class RegisteredStudentViewCourseReviewFragment extends Fragment {

    String appID = "upsilon-ityvn";
    private CourseFinal course;
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
    private String name;
    private RequestQueue queue;
    private String API ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_active_course_review,null);
        course = (CourseFinal) getArguments().get("Course");
        textView = view.findViewById(R.id.textReviewTitleText);
        ratingBar = view.findViewById(R.id.rateCourse);
        //rating = view.findViewById(R.id.rateCourse);
        review = view.findViewById(R.id.courseReview);
        submitReview = view.findViewById(R.id.submitReview);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        API = ((Upsilon)getActivity().getApplication()).getAPI();

//        app = new App(new AppConfiguration.Builder(appID)
//                .build());
//        User user = app.currentUser();
//        try {
//            name = user.getCustomData().getString("name");
//            textView.setText(name);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        textView.setText(((Upsilon)getActivity().getApplication()).getUser().getUsername());
        if(((Upsilon)getActivity().getApplication()).getUser()==null)
        {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
        else
        {
//            BasicBSONList courseReviews = course.getCourseReviews();
//            Log.v("courseReview", String.valueOf(courseReviews));
//            if(courseReviews!=null)
//            {
//                for(int counter=0;counter<courseReviews.size();counter++)
//                {
//                    //Object courseReview = (Object) courseReviews.get(counter);
//                    //CourseReview courseReview1 = (CourseReview) courseReviews.get(counter);
//                    try {
//                        LinkedHashMap courseReview1 = null;
//                        try {
//                            courseReview1 = (LinkedHashMap) courseReviews.get(counter);
//                            if(courseReview1.get("reviewAuthorId").equals(user.getId()))
//                            {
//                                //rating.setText(courseReview1.get("reviewRating").toString());
//                                //TODO
//                                ratingBar.setRating(Float.parseFloat(String.valueOf(courseReview1.get("reviewRating"))));
//                                //rating.setText((int) Double.parseDouble(String.valueOf(4.5)));
//                                review.setText(courseReview1.get("review").toString());
//                                submitReview.setVisibility(View.INVISIBLE);
//                                break;
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        //CourseReview courseReview2 = new CourseReview(courseReview1.get("review").toString(), (Double) courseReview1.get("reviewRating"),courseReview1.get("reviewAuthorId").toString());
//                        //courseReviewsArrayList.add(courseReview2);
//                        //courseReviewAdapter.notifyDataSetChanged();
//                        try {
//                            Log.v("test", (String) courseReview1.get("review"));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } catch (NumberFormatException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }

        }
        submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("courseId",course.get_id());
                    Gson gson = new Gson();
                    jsonObject.put("courseReview",gson.toJson(new CourseReview(review.getText().toString(),Double.parseDouble(String.valueOf(ratingBar.getRating())) , ((Upsilon)getActivity().getApplication()).getUser().get_Id(), ((Upsilon) getActivity().getApplication()).getUser().getUsername())));
//                    jsonObject.put("reviewRating",ratingBar.getRating());
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/writingReview",jsonObject,
                            new Response.Listener<org.json.JSONObject>() {
                                @Override
                                public void onResponse(org.json.JSONObject response) {
                                    Toast.makeText(getContext(),"Review Submitted Successfully",Toast.LENGTH_LONG).show();
                                    getReview();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(),"Review Submission Failed",Toast.LENGTH_LONG).show();

                                }
                            }
                    ){
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("token", ((Upsilon)getActivity().getApplication()).getToken());
                            return params;
                        }
                    };
                    queue.add(jsonRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                submitReview.setVisibility(View.INVISIBLE);
//                Review = review.getText().toString();
//                Rating = ratingBar.getRating();
//
//                CourseReview courseReview = new CourseReview(Review,Rating,user.getId().toString(),name);
//                BasicBSONList courseReviews = course.getCourseReviews();
//                if(courseReviews==null)
//                {
//                    courseReviews=new BasicBSONList();
//                }
//
//                courseReviews.add(courseReview);
//
//
//                course.setCourseReviews(courseReviews);
//                course.setCourseRating((float)((course.getCourseRating()*course.getNumberOfReviews())+Rating)/(course.getNumberOfReviews()+1));
//                course.setNumberOfReviews(course.getNumberOfReviews()+1);
//                BsonDocument courseDoc = new BsonDocument();
//                gsonBuilder = new GsonBuilder();
//                gson = gsonBuilder.create();
//                String object = gson.toJson(course,Course.class);
//
//                courseDoc = BsonDocument.parse(object);
//
//                MongoClient mongoClient;
//                MongoDatabase mongoDatabase;
//
//                mongoClient = user.getMongoClient("mongodb-atlas");
//                mongoDatabase = mongoClient.getDatabase("Upsilon");
//                MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");
//
//                mongoCollection.findOneAndUpdate(new Document("courseId",course.getCourseId()), courseDoc).getAsync(result -> {
//                    if(result.isSuccess())
//                    {
//                        submitReview.setVisibility(View.INVISIBLE);
//                        Log.v("Review","Added Review Successfully");
//                    }
//                    else
//                    {
//                        submitReview.setVisibility(View.VISIBLE);
//                        Toast.makeText(getActivity(),"Could not add review",Toast.LENGTH_LONG).show();
//                        Log.v("Review","Could not add Review"+result.getError().toString());
//                    }
//                });
            }
        });



        return view;
    }

    public void getReview()
    {

    }
}
