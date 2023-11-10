package com.example.obes.model.Request;

import com.example.obes.model.Book.Book;

public class ItemRequest {
    private int id;
    private int amount;
    private double price;
    private Book item;
    private String status;

    public ItemRequest(int id, int amount, Book item, String status) {
        this.id = id;
        this.amount = amount;
        this.price = item.getPrice();
        this.item = item;
        this.status = status;
    }

    public double calcSubTotal() {
        return this.getPrice() * this.getAmount();
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public Book getItem() {
        return item;
    }
}