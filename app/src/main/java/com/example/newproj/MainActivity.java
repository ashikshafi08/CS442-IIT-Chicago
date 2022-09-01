package com.example.newproj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Storing the value in a TextView
        // TextView newTxt = findViewById(R.id.txtMessage);

        // Changing the attribute to something else
        // newTxt.setText("Hello...");

    }

    


    // On Button Click execute whats inside the function
    public void onBtnClick(View view){

        // Getting all the User Inputs
        EditText FirstName = findViewById(R.id.txtBoxFirstName);
        EditText LastName = findViewById(R.id.txtBoxLastName);
        EditText Email = findViewById(R.id.txtBoxEmail);

        // Viewing the text after the button has been clicked
        TextView textViewFirstName = findViewById(R.id.txtViewFName);
        TextView textViewLastName = findViewById(R.id.txtViewLName);
        TextView textViewEmail = findViewById(R.id.txtViewEmail);

        // Converting and making sure that everything is text
        String StringFirstName = FirstName.getText().toString();
        String StringLastName = LastName.getText().toString();
        String StringEmail = Email.getText().toString();

        // According to the above, we are changing the text

        textViewFirstName.setText("First Name: " + StringFirstName);
        textViewLastName.setText("Last Name: " + StringLastName);
        textViewEmail.setText("Email: " + StringEmail);

    }
}