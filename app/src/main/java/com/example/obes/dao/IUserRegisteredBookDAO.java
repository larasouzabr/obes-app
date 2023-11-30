package com.example.obes.dao;

import com.example.obes.model.Book.Book;

import java.util.ArrayList;

public interface IUserRegisteredBookDAO {
    class UserBook {
        private int id_user;
        private int id_book;

        public UserBook(int id_user, int id_book) {
            this.id_user = id_user;
            this.id_book = id_book;
        }

        public int getId_user() {
            return id_user;
        }

        public int getId_book() {
            return id_book;
        }
    }

    public boolean addUserBook(int id_user, int id_book);
    public boolean deleteUserBook(int id_user, int id_book);
    public ArrayList<UserBook> getListUserBook();
    public ArrayList<Book> getListBookByUser(int id_user);
}