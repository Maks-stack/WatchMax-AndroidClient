package com.example.maks.maxwatchapp.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
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
        ShowNotification(remoteMessage.getData().get("message"));
    }

    private void ShowNotification(String message) {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
            .setAutoCancel(true)
            .setContentTitle("MaxWatch")
            .setContentText(message)
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0, builder.build());
    }
}
