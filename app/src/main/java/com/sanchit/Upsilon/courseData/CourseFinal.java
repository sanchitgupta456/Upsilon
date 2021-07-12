package com.sanchit.Upsilon.courseData;

import com.sanchit.Upsilon.classData.ScheduledClass;

import java.util.ArrayList;

public class CourseFinal {
    private ArrayList<String> courseCategories;
    private String coursePreReq;
    private ArrayList<CourseReview> courseReviews;
    private boolean registrationsOpen;
    private ArrayList<ScheduledClass> scheduledClasses;
    private ArrayList<Test> test;
    private ArrayList<Assignment> assignments;
    private String _id;
    private String courseDescription;
    private int courseDuration;
    private int courseFees;
    private CourseLocation courseLocation;
    private String courseMode;
    private Double courseRating;
    private int numberOfBatches;
    private int numberOfReviews;
    private int numberOfStudentsEnrolled;
    private String tutorId;

    public CourseFinal(ArrayList<String> courseCategories, String coursePreReq, ArrayList<CourseReview> courseReviews, boolean registrationsOpen, ArrayList<ScheduledClass> scheduledClasses, ArrayList<Test> test, ArrayList<Assignment> assignments, String _id, String courseDescription, int courseDuration, int courseFees, CourseLocation courseLocation, String courseMode, Double courseRating, int numberOfBatches, int numberOfReviews, int numberOfStudentsEnrolled, String tutorId) {
        this.courseCategories = courseCategories;
        this.coursePreReq = coursePreReq;
        this.courseReviews = courseReviews;
        this.registrationsOpen = registrationsOpen;
        this.scheduledClasses = scheduledClasses;
        this.test = test;
        this.assignments = assignments;
        this._id = _id;
        this.courseDescription = courseDescription;
        this.courseDuration = courseDuration;
        this.courseFees = courseFees;
        this.courseLocation = courseLocation;
        this.courseMode = courseMode;
        this.courseRating = courseRating;
        this.numberOfBatches = numberOfBatches;
        this.numberOfReviews = numberOfReviews;
        this.numberOfStudentsEnrolled = numberOfStudentsEnrolled;
        this.tutorId = tutorId;
    }

    public CourseFinal() {
    }

    public ArrayList<String> getCourseCategories() {
        return courseCategories;
    }

    public void setCourseCategories(ArrayList<String> courseCategories) {
        this.courseCategories = courseCategories;
    }

    public String getCoursePreReq() {
        return coursePreReq;
    }

    public void setCoursePreReq(String coursePreReq) {
        this.coursePreReq = coursePreReq;
    }

    public ArrayList<CourseReview> getCourseReviews() {
        return courseReviews;
    }

    public void setCourseReviews(ArrayList<CourseReview> courseReviews) {
        this.courseReviews = courseReviews;
    }

    public boolean isRegistrationsOpen() {
        return registrationsOpen;
    }

    public void setRegistrationsOpen(boolean registrationsOpen) {
        this.registrationsOpen = registrationsOpen;
    }

    public ArrayList<ScheduledClass> getScheduledClasses() {
        return scheduledClasses;
    }

    public void setScheduledClasses(ArrayList<ScheduledClass> scheduledClasses) {
        this.scheduledClasses = scheduledClasses;
    }

    public ArrayList<Test> getTest() {
        return test;
    }

    public void setTest(ArrayList<Test> test) {
        this.test = test;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public int getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(int courseDuration) {
        this.courseDuration = courseDuration;
    }

    public int getCourseFees() {
        return courseFees;
    }

    public void setCourseFees(int courseFees) {
        this.courseFees = courseFees;
    }

    public CourseLocation getCourseLocation() {
        return courseLocation;
    }

    public void setCourseLocation(CourseLocation courseLocation) {
        this.courseLocation = courseLocation;
    }

    public String getCourseMode() {
        return courseMode;
    }

    public void setCourseMode(String courseMode) {
        this.courseMode = courseMode;
    }

    public Double getCourseRating() {
        return courseRating;
    }

    public void setCourseRating(Double courseRating) {
        this.courseRating = courseRating;
    }

    public int getNumberOfBatches() {
        return numberOfBatches;
    }

    public void setNumberOfBatches(int numberOfBatches) {
        this.numberOfBatches = numberOfBatches;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    public int getNumberOfStudentsEnrolled() {
        return numberOfStudentsEnrolled;
    }

    public void setNumberOfStudentsEnrolled(int numberOfStudentsEnrolled) {
        this.numberOfStudentsEnrolled = numberOfStudentsEnrolled;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }
}
