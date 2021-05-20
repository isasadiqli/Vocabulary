package com.example.vocabularyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class WordList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private Adapter adapter;
    private static ArrayList<Word> words;
    private HashMap<String, Boolean > wordStatus = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        recyclerView = findViewById(R.id.wordList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        words = new ArrayList<>();
        ArrayList<String> cat = new ArrayList<>();
        adapter = new Adapter(this, words, wordStatus, cat, "words");


        readWords();

        //Tools.readWords(recyclerView, adapter);


    }

    private void readWords() {
        WordStatusHandler wordStatusHandler = new WordStatusHandler("", "");
        wordStatusHandler.setCategory(Tools.getCategory());

        recyclerView.setAdapter(adapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Words").child(wordStatusHandler.getCategory());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {
                    Word word = snapshot.getValue(Word.class);
                    System.out.println("menim" + word);
                    words.add(word);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static ArrayList<Word> getWords() {
        return words;
    }
}