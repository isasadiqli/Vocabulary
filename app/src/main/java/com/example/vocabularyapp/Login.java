package com.example.vocabularyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private static int haveAccount = 0;
    private EditText name, surname, email, password, passwordReentered;
    private Button loginButton;
    private TextView signUpIn;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();


        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordReentered = findViewById(R.id.passwordReentered);
        loginButton = findViewById(R.id.loginButton);
        signUpIn = findViewById(R.id.signUp);
        progressBar = findViewById(R.id.progressBar);


        Tools.editTextListener(loginButton, name, "name", true);
        Tools.editTextListener(loginButton, surname, "surname", true);
        Tools.editTextListener(loginButton, email, "email", true);
        Tools.editTextListener(loginButton, password, "password", true);
        Tools.editTextListener(loginButton, passwordReentered, "passwordReentered", true);

    }

    public void login(View v) {
        if (Tools.isSignedUp()) {
            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.signInWithEmailAndPassword(String.valueOf(email.getText()), String.valueOf(password.getText()))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            Tools tools = new Tools();
                            tools.readUserInfo("login");

                            Intent intent = new Intent(Login.this, Dashboard.class);
                            finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });


        } else {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(String.valueOf(email.getText()),
                    String.valueOf(password.getText())).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    Tools tools = new Tools();
                    tools.writeNewUserToFirebase(progressBar, this,
                            name, surname, email, password, passwordReentered, loginButton, signUpIn);

                }
            });
        }

    }

    public static void signUp(EditText name, EditText surname, EditText email, EditText password,
                              EditText passwordReentered, Button loginButton, TextView signUpIn) {
        haveAccount++;
        if (haveAccount % 2 != 0) {
            Tools.setSignedUp(false);

            name.setVisibility(View.VISIBLE);
            surname.setVisibility(View.VISIBLE);
            passwordReentered.setVisibility(View.VISIBLE);

            loginButton.setText(R.string.signUp);
            signUpIn.setText(R.string.haveAccount);


        } else {
            Tools.setSignedUp(true);

            name.setVisibility(View.GONE);
            surname.setVisibility(View.GONE);
            passwordReentered.setVisibility(View.GONE);

            loginButton.setText(R.string.login);
            signUpIn.setText(R.string.need_an_account);
        }

    }

    public void signUpClicked(View v) {
        signUp(name, surname, email, password, passwordReentered, loginButton, signUpIn);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(Login.this, Dashboard.class);
            finish();
            startActivity(intent);

        }
        //FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //updateUI(currentUser);
    }
}