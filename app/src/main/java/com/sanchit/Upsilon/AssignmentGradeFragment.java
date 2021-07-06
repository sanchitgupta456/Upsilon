package com.sanchit.Upsilon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sanchit.Upsilon.ui.assignment.AssignmentStudentData;


public class AssignmentGradeFragment extends Fragment {


    //TODO: to be completed

    AssignmentStudentData assignmentStudentData;

    public AssignmentGradeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_assignment_grade, container, false);
//        recyclerView = inflate.findViewById(R.id.submissionList);
//        submit = inflate.findViewById(R.id.submit_all);
        assert getArguments() != null;
        assignmentStudentData = (AssignmentStudentData) getArguments().getSerializable("Assignment");
        return inflate;
    }

}