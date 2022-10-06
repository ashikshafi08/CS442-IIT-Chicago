package com.example.christopher.recycler;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Employee implements Serializable {

    private final String name;
    private final long empId;
    private final String department;

    private static int ctr = 1;

    Employee() {
        this.name = "Employee Name " + ctr;
        this.empId = System.currentTimeMillis();
        this.department = "Department " + ctr;
        ctr++;
    }

    public Employee(String name, long empId, String department) {
        this.name = name;
        this.empId = empId;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    long getEmpId() {
        return empId;
    }

    String getDepartment() {
        return department;
    }

    @NonNull
    @Override
    public String toString() {
        return name + " (" + empId+ "), " + department;
    }
}