package com.example.myapplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.net.URI;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,0);

        Vibrator vibrator =(Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notify")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Task Reminders")
                .setContentText("Hey check your Task")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200,builder.build());

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone r = RingtoneManager.getRingtone(context,sound);
        r.play();
    }
}

