package com.example.obes.model.Request;

import java.util.ArrayList;

public class Request {
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
}