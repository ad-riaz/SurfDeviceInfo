package ru.surf.surfdeviceinfo.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import ru.surf.androiddevicedresser.R;

public class NotificationService extends Service {

    private static final String CHANNEL_ID = "device_info_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, createNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, createNotification());
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Device Info Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Notification createNotification() {
        String manufacturer = android.os.Build.MANUFACTURER;
        String model = android.os.Build.MODEL;
        String osVersion = android.os.Build.VERSION.RELEASE;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        double xdpi = metrics.xdpi;
        double ydpi = metrics.ydpi;
        double widthInches = widthPixels / xdpi;
        double heightInches = heightPixels / ydpi;
        double screenDiagonal = Math.sqrt(Math.pow(widthInches, 2) + Math.pow(heightInches, 2));

        String deviceInfo = manufacturer.replace(" ", "") + model.replace(" ", "") + "\n" +
                String.format("%.2f", screenDiagonal) + "\" " + widthPixels + "x" + heightPixels + "\n" +
                "Android " + osVersion;

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(manufacturer + " " + model)
                .setContentText(deviceInfo)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(deviceInfo))
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();
    }
}
