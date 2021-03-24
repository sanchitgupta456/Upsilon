package com.sanchit.Upsilon;

import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.classData.ScheduledClass;
import com.sanchit.Upsilon.courseData.Course;

import org.bson.Document;
import org.bson.types.BasicBSONList;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import io.realm.mongodb.mongo.options.UpdateOptions;

import static io.realm.Realm.getApplicationContext;

public class TeacherViewCourseActivityNotificationsFragment extends Fragment {
    RecyclerView recyclerView;
    View dialogView;
    AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teachers_viewof_course_notifications,null);
        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.btnAddNotification);
        dialogView = View.inflate(getActivity(), R.layout.layout_add_notification, null);
        EditText et = dialogView.findViewById(R.id.note_text);
        alertDialog = new AlertDialog.Builder(requireActivity()).create();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });

        dialogView.findViewById(R.id.btn_post).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                //post the note in the EditText et : dialogView.findViewById(R.id.note_text)
            }});

        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                //cancel the dialog
            }});
        return view;
    }

    void createDialog() {
        //show the dialog
    }
}
