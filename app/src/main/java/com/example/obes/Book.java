package com.example.obes;


public class Book {
    private String title;
    private int coverResourceId;
    private String author;
    private int price;
    private String description;

    public Book(String title, int coverResourceId, String author, int price, String description) {
        this.title = title;
        this.coverResourceId = coverResourceId;
        this.author = author;
        this.price = price;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public int getCoverResourceId() {
        return coverResourceId;
    }
}

