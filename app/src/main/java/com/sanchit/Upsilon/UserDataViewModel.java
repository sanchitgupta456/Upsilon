package com.sanchit.Upsilon;

import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;

public class UserDataViewModel extends ViewModel {
    private ArrayList<String> mycourses = new ArrayList<>();
    private String College = "None";

    private ArrayList<String> interests = new ArrayList<>();

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    private String name = "User";
    private String picturePath = null;
    private final String defaultUrl = "https://res.cloudinary.com/upsilon175/image/upload/v1606421188/Upsilon/blankPP_phfegu.png";

    private String city = "world", pincode = "-1", phonenumber = "0";
    private int REQUEST_FINE_LOCATION = 1234;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public void setUserLocation(Document userLocation) {
        this.userLocation = userLocation;
    }

    private Document userLocation = new Document();

    public String getCollege() {
        return College;
    }

    public void setCollege(String college) {
        College = college;
    }

    public ArrayList<String> getMycourses() {
        return mycourses;
    }

    public void setMycourses(ArrayList<String> mycourses) {
        this.mycourses = mycourses;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public String getDefaultUrl() {
        return defaultUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Document getUserLocation() {
        return userLocation;
    }

    public void addInterest(String interest){
        interests.add(interest);
    }

    public void removeInterest(String interest){
        interests.remove(interest);
    }
}
