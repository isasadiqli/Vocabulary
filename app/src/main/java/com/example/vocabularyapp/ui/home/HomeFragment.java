package com.example.vocabularyapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vocabularyapp.Adapter;
import com.example.vocabularyapp.Dashboard;
import com.example.vocabularyapp.R;
import com.example.vocabularyapp.Word;
import com.example.vocabularyapp.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private ArrayList<String> categoryTitles = new ArrayList<>();

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<Word> words = new ArrayList<>();
    private HashMap<String, String> wordStatus = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                categoryTitles.add("Verbs");
                categoryTitles.add("Adverbs");
                categoryTitles.add("Adjectives");
                categoryTitles.add("Phrases and Idioms");
                categoryTitles.add("User own list for words");

                recyclerView = getView().findViewById(R.id.categories);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new Adapter(getContext(), words, wordStatus, categoryTitles, "categories");
                recyclerView.setAdapter(adapter);

            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}