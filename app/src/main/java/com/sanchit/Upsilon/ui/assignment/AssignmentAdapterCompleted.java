package com.sanchit.Upsilon.ui.assignment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.Upsilon.R;
import com.sanchit.Upsilon.classData.ScheduleAdapter;
import com.sanchit.Upsilon.classData.ScheduledClass;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class AssignmentAdapterCompleted extends RecyclerView.Adapter<AssignmentAdapterCompleted.ViewHolder>{
    private ArrayList<AssignmentStudentData> mList;
    private Context context;

    public AssignmentAdapterCompleted(ArrayList<AssignmentStudentData> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    private ItemClickListener mClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_card_completed,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(mList.get(position).getName());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView name;
        CardView cv;

        public ViewHolder(View itemView)
        {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            cv = itemView.findViewById(R.id.container);
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
