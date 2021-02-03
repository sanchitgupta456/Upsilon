package com.sanchit.Upsilon.Interest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.sanchit.Upsilon.R;
import com.sanchit.Upsilon.TeachingStatData;
import com.sanchit.Upsilon.TeachingStatListAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InterestCardAdapter extends RecyclerView.Adapter<InterestCardAdapter.ViewHolder> {
    private static final String TAG = "InterestCardAdapter";
    //vars
    private ArrayList<Interest> mAvailableInterestsArrayList = new ArrayList<>();
    private final Context mContext;
    private ItemClickListener mClickListener;

    public InterestCardAdapter(Context context, ArrayList<Interest> list) {
        mAvailableInterestsArrayList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_grid_interest, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Interest interest = mAvailableInterestsArrayList.get(position);
        holder.name.setText(mAvailableInterestsArrayList.get(position).getNameInterest());
        //Picasso.with(mContext).load(mAvailableInterestsArrayList.get(position).getImageUri()).fit().centerCrop().into(holder.image);
        holder.cv.setChecked(interest.isSelected);
        Log.d(TAG, "onBindViewHolder: here: " + position + "checked status: " + interest.isSelected);
        holder.cv.setOnClickListener(v -> {
            interest.select(!interest.isSelected);
            holder.cv.setChecked(interest.isSelected);
            mClickListener.onItemClick(v, position);
            //checkedList.set(mAvailableInterestsArrayList.indexOf(((TextView)v.findViewById(R.id.interestName)).getText()),((MaterialCardView) v).isChecked());
        });
    }

    @Override
    public int getItemCount() {
        return mAvailableInterestsArrayList.size();
    }

    //done
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        ImageView image;
        MaterialCardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.interestName);
            image = itemView.findViewById(R.id.interestImage);
            cv = itemView.findViewById(R.id.interestCard);
            itemView.setOnClickListener(this);
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

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mAvailableInterestsArrayList.get(id).getNameInterest();
    }
}
