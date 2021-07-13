package com.sanchit.Upsilon.courseData;

import java.io.Serializable;
import java.util.ArrayList;

public class ScheduledClasses extends Object implements Serializable {
    private String ClassName;
    private long Date;
    private ArrayList<String> images;

    public ScheduledClasses(String className, long date, ArrayList<String> images) {
        ClassName = className;
        Date = date;
        this.images = images;
    }

    public ScheduledClasses() {
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
