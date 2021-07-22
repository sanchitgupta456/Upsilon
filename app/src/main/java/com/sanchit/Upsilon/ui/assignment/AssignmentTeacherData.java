package com.sanchit.Upsilon.ui.assignment;

import java.io.Serializable;
import java.util.ArrayList;

public class AssignmentTeacherData implements Serializable {
    //TODO: to be structured

    private String name;
    private String id;
    private String problemUrl;
    private ArrayList<Submission> submissions;
    private Double fullMarks;

    //extra data




    public ArrayList<Submission> getSubmissions() {
        return submissions;
    }

//    public void setSubmissions(ArrayList<Submission> submissions) {
//        this.submissions = submissions;
//    }

    public AssignmentTeacherData(String name) {
        this.name = name;
        submissions = new ArrayList<>();
    }
    public AssignmentTeacherData(String name, Double marksSet) {
        this.name = name;
        this.fullMarks = marksSet;
        submissions = new ArrayList<>();
    }
    public AssignmentTeacherData() {
        submissions = new ArrayList<>();
    }

    public String getProblemUrl() {
        return problemUrl;
    }
    public void setProblemUrl(String problemUrl) {
        this.problemUrl = problemUrl;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getFullMarks() {
        return fullMarks;
    }
    public void setFullMarks(Double fullMarks) {
        this.fullMarks = fullMarks;
    }
}
