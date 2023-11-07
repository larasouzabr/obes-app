package com.example.obes.dao.Wishlist;

import java.util.ArrayList;

public class WishlistToUserDAO {
    private ArrayList<WishToUser> listWishUser;
    private static WishlistToUserDAO instance;

    private WishlistToUserDAO() {
        this.listWishUser = new ArrayList<WishToUser>();
    }

    public static WishlistToUserDAO getInstance() {
        if (instance == null) {
            instance = new WishlistToUserDAO();
        }
        return instance;
    }

    public ArrayList<WishToUser> getListWishUser() {
        return this.listWishUser;
    }

    public boolean addWishToUser(int idWish, int idUser) {
        WishToUser newWishToUser = new WishToUser(idWish, idUser);

        this.listWishUser.add(newWishToUser);

        return true;
    }

    public boolean deleteWishToUser(int idUser) {
        for (WishlistToUserDAO.WishToUser wishToUser : this.listWishUser) {
            if (wishToUser.getIdUser() == idUser) {
                this.listWishUser.remove(wishToUser);
                return true;
            }
        }

        return false;
    }

    class WishToUser {
        private int idWish;
        private int idUser;

        public WishToUser(int idWish, int idUser) {
            this.idWish = idWish;
            this.idUser = idUser;
        }

        public int getIdWish() {
            return this.idWish;
        }

        public int getIdUser() {
            return this.idUser;
        }
    }
}