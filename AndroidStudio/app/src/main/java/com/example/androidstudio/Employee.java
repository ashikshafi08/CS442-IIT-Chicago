package com.example.androidstudio;

import androidx.annotation.NonNull;

public class Employee {

    private final String name;
    private final long empId;
    private final String department;

    private static int ctr = 1; // can be accessed only within the Employee class

    // Initializer or the Consturctor
   Employee(){
       this.name = "Employee name" + ctr;
       this.empId = System.currentTimeMillis();
       this.department = "Department " + ctr;

       // Increment is added for every instance
       ctr ++;
   }

   // Creating methods
    public String getName(){
        return name;
    }

    public long getEmpId(){
       return empId;
    }

    public String getDepartment(){
       return department;
    }

    // Over writing the return String Method
    @NonNull
    @Override
    public String toString(){
       return name + "(" + empId + ")" + department;
    }



}
