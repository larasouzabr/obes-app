package com.example.obes.dao;

import com.example.obes.model.Cart.Cart;

import java.util.ArrayList;

public class CartDAO {
    private ArrayList<Cart> listCarts;
    private static CartDAO instance;

    private CartDAO() {
        this.listCarts = new ArrayList<Cart>();
    }

    public static CartDAO getInstance() {
        if (instance == null) {
            instance = new CartDAO();
        }
        return instance;
    }

    public ArrayList<Cart> getListCarts() {
        return this.listCarts;
    }

    public Cart getCartById(int idCart) {
        Cart cart = null;

        for (Cart c : this.listCarts) {
            if (c.getId() == idCart) {
                cart = c;
            }
        }

        return cart;
    }

    public boolean addCart(Cart cart) {
        this.listCarts.add(cart);
        return true;
    }

    public boolean deleteCart(Cart cart) {
        Cart cartDelete = null;
        boolean removed = false;

        for (Cart c : listCarts) {
            if (c.getId() == cart.getId()) {
                cartDelete = c;
                break;
            }
        }

        if (cartDelete != null) {
            listCarts.remove(cartDelete);
            removed = true;
        }

        return removed;
    }
}