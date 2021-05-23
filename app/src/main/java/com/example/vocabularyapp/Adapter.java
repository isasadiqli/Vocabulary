package com.example.vocabularyapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    ArrayList<Word> words;
    HashMap<String, Boolean> wordStatus;
    int mExpandedPosition = -1;
    int previousExpandedPosition = -1;

    WordStatusHandler wordStatusHandlerSet;

    ArrayList<String> categories;
    ArrayList<Integer> categoryIcon;
    String recycleViewType;


    public Adapter(Context context, ArrayList<Word> words, HashMap<String, Boolean> wordStatus,
                   ArrayList<String> categories, ArrayList<Integer> categoryIcon, String recycleViewType) {
        this.context = context;
        this.recycleViewType = recycleViewType;

        if (recycleViewType.equals("words")) {
            this.words = words;
            this.wordStatus = wordStatus;
        } else {
            this.categories = categories;
            this.categoryIcon = categoryIcon;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (recycleViewType.equals("words"))
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false));
        else
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (recycleViewType.equals("words")) {

            Word word = words.get(position);
            holder.word.setText(word.getWord());
            holder.definition.setText(word.getDefinition());
            holder.example.setText(word.getExample());
            holder.memorized.setBackgroundColor(Color.BLUE);

            final boolean isExpanded = position == mExpandedPosition;
            //holder.definitionLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            holder.exampleLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            holder.memorized.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            if (!Tools.getCategory().toLowerCase().equals("userwordlist"))
                holder.addToList.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            else holder.addToList.setVisibility(View.GONE);

            holder.itemView.setActivated(isExpanded);

            if (isExpanded) {
                previousExpandedPosition = position;

                wordStatusHandlerSet = new WordStatusHandler(Tools.getCategory().toLowerCase(),
                        WordList.getWords().get(position).getWord());

                if (Tools.getCategory().equals("userWordList"))
                    Tools.getWordStatus(wordStatus, wordStatusHandlerSet, holder.memorized, true, true);
                else {
                    Tools.getWordStatus(wordStatus, wordStatusHandlerSet, holder.memorized, true, false);
                    Tools.getWordStatus(wordStatus, wordStatusHandlerSet, holder.addToList, false, false);
                }

                Tools.setWordPosition(position);
            }


            holder.itemView.setOnClickListener(v -> {
                mExpandedPosition = isExpanded ? -1 : position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);

                Tools.setWordPosition(position);
            });

            holder.memorized.setOnClickListener(v -> {
                Tools.setWordPosition(position);

                WordStatusHandler wordStatusHandler;
                WordStatus wordStatus;

                if (holder.memorized.getText().equals("Not memorized")) {

                    wordStatusHandler = new WordStatusHandler(Tools.getCategory().toLowerCase(),
                            holder.word.getText().toString());

                    wordStatus = new WordStatus();

                    if (Tools.getCategory().toLowerCase().equals("userwordlist")) {

                        DatabaseReference userID = FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        userID.child("userwordlist").child(wordStatusHandler.getWord())
                                .removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                removeAt(position);

                                wordStatus.setAddedToList(false);
                                wordStatusHandler.setStatus(wordStatus);
                                Tools.updateWordStatus(wordStatusHandler, false);
                            }
                        });
                    }
                    else {
                        wordStatus.setMemorized(true);
                        wordStatusHandler.setStatus(wordStatus);
                        Tools.updateWordStatus(wordStatusHandler, true);

                    }


                } else {

                    wordStatusHandler = new WordStatusHandler(Tools.getCategory().toLowerCase(),
                            WordList.getWords().get(position).getWord());

                    wordStatus = new WordStatus();
                    wordStatus.setMemorized(false);
                    wordStatusHandler.setStatus(wordStatus);
                    Tools.updateWordStatus(wordStatusHandler, true);
                }

            });

            holder.addToList.setOnClickListener(v -> {
                Tools.setWordPosition(position);

                WordStatusHandler wordStatusHandler;
                WordStatus wordStatus;

                if (holder.addToList.getText().equals("Not on the list")) {

                    wordStatusHandler = new WordStatusHandler(Tools.getCategory().toLowerCase(),
                            WordList.getWords().get(position).getWord());

                    wordStatus = new WordStatus();
                    wordStatus.setAddedToList(true);
                    wordStatusHandler.setStatus(wordStatus);

                    Word wordToBeAddedToUserList = new Word(holder.word.getText().toString(),
                            holder.definition.getText().toString(), holder.example.getText().toString(),
                            Tools.getCategory().toLowerCase());

                    Tools.addToUserWordList(wordStatusHandler, wordToBeAddedToUserList);

                } else {

                    wordStatusHandler = new WordStatusHandler(Tools.getCategory().toLowerCase(),
                            WordList.getWords().get(position).getWord());

                    wordStatus = new WordStatus();
                    wordStatus.setAddedToList(false);
                    wordStatusHandler.setStatus(wordStatus);

                    Tools.removeFromUserWordList(wordStatusHandler);

                }
                Tools.updateWordStatus(wordStatusHandler, false);
            });
        } else {
            String category = categories.get(position);
            holder.categoryTitle.setText(category);


            Integer catIcon = categoryIcon.get(position);
            holder.iconOfCategory.setImageResource(catIcon);

            holder.itemView.setOnClickListener(v -> {

                Tools.setCategoryPosition(position);

                Intent intent = new Intent(holder.context, WordList.class);
                holder.context.startActivity(intent);
            });

        }

    }

    public void removeAt(int position) {
        words.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, words.size());
    }

    @Override
    public int getItemCount() {
        if (recycleViewType.equals("words"))
            return words.size();
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        Context context;

        TextView word, definition, example;
        LinearLayout definitionLayout, exampleLayout;
        Button memorized, addToList;
        RecyclerView recyclerView;
        ImageView iconOfCategory;

        TextView categoryTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();

            word = itemView.findViewById(R.id.word);
            definition = itemView.findViewById(R.id.definition);
            example = itemView.findViewById(R.id.example);

            //definitionLayout = itemView.findViewById(R.id.definitionLayout);
            exampleLayout = itemView.findViewById(R.id.exampleLayout);
            memorized = itemView.findViewById(R.id.Learned);
            addToList = itemView.findViewById(R.id.addToList);

            recyclerView = itemView.findViewById(R.id.wordList);

            categoryTitle = itemView.findViewById(R.id.categoryTitle);

            iconOfCategory = itemView.findViewById(R.id.iconOfCategory);
        }

    }


}
