package com.sanchit.Upsilon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.classData.ScheduledClass;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseFinal;
import com.sanchit.Upsilon.notificationData.Notification;
import com.sanchit.Upsilon.notificationData.NotificationAdapter;

import org.bson.Document;
import org.bson.types.BasicBSONList;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.realm.mongodb.mongo.options.UpdateOptions;

import static io.realm.Realm.getApplicationContext;

public class TeacherViewCourseActivityNotificationsFragment extends Fragment implements NotificationAdapter.ItemClickListener {
    RecyclerView recyclerView;
    ArrayList<Notification> notifications = new ArrayList<>();
    NotificationAdapter adapter;
    View dialogView;
    AlertDialog alertDialog;
    private RequestQueue queue;
    private String API ;
    Notification notification;
    CourseFinal course;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teachers_viewof_course_notifications,null);
        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.btnAddNotification);
        recyclerView = (RecyclerView) view.findViewById(R.id.listNotifications);
        queue = Volley.newRequestQueue(getApplicationContext());
        API = ((Upsilon)getActivity().getApplication()).getAPI();
        course = (CourseFinal) getArguments().get("Course");

        //TODO: get the notifications
        getNotifications();

        adapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, true));
        adapter.notifyDataSetChanged();
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
                notification = new Notification();
                notification.setAnnouncement(et.getText().toString());
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM  dd, yyyy");
                String saveCurrentDate = currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                String saveCurrentTime = currentTime.format(calendar.getTime());
                notification.setDate(saveCurrentDate);
                notification.setTime(saveCurrentTime);

                notifications.add(notification);
                adapter.notifyDataSetChanged();

                //TODO: post the notification
                postNotification();

                alertDialog.dismiss();
            }});

        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                //cancel the dialog
                alertDialog.dismiss();
            }});
        return view;
    }

    void createDialog() {
        //show the dialog
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    public void postNotification() {
        //TODO: post the notification
        try {
            JSONObject jsonObject = new JSONObject();
            Gson gson = new Gson();
            Log.v("Notification", String.valueOf(gson.toJson(notification)));
            jsonObject.put("notification",gson.toJson(notification));
            jsonObject.put("courseId",course.get_id());
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/postNotification",jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("PostingNotification", response.toString());
                            Toast.makeText(getApplicationContext(),"Notification Sent",Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("ErrorPostingNotification", error.toString());
                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", ((Upsilon)getActivity().getApplication()).getToken());
                    return params;
                }
            };
            queue.add(jsonRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getNotifications() {
        //TODO: get the notifications
        notifications = course.getNotifications();
        /* test code start */
//        notifications.add(new Notification("14-12-2021", "07 00 AM", "Greetings! We will have pur first class today. Best."));
//        notifications.add(new Notification("14-12-2021", "07 00 AM", "Greetings! We will have pur first class today. Best."));
//        notifications.add(new Notification("14-12-2021", "07 00 AM", "Greetings! We will have pur first class today. Best."));
//        notifications.add(new Notification("14-12-2021", "07 00 AM", "Greetings! We will have pur first class today. Best."));
//        notifications.add(new Notification("14-12-2021", "07 00 AM", "Greetings! We will have pur first class today. This is a long message"
//        +"This is a really long message" +
//                "Sure it is a long message" +
//                "Of course it is a long message" +
//                "Now this is getting longer and longer" +
//                "Yep, too long" +
//                "\n" +
//                "\tIdent your message to improve its look" +
//                "\n\nBest,\nUpsilon"));
        /* test code ends */
    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        //TODO: to be completed
        return false;
    }
}
