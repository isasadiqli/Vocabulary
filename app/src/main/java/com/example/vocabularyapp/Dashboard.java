package com.example.vocabularyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vocabularyapp.databinding.ActivityDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity {

    private DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDashboardBinding binding;
    public static ArrayList<Word> words = new ArrayList<>();

    private static ArrayList<Integer> indexes;
    private static int indexOfIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDashboard.toolbar);
        binding.appBarDashboard.fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_hard_words, R.id.nav_profile)
                .setOpenableLayout(drawer)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dashboard);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    private static void generateRandomIndexes(int size) {
        indexes = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            indexes.add(i);
        }

        Collections.shuffle(indexes);
    }

    public static void initializeIndex(){
        indexOfIndex = 0;
    }

    public static int getIndex() {
        if (indexOfIndex < 10)
            return indexes.get(indexOfIndex++);
        indexOfIndex = 0;
        return -1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Tools tools = new Tools();
        tools.setUsername(findViewById(R.id.username));
        tools.setUserMail(findViewById(R.id.userMail));
        tools.readUserInfo("dashboard");

        HashMap<String, WordCategories> wordCategoriesHashMap = new HashMap<>();
        readWords(wordCategoriesHashMap);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categoriesSpinner, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {

                    generateRandomIndexes(10);

                    Tools.setCategoryPosition(position - 1);
                    if(Tools.getCategory().equals("Verbs")){
                        words = wordCategoriesHashMap.get("Verbs").getWords();
                    }
                    else if(Tools.getCategory().equals("Adjectives")){
                        words = wordCategoriesHashMap.get("Adjectives").getWords();
                    }
                    else if(Tools.getCategory().equals("Phrases")){
                        words = wordCategoriesHashMap.get("Phrases").getWords();
                    }
                    else if(Tools.getCategory().equals("Adverbs")){
                        words = wordCategoriesHashMap.get("Adverbs").getWords();
                    }

                    Intent intent = new Intent(Dashboard.this, Quiz.class);
                    startActivity(intent);
                    spinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    private void readWords(HashMap<String, WordCategories> wordCategories) {
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Words");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds :
                        snapshot.getChildren()) {
                    ArrayList<Word> word = new ArrayList<>();
                    for (DataSnapshot dsv :
                            ds.getChildren()) {
                        word.add(dsv.getValue(Word.class));
                    }
                    WordCategories wordCat = new WordCategories(ds.getKey(), word);
                    wordCategories.put(ds.getKey(), wordCat);
                }
                Toast.makeText(Dashboard.this, "words read", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dashboard);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    public void logOutClicked(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }


}