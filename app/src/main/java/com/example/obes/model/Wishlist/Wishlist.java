package com.example.obes.model.Wishlist;

import com.example.obes.dao.Wishlist.ItemWishlistDAO;
import com.example.obes.dao.Wishlist.WishlistToItemDAO;
import com.example.obes.dao.Wishlist.WishlistToUserDAO;

import java.util.ArrayList;

public class Wishlist {
    private int id;
    private ArrayList<ItemWishlist> listItems;

    public Wishlist(int id) {
        this.id = id;
        this.listItems = new ArrayList<ItemWishlist>();
    }

    public void addItem(ItemWishlist item) {
        ItemWishlistDAO.getInstance().addItemWishlist(item);
        WishlistToItemDAO.getInstance().addWishItem(this.getId(), item.getId());
    }

    public void deleteItem(ItemWishlist item) {
        ItemWishlistDAO.getInstance().deleteItemWishlist(item);
        WishlistToItemDAO.getInstance().deleteWishItem(this.getId(), item.getId());
    }

    public ArrayList<ItemWishlist> getListItems() {
        WishlistToItemDAO wishlistToItemDAO = WishlistToItemDAO.getInstance();

        return wishlistToItemDAO.getItemsByIdWish(this.getId());
    }

    public int getId() {
        return this.id;
    }
}