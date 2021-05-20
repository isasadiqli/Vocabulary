package com.example.vocabularyapp;

public class WordStatusHandler {
    private String category, word;
    private WordStatus status;

    public WordStatusHandler(String category, String word, WordStatus status) {
        this.category = category;
        this.word = word;
        this.status = status;
    }

    public WordStatusHandler(String category, String word) {
        this.category = category;
        this.word = word;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public WordStatus getStatus() {
        return status;
    }

    public void setStatus(WordStatus status) {
        this.status = status;
    }

    public static void readUserWordStatus(){

    }
}
