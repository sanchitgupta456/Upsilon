package com.sanchit.Upsilon;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExperienceViewModel extends ViewModel {
    private MutableLiveData<String> experience = new MutableLiveData<>();

    public MutableLiveData<String> getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience.setValue(experience);
    }
}