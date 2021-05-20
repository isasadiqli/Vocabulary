package com.example.vocabularyapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class User {
    private String name, surname, email, password;
    private HashMap<String, String> verbs;
    private HashMap<String, String> adverbs;
    private HashMap<String, String> adjectives;
    private HashMap<String, String> phrases;
    private HashMap<String, String> userOwnList;


    public User(String name, String surname, String email, String password,
                HashMap<String, String> verbs, HashMap<String, String> adverbs,
                HashMap<String, String> adjectives, HashMap<String, String> phrases,
                HashMap<String, String> userOwnList) {
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

    public HashMap<String, String> getVerbs() {
        return verbs;
    }

    public void setVerbs(HashMap<String, String> verbs) {
        this.verbs = verbs;
    }

    public HashMap<String, String> getAdverbs() {
        return adverbs;
    }

    public void setAdverbs(HashMap<String, String> adverbs) {
        this.adverbs = adverbs;
    }

    public HashMap<String, String> getAdjectives() {
        return adjectives;
    }

    public void setAdjectives(HashMap<String, String> adjectives) {
        this.adjectives = adjectives;
    }

    public HashMap<String, String> getPhrases() {
        return phrases;
    }

    public void setPhrases(HashMap<String, String> phrases) {
        this.phrases = phrases;
    }

    public HashMap<String, String> getUserOwnList() {
        return userOwnList;
    }

    public void setUserOwnList(HashMap<String, String> userOwnList) {
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
