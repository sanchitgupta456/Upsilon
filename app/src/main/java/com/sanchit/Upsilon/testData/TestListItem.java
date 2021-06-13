package com.sanchit.Upsilon.testData;

public class TestListItem {
    private String id; //some identification criterion

    private String name;
    private String status;
    private int marksReceived;
    private int totalMarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMarksReceived() {
        return marksReceived;
    }

    public void setMarksReceived(int marksReceived) {
        this.marksReceived = marksReceived;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }
}
