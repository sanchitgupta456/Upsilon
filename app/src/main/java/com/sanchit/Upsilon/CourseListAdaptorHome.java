package com.sanchit.Upsilon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
/*
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;


 */
import java.util.ArrayList;


public class CourseListAdaptorHome extends RecyclerView.Adapter<CourseListAdaptorHome.ViewHolder> {

    //vars
    private ArrayList<String> mCourseNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Context mContext;

    public CourseListAdaptorHome(Context context, ArrayList<String> CourseNames, ArrayList<String> imageUrls) {
        mCourseNames = CourseNames;
        mImageUrls = imageUrls;
        mContext = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_card, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        
        /*
        //handle images
        setupImageLoader();

        ImageLoader imageLoader = ImageLoader.getInstance();
        int defaultImage = mContext.getResources().getIdentifier("@drawable/background",null,mContext.getPackageName());


        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();

        imageLoader.displayImage(mImageUrls.get(position), holder.courseImage, options); //param missing: options


        holder.courseName.setText(mCourseNames.get(position));

        holder.courseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, mCourseNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }
    //done
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView courseImage;
        TextView courseName;

        public ViewHolder(View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.courseImage);
            courseName = itemView.findViewById(R.id.courseName);
        }
    }

    private void setupImageLoader(){
        /*
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP

         */
    }
}
