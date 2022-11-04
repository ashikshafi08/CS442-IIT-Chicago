package com.christopherhield.scrollviewsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Don't forget to add the below to the ScrollView
    // android:fillViewport="true"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String text = getString(R.string.story_text);
        String textReverse = new StringBuilder(text).reverse().toString();

        TextView story = findViewById(R.id.textView1);
        story.setMovementMethod(new ScrollingMovementMethod());
        story.setText(text);

        TextView storyReversed = findViewById(R.id.textView2);
        storyReversed.setMovementMethod(new ScrollingMovementMethod());
        storyReversed.setText(textReverse);
    }
}