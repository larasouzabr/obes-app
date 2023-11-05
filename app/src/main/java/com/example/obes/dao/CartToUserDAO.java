package com.example.obes.dao;

import com.example.obes.model.Cart.Cart;

import java.util.ArrayList;

public class CartToUserDAO {
    private ArrayList<CartToUser> listCartUser;

    private static CartToUserDAO instance;

    private CartToUserDAO() {
        this.listCartUser = new ArrayList<CartToUser>();
    }

    public static CartToUserDAO getInstance() {
        if (instance == null) {
            instance = new CartToUserDAO();
        }
        return instance;
    }

    public ArrayList<CartToUser> getListCartUser() {
        return this.listCartUser;
    }

    public Cart getCartByIdUser(int idUser) {
        CartDAO cartDAO = CartDAO.getInstance();

        Cart cartUser = null;

        for (CartToUser cartToUser : this.listCartUser) {
            if (cartToUser.getIdUser() == idUser) {
                cartUser = cartDAO.getCartById(cartToUser.getIdCart());
                break;
            }
        }

        return cartUser;
    }

    public boolean addCartToUser(int idCart, int idUser) {
        CartToUser newCartToUser = new CartToUser(idCart, idUser);

        this.listCartUser.add(newCartToUser);

        return true;
    }

    public boolean deleteCartToUser(int idUser) {
        for (CartToUser cartToUser : this.listCartUser) {
            if (cartToUser.getIdUser() == idUser) {
                this.listCartUser.remove(cartToUser);
                return true;
            }
        }

        return false;
    }

    class CartToUser {
        private int idCart;
        private int idUser;

        public CartToUser(int idCart, int idUser) {
            this.idCart = idCart;
            this.idUser = idUser;
        }

        public int getIdCart() {
            return this.idCart;
        }

        public int getIdUser() {
            return this.idUser;
        }
    }
}