package com.example.vocabularyapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class User {
    private String name, surname, email, password;
    private HashMap<String, WordStatus> verbs;
    private HashMap<String, WordStatus> adverbs;
    private HashMap<String, WordStatus> adjectives;
    private HashMap<String, WordStatus> phrases;
    private HashMap<String, WordStatus> userOwnList;


    public User(String name, String surname, String email, String password,
                HashMap<String, WordStatus> verbs, HashMap<String, WordStatus> adverbs,
                HashMap<String, WordStatus> adjectives, HashMap<String, WordStatus> phrases,
                HashMap<String, WordStatus> userOwnList) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.verbs = verbs;
        this.adverbs = adverbs;
        this.adjectives = adjectives;
        this.phrases = phrases;
        this.userOwnList = userOwnList;
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

    public HashMap<String, WordStatus> getVerbs() {
        return verbs;
    }

    public void setVerbs(HashMap<String, WordStatus> verbs) {
        this.verbs = verbs;
    }

    public HashMap<String, WordStatus> getAdverbs() {
        return adverbs;
    }

    public void setAdverbs(HashMap<String, WordStatus> adverbs) {
        this.adverbs = adverbs;
    }

    public HashMap<String, WordStatus> getAdjectives() {
        return adjectives;
    }

    public void setAdjectives(HashMap<String, WordStatus> adjectives) {
        this.adjectives = adjectives;
    }

    public HashMap<String, WordStatus> getPhrases() {
        return phrases;
    }

    public void setPhrases(HashMap<String, WordStatus> phrases) {
        this.phrases = phrases;
    }

    public HashMap<String, WordStatus> getUserOwnList() {
        return userOwnList;
    }

    public void setUserOwnList(HashMap<String, WordStatus> userOwnList) {
        this.userOwnList = userOwnList;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", verbs=" + verbs +
                ", adverbs=" + adverbs +
                ", adjectives=" + adjectives +
                ", phrases=" + phrases +
                ", userOwnList=" + userOwnList +
                '}';
    }
}
