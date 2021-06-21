package com.sanchit.Upsilon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link TeacherViewofCourseMaterialsFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class TeacherViewofCourseMaterialsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_viewof_course_materials, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.pdfs_for_this_course);
        add = (FloatingActionButton) view.findViewById(R.id.btnAddMaterial);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMaterial();
            }
        });
        return view;
    }

    public void addMaterial() {
        //
    }
}