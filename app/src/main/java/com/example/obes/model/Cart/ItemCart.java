package com.example.obes.model.Cart;

import com.example.obes.model.Book.Book;

public class ItemCart {
    private int id;
    private int amount;
    private Book item;
    private boolean selected;

    public ItemCart(int id, int amount, Book item) {
        this.id = id;
        this.amount = amount;
        this.item = item;
        this.selected = false;
    }

    public double calcSubTotal() {
        return this.item.getPrice() * this.amount;
    }

    public void incrementAmount() {
        this.amount++;
    }

    public void decrementAmount() {
        this.amount--;
    }

    public int getAmount() {
        return amount;
    }

    public boolean getSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Book getItem() {
        return item;
    }

    public int getId() {
        return this.id;
    }


}