package com.sanchit.Upsilon.courseData;

import org.bson.codecs.BsonArrayCodec;
import org.bson.types.BasicBSONList;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Course extends Object implements Serializable {
    private String courseName;
    private String tutorId;
    private String courseDescription;
    private String coursePreReq;
    private double courseRating;
    private String courseMode;
    private String courseFees;
    private String instructorLocation;
    private int courseDuration;
    private int numberOfStudentsEnrolled;
    private int numberOfBatches;
    private BasicBSONList courseReviews;
    private String cardImgID;

    public Course() {
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCoursePreReq() {
        return coursePreReq;
    }

    public void setCoursePreReq(String coursePreReq) {
        this.coursePreReq = coursePreReq;
    }

    public double getCourseRating() {
        return courseRating;
    }

    public void setCourseRating(float courseRating) {
        this.courseRating = courseRating;
    }

    public String getCourseMode() {
        return courseMode;
    }

    public void setCourseMode(String courseMode) {
        this.courseMode = courseMode;
    }

    public String getCourseFees() {
        return courseFees;
    }

    public void setCourseFees(String courseFees) {
        this.courseFees = courseFees;
    }

    public String getInstructorLocation() {
        return instructorLocation;
    }

    public void setInstructorLocation(String instructorLocation) {
        this.instructorLocation = instructorLocation;
    }

    public int getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(int courseDuration) {
        this.courseDuration = courseDuration;
    }

    public int getNumberOfStudentsEnrolled() {
        return numberOfStudentsEnrolled;
    }

    public void setNumberOfStudentsEnrolled(int numberOfStudentsEnrolled) {
        this.numberOfStudentsEnrolled = numberOfStudentsEnrolled;
    }

    public int getNumberOfBatches() {
        return numberOfBatches;
    }

    public void setNumberOfBatches(int numberOfBatches) {
        this.numberOfBatches = numberOfBatches;
    }

    public BasicBSONList getCourseReviews() {
        return courseReviews;
    }

    public void setCourseReviews(BasicBSONList courseReviews) {
        this.courseReviews = courseReviews;
    }

    public String getCardImgID() {
        return cardImgID;
    }

    public void setCardImgID(String cardImgID) {
        this.cardImgID = cardImgID;
    }
}
