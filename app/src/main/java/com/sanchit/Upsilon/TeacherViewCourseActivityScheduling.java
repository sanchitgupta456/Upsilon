
package com.sanchit.Upsilon;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.classData.ScheduleAdapter;
import com.sanchit.Upsilon.classData.ScheduledClass;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseFinal;
import com.sanchit.Upsilon.userData.User;


import org.bson.Document;
import org.bson.types.BasicBSONList;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.options.UpdateOptions;

import static android.content.ContentValues.TAG;
import static io.realm.Realm.getApplicationContext;

public class TeacherViewCourseActivityScheduling extends Fragment implements ScheduleAdapter.ItemClickListener {
    String appID = "upsilon-ityvn";
    int year,month,day;
    private RecyclerView recyclerView;
    CourseFinal course;
    private ArrayList<ScheduledClass> classes = new ArrayList<>();
    View dialogView;
    AlertDialog alertDialog;
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    private Gson gson;
    private GsonBuilder gsonBuilder;
    private ScheduleAdapter adapter;
    private RequestQueue queue;
    private String API ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");
        View view = inflater.inflate(R.layout.teachers_viewof_course_schedule, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.classScheduleTeacher);

        dialogView = View.inflate(getActivity(), R.layout.date_time_picker, null);
        TimePicker tp = dialogView.findViewById(R.id.time_picker);
        tp.setIs24HourView(true);
        queue = Volley.newRequestQueue(getApplicationContext());
        API = ((Upsilon)getActivity().getApplication()).getAPI();

        //alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getApplicationContext())).create();
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        course = (CourseFinal) getArguments().get("Course");
        FloatingActionButton btnAdd = (FloatingActionButton) view.findViewById(R.id.btnAddClass);
//        app = new App(new AppConfiguration.Builder(appID)
//                .build());
//        User user = app.currentUser();
//
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("Upsilon");
//        MongoCollection<org.bson.Document> mongoCollection  = mongoDatabase.getCollection("CourseData");
        getClasses();

        adapter.setClickListener(this);

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
                TextInputEditText editText = dialogView.findViewById(R.id.class_name);
                String ClassName = editText.getText().toString();
                timePicker.setIs24HourView(true);

                if(ClassName.isEmpty())
                {
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    editText.startAnimation(shake);
                    editText.setError("Please Enter a Valid Class Name");
                    editText.requestFocus();
                }
                else {
                    Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                            datePicker.getMonth(),
                            datePicker.getDayOfMonth(),
                            timePicker.getHour(),
                            timePicker.getMinute());
                    long time = calendar.getTimeInMillis();
                    Log.v("time", String.valueOf(time));
                    if(time < System.currentTimeMillis())
                    {
                        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                        editText.startAnimation(shake);
                        editText.setError("Cannot schedule a class in past");
                        editText.requestFocus();
                    }
                    else
                    {
                        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(time);
                        String timef = DateFormat.format("hh:mm a", cal).toString();
                        cal.setTimeInMillis(time+3600000);
                        String endtime = DateFormat.format("hh:mm a", cal).toString();

                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("className",ClassName);
                            jsonObject.put("timestamp",time);
                            jsonObject.put("date",String.valueOf(datePicker.getDayOfMonth()));
                            jsonObject.put("month",String.valueOf(monthNames[datePicker.getMonth()]));
                            jsonObject.put("time",timef);
                            jsonObject.put("endtime",endtime);
                            jsonObject.put("courseId",course.get_id());
                            String id = String.valueOf(course.getScheduledClasses().size()+1);
                            jsonObject.put("id",id);
                            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/scheduleClass",jsonObject,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d("FetchingisTeacher", response.toString());
//                                            ((Upsilon)getActivity().getApplication()).fetchProfile();
                                            classes.add(new ScheduledClass(ClassName,time,String.valueOf(datePicker.getDayOfMonth()),String.valueOf(monthNames[datePicker.getMonth()]) ,timef,endtime,id));
                                            adapter.notifyDataSetChanged();
                                            alertDialog.dismiss();
                                            ((TeacherViewCourseActivity)getActivity()).setClasses(classes);
//                                            Intent intent=new Intent();
//                                            intent.putExtra("course",course);
//                                            setResult(2,intent);
//                                            finish();//finishing activity
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @SuppressLint("LongLogTag")
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("ErrorFetchingisTeacher", error.toString());
                                            Toast.makeText(getApplicationContext(),"Failed to Schedule Class. Please try again later",Toast.LENGTH_LONG).show();
                                            alertDialog.dismiss();
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
//                    BasicBSONList scheduledClasses = course.getScheduledClasses();
//                    if(scheduledClasses==null)
//                    {
//                        scheduledClasses=new BasicBSONList();
//                    }
//                    Document scheduledClass = new Document();
//
//                    scheduledClass.append("ClassName",ClassName);
//                    String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
//                    scheduledClass.append("Month",String.valueOf(monthNames[datePicker.getMonth()]));
//                    scheduledClass.append("Date",String.valueOf(datePicker.getDayOfMonth()));
//
//                    Calendar calendar = new GregorianCalendar(datePicker.getYear(),
//                            datePicker.getMonth(),
//                            datePicker.getDayOfMonth(),
//                            timePicker.getHour(),
//                            timePicker.getMinute());
//                    long time = calendar.getTimeInMillis();
//
//                    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//                    cal.setTimeInMillis(time);
//                    String date = DateFormat.format("hh:mm a", cal).toString();
//                    scheduledClass.append("Time",date);
//                    scheduledClasses.add(scheduledClass);
//                    //nextClass.setText(date);
//                    classes.add(new ScheduledClass(ClassName,String.valueOf(datePicker.getDayOfMonth()),String.valueOf(monthNames[datePicker.getMonth()]),date));
//                    adapter.notifyDataSetChanged();
//                    alertDialog.dismiss();
//
//                    UpdateOptions updateOptions = new UpdateOptions();
//                    updateOptions.upsert(true);
//                    course.setScheduledClasses(scheduledClasses);
//                    course.setNextLectureOn(String.valueOf(time));
//                    gsonBuilder = new GsonBuilder();
//                    gson = gsonBuilder.create();
//                    String json = gson.toJson(course, Course.class);
//                    Document document = Document.parse(json);
//                    //document.append("nextLectureOn",String.valueOf(time));
//
//                    mongoCollection.updateOne(new Document("courseId", course.getCourseId()), document).getAsync(result -> {
//                        if (result.isSuccess()) {
//                            Log.v("Schedule", "Class Scheduled");
//                        } else {
//                            Log.v("Schedule", "Error" + result.getError().toString());
//                        }
//                    });
                }
            }});

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddClass();
            }
        });
        return view;
    }
    public void AddClass() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        //DateDialog();
        alertDialog.setView(dialogView);
        alertDialog.show();
        //add a class : to be completed
    }
    public void getClasses() {
//        course = (CourseFinal) getArguments().get("Course");
        Log.v("classes",String.valueOf(course.getScheduledClasses()));
        classes = course.getScheduledClasses();
        //get classes :
//        BasicBSONList scheduledClasses = course.getScheduledClasses();
//        if(scheduledClasses==null)
//        {
//            scheduledClasses=new BasicBSONList();
//        }
//        for(int i=0;i<scheduledClasses.size();i++)
//        {
//            try {
//                Object scheduled = (Object) scheduledClasses.get(i);
//                Log.v("scheduled", String.valueOf(scheduled));
//                LinkedHashMap Class = (LinkedHashMap) scheduled;
//                classes.add(new ScheduledClass(Class.get("ClassName").toString(), Class.get("Date").toString(), Class.get("Month").toString(), Class.get("Time").toString()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        /* this is for test */
//        /* begin test */
//        Log.d(TAG, "getClasses: getting entries");
//        /*classes.add(new ScheduledClass("Getting Started with Python!", "7", "December", "08:00 AM"));
//        classes.add(new ScheduledClass("Knowing Syntax Better!", "8", "December", "09:00 AM"));
//        classes.add(new ScheduledClass("Data Types in Python", "9", "December", "08:00 AM"));
//        classes.add(new ScheduledClass("Lists in Python", "10", "December", "10:00 AM"));
//        classes.add(new ScheduledClass("Functions", "11", "December", "08:00 AM"));
//        classes.add(new ScheduledClass("Object Oriented Programming!", "13", "December", "09:00 AM"));*/

        initRecyclerView();
        /* end test */
    }
    public void initRecyclerView() {
        adapter = new ScheduleAdapter(getContext(), classes);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), manager.getOrientation());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
        Log.d(TAG, "initRecyclerView: successful");
    }

    public void DateDialog(){
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                //nextClass.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                //Date date = new Date()
            }};
        DatePickerDialog dpDialog=new DatePickerDialog(getApplicationContext(), listener, year, month, day);
        dpDialog.show();
    }

    @Override
    public void onItemClick(View view, int position) {
        try {
            Intent intent = new Intent(getContext(), ClassActivityTeacher.class);
            intent.putExtra("ScheduledClass", classes.get(position));
            intent.putExtra("id",course.get_id());
            requireContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onItemClick: Didn't work");
        }
    }
}
