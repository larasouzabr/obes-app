package com.example.obes.model.Cart;

import com.example.obes.model.Book.Book;

public class ItemCart {
    private int id;
    private int amount;
    private Book item;
    private boolean isSelected;

    public ItemCart(int id, int amount, Book item) {
        this.id = id;
        this.amount = amount;
        this.item = item;
        this.isSelected = false;
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

    public boolean getIsSelected() {
        return this.isSelected;
    }

    public void setIsSelected(boolean selected) {
        this.isSelected = selected;
    }

    public Book getItem() {
        return item;
    }

    public int getId() {
        return this.id;
    }


}