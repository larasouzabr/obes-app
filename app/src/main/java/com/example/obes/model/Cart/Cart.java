package com.example.obes.model.Cart;

import java.util.ArrayList;

public class Cart {
    private int id;
    private ArrayList<ItemCart> listItems;

    public Cart(int id) {
        this.id = id;
        this.listItems = new ArrayList<ItemCart>();
    }

    public void addItem(ItemCart item) {
        this.listItems.add(item);
    }
    public void deleteItem(ItemCart item) {
        this.listItems.remove(item);
    }
    public double calcTotal() {
        double total = 0;

        for (ItemCart item : this.listItems) {
            total += item.calcSubTotal();
        }

        return total;
    }
    public ArrayList<ItemCart> getListItems() {
        return this.listItems;
    }
    public int getId() {
        return this.id;
    }
}