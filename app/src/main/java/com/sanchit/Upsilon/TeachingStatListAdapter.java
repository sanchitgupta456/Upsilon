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
    private ArrayList<String> mStatDatas = new ArrayList<>();
    private ArrayList<String> mStatFields = new ArrayList<>();
    private Context mContext;

    public TeachingStatListAdapter(Context context, ArrayList<String> statDatas, ArrayList<String> statFields) {
        mStatDatas = statDatas;
        mStatFields = statFields;
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

        holder.statData.setText(mStatDatas.get(position));
        holder.statField.setText(mStatFields.get(position));


    }

    @Override
    public int getItemCount() {
        return mStatFields.size();
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
