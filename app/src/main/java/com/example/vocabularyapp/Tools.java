package com.example.vocabularyapp;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Tools {

    private static String password = "", passwordReentered = " ";
    private static boolean signedUp = true;
    private static boolean nameEmpty = true;
    private static boolean surnameEmpty = true;
    private static boolean emailEmpty = true;
    private static boolean passwordEmpty = true;
    private static boolean passwordReenteredEmpty = true;

    private static ArrayList<Word> verbs = new ArrayList<>();
    private static ArrayList<Word> adverbs = new ArrayList<>();
    private static ArrayList<Word> adjectives = new ArrayList<>();
    private static ArrayList<Word> phrases = new ArrayList<>();
    private static int categoryPosition, wordPosition;
    private static User user;
    private static ArrayList<Word> words;

    private TextView username, userMail;

    private static WordStatusHandler wordStatusHandler;

    public void setUsername(TextView username) {
        this.username = username;
    }

    public void setUserMail(TextView userMail) {
        this.userMail = userMail;
    }

    public static User getUser() {
        return user;
    }

    public static String getCategory() {
        switch (categoryPosition){
            case 0:
                return "Verbs";
            case 1:
                return "Adverbs";
            case 2:
                return "Adjectives";
            case 3:
                return "Phrases";
            case 4:
                return "UserOwnList";
        }
        return "";
    }

    public static void setCategoryPosition(int categoryPosition) {
        Tools.categoryPosition = categoryPosition;
    }

    public static int getWordPosition() {
        return wordPosition;
    }

    public static void setWordPosition(int wordPosition) {
        Tools.wordPosition = wordPosition;
    }

    private static void setPassword(String password) {
        Tools.password = password;
    }

    private static void setPasswordReentered(String passwordReentered) {
        Tools.passwordReentered = passwordReentered;
    }

    private static boolean arePasswordsMatch() {
        return password.equals(passwordReentered);
    }

    public static boolean isSignedUp() {
        return signedUp;
    }

    public static void setSignedUp(boolean signedUp) {
        Tools.signedUp = signedUp;
    }

    public static String readFromFile(Context context, String fileName) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append("").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public static void writeToFile(String data, Context context, String fileName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static void editTextListener(Button button, EditText editText, String error) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //editText.setError("Enter " + error);
                if (error.equals("name")) {
                    nameEmpty = s.toString().trim().length() == 0;
                } else if (error.equals("surname")) {
                    surnameEmpty = s.toString().trim().length() == 0;
                } else if (error.equals("email")) {
                    emailEmpty = s.toString().trim().length() == 0;
                } else if (error.equals("password")) {
                    passwordEmpty = s.toString().trim().length() == 0;
                    setPassword(String.valueOf(editText.getText()));
                } else if (error.equals("passwordReentered")) {
                    passwordReenteredEmpty = s.toString().trim().length() == 0;
                    setPasswordReentered(String.valueOf(editText.getText()));
                }

                if (!signedUp) {
                    button.setEnabled(!(nameEmpty || surnameEmpty || emailEmpty || passwordEmpty || passwordReenteredEmpty)
                            && arePasswordsMatch() && password.trim().length() >= 6);
                } else button.setEnabled(!(emailEmpty || passwordEmpty));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void writeNewUserToFirebase(ProgressBar progressBar, Context context, EditText name, EditText surname, EditText email,
                                       EditText password, EditText passwordReentered, Button loginButton, TextView signUpIn) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Words");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                HashMap<String, String> verbStatus = new HashMap<>();
                HashMap<String, String> adverbStatus = new HashMap<>();
                HashMap<String, String> adjectiveStatus = new HashMap<>();
                HashMap<String, String> phrasesStatus = new HashMap<>();
                HashMap<String, String> userOwnListStatus = new HashMap<>();

                writeWordsToHashmap(snapshot, verbs, "Verbs", verbStatus);
                writeWordsToHashmap(snapshot, adverbs, "Adverbs", adverbStatus);
                writeWordsToHashmap(snapshot, adjectives, "Adjectives", adjectiveStatus);
                writeWordsToHashmap(snapshot, phrases, "Phrases", phrasesStatus);


                User user = new User(String.valueOf(name.getText()),
                        String.valueOf(surname.getText()),
                        String.valueOf(email.getText()),
                        String.valueOf(password.getText()),
                        verbStatus, adverbStatus, adjectiveStatus, phrasesStatus, userOwnListStatus);

                DatabaseReference userID = FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                if (Tools.isSignedUp()) {
                    //userID.child("verbs").setValue(wordStatus);
                } else {

                    userID.setValue(user).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(context.getApplicationContext(), "Successfully registered", Toast.LENGTH_SHORT).show();
                            Login.signUp(name, surname, email, password, passwordReentered, loginButton, signUpIn);

                        } else {

                            if (task1.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(context.getApplicationContext(), "This email already registered", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(context.getApplicationContext(), task1.getException().getMessage(), Toast.LENGTH_SHORT).show();


                        }
                        progressBar.setVisibility(View.GONE);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void writeWordsToHashmap(@NonNull @NotNull DataSnapshot snapshot,
                                    ArrayList<Word> wordArrayList, String category, HashMap<String, String> wordStatus) {
        for (DataSnapshot dataSnapshot :
                snapshot.child(category).getChildren()) {
            wordArrayList.add(dataSnapshot.getValue(Word.class));
        }

        for (Word w :
                wordArrayList) {
            wordStatus.put(w.getWord(), "Remind me later");
        }
    }

    public void readUserInfo(String location) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        System.out.println("MENIM " + userID);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                String name = snapshot.child(userID).child("name").getValue(String.class);
                String surname = snapshot.child(userID).child("surname").getValue(String.class);
                String email = snapshot.child(userID).child("email").getValue(String.class);
                String password = snapshot.child(userID).child("password").getValue(String.class);

                HashMap<String, String> verbStatus = new HashMap<>();
                HashMap<String, String> adverbStatus = new HashMap<>();
                HashMap<String, String> adjectiveStatus = new HashMap<>();
                HashMap<String, String> phrasesStatus = new HashMap<>();
                HashMap<String, String> userOwnListStatus = new HashMap<>();

                readWordsToHashmap(snapshot, verbStatus, userID, "verbs");
                readWordsToHashmap(snapshot, adverbStatus, userID, "adverbs");
                readWordsToHashmap(snapshot, adjectiveStatus, userID, "adjectives");
                readWordsToHashmap(snapshot, phrasesStatus, userID, "phrases");
                readWordsToHashmap(snapshot, userOwnListStatus, userID, "userOwnList");

                user = new User(name, surname, email, password, verbStatus, adverbStatus, adjectiveStatus, phrasesStatus, userOwnListStatus);
//                System.out.println("MENIM" + user.toString());

                if (location.equals("dashboard")) {
                    String userNameSurname = name + " " + surname;
                    username.setText(userNameSurname);
                    userMail.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void readWordsToHashmap(@NonNull @NotNull DataSnapshot snapshot, HashMap<String, String> wordStatus,
                                    String userID, String category) {
        for (DataSnapshot ds :
                snapshot.child(userID).child(category).getChildren()) {
            wordStatus.put(ds.getKey(), ds.getValue(String.class));
        }
    }

    public static void updateWordStatus(WordStatusHandler wordStatusHandler) {
        DatabaseReference userID = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userID.child(wordStatusHandler.getCategory()).child(wordStatusHandler.getWord()).setValue(wordStatusHandler.getStatus())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println("menim Succes");
                    }
                    else System.out.println("menim unsucces");
                });

    }

    public static void getWordStatus(HashMap<String,String> wordStatus, WordStatusHandler wordStatusHandler, Button button){

        DatabaseReference userID = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userID.child(wordStatusHandler.getCategory()).child(wordStatusHandler.getWord()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String w = snapshot.getValue().toString();
                wordStatus.put(wordStatusHandler.getWord(), w);

                if(w.equals("Remind me later")){
                    button.setText("      Memorized      ");
                    button.setBackgroundColor(Color.BLUE);
                }
                else {
                    button.setText(" Remind me later ");
                    button.setBackgroundColor(Color.parseColor("#74E96F"));
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public static void addWordsToUserList(RecyclerView recyclerView, Adapter adapter){


    }
}
