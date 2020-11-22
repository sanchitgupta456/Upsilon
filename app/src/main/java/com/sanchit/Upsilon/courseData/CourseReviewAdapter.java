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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.Upsilon.MainActivity;
import com.sanchit.Upsilon.R;
import com.sanchit.Upsilon.UserDataSetupActivity1;
import com.sanchit.Upsilon.ViewCourseActivity;
import com.sanchit.Upsilon.ui.login.LoginActivity;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;

public class CourseReviewAdapter extends RecyclerView.Adapter<CourseReviewAdapter.CourseReviewsViewHolder> {

    List<CourseReview> courseList;
    Context context;

    public CourseReviewAdapter(List<CourseReview>_courseList)
    {
        this.courseList = _courseList;
    }


    @NonNull
    @Override
    public CourseReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_review_card,parent,false);
        CourseReviewAdapter.CourseReviewsViewHolder viewHolder = new CourseReviewAdapter.CourseReviewsViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseReviewsViewHolder holder, int position) {
        CourseReview courseReviews = courseList.get(position);

        holder.courseReview.setText(courseReviews.getReview());
        Log.v("Adapter",courseReviews.getReview());
        //holder.imgTvShow.setImageResource(course.getCardImgID());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"The position is:"+position,Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class CourseReviewsViewHolder extends RecyclerView.ViewHolder
    {
        TextView courseReview;
        CardView cv;

        public CourseReviewsViewHolder(View itemView)
        {
            super(itemView);
            courseReview = (TextView) itemView.findViewById(R.id.course_review_view) ;
            cv = (CardView)itemView.findViewById(R.id.courseReviewCard);
        }

    }
}