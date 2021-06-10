package com.sanchit.Upsilon.testData;

import java.util.ArrayList;

public class Question {
    private int number;
    private int full_marks;
    private int marks_received;
    private String questionText;
    private ArrayList<String> mcqOptionsList;
    private int numMCQOptions;
    private String answerText;
    private boolean isMCQSingle;
    private boolean isMCQMultiple;
    private boolean isFileUploadEnabled;
    private boolean isTextEnabled;

    public Question() {
        isFileUploadEnabled = false;
        isTextEnabled = false;
        isMCQMultiple = false;
        isMCQSingle = true;
    }
    //TODO: entry for file upload list

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFull_marks() {
        return full_marks;
    }

    public void setFull_marks(int full_marks) {
        this.full_marks = full_marks;
    }

    public int getMarks_received() {
        return marks_received;
    }

    public void setMarks_received(int marks_received) {
        this.marks_received = marks_received;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public ArrayList<String> getMcqOptionsList() {
        return mcqOptionsList;
    }

    public void setMcqOptionsList(ArrayList<String> mcqOptionsList) {
        this.mcqOptionsList = mcqOptionsList;
    }

    public int getNumMCQOptions() {
        return numMCQOptions;
    }

    public void setNumMCQOptions(int numMCQOptions) {
        this.numMCQOptions = numMCQOptions;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public boolean isMCQSingle() {
        return isMCQSingle;
    }

    public void setMCQSingle(boolean MCQSingle) {
        isMCQSingle = MCQSingle;
    }

    public boolean isMCQMultiple() {
        return isMCQMultiple;
    }

    public void setMCQMultiple(boolean MCQMultiple) {
        isMCQMultiple = MCQMultiple;
    }

    public boolean isFileUploadEnabled() {
        return isFileUploadEnabled;
    }

    public void setFileUploadEnabled(boolean fileUploadEnabled) {
        isFileUploadEnabled = fileUploadEnabled;
    }

    public boolean isTextEnabled() {
        return isTextEnabled;
    }

    public void setTextEnabled(boolean textEnabled) {
        isTextEnabled = textEnabled;
    }
}
