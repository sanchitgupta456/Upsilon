package com.sanchit.Upsilon.courseData;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.sanchit.Upsilon.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ViewHolder>{
    private static final String TAG = "ContentAdapter";

    final int VIEW_TYPE_VIDEO = 1;
    final int VIEW_TYPE_IMAGE = 0;

    public SimpleExoPlayer exoPlayer;

    List<String> VideoUrls;
    List<String> ImageUrls;
    List<String> DocUrls;
    Context context;

    public ResourceAdapter(List<String> videoUrls, List<String> imageUrls, List<String> docUrls) {
        VideoUrls = videoUrls;
        ImageUrls = imageUrls;
        DocUrls = docUrls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        Log.v("Image","OnCreated");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resource_card,parent,false);
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
            holder.videoPlayerView.setVisibility(View.VISIBLE);
            holder.docAnchor.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.GONE);
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
            holder.videoPlayerView.setOnTouchListener(new View.OnTouchListener() {
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
        else if(position< VideoUrls.size() + DocUrls.size()) {
            String docUrl = DocUrls.get(position - VideoUrls.size());
            holder.videoPlayerView.setVisibility(View.GONE);
            holder.docAnchor.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
            TextView tv = (TextView) holder.docAnchor.findViewById(R.id.file_name);
            tv.setText(docUrl);
            PDFView pdfView = (PDFView) holder.docAnchor.findViewById(R.id.thumbnail);
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        InputStream input = null;
                        try {
                            input = new URL(docUrl).openStream();
                            pdfView.fromStream(input).onLoad(new OnLoadCompleteListener() {
                                @Override
                                public void loadComplete(int nbPages) {
                                    //load complete //eat five star, do nothing
                                }
                            }).load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }                } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
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
                    ProgressDialog loadingBar = new ProgressDialog(builder.getOwnerActivity());
                    loadingBar.setTitle("Loading file");
                    loadingBar.setMessage("Please wait while the file gets loaded...");
                    loadingBar.show();
                    PDFView pdfView = new PDFView(context.getApplicationContext(), null);
                    //load file
                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try  {
                                InputStream input = null;
                                try {
                                    input = new URL(docUrl).openStream();
                                    pdfView.fromStream(input).onLoad(new OnLoadCompleteListener() {
                                        @Override
                                        public void loadComplete(int nbPages) {
                                            loadingBar.dismiss();
                                            builder.addContentView(pdfView, new RelativeLayout.LayoutParams(
                                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                                    ViewGroup.LayoutParams.MATCH_PARENT));
                                            builder.show();
                                        }
                                    }).load();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }                } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    thread.start();

                }
            });
        } else {
            String imageUrl = ImageUrls.get(position - VideoUrls.size() - DocUrls.size());
            holder.videoPlayerView.setVisibility(View.GONE);
            holder.docAnchor.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);

            //TODO: TO BE COMPLETED
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
        PlayerView videoPlayerView;
        ImageView imageView;
        FrameLayout docAnchor;
        private long playbackPosition;
        private int currentWindow;
        private boolean playWhenReady;
        //ProgressBar progressBar;
        RelativeLayout cv;

        public ViewHolder(View itemView)
        {
            super(itemView);
            videoPlayerView = itemView.findViewById(R.id.video_player);
            cv = (RelativeLayout) itemView.findViewById(R.id.container);
            imageView = itemView.findViewById(R.id.image_view);
            docAnchor = itemView.findViewById(R.id.doc_anchor);
        }
        private void intiPlayer(String url) {
            try {
                exoPlayer = new SimpleExoPlayer.Builder(context).build();
                Uri videoURI = Uri.parse(url);
                MediaItem mediaItem = MediaItem.fromUri(videoURI);
                videoPlayerView.setPlayer(exoPlayer);
                exoPlayer.setMediaItem(mediaItem);
                exoPlayer.prepare();

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
