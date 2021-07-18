package com.sanchit.Upsilon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.textfield.TextInputEditText;

import org.bson.Document;

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


public class UserDataSetupFragment2 extends Fragment {

    String appID = "upsilon-ityvn";
    TextInputEditText Name;
    String name;
    int flag=0;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    CircleImageView profilepic;
    String picturePath = null;
    String defaultUrl = "https://res.cloudinary.com/upsilon175/image/upload/v1606421188/Upsilon/blankPP_phfegu.png";
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int WRITE_PERMISSION = 0x01;
    UserDataViewModel viewModel;

    public UserDataSetupFragment2() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_data_setup2, container, false);
        requestWritePermission();

        Name = (TextInputEditText) view.findViewById(R.id.userNameHolder);
        profilepic = (CircleImageView) view.findViewById(R.id.profilePhoto);
        viewModel = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);*/
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        TextWatcher textWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                flag = 1;
                name = s.toString();
                if (name.length() == 0){
//                    Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
//                    Name.startAnimation(shake);
//                    Name.setText("");
//                    Name.setError("Please Enter a Name!");
//                    Name.requestFocus();
//                    return;
                    name = "User";
                }
                viewModel.setName(name);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                flag = 1;
                name = s.toString();
                if (name.length() == 0){
//                    Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
//                    Name.startAnimation(shake);
//                    Name.setText("");
//                    Name.setError("Please Enter a Name!");
//                    Name.requestFocus();
//                    return;
                    name = "User";
                }
                viewModel.setName(name);
            }
        };
        Name.addTextChangedListener(textWatcher);

        /*
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Name.getText().toString();
                if (name.length() == 0){
                    Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                    Name.startAnimation(shake);
                    Name.setText("");
                    Name.setError("Please Enter a Name!");
                    Name.requestFocus();
                    return;
                }
                if (picturePath == null) {
                    uploadDefault();
                } else {
                    uploadGiven();
                }


            }
        });*/
        return view;
    }

    void uploadGiven(){
        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
        Document queryFilter  = new Document("userid",user.getId());

        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

        findTask.getAsync(task -> {
            if(task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                Document result = results.next();
                final int[] counter = {result.getInteger("profilePicCounter")};
                String requestId = MediaManager.get().upload(picturePath)
                        .unsigned("preset1")
                        .option("resource_type", "image")
                        .option("folder", "Upsilon/".concat(user.getId()).concat("/"))
                        .option("public_id", "profilePic" + counter[0])
                        .callback(new UploadCallback() {
                            @Override
                            public void onStart(String requestId) {
                            }

                            @Override
                            public void onProgress(String requestId, long bytes, long totalBytes) {

                            }

                            @Override
                            public void onSuccess(String requestId, Map resultData) {

                                Log.v("User", resultData.toString());
                                Log.v("User", requestId);
                                counter[0]++;
                                name = Name.getText().toString();

                                //Blank query to find every single course in db
                                //TODO: Modify query to look for user preferred course IDs
                                Document queryFilter = new Document("userid", user.getId());

                                RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                                findTask.getAsync(task -> {
                                    if (task.isSuccess()) {
                                        MongoCursor<Document> results = task.get();
                                        if (!results.hasNext()) {
                                            mongoCollection.insertOne(
                                                    new Document("userid", user.getId()).append("profilePicCounter", 0).append("favoriteColor", "pink").append("profilePicUrl", resultData.get("url").toString()))
                                                    .getAsync(result -> {
                                                        if (result.isSuccess()) {
                                                            Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                                    + result.get().getInsertedId());
                                                            Intent intent = new Intent(getContext(), UserDataSetupActivity3.class);
                                                            startActivity(intent);
                                                        } else {
                                                            Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                                        }
                                                    });
                                        } else {
                                            Document userdata = results.next();
                                            userdata.append("name", name);
                                            userdata.append("profilePicCounter", counter[0]);
                                            userdata.append("profilePicUrl", resultData.get("url").toString());

                                            mongoCollection.updateOne(
                                                    new Document("userid", user.getId()), (userdata))
                                                    .getAsync(result -> {
                                                        if (result.isSuccess()) {
                                                            Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                                    + result.get().getModifiedCount());
                                                            Intent intent = new Intent(getContext(), UserDataSetupActivity3.class);
                                                            startActivity(intent);
                                                        } else {
                                                            Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                                        }
                                                    });
                                        }
                                        while (results.hasNext()) {
                                            //Log.v("EXAMPLE", results.next().toString());
                                            Document currentDoc = results.next();
                                            Log.v("User", currentDoc.getString("userid"));
                                        }
                                    } else {
                                        Log.v("User", "Failed to complete search");
                                    }
                                });
                            }

                            @Override
                            public void onError(String requestId, ErrorInfo error) {

                            }

                            @Override
                            public void onReschedule(String requestId, ErrorInfo error) {

                            }
                        })
                        .dispatch();
            }
        });
    }

    void uploadDefault(){
        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
        Document queryFilter  = new Document("userid",user.getId());

        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

        findTask.getAsync(task -> {
            if(task.isSuccess()) {
                MongoCursor<Document> results = task.get();

                if (!results.hasNext()) {
                    mongoCollection.insertOne(
                            new Document("userid", user.getId()).append("profilePicCounter", 0).append("favoriteColor", "pink").append("profilePicUrl", defaultUrl))
                            .getAsync(result -> {
                                if (result.isSuccess()) {
                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                            + result.get().getInsertedId());
                                    Intent intent = new Intent(getContext(), UserDataSetupActivity3.class);
                                    startActivity(intent);
                                } else {
                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                }
                            });
                } else {
                    Document userdata = results.next();
                    userdata.append("name", name);
                    userdata.append("profilePicCounter", 0);
                    userdata.append("profilePicUrl", defaultUrl);

                    mongoCollection.updateOne(
                            new Document("userid", user.getId()), (userdata))
                            .getAsync(result -> {
                                if (result.isSuccess()) {
                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                            + result.get().getModifiedCount());
                                    Intent intent = new Intent(getContext(), UserDataSetupActivity3.class);
                                    startActivity(intent);
                                } else {
                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                }
                            });
                }
            }
        });
    }

    // To handle when an image is selected from the browser, add the following to your Activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            /*if (requestCode == 1) {

                // currImageURI is the global variable I'm using to hold the content:// URI of the image
                Uri currImageURI = data.getData();
                Bundle bundle = data.getExtras();
                profilepic.setImageURI(currImageURI);
                Profilepicpath = getRealPathFromURI(currImageURI);

            }*/
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                viewModel.setPicturePath(picturePath);
                cursor.close();
                // String picturePath contains the path of selected Image
                ImageView imageView = (ImageView) getView().findViewById(R.id.profilePhoto);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == WRITE_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Log.d("Hello", "Write Permission Failed");
                Toast.makeText(getContext(), "You must allow permission write external storage to your mobile device.", Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
    }

    private void requestWritePermission(){
        if(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
        }
    }
    // And to convert the image URI to the direct file system path of the image file
    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery( contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
}