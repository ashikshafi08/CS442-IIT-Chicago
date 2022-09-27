package com.christopherhield.act2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Person person;
    private TextView textView;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        person = new Person("John Smith", 49.99);

        textView = findViewById(R.id.textView);
        textView.setText(person.toString());

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);
    }

    public void openAct2(View v) {

        Intent intent = new Intent(this, OtherActivity.class);
        intent.putExtra("Person", person);
        intent.putExtra("Time", System.currentTimeMillis());

        activityResultLauncher.launch(intent);
    }


    public void handleResult(ActivityResult result) {

        if (result == null || result.getData() == null) {
            Log.d(TAG, "handleResult: NULL ActivityResult received");
            return;
        }

        Intent data = result.getData();
        if (result.getResultCode() == RESULT_OK) {
            person = (Person) data.getSerializableExtra("PERSON");
            if (person != null) {
                textView.setText(person.toString());
            }
        } else {
            Toast.makeText(this, "OTHER result not OK!", Toast.LENGTH_SHORT).show();
        }
    }

}
