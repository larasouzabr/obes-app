package com.example.obes.dao;

import com.example.obes.model.Book;

import java.util.ArrayList;

public interface IBookDAO {
    public boolean addBook(Book book);
    public boolean deleteBook(Book book);
    public boolean editBook(Book book);
    public Book getBookById(int idBook);
    public ArrayList<Book> getListBooks();
}
