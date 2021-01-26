package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.sanchit.Upsilon.ui.login.LoginActivity;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

import static io.realm.Realm.getApplicationContext;

public class UserDataSetupActivity extends AppCompatActivity {
    String appID = "upsilon-ityvn";
    private static final String TAG = "UserDataSetupActivity";
    ViewPager viewPager;
    HorizontalDotProgress progress;
    Button skip, next;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> mongoCollection;

    private UserDataViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data_setup);
        Objects.requireNonNull(getSupportActionBar()).hide();

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        progress = (HorizontalDotProgress) findViewById(R.id.dot_progress);
        skip = (Button) findViewById(R.id.skipSetup);
        next = (Button) findViewById(R.id.selectLaterNext);
        viewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");

        setupViewPager(viewPager);

        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                //something here
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                progress.setDotPosition(position);
            }

            @Override
            public void onPageSelected(int position) {
                progress.setDotPosition(position);
                if(position == 2){
                    next.setText("Get Started");
                } else {
                    next.setText(R.string.next);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = viewPager.getCurrentItem();
                switch (pos){
                    case 0:
                        viewPager.setCurrentItem(1);
                        progress.setDotPosition(1);
                        break;
                    case 1:
                        viewPager.setCurrentItem(2);
                        progress.setDotPosition(2);
                        break;
                    case 2:
                        putInterests();
                        putProfile();
                        putAddressAndPhone();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.clearFragments();
        adapter.addFragment(new UserDataSetupFragment1());
        adapter.addFragment(new UserDataSetupFragment2());
        adapter.addFragment(new UserDataSetupFragment3());
        viewPager.setAdapter(adapter);
    }


    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        public void clearFragments() {
            mFragmentList.clear();
        }

    }

    public void putInterests(){
        ArrayList<String> interests = viewModel.getInterests();
        ArrayList<String> mycourses = viewModel.getMycourses();

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");
        for (String s : interests) {
            Log.d(TAG, "onClick: interests: " + s);
        }
        Document queryFilter = new Document("userid", user.getId());

        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                if (!results.hasNext()) {
                    mongoCollection.insertOne(
                            new Document("userid", user.getId()).append("myCourses", mycourses).append("profilePicCounter", 0).append("favoriteColor", "pink").append("interests", interests))
                            .getAsync(result -> {
                                if (result.isSuccess()) {
                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                            + result.get().getInsertedId());
                                } else {
                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                }
                            });
                } else {
                    Document userdata = results.next();
                    userdata.append("interests", interests);
                    userdata.append("profilePicCounter", 0);
                    userdata.append("myCourses", mycourses);

                    mongoCollection.updateOne(
                            new Document("userid", user.getId()), (userdata))
                            .getAsync(result -> {
                                if (result.isSuccess()) {
                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                            + result.get().getModifiedCount());
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

    public void putProfile(){
        String picturePath = viewModel.getPicturePath();
        if (picturePath == null) {
            uploadDefault();
        } else {
            uploadGiven();
        }
    }


    void uploadGiven(){
        String name = viewModel.getName();
        String picturePath = viewModel.getPicturePath();

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
                                //name = Name.getText().toString();

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
                                                            //Intent intent = new Intent(getApplicationContext(), UserDataSetupActivity3.class);
                                                            //startActivity(intent);
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
                                                            //Intent intent = new Intent(getApplicationContext(), UserDataSetupActivity3.class);
                                                            //startActivity(intent);
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
            String name =viewModel.getName();
            String defaultUrl = viewModel.getDefaultUrl();
            if(task.isSuccess()) {
                MongoCursor<Document> results = task.get();

                if (!results.hasNext()) {
                    mongoCollection.insertOne(
                            new Document("userid", user.getId()).append("profilePicCounter", 0).append("favoriteColor", "pink").append("profilePicUrl", defaultUrl))
                            .getAsync(result -> {
                                if (result.isSuccess()) {
                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                            + result.get().getInsertedId());
                                    //Intent intent = new Intent(getApplicationContext(), UserDataSetupActivity3.class);
                                    //startActivity(intent);
                                    //return;
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
                                    //Intent intent = new Intent(getApplicationContext(), UserDataSetupActivity3.class);
                                    //startActivity(intent);
                                } else {
                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                }
                            });
                }
            }
        });
    }

    public void putAddressAndPhone(){
        String city = viewModel.getCity();
        String pincode = viewModel.getPincode();
        String phonenumber = viewModel.getPhonenumber();
        Document userLocation = viewModel.getUserLocation();
        String college = viewModel.getCollege();

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("UserData");
        Document queryFilter  = new Document("userid",user.getId());

        if (city.length() == 0){
            city = "world";
        }

        if (pincode.length() == 0){
            pincode = "-1";
        }

        /*if (college == "None"){
            selectYourCollege.requestFocus();
            Toast toast = Toast.makeText(getApplicationContext(), "Please select a campus!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }*/

        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

        String finalCity = city;
        String finalPincode = pincode;
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                if (!results.hasNext()) {
                    mongoCollection.insertOne(
                            new Document("userid", user.getId()).append("favoriteColor", "pink").append("city", finalCity).append("pincode", finalPincode).append("phonenumber", phonenumber).append("college",college).append("userLocation",userLocation))
                            .getAsync(result -> {
                                if (result.isSuccess()) {
                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                            + result.get().getInsertedId());
                                } else {
                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                }
                            });
                } else {
                    Document userdata = results.next();
                    userdata.append("city", finalCity).append("pincode", finalPincode).append("phonenumber", phonenumber).append("college",college).append("userLocation",userLocation);

                    mongoCollection.updateOne(
                            new Document("userid", user.getId()), (userdata))
                            .getAsync(result -> {
                                if (result.isSuccess()) {
                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                            + result.get().getModifiedCount());
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


        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //startActivity(intent);
    }
}