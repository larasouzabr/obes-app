package com.example.obes.model.Wishlist;

import java.util.ArrayList;

public class Wishlist {
    private int id;
    private ArrayList<ItemWishlist> listItems;

    public Wishlist(int id) {
        this.id = id;
        this.listItems = new ArrayList<ItemWishlist>();
    }

    public void addItem(ItemWishlist item) {
        this.listItems.add(item);
    }

    public void deleteItem(ItemWishlist item) {
        this.listItems.remove(item);
    }

    public ArrayList<ItemWishlist> getListItems() {
        return this.listItems;
    }

    public int getId() {
        return this.id;
    }
}