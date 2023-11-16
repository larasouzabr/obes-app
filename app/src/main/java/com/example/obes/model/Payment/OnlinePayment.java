package com.example.obes.model.Payment;

public abstract class OnlinePayment implements IPayment {
    protected int id;

    public OnlinePayment(int id) {
        this.id = id;
    }
    @Override
    public int getId() {
        return this.id;
    }
}