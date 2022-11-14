package com.christopherhield.eightballwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class EightBallWidget extends AppWidgetProvider {


    public static final String EIGHT_BALL_WAS_CLICKED = "EIGHT_BALL_CLICKED";
    private boolean hadFirstClick = false;

    // Pre-defined set of 8-ball responses
    private static final String[] responses = new String[]{
            "It is certain",
            "It is decidedly so",
            "Without a doubt",
            "Yes definitely",
            "You may rely on it",
            "As I see it, yes",
            "Most likely",
            "Outlook good",
            "Yes",
            "Signs point to yes",
            "Reply hazy try again",
            "Ask again later",
            "Better not tell you now",
            "Cannot predict now",
            "Concentrate and ask again",
            "Don't count on it",
            "My reply is no",
            "My sources say no",
            "Outlook not so good",
            "Very doubtful"
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(EIGHT_BALL_WAS_CLICKED) ||
                        intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")) {

            // Before the first click, the text should not change
            // - using hadFirstClick to determine
            if (intent.getAction().equals(EIGHT_BALL_WAS_CLICKED))
                hadFirstClick = true;

            // Get widget view
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.eight_ball_widget);

            // If has been clicked already, change text
            if (hadFirstClick) {
                // Pick a random response
                int idx = (int) (Math.random() * responses.length);
                String choice = responses[idx];
                remoteViews.setTextViewText(R.id.appwidget_text, choice);
            }
            remoteViews.setImageViewResource(R.id.imageView2, R.drawable.eight_ball);



            // Set up pending intent for next click
            Intent newIntent = new Intent(context, EightBallWidget.class);
            newIntent.setAction(EIGHT_BALL_WAS_CLICKED);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, newIntent, PendingIntent.FLAG_IMMUTABLE);
            remoteViews.setOnClickPendingIntent(R.id.layout, pi);

            // Update the widget
            ComponentName componentName = new ComponentName(context, EightBallWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(componentName, remoteViews);
        }
    }

}