package com.sanchit.Upsilon.courseData;

import java.io.Serializable;

public class CourseLocation extends Object implements Serializable {
    Double latitude;
    Double longitude;

    public CourseLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CourseLocation() {
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
