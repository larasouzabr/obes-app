package com.example.obes.dao;

import com.example.obes.model.Cart.ItemCart;

import java.util.ArrayList;

public class CartToItemDAO {
    private ArrayList<CartToItem> listCartsItems;

    private static CartToItemDAO instance;

    private CartToItemDAO() {
        this.listCartsItems = new ArrayList<CartToItem>();
    }

    public static CartToItemDAO getInstance() {
        if (instance == null) {
            instance = new CartToItemDAO();
        }
        return instance;
    }

    public ArrayList<CartToItem> getListCartsItems() {
        return listCartsItems;
    }
    public ArrayList<ItemCart> getItemsByIdCart(int idCart) {
        ItemCartDAO itemCartDAO = ItemCartDAO.getInstance();

        ArrayList<ItemCart> listItems = new ArrayList<ItemCart>();

        for (CartToItem cartToItem : this.listCartsItems) {
            if (cartToItem.getIdCart() == idCart) {
                listItems.add(itemCartDAO.getItemById(cartToItem.getIdItem()));
            }
        }

        return listItems;
    }

    public boolean addCartItem(CartToItem cartItem) {
        listCartsItems.add(cartItem);
        return true;
    }
    public boolean deleteCartItem(CartToItem cartItem) {
        listCartsItems.remove(cartItem);
        return true;
    }

    class CartToItem {
        private int idCart;
        private int idItem;

        public CartToItem(int idCart, int idItem) {
            this.idCart = idCart;
            this.idItem = idItem;
        }

        public int getIdCart() {
            return this.idCart;
        }

        public int getIdItem() {
            return this.idItem;
        }
    }
}