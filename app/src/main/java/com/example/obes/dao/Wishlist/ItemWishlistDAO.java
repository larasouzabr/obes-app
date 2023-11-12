package com.example.obes.dao.Wishlist;

import com.example.obes.model.Wishlist.ItemWishlist;

import java.util.ArrayList;

public class ItemWishlistDAO {
    private ArrayList<ItemWishlist> listItems;
    private static ItemWishlistDAO instance;

    private ItemWishlistDAO() {
        this.listItems = new ArrayList<ItemWishlist>();
    }

    public static ItemWishlistDAO getInstance() {
        if (instance == null) {
            instance = new ItemWishlistDAO();
        }
        return instance;
    }

    public ArrayList<ItemWishlist> getListItems() {
        return this.listItems;
    }

    public boolean addItemWishlist(ItemWishlist item) {
        this.listItems.add(item);
        return true;
    }

    public boolean deleteItemWishlist(ItemWishlist item) {
        ItemWishlist itemDelete = null;
        boolean removed = false;

        for (ItemWishlist i : this.listItems) {
            if (i.getId() == item.getId()) {
                itemDelete = i;
                break;
            }
        }

        if (itemDelete != null) {
            this.listItems.remove(itemDelete);
            removed = true;
        }

        return removed;
    }

    public ItemWishlist getItemById(int idItem) {
        ItemWishlist itemWishlist = null;

        for (ItemWishlist item : this.listItems) {
            if (item.getId() == idItem) {
                itemWishlist = item;
                break;
            }
        }

        return itemWishlist;
    }

    public ItemWishlist getItemByIdBook(int idBook) {
        ItemWishlist itemWishlist = null;

        for (ItemWishlist item : this.listItems) {
            if (item.getItem().getId() == idBook) {
                itemWishlist = item;
                break;
            }
        }

        return itemWishlist;
    }
}