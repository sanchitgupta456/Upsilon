package com.sanchit.Upsilon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;




public class TeachingStatListAdapter extends RecyclerView.Adapter<TeachingStatListAdapter.ViewHolder> {

    //vars
    private ArrayList<TeachingStatData> mTeachingStatDataArrayList = new ArrayList<>();
    private Context mContext;

    public TeachingStatListAdapter(Context context, ArrayList<TeachingStatData> TeachingStatDataArrayList) {
        mTeachingStatDataArrayList = TeachingStatDataArrayList;
        mContext = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stat_card_teacher, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.statData.setText(mTeachingStatDataArrayList.get(position).getStatData());
        holder.statField.setText(mTeachingStatDataArrayList.get(position).getStatField());
    }

    @Override
    public int getItemCount() {
        return mTeachingStatDataArrayList.size();
    }
    //done
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView statData;
        TextView statField;

        public ViewHolder(View itemView) {
            super(itemView);
            statData = itemView.findViewById(R.id.statData);
            statField = itemView.findViewById(R.id.statField);
        }
    }


}
