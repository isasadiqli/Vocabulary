package com.example.vocabularyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Quiz extends AppCompatActivity {

    private static int categoryPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
    }

    public static void setCategoryPosition(int categoryPosition) {
        Quiz.categoryPosition = categoryPosition;
    }
}