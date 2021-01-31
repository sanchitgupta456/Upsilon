package com.sanchit.Upsilon;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Objects;

public class ExperienceFragment extends Fragment {

    private ExperienceViewModel mViewModel;
    private ArrayList<String> experiences = new ArrayList<>();
    ChipGroup group;

    public static ExperienceFragment newInstance() {
        return new ExperienceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.experience_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(ExperienceViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText editText = (EditText) view.findViewById(R.id.etExperience);
        group = (ChipGroup) view.findViewById(R.id.group_experience);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    String string = v.getText().toString();
                    experiences.add(string);
                    mViewModel.setExperience(experiences);
                    LayoutInflater inflater = getLayoutInflater();
                    Chip chip = (Chip) inflater.inflate(R.layout.layout_experience_chip, group, false);
                    chip.setText(string);
                    group.addView(chip);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            group.removeView((Chip)view);
                            experiences.remove(((Chip)view).getText().toString());
                            mViewModel.setExperience(experiences);
                        }
                    });
                    v.setText("");
                    return true;
                }
                return false;
            }
        });
    }
}