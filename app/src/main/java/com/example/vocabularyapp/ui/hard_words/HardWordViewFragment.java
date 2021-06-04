package com.example.vocabularyapp.ui.hard_words;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HardWordViewFragment extends ViewModel {

    private MutableLiveData<String> mText;

    public HardWordViewFragment() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}