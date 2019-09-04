package com.example.abc.ballot;

public class Student {
    String s_name,s_contact,s_ucid,s_department,s_password;

    public Student()
    {

    }

    public Student(String s_name, String s_contact, String s_ucid, String s_department, String s_password) {
        this.s_name = s_name;
        this.s_contact = s_contact;
        this.s_ucid = s_ucid;
        this.s_department = s_department;
        this.s_password = s_password;
    }

    public String getS_name() {
        return s_name;
    }

    public String getS_contact() {
        return s_contact;
    }

    public String getS_ucid() {
        return s_ucid;
    }

    public String getS_department() {
        return s_department;
    }

    public String getS_password() {
        return s_password;
    }
}
