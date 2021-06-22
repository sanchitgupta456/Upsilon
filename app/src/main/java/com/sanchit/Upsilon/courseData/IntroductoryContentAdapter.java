package com.sanchit.Upsilon.courseData;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
//import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
//import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.sanchit.Upsilon.R;

import java.util.ArrayList;
import java.util.List;

public class IntroductoryContentAdapter extends RecyclerView.Adapter<IntroductoryContentAdapter.IntroductoryContentViewHolder> {

    final int VIEW_TYPE_VIDEO = 1;
    final int VIEW_TYPE_IMAGE = 0;

    public SimpleExoPlayer exoPlayer;

    List<String> ImageUrls;
    List<String> VideoUrls;
    Context context;



    public IntroductoryContentAdapter(ArrayList<String> introductoryImages, ArrayList<String> introductoryVideos) {

        this.ImageUrls = introductoryImages;
        this.VideoUrls = introductoryVideos;
        Log.v("Adapter","Images Initialised");
        Log.v("Adapter", String.valueOf(ImageUrls));

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

        Log.v("Adapter","Binded");
        if(position<ImageUrls.size())
        {
            String imageUrl = ImageUrls.get(position);

            //holder.courseReview.setText(courseReviews.getReview());
            Log.v("Image",imageUrl);
            Log.v("Images","Binded");
            //holder.imgTvShow.setImageResource(course.getCardImgID());
            Glide.with(context).load(imageUrl).into(holder.IntroductoryContentImage);
            holder.IntroductoryContentVideo.setVisibility(View.GONE);
            holder.IntroductoryContentImage.setVisibility(View.VISIBLE);
            holder.IntroductoryContentImage.setClickable(true);
            holder.IntroductoryContentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog builder = new Dialog(context);
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            //nothing;
                        }
                    });
                    ImageView imageView = new ImageView(context);
                    Glide.with(context).load(imageUrl).into(imageView);
                    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    builder.show();
                }
            });
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
            Uri video = Uri.parse(videoUrl);
            if (holder.isPlaying()) {
                Log.e("TAG1", "play");
                holder.releasePlayer();
                holder.intiPlayer(videoUrl);
            } else {
                Log.e("TAG1", "empty");
                holder.intiPlayer(videoUrl);
            }
            /*if(holder.IntroductoryContentVideo.isPlaying())
            {
                holder.IntroductoryContentVideo.stopPlayback();
            }
            holder.IntroductoryContentVideo.setVideoURI(video);
            holder.IntroductoryContentVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    holder.IntroductoryContentVideo.start();
                }

            });*/
            //holder.imgTvShow.setImageResource(course.getCardImgID());
            /*holder.IntroductoryContentVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    holder.IntroductoryContentVideo.setVideoPath(videoUrl);
                }
            });*/

            holder.IntroductoryContentImage.setVisibility(View.GONE);
            holder.IntroductoryContentVideo.setVisibility(View.VISIBLE);
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //holder.IntroductoryContentVideo.start();
                    //Toast.makeText(context,"The position is:"+position,Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public int getItemCount() {

        if(ImageUrls==null && VideoUrls==null)
        {
            return 0;
        }
        else if(ImageUrls==null)
        {
            return VideoUrls.size();
        }
        else if(VideoUrls==null)
        {
            return ImageUrls.size();
        }
        return ImageUrls.size()+VideoUrls.size();
    }


    public class IntroductoryContentViewHolder extends RecyclerView.ViewHolder
    {
        ImageView IntroductoryContentImage;
        PlayerView IntroductoryContentVideo;
        private long playbackPosition;
        private int currentWindow;
        private boolean playWhenReady;
        //ProgressBar progressBar;
        CardView cv;

        public IntroductoryContentViewHolder(View itemView)
        {
            super(itemView);
            IntroductoryContentImage = itemView.findViewById(R.id.introductory_image);
            IntroductoryContentVideo = itemView.findViewById(R.id.introductory_video);
            //courseReview = (TextView) itemView.findViewById(R.id.course_review_view) ;
            cv = (CardView)itemView.findViewById(R.id.courseIntroductoryCard);
        }
        private void intiPlayer(String url) {
            try {
//                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                exoPlayer = new SimpleExoPlayer.Builder(context).build();
                Uri videoURI = Uri.parse(url);
                MediaItem mediaItem = MediaItem.fromUri(videoURI);

//                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
//                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//                MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
                IntroductoryContentVideo.setPlayer(exoPlayer);
                exoPlayer.setMediaItem(mediaItem);
                exoPlayer.prepare();                //exoPlayer.setPlayWhenReady(true);
            } catch (Exception e) {
                Log.e("MainAcvtivity", " exoplayer error " + e.toString());
            }
        }

        private boolean isPlaying() {
            return exoPlayer != null
                    && exoPlayer.getPlaybackState() != Player.STATE_ENDED
                    && exoPlayer.getPlaybackState() != Player.STATE_IDLE
                    && exoPlayer.getPlayWhenReady();
        }

        private void releasePlayer() {
            if (exoPlayer != null) {
                playbackPosition = exoPlayer.getCurrentPosition();
                currentWindow = exoPlayer.getCurrentWindowIndex();
                playWhenReady = exoPlayer.getPlayWhenReady();
                exoPlayer.release();
                exoPlayer = null;
            }
        }
    }
}