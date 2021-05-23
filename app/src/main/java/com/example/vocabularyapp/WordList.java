package com.example.vocabularyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class WordList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private Adapter adapter;
    private static ArrayList<Word> words = new ArrayList<>();
    private HashMap<String, Boolean> wordStatus = new HashMap<>();

    private static ArrayList<Word> verbs = new ArrayList<>();
    private static ArrayList<Word> adverbs = new ArrayList<>();
    private static ArrayList<Word> adjectives = new ArrayList<>();
    private static ArrayList<Word> phrases = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Tools.getCategory(true));

        TypedValue typedValue = new TypedValue();
        this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        final int color = typedValue.data;

        actionBar.setBackgroundDrawable(new ColorDrawable(color));

        recyclerView = findViewById(R.id.wordList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        words = new ArrayList<>();
        ArrayList<String> cat = new ArrayList<>();
        ArrayList<Integer> catI = new ArrayList<>();
        adapter = new Adapter(this, words, wordStatus, cat, catI, "words");

        readWords();
    }

    private void readWords() {
        WordStatusHandler wordStatusHandler = new WordStatusHandler("", "");
        wordStatusHandler.setCategory(Tools.getCategory());

        recyclerView.setAdapter(adapter);

        if (Tools.getCategory().equals("userWordList")) {
            DatabaseReference userID = FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            userID.child("userwordlist").addValueEventListener(getListener());

        } else {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Words").child(wordStatusHandler.getCategory());
            System.out.println("menim 102 " + wordStatusHandler.getCategory());
            databaseReference.addValueEventListener(getListener());
        }

    }

    @NotNull
    private ValueEventListener getListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {
                    Word word = snapshot.getValue(Word.class);

                    if (search(word.getWord()))
                        words.add(word);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    public static ArrayList<Word> getWords() {
        return words;
    }

    private static boolean search(String word) {
        for (Word w :
                words) {
            if (word != null && w != null)
                if (word.equals(w.getWord()))
                    return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}