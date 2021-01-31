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

public class QualificationsFragment extends Fragment {

    private QualificationsViewModel mViewModel;
    ArrayList<String> qualifications = new ArrayList<>(), specialities = new ArrayList<>();
    ChipGroup groupS, groupQ;

    public static QualificationsFragment newInstance() {
        return new QualificationsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.qualifications_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText etQualifications = (EditText) view.findViewById(R.id.etQualification);
        EditText etSpecialities = (EditText) view.findViewById(R.id.etSpecialities);
        groupS = (ChipGroup) view.findViewById(R.id.group_specialities);
        groupQ = (ChipGroup) view.findViewById(R.id.group_qualifications);

        etSpecialities.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    String string = v.getText().toString();
                    specialities.add(string);
                    mViewModel.setSpecialities(specialities);
                    LayoutInflater inflater = getLayoutInflater();
                    Chip chip = (Chip) inflater.inflate(R.layout.layout_experience_chip, groupS, false);
                    chip.setText(string);
                    groupS.addView(chip);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            groupS.removeView((Chip)view);
                            specialities.remove(((Chip)view).getText().toString());
                            mViewModel.setSpecialities(specialities);
                        }
                    });
                    v.setText("");
                }
                return false;
            }
        });
        etQualifications.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    String string = v.getText().toString();
                    qualifications.add(string);
                    mViewModel.setQualifications(qualifications);
                    LayoutInflater inflater = getLayoutInflater();
                    Chip chip = (Chip) inflater.inflate(R.layout.layout_experience_chip, groupQ, false);
                    chip.setText(string);
                    groupQ.addView(chip);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            groupQ.removeView((Chip)view);
                            qualifications.remove(((Chip)view).getText().toString());
                            mViewModel.setQualifications(qualifications);
                        }
                    });
                    v.setText("");
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(QualificationsViewModel.class);
        // TODO: Use the ViewModel
    }

}