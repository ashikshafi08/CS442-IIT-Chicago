package com.christopherhield.snackbar;

import android.graphics.Color;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // CoordinatorLayout is needed!
    private View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.layout);
    }

    public void setShortSnackBar(View v) {
        Snackbar.make(layout, "This is a SHORT duration SnackBar", Snackbar.LENGTH_SHORT).show();
    }

    public void setLongSnackBar(View v) {
        Snackbar.make(layout, "This is a LONG duration SnackBar", Snackbar.LENGTH_LONG).show();
    }

    public void setIndefiniteSnackBar(View v) {
        Snackbar.make(layout, "This is a INDEFINITE duration SnackBar", Snackbar.LENGTH_INDEFINITE).show();
    }

    public void makeActionSnackBar(View v) {
        Snackbar snackbar = Snackbar.make(layout, "This SnackBar has an Action", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Do Something", view -> {
            int r = (int) (Math.random() * 255);
            int g = (int) (Math.random() * 255);
            int b = (int) (Math.random() * 255);
            int color = (0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
            layout.setBackgroundColor(color);
        });

        snackbar.show();
    }

    public void makeSnackBarWithTextColors(View v) {
        Snackbar snackbar = Snackbar
                .make(layout, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.RED);
        snackbar.setAction("PANIC", view -> Toast.makeText(MainActivity.this, "Eeeeeeeek!", Toast.LENGTH_SHORT).show());

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }
}
