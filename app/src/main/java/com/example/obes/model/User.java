package com.example.obes.model;

import com.example.obes.dao.BookDAO;
import com.example.obes.dao.IBookDAO;

public class User {
    private int id;
    private String name;
    private String contact;
    private String email;
    private String password;
    private boolean isUserCommon;

    public User(int id, String name, String contact, String email, String password, boolean isUserCommon) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.password = password;
        this.isUserCommon = isUserCommon;
    }

    public boolean donateABook(Book book) {
        BookDAO bookDAO = BookDAO.getInstance();

        if(this.isUserCommon) {
            if (bookDAO != null) {
                bookDAO.addBook(book);
                return true;
            } else {
                throw new IllegalStateException("O DAO de livros não está configurado.");
            }
        } else {
            throw new IllegalStateException("Apenas usuários comuns podem doar livros.");
        }
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

    public boolean getIsUserCommon() {
        return this.isUserCommon;
    }
}
