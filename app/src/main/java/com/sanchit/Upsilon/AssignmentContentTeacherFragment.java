package com.sanchit.Upsilon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sanchit.Upsilon.ui.assignment.AssignmentStudentData;
import com.sanchit.Upsilon.ui.assignment.AssignmentTeacherData;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class AssignmentContentTeacherFragment extends Fragment {
    AssignmentTeacherData assignmentTeacherData;


    public AssignmentContentTeacherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assignment_content, container, false);
        String url = "https://res.cloudinary.com/upsilon175/image/upload/v1625568680/Upsilon/Courses/5facf7889f30789191b2afea/assign3_eavpwl.pdf";
        PDFView pdfView = view.findViewById(R.id.contentView);
        FrameLayout loading = view.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    InputStream input = null;
                    try {
                        input = new URL(url).openStream();
                        pdfView.fromStream(input).onLoad(new OnLoadCompleteListener() {
                            @Override
                            public void loadComplete(int nbPages) {
                                loading.setVisibility(View.GONE);
                            }
                        }).load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        FloatingActionButton button = view.findViewById(R.id.edit_content);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: upload problem sheet (pdf file) and load it in pdfView.
            }
        });

        assert getArguments() != null;
        assignmentTeacherData = (AssignmentTeacherData) getArguments().getSerializable("Assignment");
        return view;
    }
}