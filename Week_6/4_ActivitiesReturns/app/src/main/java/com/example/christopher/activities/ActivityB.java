package com.example.christopher.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityB extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_activity);

        TextView textView = findViewById(R.id.activityLabel);
        editText = findViewById(R.id.input);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            textView.setText(String.format("ActivityB\nOpened from %s", text));
        }

    }

    public void doneClicked(View v) {
        Intent data = new Intent(); // Used to hold results data to be returned to original activity
        data.putExtra("USER_TEXT_IDENTIFIER", editText.getText().toString());
        setResult(RESULT_OK, data);
        finish(); // This closes the current activity, returning us to the original activity
    }

    @Override
    public void onBackPressed() {
        // Pressing the back arrow closes the current activity, returning us to the original activity
        Intent data = new Intent();
        data.putExtra("USER_TEXT_IDENTIFIER", editText.getText().toString());
        setResult(RESULT_OK, data);
        super.onBackPressed();
    }
}
