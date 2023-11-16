package com.example.obes.model.Book;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Book implements Parcelable {
    private int id;
    private String title;
    private String description;
    private String category;
    private boolean available;
    private int coverResourceId;
    private String author;
    private double price;
    private String condition;

    public Book(int id, String title, String description, String category, boolean available, int coverResourceId, String author, double price, String condition) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.available = available;
        this.coverResourceId = coverResourceId;
        this.author = author;
        this.price = price;
        this.condition = condition;
    }

    protected Book(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        category = in.readString();
        available = in.readByte() != 0;
        coverResourceId = in.readInt();
        author = in.readString();
        price = in.readDouble();
        condition = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

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
    public boolean getAvailable() {
        return this.available;
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

    public String getCondition() {
        return this.condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeByte((byte) (available ? 1 : 0));
        dest.writeInt(coverResourceId);
        dest.writeString(author);
        dest.writeDouble(price);
        dest.writeString(condition);
    }
}