package com.sanchit.Upsilon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.Upsilon.notificationData.Notification;
import com.sanchit.Upsilon.notificationData.NotificationAdapter;

import java.util.ArrayList;

import static io.realm.Realm.getApplicationContext;

public class RegisteredStudentViewCourseNotificationFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Notification> notifications = new ArrayList<>();
    NotificationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teachers_viewof_course_notifications,null);
        recyclerView = (RecyclerView) view.findViewById(R.id.listNotifications);

        //TODO: get the notifications
        getNotifications();

        adapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        adapter.notifyDataSetChanged();

        return view;
    }

    public void getNotifications() {
        //TODO: get the notifications

        /* test code start */
        notifications.add(new Notification("14-12-2021", "07 00 AM", "Greetings! We will have pur first class today. Best."));
        notifications.add(new Notification("14-12-2021", "07 00 AM", "Greetings! We will have pur first class today. Best."));
        notifications.add(new Notification("14-12-2021", "07 00 AM", "Greetings! We will have pur first class today. Best."));
        notifications.add(new Notification("14-12-2021", "07 00 AM", "Greetings! We will have pur first class today. Best."));
        notifications.add(new Notification("14-12-2021", "07 00 AM", "Greetings! We will have pur first class today. This is a long message"
                +"This is a really long message" +
                "Sure it is a long message" +
                "Of course it is a long message" +
                "Now this is getting longer and longer" +
                "Yep, too long" +
                "\n" +
                "\tIdent your message to improve its look" +
                "\n\nBest,\nUpsilon"));
        /* test code ends */
    }
}
