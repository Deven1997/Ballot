package com.example.abc.ballot;

public class Student {
    String name, contact, ucid, department, password;

    public Student() {

    }

    public Student(String name, String contact, String ucid, String department, String password) {
        this.name = name;
        this.contact = contact;
        this.ucid = ucid;
        this.department = department;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getUcid() {
        return ucid;
    }

    public String getDepartment() {
        return department;
    }

    public String getPassword() {
        return password;
    }
}
