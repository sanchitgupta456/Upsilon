package com.sanchit.Upsilon;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.FrameLayout;

import com.sanchit.Upsilon.advancedTestData.Question;
import com.sanchit.Upsilon.testData.TestCreationFragment;
import com.sanchit.Upsilon.ui.testcreate.PlaceholderFragment;
import com.sanchit.Upsilon.ui.testcreate.SectionsPagerAdapter;
import com.sanchit.Upsilon.databinding.ActivityTestCreationBinding;

import java.util.ArrayList;

public class TestCreationActivity extends AppCompatActivity {

    private ActivityTestCreationBinding binding;
    private FrameLayout editFrame;
    private int currentQuestionNumber;
    private ArrayList<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTestCreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        currentQuestionNumber = 0;
        questions = new ArrayList<>();
        questions.add(new Question());

//        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
//        ViewPager viewPager = binding.viewPager;
//        viewPager.setAdapter(sectionsPagerAdapter);
//        TabLayout tabs = binding.tabs;
//        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.nextAdd;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: navigate to next. if last is reached, then create a new question.
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            Bundle bundle = new Bundle();
            //bundle.putSerializable("Question",currentQuestion); //TODO: settle this on completing the right template for Question
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.question_edit_frame, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private boolean loadQuestion(int i) {
        if(i < questions.size()) {
            Question currentQuestion = questions.get(i);
            Fragment fragment = new TestCreationFragment();
            Bundle bundle = new Bundle();
            //bundle.putSerializable("Question",currentQuestion); //TODO: settle this on completing the right template for Question
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.question_edit_frame, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}