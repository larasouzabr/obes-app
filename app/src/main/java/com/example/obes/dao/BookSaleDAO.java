package com.example.obes.dao;

import com.example.obes.model.Book.Book;

import java.util.ArrayList;

public class BookSaleDAO implements IBookDAO {
    private ArrayList<Book> listBooks;
    private static BookSaleDAO instance;

    private BookSaleDAO() {
        this.listBooks = new ArrayList<Book>();
    }

    public static BookSaleDAO getInstance() {
        if(instance == null) {
            instance = new BookSaleDAO();
        }
        return instance;
    }

    public ArrayList<Book> getListBooks() {
        return listBooks;
    }

    public boolean addBook(Book book) {
        this.listBooks.add(book);
        return true;
    }

    public boolean deleteBook(Book book) {
        Book bookDelete = null;
        boolean removed = false;

        for( Book b : listBooks ){
            if( book.getId() == b.getId() ){
                bookDelete = b;
                break;
            }
        }

        if( bookDelete != null ){
            listBooks.remove( bookDelete );
            removed = true;

        }

        return removed;
    }

    public boolean editBook(Book book) {
        Book bookEdit = null;
        boolean edited = false;

        for(Book b : listBooks){
            if(book.getId() == b.getId()){
                bookEdit = b;
                edited = true;
                break;
            }
        }

        if(edited){
            bookEdit.setTitle(book.getTitle());
            bookEdit.setAuthor(book.getAuthor());
            bookEdit.setDescription(book.getDescription());
            bookEdit.setCategory(book.getCategory());
            bookEdit.setAvailable(book.getAvailable());
            bookEdit.setCoverResourceId(book.getCoverResourceId());
            bookEdit.setPrice(book.getPrice());
            bookEdit.setCondition(book.getCondition());
        }

        return edited;
    }
    public Book getBookById(int idBook) {
        Book book = null;

        for(Book b : this.listBooks){
            if( b.getId() == idBook ){
                book = b;
                break;
            }
        }

        return book;
    }
}