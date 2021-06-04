package com.example.vocabularyapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class Alarm extends BroadcastReceiver {

    private static ArrayList<Word> words = new ArrayList<>();
    private static int index;
    public static Word word;
    public static Notification notification;

    @Override
    public void onReceive(Context context, Intent intent) {


        DatabaseReference userID = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userID.child("userwordlist").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Random rand = new Random();
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {
                    Word word = snapshot.getValue(Word.class);

                    if (search(word.getWord()))
                        words.add(word);
                }

                index = rand.nextInt(words.size());
                word = words.get(index);
                notificationShade(context, word);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    private void notificationShade(Context context, Word word) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        Intent activityIntent = new Intent(context, ReminderPopup.class);
        PendingIntent contentIntend = PendingIntent.getActivity(context, 0, activityIntent, 0);

        Tools.writeToFile(word.getWord(), context, "word.txt");
        Tools.writeToFile(word.getDefinition(), context, "definition.txt");
        Tools.writeToFile(word.getExample(), context, "example.txt");

        notification = new NotificationCompat.Builder(context, App.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_alarm_24)
                .setContentTitle(word.getWord())
                .setContentText(word.getDefinition())
                .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(word.getDefinition() + "\n" + word.getExample()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntend)
                .setAutoCancel(true)
                //.addAction(R.mipmap.ic_launcher, "Memorized", memorizedActionIntend)
                //.addAction(R.mipmap.ic_launcher, "Remind me later", remindActionIntend)
                .build();

        if(ReminderPopup.isShown) {
            notificationManagerCompat.notify(1, notification);
            System.out.println("menim 105" + ReminderPopup.isShown);
        }
        else
            ReminderPopup.isShown = true;
    }

    public static ArrayList<Word> getWords() {
        return words;
    }

    public static int getIndex() {
        return index;
    }
}
