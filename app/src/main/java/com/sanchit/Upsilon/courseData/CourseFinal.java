package com.sanchit.Upsilon.courseData;

import com.sanchit.Upsilon.classData.ScheduledClass;
import com.sanchit.Upsilon.notificationData.Notification;

import java.io.Serializable;
import java.util.ArrayList;

public class CourseFinal extends Object implements Serializable {
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
    private String courseName;
    private ArrayList<String> IntroductoryContentImages;
    private ArrayList<String> IntroductoryContentVideos;
    private ArrayList<String> IntroductoryContentDocuments;
    private String courseImage;
    private ArrayList<Lecture> lectures;
    private ArrayList<Notification> notifications;

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public CourseFinal(ArrayList<String> courseCategories, String coursePreReq, ArrayList<CourseReview> courseReviews, boolean registrationsOpen, ArrayList<ScheduledClass> scheduledClasses, ArrayList<Test> test, ArrayList<Assignment> assignments, String _id, String courseDescription, int courseDuration, int courseFees, CourseLocation courseLocation, String courseMode, Double courseRating, int numberOfBatches, int numberOfReviews, int numberOfStudentsEnrolled, String tutorId, String courseName, ArrayList<String> introductoryContentImages, ArrayList<String> introductoryContentVideos, ArrayList<String> introductoryContentDocuments, String courseImage, ArrayList<Lecture> lectures, ArrayList<Notification> notifications) {
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
        this.courseName = courseName;
        IntroductoryContentImages = introductoryContentImages;
        IntroductoryContentVideos = introductoryContentVideos;
        IntroductoryContentDocuments = introductoryContentDocuments;
        this.courseImage = courseImage;
        this.lectures = lectures;
        this.notifications = notifications;
    }

    public CourseFinal() {
    }

    public CourseFinal(ArrayList<String> courseCategories, String coursePreReq, ArrayList<CourseReview> courseReviews, boolean registrationsOpen, ArrayList<ScheduledClass> scheduledClasses, ArrayList<Test> test, ArrayList<Assignment> assignments, String _id, String courseDescription, int courseDuration, int courseFees, CourseLocation courseLocation, String courseMode, Double courseRating, int numberOfBatches, int numberOfReviews, int numberOfStudentsEnrolled, String tutorId, String courseName, ArrayList<String> introductoryContentImages, ArrayList<String> introductoryContentVideos, ArrayList<String> introductoryContentDocuments, String courseImage, ArrayList<Lecture> lectures) {
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
        this.courseName = courseName;
        IntroductoryContentImages = introductoryContentImages;
        IntroductoryContentVideos = introductoryContentVideos;
        IntroductoryContentDocuments = introductoryContentDocuments;
        this.courseImage = courseImage;
        this.lectures = lectures;
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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public ArrayList<String> getIntroductoryContentImages() {
        return IntroductoryContentImages;
    }

    public void setIntroductoryContentImages(ArrayList<String> introductoryContentImages) {
        IntroductoryContentImages = introductoryContentImages;
    }

    public ArrayList<String> getIntroductoryContentVideos() {
        return IntroductoryContentVideos;
    }

    public void setIntroductoryContentVideos(ArrayList<String> introductoryContentVideos) {
        IntroductoryContentVideos = introductoryContentVideos;
    }

    public ArrayList<String> getIntroductoryContentDocuments() {
        return IntroductoryContentDocuments;
    }

    public void setIntroductoryContentDocuments(ArrayList<String> introductoryContentDocuments) {
        IntroductoryContentDocuments = introductoryContentDocuments;
    }

    public String getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(String courseImage) {
        this.courseImage = courseImage;
    }

    public ArrayList<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(ArrayList<Lecture> lectures) {
        this.lectures = lectures;
    }
}
