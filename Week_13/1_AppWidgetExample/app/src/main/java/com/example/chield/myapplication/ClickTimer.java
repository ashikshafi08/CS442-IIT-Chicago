package com.example.chield.myapplication;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Locale;

public class ClickTimer extends AppWidgetProvider {

    private static final String TAG = "ClickTimer";
    public static final String WIDGET_WAS_CLICKED = "WIDGET_CLICKED";
    private static long lastTime = System.currentTimeMillis();

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.d(TAG, "onReceive: " + intent.getAction());
        if (intent.getAction().equals(WIDGET_WAS_CLICKED)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetId = intent.getIntExtra("ID", 0);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.d(TAG, "onUpdate: ");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        long time = System.currentTimeMillis();
        long diff = time - lastTime;
        String formatted = String.format(Locale.getDefault(), "%.3f sec", diff / 1000.0);

        lastTime = time;
        Intent intent = new Intent(context, ClickTimer.class);
        intent.setAction(WIDGET_WAS_CLICKED);
        intent.putExtra("ID", appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.click_timer);
        views.setTextViewText(R.id.appwidget_text, formatted);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onEnabled(Context context) {
        // Auto-called when the widget is placed on a screen
        Log.d(TAG, "onEnabled: ");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d(TAG, "onDisabled: ");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted: ");
    }
}
