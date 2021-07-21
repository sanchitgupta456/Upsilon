package com.sanchit.Upsilon.classData;

import java.io.Serializable;

public class ScheduledClass implements Serializable {
    String className;
    long timestamp;
    String date;
    String month;
    String time;
    String endtime;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ScheduledClass(String className, long timestamp, String date, String month, String time, String endtime, String id) {
        this.className = className;
        this.timestamp = timestamp;
        this.date = date;
        this.month = month;
        this.time = time;
        this.endtime = endtime;
        this.id = id;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public ScheduledClass(String className, long timestamp, String date, String month, String time, String endtime) {
        this.className = className;
        this.timestamp = timestamp;
        this.date = date;
        this.month = month;
        this.time = time;
        this.endtime = endtime;
    }

    public ScheduledClass(String className, long timestamp, String date, String month, String time) {
        this.className = className;
        this.date = date;
        this.month = month;
        this.timestamp = timestamp;
        this.time = time;
    }

    public ScheduledClass() {
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
