package com.sanchit.Upsilon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;
import com.sanchit.Upsilon.ui.assignment.AssignmentStudentData;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class AssignmentContentFragment extends Fragment {
    AssignmentStudentData assignmentStudentData;


    public AssignmentContentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assignment_content, container, false);
        PDFView pdfView = view.findViewById(R.id.contentView);
//        InputStream input = null;
//        try {
//            input = new URL("something").openStream();
//            pdfView.fromStream(input).load();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        assert getArguments() != null;
        assignmentStudentData = (AssignmentStudentData) getArguments().getSerializable("Assignment");
        return view;
    }
}