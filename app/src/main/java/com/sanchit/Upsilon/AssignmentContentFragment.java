package com.sanchit.Upsilon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class AssignmentContentFragment extends Fragment {


    public AssignmentContentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assignment_content, container, false);
        PDFView pdfView = view.findViewById(R.id.contentView);
        final InputStream[] input = {null};
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //Your code goes here
                    try {
                        input[0] = new URL("https://res.cloudinary.com/upsilon175/image/upload/v1625568680/Upsilon/Courses/5facf7889f30789191b2afea/assign3_eavpwl.pdf").openStream();
                        pdfView.fromStream(input[0]).load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        return view;
    }
}