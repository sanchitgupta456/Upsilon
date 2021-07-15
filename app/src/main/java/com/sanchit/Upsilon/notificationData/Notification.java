package com.sanchit.Upsilon.notificationData;

import java.io.Serializable;

public class Notification extends Object implements Serializable {
    private String time, date, announcement;

    public Notification(String time, String date, String announcement) {
        this.time = time;
        this.date = date;
        this.announcement = announcement;
    }

    public Notification() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }
}
