package com.sanchit.Upsilon.classData;

public class ScheduledClass {
    String className;
    String day;
    String month;
    String time;

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
