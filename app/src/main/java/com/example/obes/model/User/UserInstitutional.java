package com.example.obes.model.User;

import com.example.obes.dao.UserInstitutionalDAO;
import com.example.obes.model.Book.Book;

import java.util.ArrayList;

public class UserInstitutional extends User {
    private int id;
    private String name;
    private String contact;
    private String email;
    private String password;
    private String type;
    private String about;

    private ArrayList<Book> listMyBooks;


    public UserInstitutional(int id, String name, String email, String password, String type) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type;
        this.about = "";
        this.contact = "";

        this.listMyBooks = new ArrayList<Book>();
    }

    public void editUser(UserInstitutional user) {
        UserInstitutionalDAO userInstitutionalDAO = UserInstitutionalDAO.getInstance();
        userInstitutionalDAO.editUser(user);
    }

    public int getId() {
        return id;
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

    public ArrayList<Book> getListMyBooks() {
        return this.listMyBooks;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbout() {
        return this.about;
    }
    public void setAbout(String about) {
        this.about = about;
    }
}