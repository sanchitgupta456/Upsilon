package com.sanchit.Upsilon;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.ForumData.MessageAdapter;
import com.sanchit.Upsilon.ForumData.Messages;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseReview;
import com.sanchit.Upsilon.courseData.CourseReviewAdapter;

import org.bson.Document;
import org.bson.types.BasicBSONList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class RegisteredStudentViewCourseForumFragment extends Fragment {


    private String messageRecieverId , messageReceiverName,messageReceiverImage,messageSenderId,doubtId;
    private TextView userName ,userLastSeen;
    private CircleImageView userImage;

    private Toolbar ChatToolBar;
    private ImageButton SendMessageButton,SendFilesButton;
    private EditText MessageInputText;

    private Uri fileUri;
    private Button button1,button2;
    private LinearLayoutManager linearLayoutManager1;
    private MessageAdapter messageAdapter;
    private RecyclerView userMessageList;

    private String saveCurrentTime,saveCurrentDate;
    private String checker="",myUrl="";
    private Course course;

    String appID = "upsilon-ityvn";
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    private User user;
    private ImageButton sendMessageButton;
    private Gson gson;
    private GsonBuilder gsonBuilder;
    ArrayList messageList;
    ArrayList<Messages> messageList1 = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_active_course_forum,null);

        MessageInputText = (EditText)view.findViewById(R.id.input_message1);
        userMessageList = (RecyclerView) view.findViewById(R.id.private_messages_list_of_users1);



        //userMessageList.setAdapter(messageAdapter);
        //messageAdapter = new MessageAdapter(messageList1);
        //userMessageList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        //userMessageList.setItemAnimator(new DefaultItemAnimator());

        linearLayoutManager1 = new LinearLayoutManager(getActivity());
        messageAdapter = new MessageAdapter(messageList1);
        userMessageList.setLayoutManager(linearLayoutManager1);
        userMessageList.setAdapter(messageAdapter);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");

        course = (Course) getArguments().get("Course");

        sendMessageButton = (ImageButton) view.findViewById(R.id.send_message_btn1);

        getMessages();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        //getMessages();


        return view;
    }

    private void sendMessage() {
        String messageText = MessageInputText.getText().toString();
        if(TextUtils.isEmpty(messageText))
        {
            Toast.makeText(getActivity(),"First write your message ..",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Messages messages = new Messages();
            messages.setFrom(user.getId());
            messages.setType("text");
            messages.setMessage(messageText);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM  dd, yyyy");
            saveCurrentDate = currentDate.format(calendar.getTime());
            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
            saveCurrentTime = currentTime.format(calendar.getTime());

            messages.setDate(saveCurrentDate);
            messages.setTime(saveCurrentTime);

            mongoClient = user.getMongoClient("mongodb-atlas");
            mongoDatabase = mongoClient.getDatabase("Upsilon");

            MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("ForumData");
            MongoCollection<Document> mongoCollection1  = mongoDatabase.getCollection("ForumData");

            //Blank query to find every single course in db
            //TODO: Modify query to look for user preferred course IDs
            Document queryFilter  = new Document("courseId",course.getCourseId());




            RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

            findTask.getAsync(task -> {
                if (task.isSuccess()) {
                    Log.v("Example","Starting");
                    MongoCursor<Document> results = task.get();
                    Log.v("Example",results.toString());
                    if(results.hasNext())
                    {
                        while (results.hasNext()) {
                            Log.v("Example","Found");

                            //Log.v("EXAMPLE", results.next().toString());
                            Document currentDoc = results.next();
                            messageList = (ArrayList) currentDoc.get("Messages");

                            if(messageList==null)
                            {
                                messageList = new ArrayList();
                                //Document test = new Document().append("review","This is a test review").append("reviewRating",2.75).append("reviewAuthorId",user.getId());
                            }
                            Document document = new Document();
                            gsonBuilder = new GsonBuilder();
                            gson = gsonBuilder.create();

                            String object = gson.toJson(messages,Messages.class);
                            document = Document.parse(object);
                            messageList.add(document);
                            currentDoc.remove("Messages");
                            currentDoc.append("Messages",messageList);
                            mongoCollection.updateOne(new Document("courseId",course.getCourseId()),currentDoc).getAsync(result -> {
                                if(result.isSuccess())
                                {
                                    Log.v("Message","Message Sent");
                                }
                                else
                                {
                                    Log.v("Message","Error"+result.getError());
                                }
                            });

                        }
                    }
                    else
                    {
                        messageList = new ArrayList();
                        Document courseDoc = new Document();
                        gsonBuilder = new GsonBuilder();
                        gson = gsonBuilder.create();

                        String object = gson.toJson(messages,Messages.class);
                        Document document = Document.parse(object);
                        messageList.add(document);

                        Document document1 = new Document("courseId",course.getCourseId()).append("Messages",messageList);
                        mongoCollection.insertOne(document1).getAsync(result -> {
                            if(result.isSuccess())
                            {
                                Log.v("Message","Message Sent");
                            }
                            else
                            {
                                Log.v("Message","Error"+result.getError());
                            }
                        });
                    }
                } else {
                    messageList = new ArrayList();
                    Document courseDoc = new Document();
                    gsonBuilder = new GsonBuilder();
                    gson = gsonBuilder.create();

                    String object = gson.toJson(messages,Messages.class);
                    Document document = Document.parse(object);
                    messageList.add(document);

                    Document document1 = new Document("courseId",course.getCourseId()).append("Messages",messageList);
                    mongoCollection.insertOne(document1).getAsync(result -> {
                        if(result.isSuccess())
                        {
                            Log.v("Message","Message Sent");
                        }
                        else
                        {
                            Log.v("Message","Error"+result.getError());
                        }
                    });
                }
            });
        }
    }

    private void getMessages() {

        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");

        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("ForumData");

        //Blank query to find every single course in db
        //TODO: Modify query to look for user preferred course IDs
        Document queryFilter  = new Document("courseId",course.getCourseId());

        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                Log.v("Example","Starting");
                MongoCursor<Document> results = task.get();
                Log.v("Example",results.toString());
                if(results.hasNext())
                {
                    while (results.hasNext()) {
                        Log.v("Example","Found");
                        //Log.v("EXAMPLE", results.next().toString());
                        Document currentDoc = results.next();
                        messageList = (ArrayList) currentDoc.get("Messages");

                        //BasicBSONList messages = (BasicBSONList) currentDoc.get("Messages");
                        for(int counter=0;counter<messageList.size();counter++)
                        {
                            Document message = (Document) messageList.get(counter);
                            Messages message2 = new Messages(message.get("from").toString(),message.get("message").toString(),message.get("type").toString(),message.get("time").toString(),message.get("date").toString());
                            messageList1.add(message2);
                            messageAdapter.notifyDataSetChanged();
                            Log.v("test", (String) message.get("message"));
                        }
                    }
                }
            }
        });


    }
}
