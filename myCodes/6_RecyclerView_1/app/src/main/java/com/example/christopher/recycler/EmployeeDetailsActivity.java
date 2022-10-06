package com.example.christopher.recycler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EmployeeDetailsActivity extends AppCompatActivity {
    private EditText empNameId;
    private EditText empId;
    private EditText deptId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        empNameId = findViewById(R.id.empNameId);
        empId = findViewById(R.id.employeeId);
        deptId = findViewById(R.id.deptId);

        // If we already have an employee object in this intent we will create it.
        // If there is no employee object, then we will create a new one.
        Intent intent = getIntent(); // intent which started
        if(intent.hasExtra("EDIT_EMPLOYEE")){
            // Cast to return the EMployee
            Employee e = (Employee) intent.getSerializableExtra("EDIT_EMPLOYEE");
            empNameId.setText(e.getName());
            empId.setText(String.valueOf(e.getEmpId()));
            deptId.setText(e.getDepartment());

            // The above will update the existing details

        }




    }

    public void doSave(View v){
        String nameText = empNameId.getText().toString();
        String empText = empId.getText().toString();
        String deptText = empNameId.getText().toString();

        long idVal = Long.parseLong(empText); // Casting coz we have long in the Constructor.

        Employee employee= new Employee(nameText , idVal , deptText);

        // Using this for sending the data from this activity, so no need to initialize any arguments
        Intent intent = new Intent();

        // Putting inside the Intent
        intent.putExtra("NEW_EMPLOYEE" , employee);
        // Now we will pass this object as a whole, then in the activity, we will use getters or setters to parse the data
        setResult(RESULT_OK , intent);
        finish();




    }
}