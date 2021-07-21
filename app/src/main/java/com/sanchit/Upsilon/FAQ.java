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
        questions.add("How to register for a course?");
        questions.add("Can I teach a course?");
        questions.add("Can I get a refund in a course?");
        questions.add("How to withdraw money from the Upsilon wallet?");
        answers.add("Yes! Your payments are completed secured by RazorPay and all transactions on the app are monitored on manual basis to ensure this fact.");
        answers.add("To register a course, simply select it and click on register. It shows the price of the course, if it is not a free course and then takes you to the fee payment portal on clicking 'proceed to pay'. Make the payment, if the course is not free and you are instantly registered with the course.");
        answers.add("Yes! Anyone registered with Upsilon can teach a course. For teaching a paid course, you must fill out your payment details. You will be prompted if you have not done that the first time you offer a paid course.");
        answers.add("Yes! You can get a refund if applied for within 7 days of the course commencement. You must fill out a specific reason for a refund. You will be refunded back within two days into your Upsilon wallet.");
        answers.add("Simply go to your wallet and click the withdraw button. Your withdraw request will be registered and the specific money will be transferred to your bank account within less than 7 days.");

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
