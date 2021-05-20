package com.example.vocabularyapp;

import java.util.ArrayList;
import java.util.HashMap;

public class Word {
    private String word;
    private String definition;
    private String example;
    //private HashMap<String, String> status = new HashMap<>();//key = userID, value = status

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }

    public String getExample() {
        return example;
    }

    /*public HashMap<String, String> getStatus() {
        return status;
    }*/

    @Override
    public String toString() {
        return "word: " + word + '\n' +
                "definition: " + definition + '\n' +
                "example: " + example;
    }
}
