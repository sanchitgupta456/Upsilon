package com.sanchit.Upsilon.courseData;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.sanchit.Upsilon.R;
import com.sanchit.Upsilon.RegisteredStudentViewCourse;
import com.sanchit.Upsilon.TeacherViewCourseActivity;
import com.sanchit.Upsilon.Upsilon;
import com.sanchit.Upsilon.ViewCourseActivity;
import com.sanchit.Upsilon.userData.User;
import com.sanchit.Upsilon.userData.UserLocation;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class CoursesAdapter1 extends RecyclerView.Adapter<CoursesAdapter1.ViewHolder> {

    List<CourseFinal> courseList;
    Context context;
    String appID = "upsilon-ityvn";
    ArrayList<String> myCourses;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    private RequestQueue queue;
    private String API;
    private Application application;
    private String Token;
    User user;

    public void updateList(List<CourseFinal> newlist) {
        courseList.clear();
        courseList.addAll(newlist);
        this.notifyDataSetChanged();
    }


//    public CoursesAdapter1(List<CourseFinal>_courseList)
//    {
//        this.courseList = _courseList;
//    }
//
//    public CoursesAdapter1(List<CourseFinal>_courseList,String _API)
//    {
//        this.courseList = _courseList;
//        this.API = _API;
//    }
//
//    public CoursesAdapter1(List<CourseFinal>_courseList,String _API , String _Token)
//    {
//        this.courseList = _courseList;
//        this.API = _API;
//        this.Token = _Token;
//    }

    public CoursesAdapter1(List<CourseFinal>_courseList,String _API , String _Token , User _user)
    {
        if(_user!=null)
        {
            this.courseList = _courseList;
            this.API = _API;
            this.Token = _Token;
            this.user = _user;
            myCourses = user.getMyCourses();
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_course_recycler_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CourseFinal course = courseList.get(position);
        queue = Volley.newRequestQueue(context.getApplicationContext());

//        App app = new App(new AppConfiguration.Builder(appID)
//                .build());
//        User user = app.currentUser();
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("Upsilon");
//        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
        //Document userLoc = new Document();

        holder.ll.setVisibility(View.GONE);
        holder.toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.ll.getVisibility() == View.VISIBLE) {
                    holder.ll.setVisibility(View.GONE);
                }
                else {
                    holder.ll.setVisibility(View.VISIBLE);
                    try {
                        Log.v("courseId",course.get_id());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("courseId",course.get_id());
                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/tutorNameLoc",jsonObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("FetchTutorNameLoc", response.toString());
                                        if(course.getCourseMode().equals("Offline"))
                                        {
                                            try {
                                                holder.textTutorTvShow.setText(response.getString("tutorName"));
                                                if(response.get("courseDistance").equals(null))
                                                {
                                                    holder.textDistanceTvShow.setText(R.string.error_not_enabled_location);
                                                }
                                                else
                                                {
                                                    holder.textDistanceTvShow.setText(new StringBuilder().append("About ")
                                                        .append(String.format("%.2f",response.get("courseDistance")))
                                                       .append(" kilometers from your location").toString());
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        else
                                        {
                                            try {
                                                holder.textTutorTvShow.setText(response.getString("tutorName"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("ErrorFetchingTutorNameLoc", error.toString());
                                    }
                                }
                        ){
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("token", Token);
                                return params;
                            }
                        };
                        queue.add(jsonRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    Document queryFilter1  = new Document("userid",course.getTutorId());
//                    RealmResultTask<MongoCursor<Document>> findTask1 = mongoCollection.find(queryFilter1).iterator();
//                    findTask1.getAsync(task1 -> {
//                        if (task1.isSuccess()) {
//                            MongoCursor<Document> results = task1.get();
//                            if(!results.hasNext())
//                            {
//
//                            }
//                            else
//                            {
//                                Document currentDoc = results.next();
//                                holder.textTutorTvShow.setText("Course By "+currentDoc.getString("name"));
//                            }
//                        } else {
//                            Log.v("User","Failed to complete search");
//                        }
//                    });
//                    Document queryFilter  = new Document("userid",user.getId());
//                    RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
//                    findTask.getAsync(task -> {
//                        if (task.isSuccess()) {
//                            MongoCursor<Document> results = task.get();
//                            if (results.hasNext()) {
//                                if(!course.getCourseMode().equals("Online")){
//                                    Document currentDoc = results.next();
//                                    Document userLoc = (Document) currentDoc.get("userLocation");
//                                    Log.v("Hello", String.valueOf(userLoc));
//                                    if(userLoc != null) {
////                                        holder.textDistanceTvShow.setText(new StringBuilder().append("About ")
////                                                .append(String.format("%.2f",calcDist(course.getCourseLocation(), userLoc)))
////                                                .append(" kilometers from your location").toString());
//                                    } else {
//                                        holder.textDistanceTvShow.setText(R.string.error_not_enabled_location);
//                                    }
//                                }
//                                else {
//                                    holder.textDistanceTvShow.setText("");
//                                }
//                            }
//                        } else {
//                            Log.v("User","Failed to complete search");
//                        }
//                    });
                }
                holder.toggle.animate().rotationBy(180).start();
            }
        });



        holder.textTvShow.setText(course.getCourseName());
//        Log.v("CourseAdapter",course.getCourseImage());
//        Picasso.with(context).load(course.getCourseImage()).fit().centerCrop().into(holder.imgTvShow);
        Glide.with(context).load(course.getCourseImage()).into(holder.imgTvShow);
//        holder.imgTvShow.setImageResource(course.getCardImgID());
        //holder.textTutorTvShow.setText(course.getTutorId());
        holder.textModeTvShow.setText(course.getCourseMode());
        if(course.getCourseMode().equals("Online")) {
            holder.textDistanceTvShow.setVisibility(View.GONE);
        } else {
            holder.textDistanceTvShow.setVisibility(View.VISIBLE);
        }
        if(course.getCourseFees()==0)
        {
            holder.textFeeTvShow.setText(R.string.free);
        }
        else
        {
            holder.textFeeTvShow.setText("Rs."+course.getCourseFees());
        }
        holder.textRatingTvShow.setText(String.format("%.3s/5", course.getCourseRating()));

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,"The position is:"+position,Toast.LENGTH_SHORT).show();

//                Log.v("Id",course.getTutorId());
//                Log.v("Id",user.getId());
                if(course.getTutorId().equals(user.get_Id()))
                {
                    Log.v("Adapter","Teacher");
                    Intent intent = new Intent(holder.itemView.getContext(), TeacherViewCourseActivity.class);
                    intent.putExtra("Course", (Serializable) course);
                    holder.itemView.getContext().startActivity(intent);
                }
                else
                {

                    int i=0;
                    for(i=0;i<myCourses.size();i++) {
                        {
                            try {

                                if (((String) myCourses.get(i)).equals((String) course.get_id())) {
                                    Log.v("Adapter","Student");
                                    Intent intent = new Intent(holder.itemView.getContext(), RegisteredStudentViewCourse.class);
                                    intent.putExtra("Course", (Serializable) course);
                                    holder.itemView.getContext().startActivity(intent);
                                    break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (i == myCourses.size()) {

                        Intent intent = new Intent(holder.itemView.getContext(), ViewCourseActivity.class);
                        intent.putExtra("Course", (Serializable) course);
                        holder.itemView.getContext().startActivity(intent);
                    }
                }

                    //Blank query to find every single course in db
                    //TODO: Modify query to look for user preferred course IDs
//                    Document queryFilter  = new Document("userid",user.getId());

//                    RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

//                    findTask.getAsync(task -> {
//                        if (task.isSuccess()) {
//                            MongoCursor<Document> results = task.get();
//                            if(!results.hasNext())
//                            {
//
//                            }
//                            else
//                            {
//                                try {
//                                    Document currentDoc = results.next();
//                                    myCourses = (ArrayList<String>) currentDoc.get("myCourses");
//                                    if(myCourses==null)
//                                    {
//                                        myCourses = new ArrayList<>();
//                                    }
//                                    int i=0;
//                                    for(i=0;i<myCourses.size();i++)
//                                    {
//                                        if(((String)myCourses.get(i)).equals((String)course.get_id()))
//                                        {
//                                            Intent intent = new Intent(holder.itemView.getContext(), RegisteredStudentViewCourse.class);
//                                            intent.putExtra("Course", (Serializable) course);
//                                            holder.itemView.getContext().startActivity(intent);
//                                            break;
//                                        }
//                                    }
//                                    if(i==myCourses.size())
//                                    {
//                                        Intent intent = new Intent(holder.itemView.getContext(), ViewCourseActivity.class);
//                                        intent.putExtra("Course", (Serializable) course);
//                                        holder.itemView.getContext().startActivity(intent);
//                                    }
//                                    Log.v("User", "successfully found the user");
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        } else {
//                            Log.v("User","Failed to complete search");
//                        }
//                    });



            }
        });


    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgTvShow;
        TextView textTvShow;
        TextView textTutorTvShow;
        TextView textModeTvShow;
        TextView textFeeTvShow;
        TextView textRatingTvShow;
        TextView textDistanceTvShow;
        CardView cv;
        ImageButton toggle;
        LinearLayout ll;

        public ViewHolder(View itemView)
        {
            super(itemView);
            imgTvShow = (ImageView)itemView.findViewById(R.id.courseImage);
            textTvShow = (TextView)itemView.findViewById(R.id.courseName);
            textTutorTvShow = (TextView)itemView.findViewById(R.id.courseInstructor);
            textModeTvShow = (TextView)itemView.findViewById(R.id.courseMode);
            textFeeTvShow = (TextView)itemView.findViewById(R.id.courseFee);
            textRatingTvShow = (TextView)itemView.findViewById(R.id.courseRating);
            textDistanceTvShow = (TextView) itemView.findViewById(R.id.courseDistance);
            toggle = (ImageButton) itemView.findViewById(R.id.toggle);
            cv = (CardView) itemView.findViewById(R.id.cvCourseCard);
            ll = (LinearLayout) itemView.findViewById(R.id.cardInfo);
        }
    }

    public double calcDist(Document courseLoc, Document userLoc) {
        Log.v("Hello Again", String.valueOf(userLoc));
        double lat1 = 0,lon1 = 0,lat2 = 0,lon2 = 0;
        try {
            lat1 = courseLoc.getDouble("latitude");
            lon2 = userLoc.getDouble("longitude");
            lat2 = userLoc.getDouble("lattitude");
            lon1 = courseLoc.getDouble("longitude");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v("Distance","calculating");
        double R = 6378;
        double dLat = Math.PI*Math.abs(lat2-lat1)/180;
        double dLon = Math.PI*Math.abs(lon2-lon1)/180;
        Log.v("calcDist", Double.toString(lat1).concat(" ").concat(Double.toString(lon1)).concat(" ").concat(Double.toString(lat2)).concat(" ").concat(Double.toString(lon2)));
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(Math.PI*(lat1)/180) * Math.cos(Math.PI*(lat2)/180) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        // Distance in km
        return R * c;
    }
}