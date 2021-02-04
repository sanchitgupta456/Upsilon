package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.sanchit.Upsilon.Interest.Interest;
import com.sanchit.Upsilon.Interest.InterestCardAdapter;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Objects;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class UserDataSetupFragment1 extends Fragment implements InterestCardAdapter.ItemClickListener{


    private static final String TAG = "UserDataSetupActivity1";

    String appID = "upsilon-ityvn";
    View actionBarView;
    //Button addInterests;
    //EditText text;
    //ChipGroup group;
    RecyclerView grid;
    InterestCardAdapter adapter;
    ArrayList<String> interests;
    String interest;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    ArrayList<String> mycourses;
    ArrayList<Interest> list;

    UserDataViewModel viewModel;

    public UserDataSetupFragment1() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_data_setup1, container, false);
        //addInterests = (Button) view.findViewById(R.id.addInterest);
        //searchView = (SearchView)findViewById(R.id.searchInterests);
        //text = (EditText) view.findViewById(R.id.textEnterInterests);
        //group = (ChipGroup) view.findViewById(R.id.groupInterests);
        grid = (RecyclerView) view.findViewById(R.id.gridInterests);
        //group.removeAllViews();

        viewModel = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        user = app.currentUser();
        setupGrid();
        interests = new ArrayList<>();
        viewModel.setInterests(interests);


        //mycourses = new ArrayList<>();
        //viewModel.setMycourses(mycourses);

        /*
        addInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                String[] _interests = Objects.requireNonNull(text.getText()).toString().toLowerCase().trim().split(" ");
                for(String interest : _interests) {
                    interests.add(interest);
                    Chip chip = (Chip) inflater.inflate(R.layout.layout_interests_recycler_view, group, false);
                    chip.setText(interest);
                    group.addView(chip);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            group.removeView(view);
                            interests.remove(chip.getText().toString());
                            viewModel.removeInterest(chip.getText().toString());
                        }
                    });
                }
                viewModel.addInterest(interest);
                text.setText("");
            }
        });

        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                    LayoutInflater inflater = getLayoutInflater();
                    String[] _interests = Objects.requireNonNull(v.getText()).toString().toLowerCase().trim().split(" ");
                    for(String interest : _interests) {
                        interests.add(interest);
                        Chip chip = (Chip) inflater.inflate(R.layout.layout_interests_recycler_view, group, false);
                        chip.setText(interest);
                        group.addView(chip);
                        chip.setOnCloseIconClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                group.removeView(view);
                                interests.remove(chip.getText().toString());
                                viewModel.removeInterest(chip.getText().toString());
                            }
                        });
                    }
                    viewModel.addInterest(interest);
                    v.setText("");
                    return true;
                }
                return false;
            }
        });*/
        return view;
    }
    public void setupGrid() {
        Log.v("UserDataSetupFragment1","Setting Up Grid");
        list = new ArrayList<>();
        /* test:
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
        list.add(new Interest("Computer Science"));
         */
        getListOfInterestsData();
        adapter = new InterestCardAdapter(getContext(), list);

        GridLayoutManager manager = new GridLayoutManager(getContext(),3);
        adapter.setClickListener(this);
        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);
        grid.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: card at pos " + position + " is clicked");
        Interest interest = list.get(position);
        if(interest.isSelected)
            interests.add(interest.getNameInterest());
        else
            interests.remove(interest.getNameInterest());
        viewModel.setInterests(interests);
        //Snackbar.make(requireView(),interest.getNameInterest().toString(), Snackbar.LENGTH_LONG).show();
        Toast.makeText(getContext(),interest.getNameInterest(),Toast.LENGTH_LONG).show();
    }

    public void getListOfInterestsData() {
        Log.v("UserDataSetupFragment1","Getting Interest Data");
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Utility");
        Document queryFilter = new Document("field","interests");
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

        findTask.getAsync(task->{
            if(task.isSuccess())
            {
                MongoCursor<Document> results = task.get();
                try {
                    Document result = results.next();
                    ArrayList<String> temporary = (ArrayList<String>) result.get("interests");
                    for(String s:temporary)
                    {
                        Log.v("UserDataSetupFragment1",s);
                        list.add(new Interest(s));
                        adapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Log.v("UserDataSetupFragment1","Error"+task.getError().toString());
            }
        });
        //TODO: Get data for list (type: ArrayList<Interest> here)
        //
        //See: Interest class and other details.
        //
    }
}