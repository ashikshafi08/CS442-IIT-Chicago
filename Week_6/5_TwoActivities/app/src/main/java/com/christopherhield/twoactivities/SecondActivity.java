package com.christopherhield.twoactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    private EditText statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView greetText = findViewById(R.id.greetText);
        statusText = findViewById(R.id.statusText);

        if (getIntent().hasExtra("NAME")) {
            String name = getIntent().getStringExtra("NAME");
            greetText.setText(String.format("Hello %s!%n%nHow are you?", name));
        }


    }

    public void doDone(View v) {

        String status = statusText.getText().toString();

        if (status.trim().isEmpty()) {
            Toast.makeText(this, "Please say how you are", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("STATUS", status);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
