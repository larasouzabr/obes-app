package com.example.obes.model.Payment;

import java.util.Date;

public class BoletoPayment extends OnlinePayment {
    private String barcode;
    private Date dueDate;
    public BoletoPayment(int id, String barcode, Date dueDate) {
        super(id);
        this.barcode = barcode;
        this.dueDate = dueDate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public int getId() {
        return this.id;
    }
}
