package com.example.obes.model.Request;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.obes.model.Book.Book;

public class ItemRequest implements Parcelable {
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

    protected ItemRequest(Parcel in) {
        id = in.readInt();
        amount = in.readInt();
        price = in.readDouble();
        status = in.readString();
    }

    public static final Creator<ItemRequest> CREATOR = new Creator<ItemRequest>() {
        @Override
        public ItemRequest createFromParcel(Parcel in) {
            return new ItemRequest(in);
        }

        @Override
        public ItemRequest[] newArray(int size) {
            return new ItemRequest[size];
        }
    };

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

    public void setItem(Book item) {
        this.item = item;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(amount);
        dest.writeDouble(price);
        dest.writeString(status);
    }
}