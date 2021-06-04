package com.example.vocabularyapp;

import java.util.ArrayList;

public class WordCategories {
    private String categories;
    private ArrayList<Word> words;

    public WordCategories() {

    }

    public WordCategories(String categories, ArrayList<Word> words) {
        this.categories = categories;
        this.words = words;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    @Override
    public String toString() {
        return "WordCategories{" +
                "categories='" + categories + '\'' +
                ", words=" + words +
                '}';
    }
}
