package com.example.obes.dao;

import com.example.obes.model.Book.Book;
import com.example.obes.model.User.User;
import com.example.obes.model.User.UserCommon;

import java.util.ArrayList;

public class UserRegisteredBookSaleDAO implements IUserRegisteredBookDAO {
    private ArrayList<UserBook> listUserBook;
    private static UserRegisteredBookSaleDAO instance;

    private UserRegisteredBookSaleDAO() {
        this.listUserBook = new ArrayList<UserBook>();
    }

    public static UserRegisteredBookSaleDAO getInstance() {
        if(instance == null) {
            instance = new UserRegisteredBookSaleDAO();
        }
        return instance;
    }

    public ArrayList<UserBook> getListUserBook() {
        return this.listUserBook;
    }

    public boolean addUserBook(int id_user, int id_book) {
        UserBook userBook = new UserBook(id_user, id_book);

        this.listUserBook.add(userBook);

        return true;
    }

    public boolean deleteUserBook(int id_user, int id_book) {
        UserBook userBookDeleted = null;
        boolean removed = false;

        for(UserBook userBook : this.listUserBook) {
            if(userBook.getId_user() == id_user && userBook.getId_book() == id_book) {
                userBookDeleted = userBook;
                break;
            }
        }

        if(userBookDeleted != null) {
            this.listUserBook.remove(userBookDeleted);
            removed = true;
        }

        return removed;
    }

    public ArrayList<Book> getListBookByUser(int id_user) {
        BookSaleDAO bookSaleDAO = BookSaleDAO.getInstance();
        BookDAO bookDonateDAO = BookDAO.getInstance();

        ArrayList<Book> listBooks = new ArrayList<Book>();

        for (UserBook userBook : this.listUserBook) {
            if (userBook.getId_user() == id_user) {
                listBooks.add(bookSaleDAO.getBookById(userBook.getId_book()));
            }
        }

        return listBooks;
    }

    public int getIdUserByIdBook(int idBook) {
        for (UserBook ub : this.listUserBook) {
            if (ub.getId_book() == idBook) {
                return ub.getId_user();
            }
        }

        return 0;
    }
}