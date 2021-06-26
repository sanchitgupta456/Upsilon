package com.sanchit.Upsilon.notificationData;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.Upsilon.ForumData.MessageAdapter;
import com.sanchit.Upsilon.ForumData.Messages;
import com.sanchit.Upsilon.Interest.InterestCardAdapter;
import com.sanchit.Upsilon.R;
import com.squareup.picasso.Picasso;

import org.bson.Document;
import org.w3c.dom.Text;

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

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private ArrayList<Notification> notifications;
    User user;
    App app;
    String appID = "upsilon-ityvn";
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    Context context;

    public NotificationAdapter(ArrayList<Notification> notifications)
    {
        this.notifications = notifications;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView date;
        TextView time;
        TextView announcement;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            announcement = (TextView) itemView.findViewById(R.id.announcement);

        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText(notifications.get(position).getDate());
        holder.time.setText(notifications.get(position).getTime());
        holder.announcement.setText(notifications.get(position).getAnnouncement());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }
}
