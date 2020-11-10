package com.sanchit.Upsilon.courseData;

import io.realm.RealmObject;

public class CourseReview extends Object {

    private String review;
    private double reviewRating;
    private String ratingAuthorId;

    public CourseReview() {
    }

    public CourseReview(String review, Double reviewRating, String ratingAuthorId) {
        this.review = review;
        this.reviewRating = reviewRating;
        this.ratingAuthorId = ratingAuthorId;
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

    public String getRatingAuthorId() {
        return ratingAuthorId;
    }

    public void setRatingAuthorId(String ratingAuthorId) {
        this.ratingAuthorId = ratingAuthorId;
    }
}
