package com.example.christopher.recycler;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity // NOTE the interfaces here!
        implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "MainActivity";

    // The above lines are important - them make this class a listener
    // for click and long click events in the ViewHolders (in the recycler

    private final List<Employee> employeeList = new ArrayList<>();  // Main content is here
    private RecyclerView recyclerView; // Layout's recyclerview
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private EmployeesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recycler);
        // Data to recyclerview adapter
         mAdapter = new EmployeesAdapter(employeeList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Make some data - not always needed - just used to fill list
        for (int i = 0; i < 30; i++) {
            employeeList.add(new Employee());
        }

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                this::getEmployeeDataResult);
    }

    // From OnClickListener
    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        // When I tap on the list, I wanna get the position of the item.
        int pos = recyclerView.getChildLayoutPosition(v);
        Employee e = employeeList.get(pos);

        // Then open a Intent
        Intent intent = new Intent(this , EmployeeDetailsActivity.class);
        intent.putExtra("EDIT_EMPLOYEE" , e);
        activityResultLauncher.launch(intent);
    }

    public void addEmployee(View v){
        Intent intent = new Intent(this, EmployeeDetailsActivity.class);
        activityResultLauncher.launch(intent);
        // Want to get the data passed back from the EmployeeDetailsActivity

    }

    public void getEmployeeDataResult(ActivityResult activityResult){
            if (activityResult.getResultCode() == RESULT_OK){
                Intent data = activityResult.getData();

                if(data == null){
                    return;
                }

                if(data.hasExtra("NEW_EMPLOYEE")){
                    // Need to cast here we are receiving a object
                    Employee new_employee =(Employee) data.getSerializableExtra("NEW_EMPLOYEE");

                    // Appending the new employee into the ArrayList
                    employeeList.add(new_employee);
                    // Tell the adapter that the position changed
                    mAdapter.notifyItemInserted(employeeList.size()); // add at the end

                }
            }
    }

    // From OnLongClickListener
    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks

        return false;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "The back button was pressed - Bye!", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

}