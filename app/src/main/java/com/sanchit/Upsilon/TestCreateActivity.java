package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.sanchit.Upsilon.advancedTestData.Question;
import com.sanchit.Upsilon.advancedTestData.QuestionCreateAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class TestCreateActivity extends AppCompatActivity {
    private RecyclerView rv;
    private QuestionCreateAdapter adapter;
    private ArrayList<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_create);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create a new test");

        rv = (RecyclerView) findViewById(R.id.rv_question_create);
        questions = new ArrayList<>();
        adapter = new QuestionCreateAdapter(questions, getApplicationContext());
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext(), RecyclerView.VERTICAL, false));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        getQuestions();
        adapter.notifyDataSetChanged();
    }

    public void getQuestions() {

        Question q = new Question();
        questions.add(q);
        q = new Question();
        questions.add(q);
        q = new Question();
        questions.add(q);
        q = new Question();
        questions.add(q);
        q = new Question();
        questions.add(q);
        q = new Question();
        questions.add(q);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}