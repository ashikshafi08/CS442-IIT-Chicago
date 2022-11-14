package com.christopherhield.weather.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

class Util {
    // Used to set up the auto-updates of the weather widget

    private static final String TAG = "Util";

    static void scheduleUpdate(Context context) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null)
            return;
        long intervalMillis = 60000;
        Log.d(TAG, "scheduleUpdate: Update in " + intervalMillis + " millis");

        PendingIntent pi = getAlarmTimerIntent(context);
        am.cancel(pi);
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), intervalMillis, pi);

        Log.d(TAG, "scheduleUpdate: DONE");
    }

    private static PendingIntent getAlarmTimerIntent(Context context) {
        Log.d(TAG, "getAlarmIntent: Creating timed auto-update intent");
        Intent intent = new Intent(context, WeatherWidget.class);
        intent.setAction(WeatherWidget.WIDGET_TIMER_UPDATE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }

    static void clearTimeUpdate(Context context) {
        Log.d(TAG, "clearUpdate: ");
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null)
            return;
        am.cancel(getAlarmTimerIntent(context));
        Log.d(TAG, "clearUpdate: DONE");
    }

}