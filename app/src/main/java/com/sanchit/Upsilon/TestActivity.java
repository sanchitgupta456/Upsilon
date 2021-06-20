package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.sanchit.Upsilon.testData.Mode;
import com.sanchit.Upsilon.testData.Question;
import com.sanchit.Upsilon.testData.QuestionAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";

    private RecyclerView rv;
    private QuestionAdapter adapter;
    private ArrayList<Question> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Test 1");

        rv = findViewById(R.id.rvTest);
        setupRv();
    }

    public void setupRv() {
        getList();
        adapter = new QuestionAdapter(list, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);
        rv.addItemDecoration(decoration);
    }

    public void getList() {
        //TODO: procure list of questions into the array list 'list'

        /* This is a test code. START. */
        list = new ArrayList<>();
        ArrayList<String> s = new ArrayList<String>();
        for (int i = 1; i <= 4; i++) {
            s.add("This is sample mcq option " + i);
        }
        Question q;
        for(int i = 1; i <= 5; i++) {
            q = new Question();
            q.setNumber(i);
            q.setMode(Mode.REVIEW);
            Log.d(TAG, "getList: q num is set to: " + i);
            q.setQuestionText("This is sample question text." +
                    "This is sample question text." +
                    "This is sample question text." +
                    "This is sample question text." +
                    "This is sample question text." +
                    "This is sample question text." +
                    "This is sample question text." +
                    "This is sample question text." +
                    "This is sample question text." +
                    "This is sample question text." +
                    "This is sample question text." +
                    "This is sample question text.");
            q.setNumMCQOptions(4);
            q.setMCQSingle(true);
//            q.setMCQMultiple(false);
//            q.setTextEnabled(true);
//            q.setFileUploadEnabled(true);
//            q.setAnswerText("");
            q.setMcqOptionsList(s);
            ArrayList<Integer> a = new ArrayList<>();
            a.add(i%4);
            q.setCorrectMCQOptions(a);
            q.setMarkedMCQOptions(new ArrayList<Integer>());
            q.setFull_marks(4);
//            q.setMarks_received(3);
            list.add(q);
            Log.d(TAG, "getList: "+ list.get(i-1).getNumber());
        }
        /* END of test code */
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}