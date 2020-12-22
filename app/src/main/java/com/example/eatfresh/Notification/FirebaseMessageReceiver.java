package com.example.eatfresh.Notification;

import android.app.Notification;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.eatfresh.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.eatfresh.Notification.App.CHANNEL_1_ID;

public class FirebaseMessageReceiver extends FirebaseMessagingService {

    private NotificationManagerCompat notificationManagerCompat;

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManagerCompat = NotificationManagerCompat.from(this);
    }

    @Override
    public void
    onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {

            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

        }
    }

    public void showNotification(String title, String text){

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.food_small_icon)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build();

        notificationManagerCompat.notify(1, notification);
    }

}