package com.example.maks.maxwatchapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.example.maks.maxwatchapp.R;
import com.example.maks.maxwatchapp.activities.MainActivity;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Maks on 30/06/17.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        ShowNotification(remoteMessage.getData().get("body"));
    }

    private void ShowNotification(String message) {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
            .setAutoCancel(true)
            .setContentTitle("MaxWatch")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setLights(0x83c3ed, 3000, 1500)
            .setColor(0x83c3ed)
            .setVibrate(new long[] { 0, 400, 200, 400, 200, 400, 200, 400, 200, 400, 400, 2500 })
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = builder.build();

        manager.notify(0, notification);
    }
}
