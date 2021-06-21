package com.sanchit.Upsilon.advancedTestData;

public class TestListItem {
    private String id; //some identification criterion

    private String name;
    private TestStatus status;
//    private String status;
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

    public int getMarksReceived() {
        return marksReceived;
    }

    public void setMarksReceived(int marksReceived) {
        this.marksReceived = marksReceived;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }
}
