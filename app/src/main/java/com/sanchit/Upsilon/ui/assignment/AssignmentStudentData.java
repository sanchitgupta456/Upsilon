package com.sanchit.Upsilon.ui.assignment;

import java.io.Serializable;
import java.util.ArrayList;

public class AssignmentStudentData implements Serializable {
    private String name;
    private String problemUrl;

    private Submission submission;

    private Double fullMarks;

    private Double receivedMarks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



//    public Double getFullMarks() {
//        return fullMarks;
//    }
//
//    public void setFullMarks(Double fullMarks) {
//        this.fullMarks = fullMarks;
//    }
//
//    public Double getReceivedMarks() {
//        return receivedMarks;
//    }
//
//    public void setReceivedMarks(Double receivedMarks) {
//        this.receivedMarks = receivedMarks;
//    }

    public AssignmentStudentData(String name) {
        this.name = name;
    }
    public AssignmentStudentData() {
    }

    public String getProblemUrl() {
        return problemUrl;
    }
//    public Submission getSubmission() {
//        return submission;
//    }
//    public void setSubmission(Submission submission) {
//        this.submission = submission;
//    }
    public Boolean submit(ArrayList<String> urls) {
        submission = new Submission();
        submission.setUrls(urls);
        //TODO: formalities

        //TODO: if success
        return true; //TODO: return false
    }
}
