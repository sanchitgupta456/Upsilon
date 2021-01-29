package com.sanchit.Upsilon.courseData;

import android.text.Editable;

import org.bson.Document;
import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.ArrayList;

public class Course extends Object implements Serializable {
    private String courseId;
    private String courseName;
    private String courseImage;
    private String tutorId;
    private String courseDescription;
    private String coursePreReq;
    private double courseRating;
    private String courseMode;
    private int courseFees;
    private String instructorLocation;
    private int courseDuration;
    private int numberOfStudentsEnrolled;
    private int numberOfBatches;
    private BasicBSONList courseReviews;
    private BasicBSONList scheduledClasses;
    private String cardImgID;
    private ArrayList<String> myRegisteredCourses;
    private ArrayList<String> IntroductoryContentImages;
    private int IntroductoryImageCounter;
    private ArrayList<String> IntroductoryContentVideos;
    private ArrayList<String> courseCategories;
    private int IntroductoryVideoCounter;
    private String nextLectureOn;
    private String meetLink;
    private Document courseLocation;
    private double courseDistance;
    private int numberOfReviews;

    public void setDistance(double d){
        this.courseDistance = d;
    }

    public double getCourseDistanceInKm(){
        return this.courseDistance;
    }

    public Course(String courseId, String courseName, String courseImage, String tutorId, String courseDescription, String coursePreReq, double courseRating, String courseMode, int courseFees, String instructorLocation, int courseDuration, int numberOfStudentsEnrolled, int numberOfBatches, BasicBSONList courseReviews, BasicBSONList scheduledClasses, String cardImgID, ArrayList<String> myRegisteredCourses, ArrayList<String> introductoryContentImages, int introductoryImageCounter, ArrayList<String> introductoryContentVideos, ArrayList<String> courseCategories, int introductoryVideoCounter, String nextLectureOn, String meetLink, Document courseLocation, int numberOfReviews) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseImage = courseImage;
        this.tutorId = tutorId;
        this.courseDescription = courseDescription;
        this.coursePreReq = coursePreReq;
        this.courseRating = courseRating;
        this.courseMode = courseMode;
        this.courseFees = courseFees;
        this.instructorLocation = instructorLocation;
        this.courseDuration = courseDuration;
        this.numberOfStudentsEnrolled = numberOfStudentsEnrolled;
        this.numberOfBatches = numberOfBatches;
        this.courseReviews = courseReviews;
        this.scheduledClasses = scheduledClasses;
        this.cardImgID = cardImgID;
        this.myRegisteredCourses = myRegisteredCourses;
        IntroductoryContentImages = introductoryContentImages;
        IntroductoryImageCounter = introductoryImageCounter;
        IntroductoryContentVideos = introductoryContentVideos;
        this.courseCategories = courseCategories;
        IntroductoryVideoCounter = introductoryVideoCounter;
        this.nextLectureOn = nextLectureOn;
        this.meetLink = meetLink;
        this.courseLocation = courseLocation;
        this.numberOfReviews = numberOfReviews;
    }

    public Course(String courseId, String courseName, String courseImage, String tutorId, String courseDescription, String coursePreReq, double courseRating, String courseMode, int courseFees, String instructorLocation, int courseDuration, int numberOfStudentsEnrolled, int numberOfBatches, BasicBSONList courseReviews, BasicBSONList scheduledClasses, String cardImgID, ArrayList<String> myRegisteredCourses, ArrayList<String> introductoryContentImages, int introductoryImageCounter, ArrayList<String> introductoryContentVideos, int introductoryVideoCounter, String nextLectureOn, String meetLink, Document courseLocation, int numberOfReviews) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseImage = courseImage;
        this.tutorId = tutorId;
        this.courseDescription = courseDescription;
        this.coursePreReq = coursePreReq;
        this.courseRating = courseRating;
        this.courseMode = courseMode;
        this.courseFees = courseFees;
        this.instructorLocation = instructorLocation;
        this.courseDuration = courseDuration;
        this.numberOfStudentsEnrolled = numberOfStudentsEnrolled;
        this.numberOfBatches = numberOfBatches;
        this.courseReviews = courseReviews;
        this.scheduledClasses = scheduledClasses;
        this.cardImgID = cardImgID;
        this.myRegisteredCourses = myRegisteredCourses;
        IntroductoryContentImages = introductoryContentImages;
        IntroductoryImageCounter = introductoryImageCounter;
        IntroductoryContentVideos = introductoryContentVideos;
        IntroductoryVideoCounter = introductoryVideoCounter;
        this.nextLectureOn = nextLectureOn;
        this.meetLink = meetLink;
        this.courseLocation = courseLocation;
        this.numberOfReviews = numberOfReviews;
    }

    public Course(String courseId, String courseName, String courseImage, String tutorId, String courseDescription, String coursePreReq, double courseRating, String courseMode, int courseFees, String instructorLocation, int courseDuration, int numberOfStudentsEnrolled, int numberOfBatches, BasicBSONList courseReviews, BasicBSONList scheduledClasses, String cardImgID, ArrayList<String> myRegisteredCourses, ArrayList<String> introductoryContentImages, int introductoryImageCounter, ArrayList<String> introductoryContentVideos, int introductoryVideoCounter, String nextLectureOn, String meetLink) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseImage = courseImage;
        this.tutorId = tutorId;
        this.courseDescription = courseDescription;
        this.coursePreReq = coursePreReq;
        this.courseRating = courseRating;
        this.courseMode = courseMode;
        this.courseFees = courseFees;
        this.instructorLocation = instructorLocation;
        this.courseDuration = courseDuration;
        this.numberOfStudentsEnrolled = numberOfStudentsEnrolled;
        this.numberOfBatches = numberOfBatches;
        this.courseReviews = courseReviews;
        this.scheduledClasses = scheduledClasses;
        this.cardImgID = cardImgID;
        this.myRegisteredCourses = myRegisteredCourses;
        IntroductoryContentImages = introductoryContentImages;
        IntroductoryImageCounter = introductoryImageCounter;
        IntroductoryContentVideos = introductoryContentVideos;
        IntroductoryVideoCounter = introductoryVideoCounter;
        this.nextLectureOn = nextLectureOn;
        this.meetLink = meetLink;
    }

    public BasicBSONList getScheduledClasses() {
        return scheduledClasses;
    }

    public void setScheduledClasses(BasicBSONList scheduledClasses) {
        this.scheduledClasses = scheduledClasses;
    }

    public Course(String courseId, String courseName, String courseImage, String tutorId, String courseDescription, String coursePreReq, double courseRating, String courseMode, int courseFees, String instructorLocation, int courseDuration, int numberOfStudentsEnrolled, int numberOfBatches, BasicBSONList courseReviews, String cardImgID, ArrayList<String> myRegisteredCourses, ArrayList<String> introductoryContentImages, int introductoryImageCounter, ArrayList<String> introductoryContentVideos, int introductoryVideoCounter, String nextLectureOn, String meetLink) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseImage = courseImage;
        this.tutorId = tutorId;
        this.courseDescription = courseDescription;
        this.coursePreReq = coursePreReq;
        this.courseRating = courseRating;
        this.courseMode = courseMode;
        this.courseFees = courseFees;
        this.instructorLocation = instructorLocation;
        this.courseDuration = courseDuration;
        this.numberOfStudentsEnrolled = numberOfStudentsEnrolled;
        this.numberOfBatches = numberOfBatches;
        this.courseReviews = courseReviews;
        this.cardImgID = cardImgID;
        this.myRegisteredCourses = myRegisteredCourses;
        IntroductoryContentImages = introductoryContentImages;
        IntroductoryImageCounter = introductoryImageCounter;
        IntroductoryContentVideos = introductoryContentVideos;
        IntroductoryVideoCounter = introductoryVideoCounter;
        this.nextLectureOn = nextLectureOn;
        this.meetLink = meetLink;
    }

    public Course(String courseId, String courseName, String courseImage, String tutorId, String courseDescription, String coursePreReq, double courseRating, String courseMode, int courseFees, String instructorLocation, int courseDuration, int numberOfStudentsEnrolled, int numberOfBatches, BasicBSONList courseReviews, String cardImgID, ArrayList<String> myRegisteredCourses, ArrayList<String> introductoryContentImages, int introductoryImageCounter, ArrayList<String> introductoryContentVideos, int introductoryVideoCounter) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseImage = courseImage;
        this.tutorId = tutorId;
        this.courseDescription = courseDescription;
        this.coursePreReq = coursePreReq;
        this.courseRating = courseRating;
        this.courseMode = courseMode;
        this.courseFees = courseFees;
        this.instructorLocation = instructorLocation;
        this.courseDuration = courseDuration;
        this.numberOfStudentsEnrolled = numberOfStudentsEnrolled;
        this.numberOfBatches = numberOfBatches;
        this.courseReviews = courseReviews;
        this.cardImgID = cardImgID;
        this.myRegisteredCourses = myRegisteredCourses;
        IntroductoryContentImages = introductoryContentImages;
        IntroductoryImageCounter = introductoryImageCounter;
        IntroductoryContentVideos = introductoryContentVideos;
        IntroductoryVideoCounter = introductoryVideoCounter;
    }

    public Course(String courseId, String courseName, String courseImage, String tutorId, String courseDescription, String coursePreReq, double courseRating, String courseMode, int courseFees, String instructorLocation, int courseDuration, int numberOfStudentsEnrolled, int numberOfBatches, BasicBSONList courseReviews, String cardImgID, ArrayList<String> myRegisteredCourses, ArrayList<String> introductoryContentImages, int introductoryImageCounter, ArrayList<String> introductoryContentVideos, int introductoryVideoCounter, String nextLectureOn) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseImage = courseImage;
        this.tutorId = tutorId;
        this.courseDescription = courseDescription;
        this.coursePreReq = coursePreReq;
        this.courseRating = courseRating;
        this.courseMode = courseMode;
        this.courseFees = courseFees;
        this.instructorLocation = instructorLocation;
        this.courseDuration = courseDuration;
        this.numberOfStudentsEnrolled = numberOfStudentsEnrolled;
        this.numberOfBatches = numberOfBatches;
        this.courseReviews = courseReviews;
        this.cardImgID = cardImgID;
        this.myRegisteredCourses = myRegisteredCourses;
        IntroductoryContentImages = introductoryContentImages;
        IntroductoryImageCounter = introductoryImageCounter;
        IntroductoryContentVideos = introductoryContentVideos;
        IntroductoryVideoCounter = introductoryVideoCounter;
        this.nextLectureOn = nextLectureOn;
    }

    public Course() {
    }

    public ArrayList<String> getCourseCategories() {
        return courseCategories;
    }

    public void setCourseCategories(ArrayList<String> courseCategories) {
        this.courseCategories = courseCategories;
    }

    public Document getCourseLocation() {
        return courseLocation;
    }

    public void setCourseLocation(Document courseLocation) {
        this.courseLocation = courseLocation;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    public String getNextLectureOn() {
        return nextLectureOn;
    }

    public void setNextLectureOn(String nextLectureOn) {
        this.nextLectureOn = nextLectureOn;
    }


    public String getMeetLink() {
        return meetLink;
    }

    public void setMeetLink(String meetLink) {
        this.meetLink = meetLink;
    }


    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public ArrayList<String> getMyRegisteredCourses() {
        return myRegisteredCourses;
    }

    public void setMyRegisteredCourses(ArrayList<String> myRegisteredCourses) {
        this.myRegisteredCourses = myRegisteredCourses;
    }

    public ArrayList<String> getIntroductoryContentImages() {
        return IntroductoryContentImages;
    }

    public void setIntroductoryContentImages(ArrayList<String> introductoryContentImages) {
        IntroductoryContentImages = introductoryContentImages;
    }

    public int getIntroductoryImageCounter() {
        return IntroductoryImageCounter;
    }

    public void setIntroductoryImageCounter(int introductoryImageCounter) {
        IntroductoryImageCounter = introductoryImageCounter;
    }

    public ArrayList<String> getIntroductoryContentVideos() {
        return IntroductoryContentVideos;
    }

    public void setIntroductoryContentVideos(ArrayList<String> introductoryContentVideos) {
        IntroductoryContentVideos = introductoryContentVideos;
    }

    public int getIntroductoryVideoCounter() {
        return IntroductoryVideoCounter;
    }

    public void setIntroductoryVideoCounter(int introductoryVideoCounter) {
        IntroductoryVideoCounter = introductoryVideoCounter;
    }

    public String getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(String courseImage) {
        this.courseImage = courseImage;
    }

    public void setCourseRating(double courseRating) {
        this.courseRating = courseRating;
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

    public int getCourseFees() {
        return courseFees;
    }

    public void setCourseFees(int courseFees) {
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
