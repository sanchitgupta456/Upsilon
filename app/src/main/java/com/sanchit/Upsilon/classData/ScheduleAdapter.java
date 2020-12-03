package com.sanchit.Upsilon.classData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.Upsilon.R;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private ArrayList<ScheduledClass> mClasses;
    private Context context;

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
        holder.day.setText(mClasses.get(position).day);
        holder.month.setText(mClasses.get(position).month);
        holder.time.setText(mClasses.get(position).time);
    }

    @Override
    public int getItemCount() {
        return mClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView month;
        TextView day;
        TextView className;
        TextView time;

        public ViewHolder(View itemView)
        {
            super(itemView);
            month = (TextView) itemView.findViewById(R.id.month);
            day = (TextView) itemView.findViewById(R.id.date);
            className = (TextView) itemView.findViewById(R.id.classTitle);
            time = (TextView) itemView.findViewById(R.id.time);
        }

    }
}
