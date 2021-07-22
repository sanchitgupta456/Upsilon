package com.sanchit.Upsilon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanchit.Upsilon.ui.assignment.AssignmentTeacherData;

public class AssignmentSubmissionsTeacherFragment extends Fragment {
    AssignmentTeacherData assignmentTeacherData;

    public AssignmentSubmissionsTeacherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assignment_submissions_teacher, container, false);
    }
}