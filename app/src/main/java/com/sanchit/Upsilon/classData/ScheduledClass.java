package com.sanchit.Upsilon.classData;

import java.io.Serializable;

public class ScheduledClass implements Serializable {
    String className;
    String day;
    String month;
    String time;
    String endTime;

    public ScheduledClass() {
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public ScheduledClass(String className, String day, String month, String time) {
        this.className = className;
        this.day = day;
        this.month = month;
        this.time = time;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
