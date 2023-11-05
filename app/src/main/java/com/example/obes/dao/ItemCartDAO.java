package com.example.obes.dao;

import com.example.obes.model.Cart.ItemCart;

import java.util.ArrayList;

public class ItemCartDAO {
    private ArrayList<ItemCart> listItensCart;

    private static ItemCartDAO instance;

    private ItemCartDAO() {
        this.listItensCart = new ArrayList<ItemCart>();
    }

    public static ItemCartDAO getInstance() {
        if (instance == null) {
            instance = new ItemCartDAO();
        }
        return instance;
    }

    public ArrayList<ItemCart> getListItensCart() {
        return listItensCart;
    }

    public boolean addItemCart(ItemCart item) {
        listItensCart.add(item);
        return true;
    }

    public boolean deleteItemCart(ItemCart item) {
        ItemCart itemDelete = null;
        boolean removed = false;

        for (ItemCart i : listItensCart) {
            if (i.getId() == item.getId()) {
                itemDelete = i;
                break;
            }
        }

        if (itemDelete != null) {
            listItensCart.remove(itemDelete);
            removed = true;
        }

        return removed;
    }

    public ItemCart getItemById(int idItem) {
        ItemCart itemCart = null;

        for (ItemCart item : this.listItensCart) {
            if (item.getId() == idItem) {
                itemCart = item;
            }
        }

        return itemCart;
    }
}