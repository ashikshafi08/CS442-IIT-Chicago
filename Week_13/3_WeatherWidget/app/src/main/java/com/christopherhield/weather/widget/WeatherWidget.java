package com.christopherhield.weather.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.christopherhield.weather.app.MainActivity;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class WeatherWidget extends AppWidgetProvider {

    private static final String TAG = "WeatherWidget";
    public static final String WIDGET_CLICKED_FOR_ACTIVITY = "WIDGET_CLICKED_FOR_ACTIVITY";
    public static final String WIDGET_CLICKED_FOR_UPDATE = "WIDGET_CLICKED_FOR_UPDATE";
    public static final String WIDGET_TIMER_UPDATE = "WIDGET_TIMER_UPDATE";
    private static SimpleDateFormat timeFormat;

    private Context context;

    private boolean fahrenheit = true;

    public WeatherWidget() {
        timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {

        // Intent action is APPWIDGET_UPDATE when android calls for a widget update
        // Intent action is WIDGET_CLICKED when widget is touched on the screen
        // Intent action is WIDGET_TIMER_UPDATE when widget updated due to the timer

        Log.d(TAG, "onReceive: Type of message received: " + intent.getAction());

        super.onReceive(context, intent);
        if (intent.getAction() == null)
            return;

        this.context = context;
        if (intent.getAction().equals(WIDGET_CLICKED_FOR_UPDATE) ||
                intent.getAction().equals(WIDGET_TIMER_UPDATE) ||
                intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")) {

            // Start async task to get weather data

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            ComponentName componentName = new ComponentName(context, WeatherWidget.class);

            remoteViews.setImageViewResource(R.id.syncImg, R.drawable.sync_white);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(componentName, remoteViews);

            SharedPreferences prefs = context.getSharedPreferences("WEATHER_SETTINGS",Context.MODE_PRIVATE);
            if (!prefs.contains("TEMP_UNIT")) {
                fahrenheit = true;
            } else {
                String unit = prefs.getString("TEMP_UNIT", "FAHRENHEIT");
                fahrenheit = unit.equals("FAHRENHEIT");
            }

            WeatherWidgetVolley.getWeatherData(context, "Chicago", this);

        } else if (intent.getAction().equals(WIDGET_CLICKED_FOR_ACTIVITY)) {
            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);

        }
    }

    public void handleWeatherError(String msg) {
        Log.d(TAG, "handleWeatherError: " + msg);
    }

    public void handleWeatherError(JSONObject jsonObject) {
        Log.d(TAG, "handleWeatherError: " + jsonObject);
    }

    public void handleWeatherData(int t, Bitmap bm) {
        Log.d(TAG, "updateData: Temp: " + t + ", Bitmap: " + bm.getWidth() + "x" + bm.getHeight());

        double temp = t;
        if (!fahrenheit)
            temp = (t - 32.0) * (5.0/9.0);

        // Get WeatherWidget XML layout
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        ComponentName componentName = new ComponentName(context, WeatherWidget.class);

        /////////////
        // Update the widget layout elements
        String unit = fahrenheit ? "F" : "C";
        remoteViews.setTextViewText(R.id.tempText, String.format(Locale.getDefault(), "%d %s°", Math.round(temp), unit));
        Log.d(TAG, "updateData: Updated temp TextView to " + String.format("%d°", (int) temp));

        remoteViews.setImageViewBitmap(R.id.weatherIcon, bm);
        Log.d(TAG, "updateData: Updated icon ImageView to " + bm);

        String timeStamp = timeFormat.format(new Date());
        remoteViews.setTextViewText(R.id.time_text, timeStamp);
        Log.d(TAG, "updateData: Updated time ImageView to " + timeStamp);

        remoteViews.setImageViewResource(R.id.syncImg, R.drawable.sync);
        /////////////


        Intent openActivityIntent = new Intent(context, WeatherWidget.class);
        openActivityIntent.setAction(WIDGET_CLICKED_FOR_ACTIVITY);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, openActivityIntent, PendingIntent.FLAG_IMMUTABLE);
        remoteViews.setOnClickPendingIntent(R.id.weatherIcon, pi);
        remoteViews.setOnClickPendingIntent(R.id.tempText, pi);

        // Set PendingIntent for the next click
        Intent refreshWidgetIntent = new Intent(context, WeatherWidget.class);
        refreshWidgetIntent.setAction(WIDGET_CLICKED_FOR_UPDATE);
        pi = PendingIntent.getBroadcast(context, 0, refreshWidgetIntent, PendingIntent.FLAG_IMMUTABLE);
        remoteViews.setOnClickPendingIntent(R.id.syncImg, pi);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(componentName, remoteViews);

    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        Log.d(TAG, "onAppWidgetOptionsChanged: ");
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
        Log.d(TAG, "onRestored: ");
    }

    @Override
    public void onEnabled(Context context) {
        // Auto-called when the widget is placed on a screen
        Log.d(TAG, "onEnabled: ");
        // Setup timer update
        Util.scheduleUpdate(context);
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "onDisabled: ");
        // Cancel timer updates
        Util.clearTimeUpdate(context);
        super.onDisabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted: ");
        // Cancel timer updates
        Util.clearTimeUpdate(context);
        super.onDeleted(context, appWidgetIds);
    }

}

