package com.example.obes.model.Wishlist;

import com.example.obes.model.Book.Book;

public class ItemWishlist {
    private int id;
    private Book item;

    public ItemWishlist(int id, Book item) {
        this.id = id;
        this.item = item;
    }

    public int getId() {
        return id;
    }

    public Book getItem() {
        return item;
    }

    public void setItem(Book item) {
        this.item = item;
    }
}