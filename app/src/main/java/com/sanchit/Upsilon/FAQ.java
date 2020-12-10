package com.sanchit.Upsilon;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class FAQ extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<String> questions;
    private ArrayList<String> answers;
    private View bar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq);
        recyclerView = (RecyclerView) findViewById(R.id.faq_recycler);
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.faq);
        bar = (View) getSupportActionBar().getCustomView();
        getFAQList();
    }
    public void getFAQList() {
        questions.add("Is my payment safe and secure?");
        questions.add("Is my payment safe and secure?");
        questions.add("Is my payment safe and secure?");
        questions.add("Is my payment safe and secure?");
        questions.add("Is my payment safe and secure?");
        answers.add("Yes!");
        answers.add("Yes!");
        answers.add("Yes!");
        answers.add("Yes!");
        answers.add("Yes!");

        initRecyclerViews();
    }

    public void initRecyclerViews() {
        FAQAdapter adapter = new FAQAdapter(questions, answers, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getActionView()==bar){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
