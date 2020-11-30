package com.sanchit.Upsilon.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.sanchit.Upsilon.MainActivity;
import com.sanchit.Upsilon.R;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

import static com.sanchit.Upsilon.notifications.NotifChannel.CHANNEL_ID;

public class UpsilonJobService extends JobService {
    private static final String TAG = "UpsilonJobService";
    private boolean jobCancelled = false;
    String appID = "upsilon-ityvn";
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    private ArrayList<String> myCourses;

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
                while(true) {
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    App app = new App(new AppConfiguration.Builder(appID).build());
                    User user = app.currentUser();
                    mongoClient = user.getMongoClient("mongodb-atlas");
                    mongoDatabase = mongoClient.getDatabase("Upsilon");
                    MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");
                    MongoCollection<Document> mongoCollection2 = mongoDatabase.getCollection("CourseData");

                    myCourses = new ArrayList<>();
                    Document queryFilter = new Document("userid", user.getId());

                    RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();


                    Document results = findTask.get().next();
                    myCourses = (ArrayList<String>) results.get("myCourses");
                    for(int i=0;i<myCourses.size();i++)
                    {
                        Document queryFilter1 = new Document("courseId",myCourses.get(i));
                        RealmResultTask<MongoCursor<Document>> findTask1 = mongoCollection2.find(queryFilter1).iterator();
                        MongoCursor<Document> result2 = findTask1.get();
                        //Log.v("Iterating",myCourses.get(i));
                        //Log.v("Result", String.valueOf(result2.next()));
                        if(result2.hasNext())
                        {
                            Log.v("Found","found");
                            Document result1 = result2.next();
                            if(result1!=null && result1.getString("nextLectureOn")!=null)
                            {
                                Log.v("Found Lecture",String.valueOf(Long.parseLong(result1.getString("nextLectureOn"))));
                                Log.v("System Time", String.valueOf(System.currentTimeMillis()));
                                Long a = Long.parseLong(result1.getString("nextLectureOn"));
                                Long b = System.currentTimeMillis();
                                if(a>=b & a<=(b+3600000))
                                {
                                    Log.v("Hello","Notif");
                                    createNotificationChannel();
                                    displayNotif();
                                    Log.d(TAG, "Job finished!");
                                    jobFinished(params, false);
                                }
                            }
                        }


                    }

                    /*findTask.getAsync(task -> {
                        if (task.isSuccess()) {
                            MongoCursor<Document> results = task.get();
                            while (results.hasNext()) {
                                //Log.v("EXAMPLE", results.next().toString());
                                Document currentDoc = results.next();
                                Log.v("User", currentDoc.getString("userid"));
                                myCourses = (ArrayList<String>) currentDoc.get("myCourses");
                            }
                        } else {
                            Log.v("User", "Failed to complete search");
                        }
                    });*/

                    /*findTask2.getAsync(task -> {
                        if (task.isSuccess()) {
                            MongoCursor<Document> results = task.get();
                            while (results.hasNext()) {
                                //Log.v("EXAMPLE", results.next().toString());
                                Document currentDoc = results.next();
                                //Log.v("User",currentDoc.getString("userid"));
                                //myCourses = (ArrayList<String>) currentDoc.get("myCourses");
                                if (currentDoc.getString("courseId").equals(user.getId()) && currentDoc.getString("nextLectureOn") != null) {
                                    Log.v("Current", String.valueOf(System.currentTimeMillis()));
                                    Log.v("LectureTime", String.valueOf(Long.parseLong(currentDoc.getString("nextLectureOn"))));
                                    if (System.currentTimeMillis() * 1000 == Long.parseLong(currentDoc.getString("nextLectureOn"))) {
                                        createNotificationChannel();
                                        displayNotif();
                                        Log.d(TAG, "Job finished!");
                                        jobFinished(params, false);
                                    }
                                }
                            }
                        }
                    });*/


                }

            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion!");
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent(this, UpsilonBroadcastReciever.class);
        jobCancelled = false;
        return false;
    }

    public void displayNotif(){
        Intent fullScreenIntent = new Intent(this, MainActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.lightlogo1)
                        .setContentTitle("Upsilon")
                        .setContentText("Class is about to start!")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_CALL)

                        // Use a full-screen intent only for the highest-priority alerts where you
                        // have an associated activity that you would like to launch after the user
                        // interacts with the notification. Also, if your app targets Android 10
                        // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
                        // order for the platform to invoke this notification.
                        .setFullScreenIntent(fullScreenPendingIntent, true);

        Notification incomingCallNotification = notificationBuilder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(UpsilonJobService.this);
// notificationId is a unique int for each notification that you must define
        notificationManager.notify(123, notificationBuilder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Upsilon";
            String description = "Helllo!!";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
