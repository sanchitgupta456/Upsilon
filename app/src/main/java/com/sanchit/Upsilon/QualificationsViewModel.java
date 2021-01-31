package com.sanchit.Upsilon;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class QualificationsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<String>> specialities = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> qualifications = new MutableLiveData<>();

    public MutableLiveData<ArrayList<String>> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(ArrayList<String> specialities) {
        this.specialities.setValue(specialities);
    }

    public MutableLiveData<ArrayList<String>> getQualifications() {
        return qualifications;
    }

    public void setQualifications(ArrayList<String> qualifications) {
        this.qualifications.setValue(qualifications);
    }
}