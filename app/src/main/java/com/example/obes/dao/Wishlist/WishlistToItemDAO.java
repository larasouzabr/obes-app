package com.example.obes.dao.Wishlist;

import com.example.obes.model.Wishlist.ItemWishlist;

import java.util.ArrayList;

public class WishlistToItemDAO {
    private ArrayList<WishToItem> listWishItem;
    private static WishlistToItemDAO instance;

    private WishlistToItemDAO() {
        this.listWishItem = new ArrayList<WishToItem>();
    }

    public static WishlistToItemDAO getInstance() {
        if (instance == null) {
            instance = new WishlistToItemDAO();
        }
        return instance;
    }

    public ArrayList<WishToItem> getListWishItem() {
        return this.listWishItem;
    }

    public ArrayList<ItemWishlist> getItemsByIdWish(int idWish) {
        ItemWishlistDAO itemWishlistDAO = ItemWishlistDAO.getInstance();

        ArrayList<ItemWishlist> listItems = new ArrayList<ItemWishlist>();

        for (WishToItem wishToItem : this.listWishItem) {
            if (wishToItem.getIdWish() == idWish) {
                listItems.add(itemWishlistDAO.getItemById(wishToItem.getIdItem()));
            }
        }

        return listItems;
    }

    public int getIdWishByIdItem(int idItem) {
        int idWish = 0;

        for (WishToItem wi : this.listWishItem) {
            if (wi.getIdWish() == idItem) {
                idWish = wi.getIdWish();
                break;
            }
        }

        return idWish;
    }

    public boolean addWishItem(int idWish, int idItem) {
        WishToItem newWishToItem = new WishToItem(idWish, idItem);

        this.listWishItem.add(newWishToItem);

        return true;
    }

    public boolean deleteWishItem(int idWish, int idItem) {
        WishlistToItemDAO.WishToItem wishToItemDeleted = null;
        boolean deleted = false;

        for (WishlistToItemDAO.WishToItem wi : this.listWishItem) {
            if (wi.getIdWish() == idWish) {
                if (wi.getIdItem() == idItem) {
                    wishToItemDeleted = wi;
                    break;
                }
            }
        }

        if (wishToItemDeleted != null) {
            this.listWishItem.remove(wishToItemDeleted);
            deleted = true;
        }

        return deleted;
    }

    class WishToItem {
        private int idWish;
        private int idItem;

        public WishToItem(int idWish, int idItem) {
            this.idWish = idWish;
            this.idItem = idItem;
        }

        public int getIdWish() {
            return this.idWish;
        }

        public int getIdItem() {
            return this.idItem;
        }
    }
}