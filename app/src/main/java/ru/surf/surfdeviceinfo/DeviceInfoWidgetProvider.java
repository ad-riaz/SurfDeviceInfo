package ru.surf.surfdeviceinfo;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.widget.RemoteViews;

import ru.surf.androiddevicedresser.R;
import ru.surf.surfdeviceinfo.service.NotificationService;

public class DeviceInfoWidgetProvider extends AppWidgetProvider {
    String manufacturer;
    String model;
    String deviceNameNoSpaces;
    DisplayMetrics metrics;
    int widthPixels;
    int heightPixels;
    double xdpi;
    double ydpi;
    double widthInches;
    double heightInches;
    double screenDiagonal;
    String osVersion;

    @SuppressLint("DefaultLocale")
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        manufacturer = android.os.Build.MANUFACTURER;
        model = android.os.Build.MODEL;
        deviceNameNoSpaces = manufacturer + model.replaceAll("\\s+", "");
        metrics = context.getResources().getDisplayMetrics();
        widthPixels = metrics.widthPixels;
        heightPixels = metrics.heightPixels;
        xdpi = metrics.xdpi;
        ydpi = metrics.ydpi;
        widthInches = widthPixels / xdpi;
        heightInches = heightPixels / ydpi;
        screenDiagonal = Math.sqrt(Math.pow(widthInches, 2) + Math.pow(heightInches, 2));
        osVersion = android.os.Build.VERSION.RELEASE;

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            // Set text in the widget
            views.setTextViewText(R.id.full_device_name, manufacturer + " " + model);
            views.setTextViewText(R.id.device_name_no_spaces, deviceNameNoSpaces);
            views.setTextViewText(R.id.screen_size, String.format("%.2f", screenDiagonal) + "\" " + widthPixels + "x" + heightPixels);
            views.setTextViewText(R.id.os_version, "Android " + osVersion);

            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        // Запуск службы для отображения уведомления
        context.startForegroundService(new Intent(context, NotificationService.class));
    }
}

