package com.sanchit.Upsilon.courseData;

import android.content.Context;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.sanchit.Upsilon.R;
import com.sanchit.Upsilon.RegisteredStudentViewCourse;
import com.sanchit.Upsilon.TeacherViewCourseActivity;
import com.sanchit.Upsilon.ViewCourseActivity;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class CoursesAdapter1 extends RecyclerView.Adapter<CoursesAdapter1.ViewHolder> {

    List<Course> courseList;
    Context context;
    String appID = "upsilon-ityvn";
    ArrayList<String> myCourses;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    public CoursesAdapter1(List<Course>_courseList)
    {
        this.courseList = _courseList;
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
        Course course = courseList.get(position);
        App app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
        //Document userLoc = new Document();

        Document queryFilter1  = new Document("userid",course.getTutorId());

        RealmResultTask<MongoCursor<Document>> findTask1 = mongoCollection.find(queryFilter1).iterator();

        findTask1.getAsync(task1 -> {
            if (task1.isSuccess()) {
                MongoCursor<Document> results = task1.get();
                if(!results.hasNext())
                {

                }
                else
                {
                    Document currentDoc = results.next();
                    holder.textTutorTvShow.setText("Course By "+currentDoc.getString("name"));
                }
            } else {
                Log.v("User","Failed to complete search");
            }
        });
        queryFilter1  = new Document("userid",user.getId());

        findTask1 = mongoCollection.find(queryFilter1).iterator();

        findTask1.getAsync(task1 -> {
            if (task1.isSuccess()) {
                MongoCursor<Document> results = task1.get();
                if(!results.hasNext())
                {

                }
                else
                {
                    Document currentDoc = results.next();
                    Document userLoc = (Document) currentDoc.get("userLocation");
                    holder.textDistanceTvShow.setText(new StringBuilder().append("About ")
                            .append(Double.toString(calcDist(course.getCourseLocation(), userLoc)))
                            .append(" kilometers from your location").toString());
                }
            } else {
                Log.v("User","Failed to complete search");
            }
        });
        holder.ll.setVisibility(View.GONE);

        holder.toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.ll.getVisibility() == View.VISIBLE) {
                    holder.ll.setVisibility(View.GONE);
                }
                else {
                    holder.ll.setVisibility(View.VISIBLE);
                }
                holder.toggle.animate().rotationBy(180).start();
            }
        });



        holder.textTvShow.setText(course.getCourseName());
        Log.v("CourseAdapter",course.getCourseImage());
        //Picasso.with(context).load(course.getCourseImage()).fit().centerCrop().into(holder.imgTvShow);
        Glide.with(context).load(course.getCourseImage()).into(holder.imgTvShow);
        //holder.imgTvShow.setImageResource(course.getCardImgID());
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

                Log.v("Id",course.getTutorId());
                Log.v("Id",user.getId());
                if(course.getTutorId().equals(user.getId()))
                {
                    Log.v("Id","matched");
                    Intent intent = new Intent(holder.itemView.getContext(), TeacherViewCourseActivity.class);
                    intent.putExtra("Course", course);
                    holder.itemView.getContext().startActivity(intent);
                }
                else
                {

                    //Blank query to find every single course in db
                    //TODO: Modify query to look for user preferred course IDs
                    Document queryFilter  = new Document("userid",user.getId());

                    RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                    findTask.getAsync(task -> {
                        if (task.isSuccess()) {
                            MongoCursor<Document> results = task.get();
                            if(!results.hasNext())
                            {

                            }
                            else
                            {
                                try {
                                    Document currentDoc = results.next();
                                    myCourses = (ArrayList<String>) currentDoc.get("myCourses");
                                    int i=0;
                                    for(i=0;i<myCourses.size();i++)
                                    {
                                        if(((String)myCourses.get(i)).equals((String)course.getCourseId()))
                                        {
                                            Intent intent = new Intent(holder.itemView.getContext(), RegisteredStudentViewCourse.class);
                                            intent.putExtra("Course", course);
                                            holder.itemView.getContext().startActivity(intent);
                                            break;
                                        }
                                    }
                                    if(i==myCourses.size())
                                    {
                                        Intent intent = new Intent(holder.itemView.getContext(), ViewCourseActivity.class);
                                        intent.putExtra("Course", course);
                                        holder.itemView.getContext().startActivity(intent);
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
        double lat1 = 0,lon1 = 0,lat2 = 0,lon2 = 0;
        try {
            lat1 = courseLoc.getDouble("latitude");
            lon2 = userLoc.getDouble("longitude");
            lat2 = userLoc.getDouble("latitude");
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