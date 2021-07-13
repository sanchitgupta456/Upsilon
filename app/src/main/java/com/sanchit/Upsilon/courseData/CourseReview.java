package com.sanchit.Upsilon.courseData;

import java.io.Serializable;

import io.realm.RealmObject;

public class CourseReview extends Object implements Serializable {

    private String review;
    private double reviewRating;
    private String reviewAuthorId;
    private String reviewName;

    public CourseReview(String review, double reviewRating, String reviewAuthorId, String reviewName) {
        this.review = review;
        this.reviewRating = reviewRating;
        this.reviewAuthorId = reviewAuthorId;
        this.reviewName = reviewName;
    }

    public void setReviewAuthorId(String reviewAuthorId) {
        this.reviewAuthorId = reviewAuthorId;
    }

    public String getReviewName() {
        return reviewName;
    }

    public void setReviewName(String reviewName) {
        this.reviewName = reviewName;
    }

    public CourseReview() {
    }

    public CourseReview(String review, Double reviewRating, String reviewAuthorId) {
        this.review = review;
        this.reviewRating = reviewRating;
        this.reviewAuthorId = reviewAuthorId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public double getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(double reviewRating) {
        this.reviewRating = reviewRating;
    }

    public String getReviewAuthorId() {
        return reviewAuthorId;
    }

    public void setRatingAuthorId(String reviewAuthorId) {
        this.reviewAuthorId = reviewAuthorId;
    }
}
