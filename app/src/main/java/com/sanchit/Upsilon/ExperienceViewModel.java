package com.sanchit.Upsilon;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ExperienceViewModel extends ViewModel {
    private MutableLiveData<ArrayList<String>> experience = new MutableLiveData<>();

    public MutableLiveData<ArrayList<String>> getExperience() {
        return experience;
    }

    public void setExperience(ArrayList<String> experience) {
        this.experience.setValue(experience);
    }

}