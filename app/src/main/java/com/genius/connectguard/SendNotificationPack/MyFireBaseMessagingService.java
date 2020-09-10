package com.genius.connectguard.SendNotificationPack;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.genius.connectguard.R;
import com.genius.connectguard.RegisterActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.app.PendingIntent.FLAG_ONE_SHOT;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        if (remoteMessage.getData().isEmpty()){

            showNotification(remoteMessage);

        }else {

            showNotification(remoteMessage.getData());

        }

    }

    private void showNotification(Map<String, String> data){

        String title = data.get("Title");
        String message = data.get("Message");

        Intent intent = new Intent(this, RegisterActivity.class);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT);
        Notification notification = new NotificationCompat.Builder(this, "my_channel")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.app_icon)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(notificationID, notification);


    }

    private void showNotification(RemoteMessage remoteMessage){

        Intent intent = new Intent(this, RegisterActivity.class);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT);
        Notification notification = new NotificationCompat.Builder(this, "my_channel")
                .setContentTitle(remoteMessage.getData().get("Title"))
                .setContentText(remoteMessage.getData().get("Message"))
                .setSmallIcon(R.drawable.app_icon)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(notificationID, notification);

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager notificationManager) {
        String channelName = "channelName";
        NotificationChannel channel = new NotificationChannel("my_channel", channelName, IMPORTANCE_HIGH);
        channel.setDescription("My channel description");
        channel.enableLights(true);
        channel.setLightColor(Color.BLUE);
        channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
        channel.enableVibration(true);
        notificationManager.createNotificationChannel(channel);
    }

}
