package com.sanchit.Upsilon.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.sanchit.Upsilon.R;

public class UpsilonJobService extends JobService {
    private static final String TAG = "UpsilonJobService";
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started!");

        doBackgroundWork(params);

        return true;
    }

    public void doBackgroundWork(JobParameters params){
        new Thread(new Runnable(){
            @Override
            public void run(){
                

                for (int i = 0; i < 10; i++){
                    Log.d(TAG, "run" + i);
                    if (jobCancelled){
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "Job finished!");
                jobFinished(params, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion!");
        jobCancelled = false;
        return false;
    }
}
