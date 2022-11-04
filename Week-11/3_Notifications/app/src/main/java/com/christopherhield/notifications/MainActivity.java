package com.christopherhield.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private int notificationId = 0;
    private NotificationManager mNotificationManager;
    private boolean lowNotified = false;
    private final String CHANNEL_ID = "NOTIFICATIONS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        makeChannel();

        BatteryReceiver br = new BatteryReceiver();
        IntentFilter filter1 = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(br, filter1);
    }

    private void makeChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    protected void onDestroy() {
        mNotificationManager.cancelAll();
        super.onDestroy();
    }

    public void doNoActionNotification(View v) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.noaction)
                .setContentTitle("App Generated Notification")
                .setContentText("This Notification has NO Action!")
                .setSound(RingtoneManager.getDefaultUri(
                        RingtoneManager.TYPE_NOTIFICATION))
                .setLights(Color.MAGENTA, 1000, 300);

        Notification note = mBuilder.build();
        mNotificationManager.notify(++notificationId, note);
    }

    public void doSingleActionNotification(View v) {

        Intent resultIntent = new Intent(this, ActivityFromNotification.class);

        PendingIntent pi = PendingIntent.getActivity(
                this, notificationId, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.action)
                .setContentTitle("App Generated Notification")
                .setContentText("This Notification has an Action!")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLights(Color.MAGENTA, 1000, 300);

        Notification note = mBuilder.build();
        mNotificationManager.notify(++notificationId, note);
    }

    public void doMultiActionNotification(View v) {

        // Main action
        Intent resultIntent = new Intent(this, ActivityFromNotification.class);
        PendingIntent pi = PendingIntent.getActivity(
                this, notificationId, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);

        // Action #1
        Intent powerIntent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
        PendingIntent p1 = PendingIntent.getActivity(
                this, notificationId, powerIntent,
                PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Action action1 = new NotificationCompat.Action.Builder(
                R.mipmap.ic_launcher, "Power", p1).build();

        // Action #2
        Intent networkIntent = new Intent(Intent.ACTION_APPLICATION_PREFERENCES);
        PendingIntent p2 = PendingIntent.getActivity(
                this, notificationId, networkIntent,
                PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Action action2 = new NotificationCompat.Action.Builder(
                R.mipmap.ic_launcher, "Prefs", p2).build();

        // Action #3
        Intent battIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        PendingIntent p3 = PendingIntent.getActivity(
                this, notificationId, battIntent,
                PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Action action3 = new NotificationCompat.Action.Builder(
                R.mipmap.ic_launcher, "Wifi", p3).build();


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setSmallIcon(R.drawable.multi)
                .setContentTitle("App Generated Notification")
                .setContentText("This Notification has Multiple Actions!")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLights(Color.MAGENTA, 1000, 300)
                .addAction(action1)
                .addAction(action2)
                .addAction(action3);
        Notification note = mBuilder.build();
        mNotificationManager.notify(++notificationId, note);
    }

    public void doClearLast(View v) {
        mNotificationManager.cancel(notificationId);
    }

    public void doClearAll(View v) {
        mNotificationManager.cancelAll();
    }


    ////////////////////////
    class BatteryReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent batteryStatus) {

            int chgLvel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

            if (chgLvel <= 50) {
                if (!lowNotified) {
                    lowNotified = true;
                    makeBatteryNotification(chgLvel);
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibe != null)
                        vibe.vibrate(750);
                }
            } else {
                lowNotified = false;
            }
        }

        private void makeBatteryNotification(int level) {

            Intent resultIntent = new Intent(MainActivity.this, ResultActivity.class);
            resultIntent.putExtra("LEVEL", level);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(MainActivity.this, notificationId, resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                    .setContentIntent(resultPendingIntent)
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.battery)
                            .setContentTitle("Battery Status")
                            .setContentText("Battery is at " + level + "%")
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setLights(Color.MAGENTA, 1000, 300);

            Notification note = mBuilder.build();
            mNotificationManager.notify(++notificationId, note);

        }
    }
}
