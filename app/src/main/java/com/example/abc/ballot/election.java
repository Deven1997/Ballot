package com.example.abc.ballot;

import java.util.HashMap;

public class election {
    String class_name,election_name,range_from,range_to;
    HashMap<String,String> post = new HashMap<>();

    public election()
    {

    }

    public election(String class_name, String election_name, String range_from, String range_to, HashMap<String, String> post) {
        this.class_name = class_name;
        this.election_name = election_name;
        this.range_from = range_from;
        this.range_to = range_to;
        this.post = post;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getElection_name() {
        return election_name;
    }

    public String getRange_from() {
        return range_from;
    }

    public String getRange_to() {
        return range_to;
    }

    public HashMap<String, String> getPost() {
        return post;
    }
}