package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

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

public class UserDataSetupFragment1 extends Fragment {


    private static final String TAG = "UserDataSetupActivity1";

    String appID = "upsilon-ityvn";
    View actionBarView;
    Button addInterests;
    EditText text;
    ChipGroup group;
    ArrayList<String> interests;
    String interest;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    ArrayList<String> mycourses;

    UserDataViewModel viewModel;

    public UserDataSetupFragment1() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_data_setup1, container, false);
        addInterests = (Button) view.findViewById(R.id.addInterest);
        //searchView = (SearchView)findViewById(R.id.searchInterests);
        text = (EditText) view.findViewById(R.id.textEnterInterests);
        group = (ChipGroup) view.findViewById(R.id.groupInterests);
        group.removeAllViews();
        viewModel = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        user = app.currentUser();
        interests = new ArrayList<>();
        viewModel.setInterests(interests);
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");

        mycourses = new ArrayList<>();
        viewModel.setMycourses(mycourses);

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
        return view;
    }
}