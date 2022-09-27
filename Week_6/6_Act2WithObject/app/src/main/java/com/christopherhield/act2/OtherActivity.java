package com.christopherhield.act2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OtherActivity extends AppCompatActivity {

    private Person p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        TextView tv = findViewById(R.id.textView);

        Intent intent = getIntent();
        if (intent.hasExtra("Person")) {
            p = (Person) intent.getSerializableExtra("Person");
            if (p != null)
                tv.setText(p.toString());
        } else {
            tv.setText(R.string.no_person);
        }

        long time = intent.getLongExtra("Time", 0);

        Toast.makeText(this, "Time: " + time, Toast.LENGTH_SHORT).show();

    }

    public void doReturn(View v) {

        EditText editText = findViewById(R.id.editText);

        String doubleStr = editText.getText().toString();
        double value = p.getHourlyRate();

        if (!doubleStr.trim().isEmpty())
            value = Double.parseDouble(editText.getText().toString());

        p.setHourlyRate(value);
        Intent intent = new Intent();
        intent.putExtra("PERSON", p);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        doReturn(null);
        super.onBackPressed();
    }
}
