package com.example.cuoiki.support;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.cuoiki.MainActivity;

public class Notification extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get id & message from intent.
        int notificationId = intent.getIntExtra("notificationId", 0);
        String message = intent.getStringExtra("todo");

        // When notification is tapped, call MainActivity.
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);
        contentIntent.cancel();
        NotificationManager myNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Prepare notification.
        android.app.Notification.Builder builder = new android.app.Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(message + " is working")
                .setContentText("Tap to turn off notification")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(contentIntent)
                .setPriority(android.app.Notification.PRIORITY_MAX)
                .setDefaults(android.app.Notification.DEFAULT_ALL);

        // Notify
        myNotificationManager.notify(notificationId, builder.build());

    }
}
