package com.example.activitiesintents;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView newText;
    // If we want the Activity to Return something, we use this.
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newText = findViewById(R.id.displayText);

        // Create the instance of the ActivityResultLauncher
       activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                this::handleResult); // function should be called, when the value is returned.

    }

    public void goToA(View v){
        Intent intent = new Intent(this, ActivityA.class);
        intent.putExtra("OPENING_CLASS" , MainActivity.class.getSimpleName());
        activityResultLauncher.launch(intent);
    }



    public void goToB(View v){
        Intent intent = new Intent(this, ActivityB.class);
        activityResultLauncher.launch(intent);
    }

    public void handleResult(ActivityResult result){

        // Checking if the result code or the intent recieved is null
        if(result == null || result.getData() == null){
            Toast.makeText(this, "Null Activity Result Recieved" , Toast.LENGTH_LONG).show();
            return;
        }

        // Create an intent to recieve data // Which should get th Data
        Intent data = result.getData();

        // If the code is write, then get the value
        if(result.getResultCode() == RESULT_OK){
            // Executes only if the above is False
            String text = data.getStringExtra("USER_TEXT");

            if(text == null){
                Toast.makeText(this, "Received Null Text" , Toast.LENGTH_SHORT).show();
                return;
            }

            if (text.isEmpty()){
                Toast.makeText(this, "Empty Text Received" , Toast.LENGTH_SHORT).show();
            }

            newText.setText(text);


        }
    }


}