package com.sanchit.Upsilon.notificationData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.Upsilon.R;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private ArrayList<Notification> notifications;
    User user;
    App app;
    String appID = "upsilon-ityvn";
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    Context context;
    public ItemClickListener mClickListener;

    public NotificationAdapter(ArrayList<Notification> notifications)
    {
        this.notifications = notifications;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener
    {
        TextView date;
        TextView time;
        TextView announcement;
        RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            announcement = (TextView) itemView.findViewById(R.id.announcement);
            container = (RelativeLayout) itemView.findViewById(R.id.container);
        }

        @Override
        public boolean onLongClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
            }
            return false;
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
        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return mClickListener.onItemLongClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        boolean onItemLongClick(View view, int position);
    }
}
