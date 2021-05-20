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
    HashMap<String, String> wordStatus;
    int mExpandedPosition = -1;
    int previousExpandedPosition = -1;
    private int memorized = 0;

    WordStatusHandler wordStatusHandlerSet;

    ArrayList<String> categories;
    String recycleViewType;

    public Adapter(Context context, ArrayList<Word> words, HashMap<String, String> wordStatus, ArrayList<String> categories, String recycleViewType) {
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

            holder.itemView.setActivated(isExpanded);

            if (isExpanded) {
                previousExpandedPosition = position;

                wordStatusHandlerSet = new WordStatusHandler(Tools.getCategory().toLowerCase(),
                        WordList.getWords().get(position).getWord());

                Tools.getWordStatus(wordStatus, wordStatusHandlerSet, holder.memorized);
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
                memorized++;
                Drawable backgroundColor = holder.memorized.getBackground();
                //Tools.updateUserWordList();

                WordStatusHandler wordStatusHandler;

                if (memorized % 2 != 0) {

                    wordStatusHandler = new WordStatusHandler(Tools.getCategory().toLowerCase(),
                            WordList.getWords().get(position).getWord(), "Memorized");


                    holder.memorized.setBackgroundColor(Color.parseColor("#74E96F"));
                    holder.memorized.setText(R.string.remindMeLater);

                } else {

                    wordStatusHandler = new WordStatusHandler(Tools.getCategory().toLowerCase(),
                            WordList.getWords().get(position).getWord(), "Remind me later");

                    holder.memorized.setBackgroundColor(Color.BLUE);
                    holder.memorized.setText(R.string.memorized);
                }
                Tools.updateWordStatus(wordStatusHandler);


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
        Button memorized;
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

            recyclerView = itemView.findViewById(R.id.wordList);


            categoryTitle = itemView.findViewById(R.id.categoryTitle);
        }

    }


}
