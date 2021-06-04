package com.example.vocabularyapp.ui.hard_words;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vocabularyapp.Adapter;
import com.example.vocabularyapp.Alarm;
import com.example.vocabularyapp.Dashboard;
import com.example.vocabularyapp.Quiz;
import com.example.vocabularyapp.R;
import com.example.vocabularyapp.Tools;
import com.example.vocabularyapp.Word;
import com.example.vocabularyapp.WordList;
import com.example.vocabularyapp.WordStatus;
import com.example.vocabularyapp.WordStatusHandler;
import com.example.vocabularyapp.databinding.FragmentHardWordsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class HardWordsFragment extends Fragment {

    private HardWordViewFragment hardWordViewFragment;
    private FragmentHardWordsBinding binding;

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private Adapter adapter;
    private static ArrayList<Word> words = new ArrayList<>();
    private HashMap<String, Boolean> wordStatus = new HashMap<>();

    private static ArrayList<Word> verbs = new ArrayList<>();
    private static ArrayList<Word> adverbs = new ArrayList<>();
    private static ArrayList<Word> adjectives = new ArrayList<>();
    private static ArrayList<Word> phrases = new ArrayList<>();

    public static long interval = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hardWordViewFragment =
                new ViewModelProvider(this).get(HardWordViewFragment.class);

        binding = FragmentHardWordsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        hardWordViewFragment.getText().observe(getViewLifecycleOwner(), s -> {

            recyclerView = getView().findViewById(R.id.hardWordList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            words = new ArrayList<>();
            ArrayList<String> cat = new ArrayList<>();
            ArrayList<Integer> catI = new ArrayList<>();
            adapter = new Adapter(getContext(), words, wordStatus, cat, catI, "words");

            Tools.setCategoryPosition(4);
            readWords();

            final int[] typeOfTime = {0};

            Spinner spinner = getView().findViewById(R.id.spinnerTime);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.typesOfTime, android.R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(0);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    typeOfTime[0] = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            createNotificationChannel();
            getView().findViewById(R.id.confirm).setOnClickListener(v -> {

                Toast.makeText(getContext(), "reminderrrrr", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), Alarm.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                switch (typeOfTime[0]) {
                    case 0:
                        interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                        break;
                    case 1:
                        interval = AlarmManager.INTERVAL_HALF_HOUR;
                        break;
                    case 2:
                        interval = AlarmManager.INTERVAL_HOUR;
                        break;
                    case 3:
                        interval = AlarmManager.INTERVAL_HALF_DAY;
                        break;
                    case 4:
                        interval = AlarmManager.INTERVAL_DAY;
                        break;
                }

                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),
                        interval, pendingIntent);

            });

            getView().findViewById(R.id.addNewWord).setOnClickListener(this::addNewWordClicked);


            OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
                @Override
                public void handleOnBackPressed() {
                    // Handle the back button event
                    getActivity().finish();
                }
            };
            requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        });
        return root;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NotificationName";
            String description = "Channel for notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel1", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    private void readWords() {
        WordStatusHandler wordStatusHandler = new WordStatusHandler("", "");
        wordStatusHandler.setCategory(Tools.getCategory());

        recyclerView.setAdapter(adapter);

        DatabaseReference userID = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userID.child("userwordlist").addValueEventListener(getListener());

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

    public void addNewWordClicked(View v) {
        Dialog dialog = new Dialog(getContext(), R.style.Dialog);
        dialog.setContentView(R.layout.add_word_dialog);
        dialog.setTitle("Add new word");

        EditText wordTV = dialog.findViewById(R.id.add_word);
        EditText definitionTV = dialog.findViewById(R.id.add_definition);
        EditText exampleTV = dialog.findViewById(R.id.add_example);

        Button add = dialog.findViewById(R.id.addComplete);
        add.setEnabled(false);

        Tools.editTextListener(add, wordTV, "word", false);
        Tools.editTextListener(add, definitionTV, "definition", false);
        Tools.editTextListener(add, exampleTV, "example", false);

        add.setOnClickListener(v1 -> {
            String word = wordTV.getText().toString();
            String definition = definitionTV.getText().toString();
            String example = exampleTV.getText().toString();

            Word wordToBeAddedToUserList = new Word(word, definition, example,"userAdded");

            WordStatusHandler wordStatusHandler = new WordStatusHandler("userAdded",
                    wordToBeAddedToUserList.getWord());

            WordStatus wordStatus = new WordStatus();
            wordStatus.setAddedToList(true);
            wordStatusHandler.setStatus(wordStatus);

            Tools.addToUserWordList(wordStatusHandler, wordToBeAddedToUserList);

            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}