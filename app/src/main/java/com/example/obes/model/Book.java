package com.example.obes.model;


public class Book {
    private int id;
    private String title;
    private String description;
    private String category;
    private boolean available;
    private int coverResourceId;
    private String author;
    private double price;

    public Book(int id, String title, String description, String category, boolean available, int coverResourceId, String author, double price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.available = available;
        this.coverResourceId = coverResourceId;
        this.author = author;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getCoverResourceId() {
        return coverResourceId;
    }

    public void setCoverResourceId(int coverResourceId) {
        this.coverResourceId = coverResourceId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

