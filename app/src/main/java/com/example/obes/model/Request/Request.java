package com.example.obes.model.Request;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Request implements Parcelable {
    private int id;
    private String date;
    private String status;
    private ArrayList<ItemRequest> listItems;

    public Request(int id, String date, String status) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.listItems = new ArrayList<ItemRequest>();
    }

    protected Request(Parcel in) {
        id = in.readInt();
        date = in.readString();
        status = in.readString();
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getDate() {
        return this.date;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<ItemRequest> getListItems() {
        return this.listItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeString(status);
    }
}