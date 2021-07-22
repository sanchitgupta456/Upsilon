package com.sanchit.Upsilon.ui.assignment;

import java.util.ArrayList;

public class Submission {
    private String assignmentID;
    private String submissionID;
    private String studentID;
    private ArrayList<String> urls;
    private Boolean evaluated;
    private Double totalMarks;
    private Double marksScored;

    public String getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(String assignmentID) {
        this.assignmentID = assignmentID;
    }

    public String getSubmissionID() {
        return submissionID;
    }

    public void setSubmissionID(String submissionID) {
        this.submissionID = submissionID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    public Boolean getEvaluated() {
        return evaluated;
    }

    public void setEvaluated(Boolean evaluated) {
        this.evaluated = evaluated;
    }

    public Double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Double totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Double getMarksScored() {
        return marksScored;
    }

    public void setMarksScored(Double marksScored) {
        this.marksScored = marksScored;
    }

    public Submission() {
        this.evaluated = false;
    }
}
