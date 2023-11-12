package com.example.obes.model.Review;

import java.util.Date;

public class Review {
    private final int id;
    private double rate;
    private String comment;
    private Date dateCreated;
    private Date dateUpdated;

    public Review(int id, double rate, String comment, Date dateCreated) {
        this.id = id;
        this.rate = rate;
        this.comment = comment;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateCreated;
    }

    public int getId() {
        return id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public Date getDateUpdated() {
        return this.dateUpdated;
    }

    public void setDateUpdated(Date date) {
        this.dateUpdated = date;
    }
}