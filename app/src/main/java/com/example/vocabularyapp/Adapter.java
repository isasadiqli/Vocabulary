package com.example.vocabularyapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    ArrayList<Word> words;
    HashMap<String, Boolean> wordStatus;
    int mExpandedPosition = -1;
    int previousExpandedPosition = -1;

    WordStatusHandler wordStatusHandlerSet;

    ArrayList<String> categories;
    String recycleViewType;

    public Adapter(Context context, ArrayList<Word> words, HashMap<String, Boolean> wordStatus, ArrayList<String> categories, String recycleViewType) {
        this.context = context;
        this.recycleViewType = recycleViewType;

        if (recycleViewType.equals("words")) {
            this.words = words;
            this.wordStatus = wordStatus;
        }
        else
            this.categories = categories;
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
            holder.definitionLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            holder.exampleLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            holder.memorized.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            holder.addToList.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            holder.itemView.setActivated(isExpanded);

            if (isExpanded) {
                previousExpandedPosition = position;

                wordStatusHandlerSet = new WordStatusHandler(Tools.getCategory().toLowerCase(),
                        WordList.getWords().get(position).getWord());

                Tools.getWordStatus(wordStatus, wordStatusHandlerSet, holder.memorized, true);
                Tools.getWordStatus(wordStatus, wordStatusHandlerSet, holder.addToList, false);

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
                            WordList.getWords().get(position).getWord());

                    wordStatus = new WordStatus();
                    wordStatus.setMemorized(true);
                    wordStatusHandler.setStatus(wordStatus);

                } else {

                    wordStatusHandler = new WordStatusHandler(Tools.getCategory().toLowerCase(),
                            WordList.getWords().get(position).getWord());

                    wordStatus = new WordStatus();
                    wordStatus.setMemorized(false);
                    wordStatusHandler.setStatus(wordStatus);
                }
                Tools.updateWordStatus(wordStatusHandler, true);


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

                } else {

                    wordStatusHandler = new WordStatusHandler(Tools.getCategory().toLowerCase(),
                            WordList.getWords().get(position).getWord());

                    wordStatus = new WordStatus();
                    wordStatus.setAddedToList(false);
                    wordStatusHandler.setStatus(wordStatus);
                }
                Tools.updateWordStatus(wordStatusHandler, false);
            });
        }
        else {
            String category = categories.get(position);
            holder.categoryTitle.setText(category);

            holder.itemView.setOnClickListener(v -> {
                Tools.setCategoryPosition(position);
                Intent intent = new Intent(holder.context, WordList.class);
                holder.context.startActivity(intent);
            });

        }

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

        TextView categoryTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();

            word = itemView.findViewById(R.id.word);
            definition = itemView.findViewById(R.id.definition);
            example = itemView.findViewById(R.id.example);

            definitionLayout = itemView.findViewById(R.id.definitionLayout);
            exampleLayout = itemView.findViewById(R.id.exampleLayout);
            memorized = itemView.findViewById(R.id.Learned);
            addToList = itemView.findViewById(R.id.addToList);

            recyclerView = itemView.findViewById(R.id.wordList);

            categoryTitle = itemView.findViewById(R.id.categoryTitle);
        }

    }


}
