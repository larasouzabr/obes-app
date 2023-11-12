package com.example.obes.dao;

import com.example.obes.model.Book.Book;
import com.example.obes.model.User.UserCommon;

import java.util.ArrayList;

public class UserRegisteredBookDonateDAO implements IUserRegisteredBookDAO {
    private ArrayList<UserBook> listUserBook;
    private static UserRegisteredBookDonateDAO instance;

    private UserRegisteredBookDonateDAO() {
        this.listUserBook = new ArrayList<UserBook>();
    }

    public static UserRegisteredBookDonateDAO getInstance() {
        if(instance == null) {
            instance = new UserRegisteredBookDonateDAO();
        }
        return instance;
    }

    public ArrayList<UserBook> getListUserBook() {
        return listUserBook;
    }

    public boolean addUserBook(int id_user, int id_book) {
        UserBook userBook = new UserBook(id_user, id_book);

        listUserBook.add(userBook);

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
        BookDAO bookDAO = BookDAO.getInstance();

        ArrayList<Book> listBooks = new ArrayList<Book>();

        for (UserBook userBook : this.listUserBook) {
            if (userBook.getId_user() == id_user) {
                listBooks.add(bookDAO.getBookById(userBook.getId_book()));
            }
        }

        return listBooks;
    }

    public UserCommon getUserByIdBook(int idBook) {
        UserCommon user = null;

        for (UserBook ub : this.listUserBook) {
            if (ub.getId_book() == idBook) {
                user = UserCommonDAO.getInstance().getUserById(ub.getId_user());
                break;
            }
        }

        return user;
    }
}