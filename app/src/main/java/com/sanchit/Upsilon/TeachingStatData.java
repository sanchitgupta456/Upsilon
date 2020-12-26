package com.sanchit.Upsilon;

public class TeachingStatData {
    private String statField;
    private String statData;

    public TeachingStatData(String statField, String statData) {
        this.statField = statField;
        this.statData = statData;
    }

    public String getStatField() {
        return statField;
    }

    public void setStatField(String statField) {
        this.statField = statField;
    }

    public String getStatData() {
        return statData;
    }

    public void setStatData(String statData) {
        this.statData = statData;
    }
}
