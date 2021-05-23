package com.example.vocabularyapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class User {
    private String name, surname, email, password;
    private HashMap<String, WordStatus> wordStatus;

    public User(String name, String surname, String email, String password, HashMap<String, WordStatus> wordStatus) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.wordStatus = wordStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HashMap<String, WordStatus> getWordStatus() {
        return wordStatus;
    }

    public void setWordStatus(HashMap<String, WordStatus> wordStatus) {
        this.wordStatus = wordStatus;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", wordStatus=" + wordStatus +
                '}';
    }
}
