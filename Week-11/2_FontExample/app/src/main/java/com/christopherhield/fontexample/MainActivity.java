package com.christopherhield.fontexample;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Typeface myCustomFont;
    private TextView textView;
    private Button button;
    private CheckBox checkBox;
    private EditText editText;

    private final String[] fontsToUse = {
            "fonts/GrandHotel-Regular.otf",
            "fonts/Acme-Regular.ttf",
            "fonts/Pacifico.ttf",
            "fonts/SeaSide.ttf" ,
            "fonts/ArianaVioleta.ttf",
            "fonts/Badrips.otf",
            "fonts/Classroom.ttf"
    };
    private int fontIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        checkBox = findViewById(R.id.checkBox);
        editText = findViewById(R.id.editText);
    }

    private void setFonts() {
        // Fonts go in the "assets" folder, with java and res
        myCustomFont = Typeface.createFromAsset(getAssets(), fontsToUse[fontIndex]);

        textView.setTypeface(myCustomFont);
        button.setTypeface(myCustomFont);
        checkBox.setTypeface(myCustomFont);
        editText.setTypeface(myCustomFont);

        customizeActionBar();
    }

    private void customizeActionBar() {

        // This function sets the font of the title in the app bar

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        String t = getTitle().toString();
        TextView tv = new TextView(this);

        tv.setText(t);
        tv.setTextSize(24);
        tv.setTextColor(Color.WHITE);
        tv.setTypeface(myCustomFont, Typeface.NORMAL);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(tv);
    }

    public void changeFont(View v) {
        setFonts();
        fontIndex++;
        if (fontIndex == fontsToUse.length)
            fontIndex = 0;
    }
}
