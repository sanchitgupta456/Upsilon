package com.sanchit.Upsilon.classData;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.sanchit.Upsilon.ClassActivity;
import com.sanchit.Upsilon.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private ArrayList<ScheduledClass> mClasses;
    private Context context;
    private ItemClickListener mClickListener;

    public ScheduleAdapter(Context context, ArrayList<ScheduledClass> mClasses) {
        this.mClasses = mClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_card,parent,false);
        ScheduleAdapter.ViewHolder viewHolder = new ScheduleAdapter.ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.className.setText(mClasses.get(position).className);
        holder.day.setText(mClasses.get(position).date);
        holder.month.setText(mClasses.get(position).month);
        holder.time.setText(String.valueOf(mClasses.get(position).time));
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView month;
        TextView day;
        TextView className;
        TextView time;
        LinearLayout cv;

        public ViewHolder(View itemView)
        {
            super(itemView);
            month = (TextView) itemView.findViewById(R.id.month);
            day = (TextView) itemView.findViewById(R.id.date);
            className = (TextView) itemView.findViewById(R.id.classTitle);
            time = (TextView) itemView.findViewById(R.id.time);
            cv = (LinearLayout) itemView.findViewById(R.id.card);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                Log.d(TAG, "onClick: inside view holder method: here");
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        Log.d(TAG, "setClickListener: here");
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
