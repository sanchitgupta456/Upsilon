package com.sanchit.Upsilon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.bson.Document;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
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

import static android.content.ContentValues.TAG;
import static io.realm.Realm.getApplicationContext;

public class UserDataSetupFragment3 extends Fragment implements AdapterView.OnItemSelectedListener {
    String appID = "upsilon-ityvn";
    CircleImageView locationbutton;
    EditText City, Pincode, PhoneNumber;
    String city, pincode, phonenumber;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    private final int REQUEST_FINE_LOCATION = 1234;
    FusedLocationProviderClient fusedLocationProviderClient;
    private String College;
    private Document userLocation = new Document();

    UserDataViewModel viewModel;

    public UserDataSetupFragment3() {
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
        View view;
        view = inflater.inflate(R.layout.fragment_user_data_setup3, container, false);
        City = (EditText) view.findViewById(R.id.cityNameHolder);
        Pincode = (EditText) view.findViewById(R.id.pincodeHolder);
        PhoneNumber = (EditText) view.findViewById(R.id.contactNumberHolder);
        locationbutton = (CircleImageView) view.findViewById(R.id.user_data_setup_google_location);
        AppCompatSpinner selectYourCollege = (AppCompatSpinner) view.findViewById(R.id.spinnerUserCollege);
        viewModel = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);

        String[] Colleges = {"None", "IIT Madras"};
        College = "None";
        viewModel.setCollege(College);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_expandable_list_item_custom, Colleges);
        selectYourCollege.setAdapter(adapter);
        //selectYourCollege.setOnItemClickListener(this::onItemSelected);
        /*
        selectYourCollege.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                College = selectYourCollege.getItemAtPosition(pos).toString();
                viewModel.setCollege(College);
            }
        });*/

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));
        locationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
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

        City.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                city = charSequence.toString();
                if (city.length() == 0){
                    city = "world";
                }
                viewModel.setCity(city);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        PhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phonenumber = PhoneNumber.getText().toString();
                viewModel.setPhonenumber(phonenumber);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pincode = Pincode.getText().toString();
                if (pincode.length() == 0){
                    pincode = "-1";
                }
                viewModel.setPincode(pincode);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /*
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Document queryFilter = new Document("userid", user.getId());
                city = City.getText().toString();
                pincode = Pincode.getText().toString();
                phonenumber = PhoneNumber.getText().toString();

                if (city.length() == 0){
                    city = "world";
                }

                if (pincode.length() == 0){
                    pincode = "-1";
                }

                if (College.equals("None")){
                    selectYourCollege.requestFocus();
                    Toast toast = Toast.makeText(getApplicationContext(), "Please select a campus!", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

                findTask.getAsync(task -> {
                    if (task.isSuccess()) {
                        MongoCursor<Document> results = task.get();
                        if (!results.hasNext()) {
                            mongoCollection.insertOne(
                                    new Document("userid", user.getId()).append("favoriteColor", "pink").append("city", city).append("pincode", pincode).append("phonenumber", phonenumber).append("college",College).append("userLocation",userLocation))
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
                            userdata.append("city", city).append("pincode", pincode).append("phonenumber", phonenumber).append("college",College).append("userLocation",userLocation);

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


                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
         */

        return view;
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        City.setText(addresses.get(0).getLocality());
                        viewModel.setCity(addresses.get(0).getLocality());
                        Pincode.setText(addresses.get(0).getPostalCode());
                        viewModel.setPincode(addresses.get(0).getPostalCode());
                        userLocation.append("lattitude",location.getLatitude());
                        userLocation.append("longitude",location.getLongitude());
                        viewModel.setUserLocation(userLocation);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        College = parent.getItemAtPosition(position).toString();
        Log.d(TAG, "onItemSelected: College is selected: "+College);
        viewModel.setCollege(College);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}