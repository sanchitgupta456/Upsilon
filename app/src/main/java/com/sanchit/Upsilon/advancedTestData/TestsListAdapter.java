package com.sanchit.Upsilon.advancedTestData;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.Upsilon.R;

import java.util.ArrayList;

public class TestsListAdapter extends RecyclerView.Adapter<TestsListAdapter.ViewHolder>{

    private ArrayList<TestListItem> list;
    private Context context;

    public TestsListAdapter(ArrayList<TestListItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_test_student_thumbnail,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TestListItem item = list.get(position);
        String id = item.getId(); //use it
        holder.nameTvShow.setText(item.getName());
        TestStatus status = item.getStatus();
//        holder.statusTvShow.setText(status.toString());
        holder.llGrades.setVisibility(View.GONE);
        switch (status) {
            case YET_TO_START:
                holder.statusTvShow.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                holder.statusTvShow.setText(R.string.yet_to_start);
                break;
            case ONGOING:
                holder.statusTvShow.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                holder.statusTvShow.setText(R.string.ongoing);
                break;
            case COMPLETED_NOT_GRADED:
                holder.statusTvShow.setTextColor(ContextCompat.getColor(context, R.color.GreyDark));
                holder.statusTvShow.setText(R.string.completed_ungraded);
                break;
            case GRADED:
                holder.statusTvShow.setTextColor(Color.parseColor("#007700"));
                holder.statusTvShow.setText(R.string.graded);
                holder.llGrades.setVisibility(View.VISIBLE);
                holder.receivedMarksTvShow.setText(String.valueOf(item.getMarksReceived()));
                holder.fullMarksTvShow.setText(String.valueOf(item.getTotalMarks()));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameTvShow;
        TextView receivedMarksTvShow;
        TextView fullMarksTvShow;
        TextView statusTvShow;
        LinearLayout llGrades;
        LinearLayout container;

        public ViewHolder(View itemView)
        {
            super(itemView);
            nameTvShow = (TextView)itemView.findViewById(R.id.name);
            receivedMarksTvShow = (TextView)itemView.findViewById(R.id.rMarks);
            fullMarksTvShow = (TextView) itemView.findViewById(R.id.fMarks);
            statusTvShow = (TextView) itemView.findViewById(R.id.status);
            llGrades = (LinearLayout) itemView.findViewById(R.id.ll_grade);
            container = (LinearLayout) itemView.findViewById(R.id.ll_frame);
        }

    }
}
