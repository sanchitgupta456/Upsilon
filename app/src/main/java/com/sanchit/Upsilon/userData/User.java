package com.sanchit.Upsilon.userData;

import java.util.ArrayList;

public class User {
    String email;
    String username;
    ArrayList<String> interests;
    String _id;
    UserLocation userLocation;
    ArrayList<String> myCourses;
    String role;
    String phone;
    String pincode;
    String img;
    Integer walletAmount;

    public Integer getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(Integer walletAmount) {
        this.walletAmount = walletAmount;
    }

    public User(String email, String username, ArrayList<String> interests, String _id, UserLocation userLocation, ArrayList<String> myCourses, String role, String phone, String pincode, String img, Integer walletAmount) {
        this.email = email;
        this.username = username;
        this.interests = interests;
        this._id = _id;
        this.userLocation = userLocation;
        this.myCourses = myCourses;
        this.role = role;
        this.phone = phone;
        this.pincode = pincode;
        this.img = img;
        this.walletAmount = walletAmount;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public User(String email, String username, ArrayList<String> interests, String _id, UserLocation userLocation, ArrayList<String> myCourses, String role, String phone, String pincode, String img) {
        this.email = email;
        this.username = username;
        this.interests = interests;
        this._id = _id;
        this.userLocation = userLocation;
        this.myCourses = myCourses;
        this.role = role;
        this.phone = phone;
        this.pincode = pincode;
        this.img = img;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public User(String email, String username, ArrayList<String> interests, String _id, UserLocation userLocation, ArrayList<String> myCourses, String role, String phone, String pincode) {
        this.email = email;
        this.username = username;
        this.interests = interests;
        this._id = _id;
        this.userLocation = userLocation;
        this.myCourses = myCourses;
        this.role = role;
        this.phone = phone;
        this.pincode = pincode;
    }

    public User(String email, String username, String _id, UserLocation userLocation, ArrayList<String> myCourses, String role) {
        this.email = email;
        this.username = username;
        this._id = _id;
        this.userLocation = userLocation;
        this.myCourses = myCourses;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User(String email, String username, String _id) {
        this.email = email;
        this.username = username;
        this._id = _id;
    }

    public User(String email, String username, String _id, UserLocation userLocation) {
        this.email = email;
        this.username = username;
        this._id = _id;
        this.userLocation = userLocation;
    }

    public User(String email, String username, String _id, UserLocation userLocation, ArrayList<String> myCourses) {
        this.email = email;
        this.username = username;
        this._id = _id;
        this.userLocation = userLocation;
        this.myCourses = myCourses;
    }

    public ArrayList<String> getMyCourses() {
        return myCourses;
    }

    public void setMyCourses(ArrayList<String> myCourses) {
        this.myCourses = myCourses;
    }

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String get_Id() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }
}
