package com.example.vocabularyapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QuizFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static int categoryPosition;
    private static ArrayList<Word> words;
    private ArrayList<Integer> indexForRB;
    private int index;
    private TextView wordTV;
    private ProgressBar progressBar;
    public static int score = 0;

    private RadioButton[] radioButtons;
    private int[] radioButtonIds;
    private RadioGroup radioGroup;
    private boolean correct = false;


    private View view;

    public static boolean finished = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QuizFragment() {
        // Required empty public constructor
    }

    public static QuizFragment newInstance(String param1, String param2) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            progressBar.setProgress(0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_quiz, container, false);
        setHasOptionsMenu(true);
        words = new ArrayList<>();
        wordTV = view.findViewById(R.id.word_quiz);

        radioGroup = view.findViewById((R.id.radioGroup));

        radioButtonIds = new int[4];
        radioButtonIds[0] = R.id.radioButton0;
        radioButtonIds[1] = R.id.radioButton1;
        radioButtonIds[2] = R.id.radioButton2;
        radioButtonIds[3] = R.id.radioButton3;

        radioButtons = new RadioButton[4];
        radioButtons[0] = view.findViewById(R.id.radioButton0);
        radioButtons[1] = view.findViewById(R.id.radioButton1);
        radioButtons[2] = view.findViewById(R.id.radioButton2);
        radioButtons[3] = view.findViewById(R.id.radioButton3);

        progressBar = getActivity().findViewById(R.id.quizProgressBar);


        words = Dashboard.words;
        int size = 10;
        index = Dashboard.getIndex();
        generateQuizLevels(size, index);
        progressBar.setMax(size);

        if (score == size)
            score = 0;
        progressBar.setProgress(score);

        view.findViewById(R.id.continue_button).setOnClickListener(this::continueClicked);

        for (RadioButton rb :
                radioButtons) {
            rb.setOnClickListener(this::radioButtonClicked);
        }

        return view;
    }

    public void radioButtonClicked(View v) {
        int radioID = radioGroup.getCheckedRadioButtonId();

        if (radioID == radioButtonIds[indexForRB.get(0)]) {
            correct = true;
            radioButtons[indexForRB.get(0)].setBackgroundColor(Color.GREEN);

            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                if (i != indexForRB.get(0))
                    (radioGroup.getChildAt(i)).setEnabled(false);
            }
        } else {
            view.findViewById(radioID).setEnabled(false);
        }
    }

    public void continueClicked(View v) {
        if (correct && index != -1) {
            score++;

            if (score == words.size()) {
                Dashboard.initializeIndex();
                progressBar.setProgress(score);

                TextView textView = view.findViewById(R.id.finished_textview);
                textView.setText(Tools.getCongratsCategory());

                view.findViewById(R.id.finished_layout).setVisibility(View.VISIBLE);
                view.findViewById(R.id.quiz_layout).setVisibility(View.GONE);
                view.findViewById(R.id.finished_button).setOnClickListener(v1 -> {
                    getActivity().finish();
                    score = 0;
                });

            } else {
                QuizFragment fragment2 = new QuizFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left);
                fragmentTransaction.replace(R.id.fragmentContainerView, fragment2);
                fragmentTransaction.commit();
            }
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
        } else {
            //getActivity().finish();//change it later
        }
    }

    public static void setCategoryPosition(int categoryPosition) {
        QuizFragment.categoryPosition = categoryPosition;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
            Dashboard.initializeIndex();
            getActivity().finish();
            score = 0;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}