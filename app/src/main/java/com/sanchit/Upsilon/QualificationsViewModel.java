package com.sanchit.Upsilon;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QualificationsViewModel extends ViewModel {
    private MutableLiveData<String[]> specialities = new MutableLiveData<>();
    private MutableLiveData<String> qualification = new MutableLiveData<>();

    public MutableLiveData<String[]> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(String[] specialities) {
        this.specialities.setValue(specialities);
    }

    public MutableLiveData<String> getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification.setValue(qualification);
    }
}