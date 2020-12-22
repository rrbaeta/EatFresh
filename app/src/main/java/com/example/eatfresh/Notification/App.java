package com.example.eatfresh.Notification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class App extends Application {

    public static final String CHANNEL_1_ID = "itemExpiryAlert";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    public void createNotificationChannels(){
        NotificationChannel channel1 = new NotificationChannel(
                CHANNEL_1_ID,
                "itemExpiryAlert",
                NotificationManager.IMPORTANCE_HIGH);
        channel1.setDescription("Notifies the user of an item that is about to expire");

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);
    }

}
