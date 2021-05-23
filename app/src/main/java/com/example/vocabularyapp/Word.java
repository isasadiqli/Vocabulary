package com.example.vocabularyapp;

import java.util.ArrayList;
import java.util.HashMap;

public class Word {
    private String word;
    private String definition;
    private String example;
    private String category;
    //private HashMap<String, String> status = new HashMap<>();//key = userID, value = status

    public Word(){

    }

    public Word(String word, String definition, String example, String category) {
        this.word = word;
        this.definition = definition;
        this.example = example;
        this.category = category;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }

    public String getExample() {
        return example;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", definition='" + definition + '\'' +
                ", example='" + example + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
