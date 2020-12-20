package com.sanchit.Upsilon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<Boolean> isChecked = new ArrayList<>();
    private Context context;

    public FilterAdapter(ArrayList<String> tags, ArrayList<Boolean> isChecked, Context context) {
        this.tags = tags;
        this.isChecked = isChecked;
        this.context = context;
    }

    @NonNull
    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_card, parent, false);
        return new FilterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.ViewHolder holder, int position) {
        holder.tag.setTextOn(tags.get(position));
        holder.tag.setTextOff(tags.get(position));
        holder.tag.setChecked(isChecked.get(position));
        if(isChecked.get(position)) {
            holder.tag.setBackgroundResource(R.drawable.filter_checked);
            holder.tag.setTextColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.tag.setBackgroundResource(R.drawable.filter_unchecked);
            holder.tag.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        holder.tag.setOnClickListener(view -> {
            if (!holder.tag.isChecked()) {
                isChecked.set(position, true);
                holder.tag.setBackgroundResource(R.drawable.filter_checked);
                holder.tag.setTextColor(context.getResources().getColor(R.color.colorWhite));
            } else {
                isChecked.set(position, false);
                holder.tag.setBackgroundResource(R.drawable.filter_unchecked);
                holder.tag.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ToggleButton tag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tag = (ToggleButton) itemView.findViewById(R.id.tag);
        }
    }
}
