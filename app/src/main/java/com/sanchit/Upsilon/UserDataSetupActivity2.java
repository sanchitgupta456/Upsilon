package com.sanchit.Upsilon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.utils.ObjectUtils;
import com.google.android.gms.common.internal.Constants;

import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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

public class UserDataSetupActivity2 extends AppCompatActivity {

    String appID = "upsilon-ityvn";
    Button nextButton;
    EditText Name;
    String name;
    int flag=0;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    CircleImageView profilepic;
    String picturePath;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int WRITE_PERMISSION = 0x01;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_2);

        requestWritePermission();

        nextButton = (Button) findViewById(R.id.selectLaterNext2);
        Name = (EditText) findViewById(R.id.userNameHolder);
        profilepic = (CircleImageView) findViewById(R.id.profilePhoto);

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

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==1)
                {
                    Map config = new HashMap();
                    config.put("cloud_name", "upsilon175");
                    MediaManager.init(UserDataSetupActivity2.this, config);
                    String requestId = MediaManager.get().upload(picturePath)
                            .unsigned("preset1")
                            .option("resource_type", "image")
                            .option("folder", "Upsilon/".concat(user.getId()).concat("/"))
                            .option("public_id", "profPic")
                            .dispatch();
                    String fileExtension = picturePath.substring(picturePath.lastIndexOf('.'));
                    String serverPath = "Upsilon/".concat(user.getId()).concat("/profPic").concat(fileExtension);
                    String url = MediaManager.get().url().generate(serverPath);
                    Log.v("CLOUDINARY", url);
                    name = Name.getText().toString();
                    mongoClient = user.getMongoClient("mongodb-atlas");
                    mongoDatabase = mongoClient.getDatabase("Upsilon");
                    MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");

                    //Blank query to find every single course in db
                    //TODO: Modify query to look for user preferred course IDs
                    Document queryFilter  = new Document("userid",user.getId());

                    RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                    findTask.getAsync(task -> {
                        if (task.isSuccess()) {
                            MongoCursor<Document> results = task.get();
                            if(!results.hasNext())
                            {
                                mongoCollection.insertOne(
                                        new Document("userid", user.getId()).append("favoriteColor", "pink").append("profilePicUrl",url))
                                        .getAsync(result -> {
                                            if (result.isSuccess()) {
                                                Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                        + result.get().getInsertedId());
                                                Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                startActivity(intent);
                                            } else {
                                                Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                            }
                                        });
                            }
                            else
                            {
                                Document userdata = results.next();
                                userdata.append("name",name);
                                userdata.append("profilePicUrl",url);

                                mongoCollection.updateOne(
                                        new Document("userid", user.getId()),(userdata))
                                        .getAsync(result -> {
                                            if (result.isSuccess()) {
                                                Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                                        + result.get().getModifiedCount());
                                                Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                                                startActivity(intent);
                                            } else {
                                                Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                            }
                                        });
                            }
                            while (results.hasNext()) {
                                //Log.v("EXAMPLE", results.next().toString());
                                Document currentDoc = results.next();
                                Log.v("User",currentDoc.getString("userid"));
                            }
                        } else {
                            Log.v("User","Failed to complete search");
                        }
                    });
                }
                //Intent intent = new Intent(UserDataSetupActivity2.this,UserDataSetupActivity3.class);
                //startActivity(intent);
            }
        });

        TextWatcher textWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                nextButton.setText("Next");
                flag=1;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                nextButton.setText("Select Later");
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            nextButton.setText("Next");
            flag=1;
            }
        };

        Name.addTextChangedListener(textWatcher);


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
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            // String picturePath contains the path of selected Image
            ImageView imageView = (ImageView) findViewById(R.id.profilePhoto);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == WRITE_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Log.d("Hello", "Write Permission Failed");
                Toast.makeText(this, "You must allow permission write external storage to your mobile device.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void requestWritePermission(){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
        }
    }
    // And to convert the image URI to the direct file system path of the image file
    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

}