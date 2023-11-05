package com.example.obes.model.User;

public abstract class User {
    public abstract int getId();

    public abstract String getName();

    public abstract void setName(String name);

    public abstract String getContact();

    public abstract void setContact(String contact);

    public abstract String getEmail();

    public abstract void setEmail(String email);

    public abstract String getPassword();

    public abstract void setPassword(String password);

    public abstract String getAbout();

    public abstract void setAbout(String about);
}