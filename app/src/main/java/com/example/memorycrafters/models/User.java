package com.example.memorycrafters.models;


public class User {
    private String email;

    private String uuid;

    public User(String email, String uuid) {
        this.email = email;
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public String getUUID() {return uuid;}
}
