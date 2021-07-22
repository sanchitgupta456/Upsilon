package com.sanchit.Upsilon.ui.assignment;

import java.io.Serializable;
import java.util.ArrayList;

public class AssignmentTeacherData implements Serializable {
    //TODO: to be structured

    private String name;

    private String problemUrl;

    public String getProblemUrl() {
        return problemUrl;
    }

    public void setProblemUrl(String problemUrl) {
        this.problemUrl = problemUrl;
    }

    private ArrayList<String> submissions;

    private Double fullMarks;

    private Double receivedMarks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(ArrayList<String> submissions) {
        this.submissions = submissions;
    }

    public Double getFullMarks() {
        return fullMarks;
    }

    public void setFullMarks(Double fullMarks) {
        this.fullMarks = fullMarks;
    }

    public Double getReceivedMarks() {
        return receivedMarks;
    }

    public void setReceivedMarks(Double receivedMarks) {
        this.receivedMarks = receivedMarks;
    }

    public AssignmentTeacherData(String name) {
        this.name = name;
    }
    public AssignmentTeacherData() {
    }
}
