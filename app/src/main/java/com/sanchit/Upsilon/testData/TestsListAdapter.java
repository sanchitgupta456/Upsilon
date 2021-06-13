package com.sanchit.Upsilon.testData;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sanchit.Upsilon.R;

import java.lang.reflect.Array;
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
        String status = item.getStatus();
        holder.statusTvShow.setText(status);
        holder.llGrades.setVisibility(View.GONE);
        switch (status) {
            case "YET TO START":
                holder.statusTvShow.setTextColor(Color.parseColor("@color/colorPrimary"));
                break;
            case "ONGOING":
                holder.statusTvShow.setTextColor(Color.parseColor("@color/colorRed"));
                break;
            case "COMPLETED UNGRADED":
                holder.statusTvShow.setTextColor(Color.parseColor("@color/GreyDark"));
                break;
            case "GRADED":
                holder.statusTvShow.setTextColor(Color.parseColor("#007700"));
                holder.llGrades.setVisibility(View.VISIBLE);
                holder.receivedMarksTvShow.setText(item.getMarksReceived());
                holder.fullMarksTvShow.setText(item.getTotalMarks());
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
