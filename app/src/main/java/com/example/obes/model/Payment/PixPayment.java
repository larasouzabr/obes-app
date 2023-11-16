package com.example.obes.model.Payment;

public class PixPayment extends OnlinePayment {
    private String pixKey;

    public PixPayment(int id, String pixKey) {
        super(id);
        this.pixKey = pixKey;
    }

    public String getPixKey() {
        return this.pixKey;
    }

    public void setPixKey(String pixKey) {
        this.pixKey = pixKey;
    }

    @Override
    public int getId() {
        return this.id;
    }
}