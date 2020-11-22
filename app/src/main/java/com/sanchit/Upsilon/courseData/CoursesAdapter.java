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
import com.sanchit.Upsilon.TeacherViewCourseActivity;
import com.sanchit.Upsilon.UserDataSetupActivity1;
import com.sanchit.Upsilon.ViewCourseActivity;
import com.sanchit.Upsilon.ui.login.LoginActivity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {

    List<Course> courseList;
    Context context;
    String appID = "upsilon-ityvn";

    public CoursesAdapter(List<Course>_courseList)
    {
        this.courseList = _courseList;
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
        Course course = courseList.get(position);

        holder.textTvShow.setText(course.getCourseName());
        Log.v("CourseAdapter",course.getCourseImage());
        //Picasso.with(context).load(course.getCourseImage()).fit().centerCrop().into(holder.imgTvShow);
        Glide.with(context).load(course.getCourseImage()).into(holder.imgTvShow);
        //holder.imgTvShow.setImageResource(course.getCardImgID());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,"The position is:"+position,Toast.LENGTH_SHORT).show();
                App app = new App(new AppConfiguration.Builder(appID)
                        .build());
                User user = app.currentUser();
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
                    Intent intent = new Intent(holder.itemView.getContext(), ViewCourseActivity.class);
                    intent.putExtra("Course", course);
                    holder.itemView.getContext().startActivity(intent);
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