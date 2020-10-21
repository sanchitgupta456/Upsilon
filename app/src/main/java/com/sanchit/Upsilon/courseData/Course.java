package com.sanchit.Upsilon.courseData;

import io.realm.RealmObject;

public class Course extends RealmObject {
    private String courseName;
    private String tutorName;
    private int numberOfStudentsEnrolled;
    private int cardImgID;


    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCardImgID(int cardImgID) {
        this.cardImgID = cardImgID;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public void setNumberOfStudentsEnrolled(int numberOfStudentsEnrolled) {
        this.numberOfStudentsEnrolled = numberOfStudentsEnrolled;
    }

    public void incrEnrolled(){
        this.numberOfStudentsEnrolled += 1;
    }

    public String getCourseName(){
        return courseName;
    }

    public String getTutorName(){
        return tutorName;
    }

    public int getNumberOfStudentsEnrolled(){
        return numberOfStudentsEnrolled;
    }

    public int getCardImgID() {
        return cardImgID;
    }
}
