package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TeacherDataSetupActivity extends AppCompatActivity {
    private static final String TAG = "TeacherSetupActivity";
    HorizontalDotProgress progress;
    Button next;
    private QualificationsViewModel model0;
    private ExperienceViewModel model1;
    private PaymentDetailsViewModel model2;
    //TODO: Use these view models.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_data_setup);
        progress = (HorizontalDotProgress) findViewById(R.id.dot_progress);
        next = (Button) findViewById(R.id.btnNext);
        model0 = new ViewModelProvider(this).get(QualificationsViewModel.class);
        model1 = new ViewModelProvider(this).get(ExperienceViewModel.class);
        model2 = new ViewModelProvider(this).get(PaymentDetailsViewModel.class);
        ViewPagerAdapter viewPagerAdapter =
                new ViewPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                progress.setDotPosition(position);
            }

            @Override
            public void onPageSelected(int position) {
                progress.setDotPosition(position);
                if(position == 2){
                    next.setText("Go");
                } else {
                    next.setText(R.string.next);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = viewPager.getCurrentItem();
                switch (pos){
                    case 0:
                        viewPager.setCurrentItem(1);
                        progress.setDotPosition(1);
                        break;
                    case 1:
                        viewPager.setCurrentItem(2);
                        progress.setDotPosition(2);
                        break;
                    case 2:
                        putData();
                        //TODO: Navigation to next activity from here.
                        //Intent intent = new Intent(getApplicationContext(), AddCourseActivity.class);
                        //startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void putData(){
        //TODO: upload data
        //get data from model0, model1 and model2 using getter and then getValue
        //eg. model0.getSpecialities().getValue()
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final Context mContext;
        public ViewPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            if (position == 0) {
                return QualificationsFragment.newInstance();
            } else if (position == 1){
                return ExperienceFragment.newInstance();
            } else {
                return PaymentDetailsFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}