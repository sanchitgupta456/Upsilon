package com.sanchit.Upsilon;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {

    private static final String TAG = "CourseListAdapter";

    private ArrayList<String> courseNames = new ArrayList<>();
    private Context context;

    public CourseListAdapter(Context context, ArrayList<String> courseNames) {
        this.courseNames = courseNames;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view) ;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: added to screen : " + courseNames.get(position));
        holder.textView.setText(courseNames.get(position));
    }

    @Override
    public int getItemCount() {
        return courseNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.courseListItemText);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.courseListItem);
        }
    }
}
