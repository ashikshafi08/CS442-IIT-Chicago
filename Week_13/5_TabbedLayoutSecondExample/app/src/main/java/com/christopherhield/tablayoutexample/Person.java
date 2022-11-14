package com.christopherhield.tablayoutexample;

import java.io.Serializable;

public class Person implements Serializable, Comparable<Person>{

    private final String fName;
    private final String lName;
    private final String department;
    private final String location;
    private final String phone;

    Person(String fName, String lName, String department, String location, String phone) {
        this.fName = fName;
        this.lName = lName;
        this.department = department;
        this.location = location;
        this.phone = phone;
    }

    String getfName() {
        return fName;
    }

    String getlName() {
        return lName;
    }

    String getDepartment() {
        return department;
    }

    String getLocation() {
        return location;
    }

    String getLastFirst() {
        return lName + ", " + fName;
    }

    String getPhone() {
        return phone;
    }

    @Override
    public int compareTo(Person person) {
        if (lName.equals(person.lName))
            return fName.compareTo(person.fName);

        return lName.compareTo(person.lName);
    }

    @Override
    public String toString() {
        return "Person{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                '}';
    }
}
