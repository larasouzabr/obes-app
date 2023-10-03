package com.example.obes.dao;

import android.content.Context;

import com.example.obes.model.Book;

import java.util.ArrayList;

public class BookDAO implements IBookDAO {
    static ArrayList<Book> listBooks;
    static Context context;

    private static BookDAO bookDAO;

    private BookDAO(Context context) {
        BookDAO.context = context;
        BookDAO.listBooks = new ArrayList<Book>();
    }

    public static IBookDAO getInstance( Context context ) {

        if( BookDAO.bookDAO == null ){
            BookDAO.bookDAO = new BookDAO( context );
        }
        return BookDAO.bookDAO;
    }

    public boolean init(){
        return true;
    }

    public ArrayList<Book> getListBooks() {
        return listBooks;
    }

    public boolean addBook(Book book) {
        listBooks.add(book);
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
            bookEdit.setDescription(book.getDescription());
            bookEdit.setCategory(book.getCategory());
            bookEdit.setAvailable(book.getAvailable());
            bookEdit.setCoverResourceId(book.getCoverResourceId());
            bookEdit.setPrice(book.getPrice());
        }

        return edited;
    }
    public Book getBookById(int idBook) {
        Book book = null;

        for(Book b : listBooks){
            if( b.getId() == idBook ){
                book = b;
                break;
            }
        }

        return book;
    }
}
