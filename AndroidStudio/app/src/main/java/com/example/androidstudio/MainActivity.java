package com.example.androidstudio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final ArrayList<Employee> myEmployees= new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference for the Recycler View
        recyclerView = findViewById(R.id.recycler);

        // Creating fake employee's data
        for (int i = 0; i < 50 ; i++) {
            // new ClassName -> actually creates new class instance
            myEmployees.add(new Employee());
        }

        // Create the Adapter by passing the Employee List and the current Activity
        // this -> MainActivity class
        EmployeeListAdapter newAdapter = new EmployeeListAdapter(myEmployees , this);

        // Setting the adapter to the recycler view
        recyclerView.setAdapter(newAdapter);

        // Setting default Layout Manager, that is Vertical
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }

    public void openActivity(View v){
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu){
        getMenuInflater().inflate(R.menu.create_note , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.create_new_note_id){
                Intent secondIntent = new Intent(this, SecondActivity.class);
                startActivity(secondIntent);
                return true; }
            else if(item.getItemId() == R.id.about_info_id){
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                return true;}
            else{
            return super.onOptionsItemSelected(item);
        }
        }
    }
