package com.sanchit.Upsilon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.MyHolder> {


    ArrayList<String> mylist;

    private Context context;
    private LayoutInflater inflater;
    //List<Data> datas = Collections.emptyList();
    int currentpos = 0;

    //create constructor to initializ context and data sent from main activity.
    public InterestsAdapter(ArrayList<String> mylist, Context context) {

        this.mylist = mylist;
    }


    @Override
    public InterestsAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_interests_recycler_view, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        holder.textData.setText(mylist.get(position).toString());

    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView textData;

        //contructor for getting reference to the widget
        public MyHolder(View itemView) {
            super(itemView);

            textData = (TextView) itemView.findViewById(R.id.textdata);

        }


    }
}