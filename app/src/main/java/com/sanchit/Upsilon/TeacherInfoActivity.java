package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.nostra13.universalimageloader.utils.L;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseFinal;
import com.sanchit.Upsilon.courseData.CoursesAdapter1;
import com.sanchit.Upsilon.paymentLog.PaymentLog;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

import static io.realm.Realm.getApplicationContext;

public class TeacherInfoActivity extends AppCompatActivity {
    //add more variables as and when required

    CircleImageView imageView;
    TextView name, speciality, qualifications;
    RecyclerView recyclerView;
    ArrayList<CourseFinal> list = new ArrayList<>();
    CoursesAdapter1 adapter;
    LinearLayoutManager manager;
    String tutorId;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection,mongoCollection1;
    App app;
    String appID = "upsilon-ityvn";
    User user;
    Gson gson;
    GsonBuilder gsonBuilder;
    JSONArray specialities,qualificationsArray,experience;
    private RequestQueue queue;
    private String API ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
        queue = Volley.newRequestQueue(getApplicationContext());
        API = ((Upsilon)getApplication()).getAPI();
        //getSupportActionBar().setTitle("View Teacher Details");
        imageView = (CircleImageView) findViewById(R.id.img_tutor);
        name = (TextView) findViewById(R.id.text_tutor_name);
        speciality = (TextView) findViewById(R.id.text_tutor_speciality);
        qualifications = (TextView) findViewById(R.id.text_tutor_qualifications);
        recyclerView = (RecyclerView) findViewById(R.id.list_courses_by_tutor);
        Intent intent = getIntent();
        tutorId = (String) intent.getExtras().get("tutorId");
//        app = new App(new AppConfiguration.Builder(appID).build());
//        user = app.currentUser();
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("Upsilon");
//        mongoCollection  = mongoDatabase.getCollection("UserData");
//        mongoCollection1  = mongoDatabase.getCollection("CourseData");
        setupRecycler();
        getTutorData();
    }
    public void getTutorData(){
        //TODO: Get data here and set up the text fields and course array list

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tutorId",tutorId);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, API+"/tutorInfo",jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.v("tutorInfoResponse",response.toString());
                                    JSONArray jsonArray = (JSONArray) response.get("courses");
                                    Log.v("array",String.valueOf(jsonArray));
                                    for(int i=0;i<jsonArray.length();i++)
                                    {
                                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                        Gson gson= new Gson();
                                        CourseFinal course = gson.fromJson(jsonObject.toString(),CourseFinal.class);
                                        list.add(course);
                                        adapter.notifyDataSetChanged();
                                        Log.v("course",String.valueOf(course.getCourseReviews()));
                                    }
                                                    try {
                        Glide.with(getApplicationContext()).load(response.get("image").toString()).into(imageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                                                    name.setText(response.getString("name"));
                    specialities = (JSONArray) response.get("specialities");
                                Log.v("specialities",String.valueOf(specialities));
                    if(specialities==null)
                    {
                        speciality.setText("No specialisations declosed");
                    }
                    else
                    {

                        speciality.setText("Specializes in ");
                        for(int i=0;i<specialities.length();i++)
                        {
                            Log.v("speciality",specialities.getString(i));
                            speciality.append(specialities.get(i)+" ");
                        }
                    }


                    qualificationsArray = (JSONArray) response.get("qualifications");
                    if(qualificationsArray==null)
                    {
                        qualifications.setText("No qualifications disclosed");
                    }
                    else
                    {
//                        qualifications.setText("Qualifications in ");
                        for(int i=0;i<qualificationsArray.length();i++)
                        {
                            qualifications.append(qualificationsArray.get(i)+" \n");
                        }
                    }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("ErrorFetchingTutorNameLoc", error.toString());
                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", ((Upsilon)getApplication()).getToken());
                    return params;
                }
            };
            queue.add(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Document queryFilter = new Document("userid",tutorId);
//        Document queryFilter1 = new Document("tutorId",tutorId);
//
//        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
//        RealmResultTask<MongoCursor<Document>> findTask1 = mongoCollection1.find(queryFilter1).iterator();
//
//        findTask.getAsync(task->{
//            if(task.isSuccess())
//            {
//                MongoCursor<Document> results = task.get();
//
//                while (results.hasNext())
//                {
//                    Document teacherInfo = results.next();
//                    try {
//                        Glide.with(getApplicationContext()).load(teacherInfo.get("profilePicUrl")).into(imageView);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    name.setText(teacherInfo.getString("name"));
//                    specialities = (ArrayList<String>) teacherInfo.get("specialities");
//                    if(specialities==null)
//                    {
//                        speciality.setText("No specialisations declosed");
//                    }
//                    else
//                    {
//                        speciality.setText("Specializes in ");
//                        for(int i=0;i<specialities.size();i++)
//                        {
//                            speciality.append(specialities.get(i)+" ");
//                        }
//                    }
//
//
//                    qualificationsArray = (ArrayList<String>) teacherInfo.get("qualifications");
//                    if(qualificationsArray==null)
//                    {
//                        qualifications.setText("No qualifications disclosed");
//                    }
//                    else
//                    {
//                        qualifications.setText("Qualifications in ");
//                        for(int i=0;i<qualificationsArray.size();i++)
//                        {
//                            qualifications.append(qualificationsArray.get(i)+" ");
//                        }
//                    }
//
//                }
//            }
//            else
//            {
//                Log.v("TeacherInfoError",task.getError().toString());
//            }
//        });
//
//        findTask1.getAsync(task1->{
//            if(task1.isSuccess())
//            {
//                MongoCursor<Document> results = task1.get();
//                while (results.hasNext())
//                {
//                    try {
//                        Document document = results.next();
//                        document.toJson();
//                        gsonBuilder = new GsonBuilder();
//                        gson = gsonBuilder.create();
//                        CourseFinal course = gson.fromJson(document.toJson(),CourseFinal.class);
//                        list.add(course);
//                        Log.v("course",String.valueOf(course));
//                        adapter.notifyDataSetChanged();
//                    } catch (JsonSyntaxException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            else
//            {
//                Log.v("TeacherInfoError",task1.getError().toString());
//            }
//        });


    }
    public void setupRecycler(){
        if(list != null){
            adapter = new CoursesAdapter1(list,((Upsilon)getApplication()).getAPI() , ((Upsilon)getApplication()).getToken() , ((Upsilon) getApplication()).getUser());
            manager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(manager);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}