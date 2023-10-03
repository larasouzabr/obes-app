package com.example.obes.model;

public class User {
    private int id;
    private String name;
    private String contact;
    private String email;
    private String password;
    private String type;

    public User(int id, String name, String contact, String email, String password, String type) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
