package com.example.vocabularyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.vocabularyapp.ui.hard_words.HardWordsFragment;

import java.util.Calendar;

public class ReminderPopup extends AppCompatActivity {

    public static boolean isShown = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_popup);

        TextView word = findViewById(R.id.remindedWord);
        TextView definition = findViewById(R.id.remindeDefinition);
        TextView example = findViewById(R.id.remindExample);

        word.setText(Tools.readFromFile(this, "word.txt"));
        definition.setText(Tools.readFromFile(this, "definition.txt"));
        example.setText(Tools.readFromFile(this, "example.txt"));


    }


    public void memorizedClicked(View view) {
        WordStatusHandler wordStatusHandler = new WordStatusHandler(Tools.getCategory().toLowerCase(),
                Alarm.getWords().get(Alarm.getIndex()).getWord());

        WordStatus wordStatus = new WordStatus();
        wordStatus.setAddedToList(false);
        wordStatusHandler.setStatus(wordStatus);

        Tools.removeFromUserWordList(wordStatusHandler);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancelAll();

        //isShown = false;

        /*Intent intent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),
                HardWordsFragment.interval, pendingIntent);*/

        finish();
    }

    public void remindClicked(View view) {
        finish();
    }
}