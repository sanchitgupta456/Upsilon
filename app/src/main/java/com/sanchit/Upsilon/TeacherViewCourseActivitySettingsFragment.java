package com.sanchit.Upsilon;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.courseData.Course;
import com.squareup.picasso.Picasso;

import org.bson.BsonDocument;
import org.bson.Document;
import org.cloudinary.json.JSONObject;
import org.cloudinary.json.JSONString;

import java.util.Calendar;
import java.util.Locale;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

import static android.content.ContentValues.TAG;
import static io.realm.Realm.getApplicationContext;

public class TeacherViewCourseActivitySettingsFragment extends Fragment {

    //private TextView textRatings, textEnrolled, textStartDate, textPercentCompleted;
    private SwitchMaterial enableRegistrations;
    //private SwitchMaterial isCourseFree;
    //private RadioButton online, offline;
    private TextInputEditText etCourseName, etDescription, etDuration, etFee;
    //private EditText etLocation;
    private TextView textIsOnline;
    private Button editLocation;
    private ImageView courseImage;
    private Button update;
    private Boolean name;
    private Boolean des;
    private Boolean dur;
    private Boolean fee;
    Button saveButton;

    App app;
    String appID = "upsilon-ityvn";

    private Course course;

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    private Gson gson;
    private GsonBuilder gsonBuilder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");
        View view = inflater.inflate(R.layout.teachers_viewof_course_settings, container, false);
        course = (Course) getArguments().get("Course");
        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();

        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("CourseData");

        courseImage = (ImageView) view.findViewById(R.id.imgCourseImage);
        //textRatings = (TextView) view.findViewById(R.id.teacher_view_course_rating);
        //textEnrolled = (TextView) view.findViewById(R.id.teacher_view_course_enrolled);
        //textStartDate = (TextView) view.findViewById(R.id.teacher_view_course_start_date);
        //textPercentCompleted = (TextView) view.findViewById(R.id.teacher_view_course_completed);

        enableRegistrations = (SwitchMaterial) view.findViewById(R.id.toggleRegistrationAcceptance);
        //isCourseFree = (SwitchMaterial) view.findViewById(R.id.isCourseFree);

        //online = (RadioButton) view.findViewById(R.id.online);
        //offline = (RadioButton) view.findViewById(R.id.offline);
        update = (Button) view.findViewById(R.id.updateChange);
        //update.setVisibility(View.GONE);

        etCourseName = (TextInputEditText) view.findViewById(R.id.editTextCourseName);
        etDescription = (TextInputEditText) view.findViewById(R.id.editTextCourseDescription);
        etDuration = (TextInputEditText) view.findViewById(R.id.editTextCourseDuration);
        //etLocation = (EditText) view.findViewById(R.id.editTextCourseLocation);
        editLocation = (Button) view.findViewById(R.id.btn_loc);
        textIsOnline = (TextView) view.findViewById(R.id.textIsOnline);
        etFee = (TextInputEditText) view.findViewById(R.id.editTextCourseFees);

        Picasso.with(getApplicationContext()).load(course.getCourseImage()).into(courseImage);
        //textRatings.setText("Rating "+course.getCourseRating() + "/5");
        //textEnrolled.setText(course.getNumberOfStudentsEnrolled() + " students have enrolled in this course");
        //textStartDate.setText("Course not started yet");
        //textPercentCompleted.setText("0% completed");

        //TODO: enableRegistrations
        etCourseName.setText(course.getCourseName());
        etDescription.setText(course.getCourseDescription());
        etDuration.setText(""+course.getCourseDuration());

        if(course.getCourseMode().equals("Online")) {
            textIsOnline.setVisibility(View.VISIBLE);
            editLocation.setVisibility(View.GONE);
        } else {
            textIsOnline.setVisibility(View.GONE);
            editLocation.setVisibility(View.VISIBLE);
            //TODO: Init course location viewing/editing.

        }
        if (course.getCourseFees()==0){
            //isCourseFree.setChecked(true);
            etFee.setText("Rs. 0");
        } else {
            //isCourseFree.setChecked(false);
            etFee.setText("Rs. " + course.getCourseFees());
        }

        name = des = dur = fee = false;

        etCourseName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name=false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name=true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                name=true;
            }
        });
        etCourseName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                des=false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                des=true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                des=true;
            }
        });
        etDescription.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etDuration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dur=false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dur=true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                dur=true;
            }
        });
        etDuration.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //etLocation.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fee=false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fee=true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                fee=true;
            }
        });
        etFee.setImeOptions(EditorInfo.IME_ACTION_DONE);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name){
                    course.setCourseName(etCourseName.getText().toString());
                }
                if(des){
                    course.setCourseDescription(etDescription.getText().toString());
                }
                if(dur){
                    course.setCourseDuration(Integer.parseInt(etDuration.getText().toString()));
                }
                if(fee){
                    course.setCourseFees(Integer.parseInt(etFee.getText().toString()));
                }
                //TODO: Update in backend
                course.setRegistrationsOpen(enableRegistrations.isChecked());
                Document queryFilter = new Document().append("courseId",course.getCourseId());
                gsonBuilder = new GsonBuilder();
                gson = gsonBuilder.create();
                BsonDocument coursedoc = BsonDocument.parse(gson.toJson(course));
                mongoCollection.updateOne(queryFilter,coursedoc).getAsync(result -> {
                    if(result.isSuccess())
                    {
                        Log.v("CourseUpdate","Updated Successfully");
                    }
                    else
                    {
                        Log.v("CourseUpdate",result.getError().toString());
                    }
                });
            }
        });


        return view;
    }
}
