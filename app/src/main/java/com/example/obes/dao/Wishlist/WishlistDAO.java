package com.example.obes.dao.Wishlist;

import com.example.obes.model.Cart.Cart;
import com.example.obes.model.Wishlist.Wishlist;

import java.util.ArrayList;

public class WishlistDAO {
    private ArrayList<Wishlist> wishlists;
    private static WishlistDAO instance;

    private WishlistDAO() {
        this.wishlists = new ArrayList<Wishlist>();
    }

    public static WishlistDAO getInstance() {
        if (instance == null) {
            instance = new WishlistDAO();
        }
        return instance;
    }

    public ArrayList<Wishlist> getWishlists() {
        return this.wishlists;
    }

    public Wishlist getWishlistById(int idWishlist) {
        Wishlist wishlist = null;

        for (Wishlist w : this.wishlists) {
            if (w.getId() == idWishlist) {
                wishlist = w;
            }
        }

        return wishlist;
    }

    public boolean addWishlist(Wishlist wishlist) {
        this.wishlists.add(wishlist);
        return true;
    }

    public boolean deleteWishlist(Wishlist wishlist) {
        Wishlist wishlistDelete = null;
        boolean removed = false;

        for (Wishlist w : this.wishlists) {
            if (w.getId() == wishlist.getId()) {
                wishlistDelete = w;
                break;
            }
        }

        if (wishlistDelete != null) {
            this.wishlists.remove(wishlist);
            removed = true;
        }

        return removed;
    }
}