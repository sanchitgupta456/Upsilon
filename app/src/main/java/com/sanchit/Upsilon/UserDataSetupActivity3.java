package com.sanchit.Upsilon;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class UserDataSetupActivity3 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String appID = "upsilon-ityvn";
    Button nextButton;
    CircleImageView locationbutton;
    EditText City, Pincode, PhoneNumber;
    String city, pincode, phonenumber;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    private final int REQUEST_FINE_LOCATION = 1234;
    FusedLocationProviderClient fusedLocationProviderClient;
    private Spinner selectYourCollege;
    private String College;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_3);

        nextButton = (Button) findViewById(R.id.selectLaterNext3);
        City = (EditText) findViewById(R.id.cityNameHolder);
        Pincode = (EditText) findViewById(R.id.pincodeHolder);
        PhoneNumber = (EditText) findViewById(R.id.contactNumberHolder);
        locationbutton = (CircleImageView) findViewById(R.id.user_data_setup_google_location);
        selectYourCollege = (Spinner) findViewById(R.id.spinnerUserCollege);

        String[] Colleges = {"IIT Madras"};

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_expandable_list_item_1,Colleges);
        selectYourCollege.setAdapter(adapter);
        selectYourCollege.setOnItemSelectedListener(UserDataSetupActivity3.this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UserDataSetupActivity3.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserDataSetupActivity3.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
                } else {
                    getLocation();
                }
            }
        });

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");

        TextWatcher textWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                nextButton.setText("Next");
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                nextButton.setText("Select Later");
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                nextButton.setText("Next");
            }
        };

        City.addTextChangedListener(textWatcher);
        PhoneNumber.addTextChangedListener(textWatcher);
        Pincode.addTextChangedListener(textWatcher);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Document queryFilter = new Document("userid", user.getId());
                city = City.getText().toString();
                pincode = Pincode.getText().toString();
                phonenumber = PhoneNumber.getText().toString();
                RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                findTask.getAsync(task -> {
                    if (task.isSuccess()) {
                        MongoCursor<Document> results = task.get();
                        if (!results.hasNext()) {
                            mongoCollection.insertOne(
                                    new Document("userid", user.getId()).append("favoriteColor", "pink").append("city", city).append("pincode", pincode).append("phonenumber", phonenumber).append("college",College))
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
                            userdata.append("city", city).append("pincode", pincode).append("phonenumber", phonenumber);

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


                Intent intent = new Intent(UserDataSetupActivity3.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onItemSelected(AdapterView parent, View view, int pos,
                               long id) {

        College = selectYourCollege.getItemAtPosition(pos).toString();

        //Toast.makeText(getApplicationContext(),
        //      spinner.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG)
        //    .show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location!=null)
                {
                    Log.v("Location","Location"+location.getLatitude());
                    Geocoder geocoder = new Geocoder(UserDataSetupActivity3.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        City.setText(addresses.get(0).getLocality());
                        Pincode.setText(addresses.get(0).getPostalCode());
                        Log.v("Location",addresses.get(0).getPostalCode()+" "+addresses.get(0).getLocality()+" "+addresses.get(0).getSubLocality());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Log.v("Location","Error");
                }
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}