package com.sanchit.Upsilon.courseData;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sanchit.Upsilon.MainActivity;
import com.sanchit.Upsilon.R;
import com.sanchit.Upsilon.RegisteredStudentViewCourse;
import com.sanchit.Upsilon.TeacherViewCourseActivity;
import com.sanchit.Upsilon.UserDataSetupActivity1;
import com.sanchit.Upsilon.ViewCourseActivity;
import com.sanchit.Upsilon.ui.login.LoginActivity;
import com.squareup.picasso.Picasso;

import org.bson.Document;

import java.io.Serializable;
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

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {

    List<CourseFinal> courseList;
    Context context;
    String appID = "upsilon-ityvn";
    ArrayList<String> myCourses;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    String userId;

    public CoursesAdapter(List<CourseFinal>_courseList)
    {
        this.courseList = _courseList;
    }

    public CoursesAdapter(List<CourseFinal>_courseList , String _userId)
    {
        this.courseList = _courseList;
        this.userId = _userId;
    }

    public CoursesAdapter(List<CourseFinal>_courseList , String _userId , ArrayList<String> _myCourses)
    {
        this.courseList = _courseList;
        this.userId = _userId;
        this.myCourses = _myCourses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CourseFinal course = courseList.get(position);

        holder.textTvShow.setText(course.getCourseName());
        //Picasso.with(context).load(course.getCourseImage()).fit().centerCrop().into(holder.imgTvShow);
        Glide.with(context).load(course.getCourseImage()).into(holder.imgTvShow);
        //holder.imgTvShow.setImageResource(course.getCardImgID());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        if(course.getTutorId().equals(userId))
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
                //Toast.makeText(context,"The position is:"+position,Toast.LENGTH_SHORT).show();
//                App app = new App(new AppConfiguration.Builder(appID)
//                        .build());
//                User user = app.currentUser();
//                Log.v("Id",course.getTutorId());
//                Log.v("Id",user.getId());
//                if(course.getTutorId().equals(user.getId()))
//                {
//                    Log.v("Id","matched");
//                    Intent intent = new Intent(holder.itemView.getContext(), TeacherViewCourseActivity.class);
//                    intent.putExtra("Course", course);
//                    holder.itemView.getContext().startActivity(intent);
//                }
//                else
//                {
//                    mongoClient = user.getMongoClient("mongodb-atlas");
//                    mongoDatabase = mongoClient.getDatabase("Upsilon");
//                    MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
//
//                    //Blank query to find every single course in db
//                    //TODO: Modify query to look for user preferred course IDs
//                    Document queryFilter  = new Document("userid",user.getId());
//
//                    RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
//
//                    findTask.getAsync(task -> {
//                        if (task.isSuccess()) {
//                            MongoCursor<Document> results = task.get();
//                            if(!results.hasNext())
//                            {
//
//                            }
//                            else
//                            {
//                                Document currentDoc = results.next();
//                                myCourses = (ArrayList<String>) currentDoc.get("myCourses");
//                                int i=0;
//                                for(i=0;i<myCourses.size();i++)
//                                {
//                                    try {
//                                        if(((String)myCourses.get(i)).equals((String)course.getCourseId()))
//                                        {
//                                            Intent intent = new Intent(holder.itemView.getContext(), RegisteredStudentViewCourse.class);
//                                            intent.putExtra("Course", course);
//                                            holder.itemView.getContext().startActivity(intent);
//                                            break;
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                                if(i==myCourses.size())
//                                {
//                                    Intent intent = new Intent(holder.itemView.getContext(), ViewCourseActivity.class);
//                                    intent.putExtra("Course", course);
//                                    holder.itemView.getContext().startActivity(intent);
//                                }
//                                Log.v("User", "successfully found the user");
//                            }
//                        } else {
//                            Log.v("User","Failed to complete search");
//                        }
//                    });


//                }
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
        CardView cv;

        public ViewHolder(View itemView)
        {
            super(itemView);
            imgTvShow = (ImageView)itemView.findViewById(R.id.courseImage);
            textTvShow = (TextView)itemView.findViewById(R.id.courseName);
            cv = (CardView)itemView.findViewById(R.id.courseCard);
        }

    }
}