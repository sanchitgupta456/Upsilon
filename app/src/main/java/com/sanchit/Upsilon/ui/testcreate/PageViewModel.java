package com.sanchit.Upsilon.ui.testcreate;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sanchit.Upsilon.advancedTestData.Question;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private MutableLiveData<Question> mQuestion = new MutableLiveData<>();
//    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
//        @Override
//        public String apply(Integer input) {
//            return "Hello world from section: " + input;
//        }
//    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

//    public LiveData<String> getText() {
//        return mText;
//    }
}