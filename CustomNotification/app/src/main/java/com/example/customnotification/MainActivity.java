package com.example.customnotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
private static final String CHANNEL_ID = "My Channel";
private static final int NOTIFICATION_ID = 100;
private static final int REQ_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.messages_icon, null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;

        assert bitmapDrawable != null;
        Bitmap largeIcon = bitmapDrawable.getBitmap();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;
        Intent iNotify = new Intent(getApplicationContext(), MainActivity.class);
        iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, REQ_CODE, iNotify, PendingIntent.FLAG_UPDATE_CURRENT);

        //Big Picture Styles
        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle()
                .bigPicture(((BitmapDrawable)(ResourcesCompat.getDrawable(getResources(), R.drawable.flower, null))).getBitmap())
                .bigLargeIcon(largeIcon)
                .setBigContentTitle("Image sent by Kunal")
                .setSummaryText("Image Message");

        //Inbox Style
        Notification.InboxStyle inboxStyle = new Notification.InboxStyle()
                .addLine("A")
                .addLine("A")
                .addLine("A")
                .addLine("A")
                .addLine("A")
                .addLine("G")
                .addLine("A")
                .addLine("A")
                .addLine("c")
                .addLine("A")
                .addLine("A")
                .addLine("A")
                .addLine("A")
                .addLine("A")
                .addLine("A")
                .addLine("A")
                .addLine("A")
                .addLine("A")
                .addLine("A")
                .addLine("A")
                .addLine("A")
                .setBigContentTitle("Full Message")
                .setSummaryText("Message From kunal");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setContentText("New Message")
                    .setSubText("New message from kunal")
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.messages_icon)
                    .setContentIntent(pi)
                    .setStyle(bigPictureStyle)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setChannelId(CHANNEL_ID)
                    .build();
            notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH));
        } else {
            notification = new Notification.Builder(this)
                    .setContentText("New Message")
                    .setSubText("New message from kunal")
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.messages_icon)
                    .setContentIntent(pi)
                    .setStyle(inboxStyle)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .build();
        }
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}