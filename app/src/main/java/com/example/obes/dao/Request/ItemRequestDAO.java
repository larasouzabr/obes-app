package com.example.obes.dao.Request;

import com.example.obes.dao.Wishlist.ItemWishlistDAO;
import com.example.obes.model.Request.ItemRequest;
import com.example.obes.model.Wishlist.ItemWishlist;

import java.util.ArrayList;

public class ItemRequestDAO {
    private ArrayList<ItemRequest> listItemRequests;
    private static ItemRequestDAO instance;

    private ItemRequestDAO() {
        this.listItemRequests = new ArrayList<ItemRequest>();
    }

    public static ItemRequestDAO getInstance() {
        if (instance == null) {
            instance = new ItemRequestDAO();
        }
        return instance;
    }

    public ArrayList<ItemRequest> getListItemRequests() {
        return this.listItemRequests;
    }

    public boolean addItemRequest(ItemRequest item) {
        this.listItemRequests.add(item);
        return true;
    }

    public boolean deleteItemRequest(ItemRequest item) {
        ItemRequest itemDelete = null;
        boolean removed = false;

        for (ItemRequest i : this.listItemRequests) {
            if (i.getId() == item.getId()) {
                itemDelete = i;
                break;
            }
        }

        if (itemDelete != null) {
            this.listItemRequests.remove(itemDelete);
            removed = true;
        }

        return removed;
    }

    public ItemRequest getItemById(int idItem) {
        ItemRequest itemRequest = null;

        for (ItemRequest item : this.listItemRequests) {
            if (item.getId() == idItem) {
                itemRequest = item;
                break;
            }
        }

        return itemRequest;
    }

    public ItemRequest getItemByIdBook(int idBook) {
        ItemRequest itemRequest = null;

        for (ItemRequest item : this.listItemRequests) {
            if (item.getItem().getId() == idBook) {
                itemRequest = item;
                break;
            }
        }

        return itemRequest;
    }
}