package com.christopherhield.invisibility;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changeImageVisibility(View v) {
        ImageView iv = findViewById(R.id.imageView);
        if (((CheckBox)v).isChecked())
            iv.setVisibility(View.INVISIBLE);
        else
            iv.setVisibility(View.VISIBLE);
    }

    public void changeTextVisibility(View v) {
        TextView tv = findViewById(R.id.textView);
        if (((CheckBox)v).isChecked())
            tv.setVisibility(View.INVISIBLE);
        else
            tv.setVisibility(View.VISIBLE);
    }

    public void changeImageGone(View v) {
        ImageView iv = findViewById(R.id.imageView);
        if (((CheckBox)v).isChecked())
            iv.setVisibility(View.GONE);
        else
            iv.setVisibility(View.VISIBLE);
    }

    public void changeTextGone(View v) {
        TextView tv = findViewById(R.id.textView);
        if (((CheckBox)v).isChecked())
            tv.setVisibility(View.GONE);
        else
            tv.setVisibility(View.VISIBLE);
    }
}
