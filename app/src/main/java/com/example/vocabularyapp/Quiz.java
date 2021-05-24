package com.example.vocabularyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Quiz extends AppCompatActivity {

    private static int categoryPosition;
    private ArrayList<Word> words;
    private ArrayList<Integer> indexForRB;
    private int index;
    private TextView wordTV;

    private RadioButton[] radioButtons;
    private int[] radioButtonIds;
    private RadioGroup radioGroup;
    private boolean correct = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        words = new ArrayList<>();
        wordTV = findViewById(R.id.word_quiz);

        radioGroup = findViewById((R.id.radioGroup));

        radioButtonIds = new int[4];
        radioButtonIds[0] = R.id.radioButton0;
        radioButtonIds[1] = R.id.radioButton1;
        radioButtonIds[2] = R.id.radioButton2;
        radioButtonIds[3] = R.id.radioButton3;

        radioButtons = new RadioButton[4];
        radioButtons[0] = findViewById(R.id.radioButton0);
        radioButtons[1] = findViewById(R.id.radioButton1);
        radioButtons[2] = findViewById(R.id.radioButton2);
        radioButtons[3] = findViewById(R.id.radioButton3);

        Tools.setCategoryPosition(categoryPosition - 1);
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Words");
        dRef.child(Tools.getCategory()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds :
                        snapshot.getChildren()) {
                    Word word = ds.getValue(Word.class);
                    words.add(word);
                }

                int size = words.size();
                index = Dashboard.getIndex();
                generateQuizLevels(size, index);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    public void radioButtonClicked(View v) {
        int radioID = radioGroup.getCheckedRadioButtonId();

        if (radioID == radioButtonIds[indexForRB.get(0)]) {
            correct = true;
            radioButtons[indexForRB.get(0)].setBackgroundColor(Color.GREEN);
        } else {
            //radioButtons[radioID].setBackgroundColor(Color.RED);
        }
    }

    public void continueClicked(View v) {
        if (correct && index != -1) {
            Intent intent = new Intent(this, Quiz.class);
            finish();
            startActivity(intent);
        }
    }

    private void generateQuizLevels(int size, int index) {

        if (index != -1) {
            Random rand = new Random();
            int[] indexForVar = new int[3];
            indexForVar[0] = rand.nextInt(size);
            indexForVar[1] = rand.nextInt(size);
            indexForVar[2] = rand.nextInt(size);

            indexForRB = new ArrayList<>();

            for (int i = 0; i < 4; i++)
                indexForRB.add(i);
            Collections.shuffle(indexForRB);

            while ((indexForVar[0] == indexForVar[1]) || (indexForVar[1] == indexForVar[2])
                    || (indexForVar[0] == indexForVar[2]) || (indexForVar[1] == index)
                    || (indexForVar[2] == index) || (indexForVar[0] == index)) {

                if (indexForVar[0] == indexForVar[1] || indexForVar[1] == index) {
                    indexForVar[1]++;
                    if (indexForVar[1] >= 10)
                        indexForVar[1] -= 2;
                }

                if (indexForVar[1] == indexForVar[2] || indexForVar[2] == index) {
                    indexForVar[2]++;
                    if (indexForVar[2] >= 10)
                        indexForVar[2] -= 2;
                }

                if (indexForVar[0] == indexForVar[2] || indexForVar[0] == index) {
                    indexForVar[0]++;
                    if (indexForVar[0] >= 10)
                        indexForVar[0] -= 2;
                }
            }


            wordTV.setText(words.get(index).getWord());
            radioButtons[indexForRB.get(0)].setText(words.get(index).getDefinition());
            radioButtons[indexForRB.get(1)].setText(words.get(indexForVar[0]).getDefinition());
            radioButtons[indexForRB.get(2)].setText(words.get(indexForVar[1]).getDefinition());
            radioButtons[indexForRB.get(3)].setText(words.get(indexForVar[2]).getDefinition());
        }
        else
            finish();//change it later

    }

    public static void setCategoryPosition(int categoryPosition) {
        Quiz.categoryPosition = categoryPosition;

    }
}