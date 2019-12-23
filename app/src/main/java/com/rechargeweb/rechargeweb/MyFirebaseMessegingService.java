package com.rechargeweb.rechargeweb;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rechargeweb.rechargeweb.Activity.SignUpActivity;
import com.rechargeweb.rechargeweb.Constant.Constants;

public class MyFirebaseMessegingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessegingService.class.getSimpleName();
    private String notiTitle, notiContent;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        notiTitle = remoteMessage.getNotification().getTitle();
        notiContent = remoteMessage.getNotification().getBody();
        //Make a channel if device is running on Android O or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //Channel name, description, importance
            CharSequence name = Constants.NOTIFICATION_CHANNEL_NAME;
            String description = Constants.NOTIFICATION_CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel notificationChannel = new NotificationChannel(Constants.CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);

            //Add the channel
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,Constants.CHANNEL_ID)
                .setSmallIcon(R.mipmap.formax_round_icon)
                .setContentTitle(notiTitle)
                .setContentText(notiContent)
                .setContentIntent(contentIntent(this))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColorized(true);

        //Make notification
        NotificationManagerCompat.from(this).notify(Constants.NOTIFICATION_ID,builder.build());
    }

    //Creating Pending Intent
    private static PendingIntent contentIntent(Context context) {
        Intent startContent = new Intent(context, SignUpActivity.class);
        return PendingIntent.getActivity(
                context,
                Constants.PENDING_INTENT_ID,
                startContent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
