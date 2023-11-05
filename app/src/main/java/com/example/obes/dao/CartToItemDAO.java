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

    public boolean addCartItem(int idCart, int idItem) {
        CartToItem newCartToItem = new CartToItem(idCart, idItem);

        listCartsItems.add(newCartToItem);
        return true;
    }
    public boolean deleteCartItem(int idCart, int idItem) {
        CartToItem cartToItemDeleted = null;
        boolean deleted = false;

        for (CartToItem ci : this.listCartsItems) {
            if (ci.getIdCart() == idCart) {
                if (ci.getIdItem() == idItem) {
                    cartToItemDeleted = ci;
                    break;
                }
            }
        }

        if (cartToItemDeleted != null) {
            listCartsItems.remove(cartToItemDeleted);
            deleted = true;
        }


        return deleted;
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