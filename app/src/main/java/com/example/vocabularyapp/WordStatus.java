package com.example.vocabularyapp;

import java.util.ArrayList;

public class WordStatus {
    private boolean memorized, addedToList;

    public WordStatus() {
    }

    public boolean isMemorized() {
        return memorized;
    }


    public void setMemorized(boolean memorized) {
        this.memorized = memorized;
    }

    public boolean isAddedToList() {
        return addedToList;
    }

    public void setAddedToList(boolean addedToList) {
        this.addedToList = addedToList;
    }
}
