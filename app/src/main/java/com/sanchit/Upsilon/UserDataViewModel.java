package com.sanchit.Upsilon;

import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;

public class UserDataViewModel extends ViewModel {
    //private ArrayList<String> mycourses = new ArrayList<>();
    private MutableLiveData<String> College = new MutableLiveData<>();

    public UserDataViewModel() {
        College.setValue("None");
        interests.setValue(new ArrayList<>());
        name.setValue("User");
        picturePath.setValue(null);
        city.setValue("world");
        pincode.setValue("-1");
        phonenumber.setValue("0");
        userLocation.setValue(new Document());
    }

    private MutableLiveData<ArrayList<String>> interests = new MutableLiveData<>();

    public void setPicturePath(String picturePath) {
        this.picturePath.setValue(picturePath);
    }

    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<String> picturePath = new MutableLiveData<>();
    //private final String defaultUrl = "https://res.cloudinary.com/upsilon175/image/upload/v1606421188/Upsilon/blankPP_phfegu.png";

    private MutableLiveData<String> city = new MutableLiveData<>(), pincode = new MutableLiveData<>(), phonenumber = new MutableLiveData<>();
    //private final int REQUEST_FINE_LOCATION = 1234;
    //private FusedLocationProviderClient fusedLocationProviderClient;

    public void setUserLocation(Document userLocation) {
        this.userLocation.setValue(userLocation);
    }

    private MutableLiveData<Document> userLocation = new MutableLiveData<>();

    public MutableLiveData<String> getCollege() {
        return College;
    }

    public void setCollege(String college) {
        College.setValue(college);
    }

    /*public ArrayList<String> getMycourses() {
        return mycourses;
    }

    public void setMycourses(ArrayList<String> mycourses) {
        this.mycourses = mycourses;
    }*/

    public MutableLiveData<ArrayList<String>> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests.setValue(interests);
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public MutableLiveData<String> getPicturePath() {
        return picturePath;
    }

    /*public String getDefaultUrl() {
        return defaultUrl;
    }*/

    public MutableLiveData<String> getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city.setValue(city);
    }

    public MutableLiveData<String> getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode.setValue(pincode);
    }

    public MutableLiveData<String> getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber.setValue(phonenumber);
    }

    public Document getUserLocation() {
        return userLocation.getValue();
    }

    public void addInterest(String interest){
        interests.getValue().add(interest);
    }

    public void removeInterest(String interest){
        interests.getValue().remove(interest);
    }
}
