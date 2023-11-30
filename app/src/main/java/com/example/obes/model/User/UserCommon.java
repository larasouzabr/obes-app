package com.example.obes.model.User;

import com.example.obes.dao.BookDAO;
import com.example.obes.dao.BookSaleDAO;
import com.example.obes.dao.UserCommonDAO;
import com.example.obes.dao.UserRegisteredBookDonateDAO;
import com.example.obes.dao.UserRegisteredBookSaleDAO;
import com.example.obes.model.Book.Book;

import java.util.ArrayList;

public class UserCommon extends User {
    private int id;
    private String name;
    private String contact;
    private String email;
    private String password;
    private String cpf;
    private String dateOfBirth;

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    private String about;

    private ArrayList<Book> listBooksAvailable;

    public UserCommon() {

    }

    public UserCommon(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.about = "";
        this.cpf = "";
        this.dateOfBirth = "";

        this.listBooksAvailable = new ArrayList<Book>();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean donateABook(Book book) {
        BookDAO bookDAO = BookDAO.getInstance();
        UserRegisteredBookDonateDAO userRegisteredBookDonateDAO = UserRegisteredBookDonateDAO.getInstance();

        if (bookDAO != null) {
            bookDAO.addBook(book);

            userRegisteredBookDonateDAO.addUserBook(this.getId(), book.getId());

            return true;
        } else {
            throw new IllegalStateException("O DAO de livros para doação não está configurado.");
        }
    }

    public boolean sellABook(Book book) {
        BookSaleDAO bookSaleDAO = BookSaleDAO.getInstance();
        UserRegisteredBookSaleDAO userRegisteredBookSaleDAO = UserRegisteredBookSaleDAO.getInstance();

        if (bookSaleDAO != null) {
            bookSaleDAO.addBook(book);
            userRegisteredBookSaleDAO.addUserBook(this.getId(), book.getId());
            return true;
        } else {
            throw new IllegalStateException("O DAO de livros para venda não está configurado.");
        }
    }

    public void editUser(UserCommon user) {
        UserCommonDAO userCommonDAO = UserCommonDAO.getInstance();
        userCommonDAO.editUser(user);
    }

    public void deleteBook(Book book) {
        BookDAO bookDAO = BookDAO.getInstance();
        boolean deleted = bookDAO.deleteBook(book);

        if (deleted) {
            UserRegisteredBookDonateDAO.getInstance().deleteUserBook(this.getId(), book.getId());
        } else {
            BookSaleDAO bookSaleDAO = BookSaleDAO.getInstance();
            bookSaleDAO.deleteBook(book);

            UserRegisteredBookSaleDAO.getInstance().deleteUserBook(this.getId(), book.getId());
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

    public ArrayList<Book> getListBooksAvailable() {
        this.listBooksAvailable = new ArrayList<Book>();

        UserRegisteredBookSaleDAO userRegisteredBookSaleDAO = UserRegisteredBookSaleDAO.getInstance();
        UserRegisteredBookDonateDAO userRegisteredBookDonateDAO = UserRegisteredBookDonateDAO.getInstance();

        ArrayList<Book> listBooksSale = userRegisteredBookSaleDAO.getListBookByUser(this.getId());
        ArrayList<Book> listBooksDonate = userRegisteredBookDonateDAO.getListBookByUser(this.getId());

        this.listBooksAvailable.addAll(listBooksSale);
        this.listBooksAvailable.addAll(listBooksDonate);

        return this.listBooksAvailable;
    }
}