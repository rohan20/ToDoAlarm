package com.example.rohan.todoalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import com.example.rohan.todoSQLMaterialDesign.R;

import static com.example.rohan.todoSQLMaterialDesign.R.drawable.todoappicon;


public class AlarmReceiver extends BroadcastReceiver
{


    public AlarmReceiver()
    {

    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.


        //Alarm
//        MediaPlayer mp = MediaPlayer.create(context, R.raw.lean_on);
//        mp.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
//        mp.start();

        String title = intent.getStringExtra("AllTodosTitle");
        String description = intent.getStringExtra("AllTodosDescription");

        Toast.makeText(context, "Pending ToDo: " + title, Toast.LENGTH_LONG).show();

        //Notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Intent i = new Intent(context, AllToDos.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);


        //Notification not visible when large icon is set
//        mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.todoappicon));
        mBuilder.setSmallIcon(R.drawable.todoappicon);
        mBuilder.setTicker("Pending ToDo: " + title);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(description);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), AudioManager.STREAM_NOTIFICATION);
//              .setSound(alarmSound, AudioManager.STREAM_NOTIFICATION)
        mBuilder.setVibrate(new long[]{0, 500, 500, 500, 500});
        mBuilder.setPriority(Notification.PRIORITY_MAX);
//      mBuilder.setVibrate(new long[]{delay, vibrate, sleep, vibrate, sleep});
        mBuilder.setContentIntent(pIntent);
        //to cancel the to-do on notification click
        mBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mBuilder.build());




    }
}
