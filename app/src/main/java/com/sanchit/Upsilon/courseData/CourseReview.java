package com.sanchit.Upsilon.courseData;

import io.realm.RealmObject;

public class CourseReview extends Object {

    private String review;
    private double reviewRating;
    private String reviewAuthorId;

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
