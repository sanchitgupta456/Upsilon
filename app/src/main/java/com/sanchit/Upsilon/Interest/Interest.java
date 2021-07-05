package com.sanchit.Upsilon.Interest;

public class Interest {
    private String nameInterest;
    private String imageUri;
    public boolean isSelected;

    public Interest(String nameInterest) {
        this.nameInterest = nameInterest;
        this.isSelected = false;
    }

    public Interest(String nameInterest, String imageUri) {
        this.nameInterest = nameInterest;
        this.isSelected = false;
        this.imageUri = imageUri;
    }

    public String getNameInterest() {
        return nameInterest;
    }

    public void setNameInterest(String nameInterest) {
        this.nameInterest = nameInterest;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void select(boolean b) {
        isSelected = b;
    }
}
