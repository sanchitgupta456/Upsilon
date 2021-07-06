package com.sanchit.Upsilon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sanchit.Upsilon.ui.assignment.AssignmentStudentData;


public class AssignmentSubmissionFragment extends Fragment {

    RecyclerView recyclerView;
    Button submit;
    AssignmentStudentData assignmentStudentData;
    public AssignmentSubmissionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_assignment_submission, container, false);
        FloatingActionButton fab = inflate.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add a submission, currently unavailable", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        assert getArguments() != null;
        assignmentStudentData = (AssignmentStudentData) getArguments().getSerializable("Assignment");
        recyclerView = inflate.findViewById(R.id.submissionList);
        submit = inflate.findViewById(R.id.submit_all);
        return inflate;
    }
}