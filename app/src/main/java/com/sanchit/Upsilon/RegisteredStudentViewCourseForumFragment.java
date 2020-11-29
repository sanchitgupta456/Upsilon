package com.sanchit.Upsilon;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanchit.Upsilon.ForumData.MessageAdapter;
import com.sanchit.Upsilon.ForumData.Messages;
import com.sanchit.Upsilon.courseData.Course;
import com.sanchit.Upsilon.courseData.CourseReview;
import com.sanchit.Upsilon.courseData.CourseReviewAdapter;

import org.bson.Document;
import org.bson.types.BasicBSONList;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

import static android.app.Activity.RESULT_OK;

public class RegisteredStudentViewCourseForumFragment extends Fragment {


    private String messageRecieverId , messageReceiverName,messageReceiverImage,messageSenderId,doubtId;
    private TextView userName ,userLastSeen;
    private CircleImageView userImage;
    private String picturePath;

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
    private ProgressDialog loadingBar;
    private ProgressBar progressBar;

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
        SendFilesButton = (ImageButton) view.findViewById(R.id.send_files_btn1);
        loadingBar = new ProgressDialog(getActivity());
        progressBar = (ProgressBar) view.findViewById(R.id.forum_progress_bar);

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
                progressBar.setVisibility(View.VISIBLE);
                String messageText = MessageInputText.getText().toString();
                MessageInputText.setText("");
                if(TextUtils.isEmpty(messageText))
                {
                    Toast.makeText(getActivity(),"Enter your message",Toast.LENGTH_SHORT).show();
                }
                else {
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
                    messageList1.add(messages);
                    messageAdapter.notifyDataSetChanged();

                    sendMessage(messages);
                }
            }
        });

        SendFilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[]=new CharSequence[]
                        {
                                "Images",
                                "PDF Files",
                                "MS Word Files"
                        };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select the file");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0)
                        {
                            checker="image";
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 438);
                        }
                        if(i==1)
                        {
                            checker="pdf";
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(intent.createChooser(intent,"Select PDF file"),438);
                        }
                        if(i==2)
                        {
                            checker="docx";
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/msword");
                            startActivityForResult(intent.createChooser(intent,"Select MS Word file"),438);
                        }
                    }
                });
                builder.show();
            }
        });

        //getMessages();


        return view;
    }

    private void sendMessage(Messages messages) {

            sendMessageButton.setVisibility(View.INVISIBLE);
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
                                    progressBar.setVisibility(View.INVISIBLE);
                                    getMessages();
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
                                progressBar.setVisibility(View.INVISIBLE);
                                getMessages();
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
                            progressBar.setVisibility(View.INVISIBLE);
                            getMessages();
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

    private void getMessages() {

        progressBar.setVisibility(View.VISIBLE);
        if(messageList1!=null)
        {
            messageList1.clear();
        }

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
                            if (counter == messageList.size()-1)
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                sendMessageButton.setVisibility(View.VISIBLE);
                            }
                        }
                        userMessageList.scrollToPosition(messageList.size()-1);
                    }
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==438 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            loadingBar.setTitle("Sending File");
            loadingBar.setMessage("Please wait,we are sending that file...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            fileUri = data.getData();
            if(!checker.equals("image"))
            {
                Messages messages = new Messages();
                messages.setFrom(user.getId());
                messages.setType("pdf");
                //messages.setMessage(messageText);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM  dd, yyyy");
                saveCurrentDate = currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                saveCurrentTime = currentTime.format(calendar.getTime());

                messages.setDate(saveCurrentDate);
                messages.setTime(saveCurrentTime);
                //messageList1.add(messages);
                //messageAdapter.notifyDataSetChanged();
                //String picturePath;
                /*String[] filePathColumn = { MediaStore.Files.FileColumns.DATA };
                Cursor cursor = getActivity().getContentResolver().query(fileUri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();*/

                /*String[] proj = { MediaStore.MediaColumns.DATA };
                Cursor cursor = getActivity().getContentResolver().query(fileUri, proj, null, null, null);
                if (cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    picturePath = cursor.getString(column_index);
                }
                cursor.close();*/
                File myFile = new File(fileUri.getPath());
                picturePath = myFile.getAbsolutePath();
                Log.v("Path",picturePath);
                uploadGiven1(messages);
            }
            else if(checker.equals("image"))
            {
                Messages messages = new Messages();
                messages.setFrom(user.getId());
                messages.setType("image");
                //messages.setMessage(messageText);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM  dd, yyyy");
                saveCurrentDate = currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                saveCurrentTime = currentTime.format(calendar.getTime());

                messages.setDate(saveCurrentDate);
                messages.setTime(saveCurrentTime);
                //messageList1.add(messages);
                //messageAdapter.notifyDataSetChanged();
                //String picturePath;
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getActivity().getContentResolver().query(fileUri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
                uploadGiven(messages);
                // String picturePath contains the path of selected Image

            }
            else
            {
                loadingBar.dismiss();
                Toast.makeText(getActivity(),"Nothing selected",Toast.LENGTH_SHORT).show();
            }
        }
    }

    void uploadGiven(Messages messages){
                String requestId = MediaManager.get().upload(picturePath)
                        .unsigned("preset1")
                        .option("folder", "Upsilon/".concat(course.getCourseId()).concat("/forum/"))
                        .option("public_id", "forumImage"+user.getId()+System.currentTimeMillis())
                        .callback(new UploadCallback() {
                            @Override
                            public void onStart(String requestId) {
                            }

                            @Override
                            public void onProgress(String requestId, long bytes, long totalBytes) {

                            }

                            @Override
                            public void onSuccess(String requestId, Map resultData) {
                                messages.setMessage(resultData.get("url").toString());
                                loadingBar.dismiss();
                                sendMessage(messages);
                            }

                            @Override
                            public void onError(String requestId, ErrorInfo error) {
                                loadingBar.dismiss();
                                Snackbar.make(getView(),"Error"+error,Snackbar.LENGTH_LONG).show();
                            }

                            @Override
                            public void onReschedule(String requestId, ErrorInfo error) {

                            }
                        })
                        .dispatch();
            }

    void uploadGiven1(Messages messages){
        String requestId = MediaManager.get().upload(fileUri)
                .unsigned("preset1")
                .option("resource_type","auto" +
                        "")
                .option("folder", "Upsilon/".concat(course.getCourseId()).concat("/forum/"))
                .option("public_id", "forumImage"+user.getId()+System.currentTimeMillis())
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        messages.setMessage(resultData.get("url").toString());
                        loadingBar.dismiss();
                        sendMessage(messages);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        loadingBar.dismiss();
                        Snackbar.make(getView(),"Error"+error,Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                })
                .dispatch();
    }

}

