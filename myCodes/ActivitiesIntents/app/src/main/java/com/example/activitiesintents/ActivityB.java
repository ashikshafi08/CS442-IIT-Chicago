package com.example.activitiesintents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityB extends AppCompatActivity {

   private EditText holderText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        holderText = findViewById(R.id.editText);
    }

    public void doneClicked(View v){

        // Starting a intent, using it like a bag or holder for data
        Intent data = new Intent();
        data.putExtra("USER_TEXT" , holderText.getText().toString()); // Passing the String back
        setResult(RESULT_OK , data); // return when user completed something and returned.
        // Closes the current activity and returns back to the original activity
        finish();
    }

    // The same for the OnBackPressed
    @Override
    public void onBackPressed(){
        Intent data = new Intent();
        data.putExtra("USER_TEXT" , holderText.getText().toString());
        setResult(RESULT_OK , data);
        //Toast.makeText(this, holderText.getText().toString(), Toast.LENGTH_SHORT).show();

        super.onBackPressed(); // to get out of the Activity
    }
}