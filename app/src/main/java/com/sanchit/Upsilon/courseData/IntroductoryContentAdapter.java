package com.sanchit.Upsilon.courseData;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sanchit.Upsilon.R;

import java.util.ArrayList;
import java.util.List;

public class IntroductoryContentAdapter extends RecyclerView.Adapter<IntroductoryContentAdapter.IntroductoryContentViewHolder> {

    final int VIEW_TYPE_VIDEO = 1;
    final int VIEW_TYPE_IMAGE = 0;

    List<String> ImageUrls;
    List<String> VideoUrls;
    Context context;

    public IntroductoryContentAdapter(ArrayList<String> introductoryImages, ArrayList<String> introductoryVideos) {

        this.ImageUrls = introductoryImages;
        this.VideoUrls = introductoryVideos;
        Log.v("Adapter","Images Initialised");
        Log.v("Adapter", String.valueOf(ImageUrls));

    }

    public IntroductoryContentAdapter(List<String> imageUrls) {

    }

    @NonNull
    @Override
    public IntroductoryContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        Log.v("Image","OnCreated");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_introductory_content_card,parent,false);
        IntroductoryContentAdapter.IntroductoryContentViewHolder viewHolder = new IntroductoryContentAdapter.IntroductoryContentViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IntroductoryContentViewHolder holder, int position) {

        if(position<ImageUrls.size())
        {
            String imageUrl = ImageUrls.get(position);

            //holder.courseReview.setText(courseReviews.getReview());
            Log.v("Image",imageUrl);
            Log.v("Images","Binded");
            //holder.imgTvShow.setImageResource(course.getCardImgID());
            Glide.with(context).load(imageUrl).into(holder.IntroductoryContentImage);
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Toast.makeText(context,"The position is:"+position,Toast.LENGTH_SHORT).show();

                }
            });
        }
        else if(position-ImageUrls.size()<VideoUrls.size())
        {
            String videoUrl = VideoUrls.get(position-ImageUrls.size());

            //holder.courseReview.setText(courseReviews.getReview());
            Log.v("Video",videoUrl);
            Log.v("Video","Binded");
            //holder.imgTvShow.setImageResource(course.getCardImgID());
            holder.IntroductoryContentVideo.setVideoPath(videoUrl);
            holder.IntroductoryContentImage.setVisibility(View.INVISIBLE);
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.IntroductoryContentVideo.start();
                    //Toast.makeText(context,"The position is:"+position,Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return ImageUrls.size()+VideoUrls.size();
    }

    public class IntroductoryContentViewHolder extends RecyclerView.ViewHolder
    {
        ImageView IntroductoryContentImage;
        VideoView IntroductoryContentVideo;
        CardView cv;

        public IntroductoryContentViewHolder(View itemView)
        {
            super(itemView);
            IntroductoryContentImage = itemView.findViewById(R.id.introductory_image);
            IntroductoryContentVideo = itemView.findViewById(R.id.introductory_video);
            //courseReview = (TextView) itemView.findViewById(R.id.course_review_view) ;
            cv = (CardView)itemView.findViewById(R.id.courseIntroductoryCard);
        }

    }
}