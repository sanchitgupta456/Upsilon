package com.sanchit.Upsilon.courseData;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.sanchit.Upsilon.R;

import java.util.ArrayList;
import java.util.List;

public class VideoResourceAdapter extends RecyclerView.Adapter<VideoResourceAdapter.ViewHolder>{
    private static final String TAG = "ContentAdapter";

    final int VIEW_TYPE_VIDEO = 1;
    final int VIEW_TYPE_IMAGE = 0;

    public SimpleExoPlayer exoPlayer;

    List<String> VideoUrls;
    Context context;



    public VideoResourceAdapter(ArrayList<String> introductoryVideos) {
        this.VideoUrls = introductoryVideos;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        Log.v("Image","OnCreated");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_resource_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.v("Adapter","Binded");

        if(position<VideoUrls.size())
        {
            String videoUrl = VideoUrls.get(position);

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

            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: working");

                    //holder.IntroductoryContentVideo.start();
                    //Toast.makeText(context,"The position is:"+position,Toast.LENGTH_SHORT).show();
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
                    PlayerView playerView = new PlayerView(context);
                    ExoPlayer player = new SimpleExoPlayer.Builder(context).build();
                    player.setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)));
                    player.prepare();
                    playerView.setPlayer(player);
                    builder.addContentView(playerView, new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    builder.show();
                }
            });
            holder.IntroductoryContentVideo.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
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
                    PlayerView playerView = new PlayerView(context);
                    PlayerView.switchTargetView(exoPlayer, (PlayerView) v, playerView);
                    builder.addContentView(playerView, new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    builder.show();
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        if(VideoUrls==null)
        {
            return 0;
        }
        return VideoUrls.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        PlayerView IntroductoryContentVideo;
        private long playbackPosition;
        private int currentWindow;
        private boolean playWhenReady;
        //ProgressBar progressBar;
        RelativeLayout cv;

        public ViewHolder(View itemView)
        {
            super(itemView);
            IntroductoryContentVideo = itemView.findViewById(R.id.video_player);
            cv = (RelativeLayout) itemView.findViewById(R.id.container);
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
//                Dialog builder = new Dialog(context);
//                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                builder.getWindow().setBackgroundDrawable(
//                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//                        //nothing;
//                    }
//                });
//                PlayerView playerView = new PlayerView(context);
////                ExoPlayer player = new SimpleExoPlayer.Builder(context).build();
////                player.setMediaItem(mediaItem);
////                player.prepare();
////                playerView.setPlayer(player);
//                PlayerView.switchTargetView(exoPlayer, IntroductoryContentVideo, playerView);
//                builder.addContentView(playerView, new RelativeLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT));
//                builder.show();

            } catch (Exception e) {
                Log.e("MainActivity", " exoplayer error " + e.toString());
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
