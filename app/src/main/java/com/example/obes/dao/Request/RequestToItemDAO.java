package com.example.obes.dao.Request;

import com.example.obes.dao.Wishlist.ItemWishlistDAO;
import com.example.obes.dao.Wishlist.WishlistToItemDAO;
import com.example.obes.model.Request.ItemRequest;
import com.example.obes.model.Request.Request;
import com.example.obes.model.Wishlist.ItemWishlist;

import java.util.ArrayList;

public class RequestToItemDAO {
    private ArrayList<RequestToItem> listRequestItems;
    private static RequestToItemDAO instance;

    private RequestToItemDAO() {
        this.listRequestItems = new ArrayList<RequestToItem>();
    }

    public static RequestToItemDAO getInstance() {
        if (instance == null) {
            instance = new RequestToItemDAO();
        }
        return instance;
    }

    public ArrayList<ItemRequest> getItemsByIdRequest(int idRequest) {
        ItemRequestDAO itemRequestDAO = ItemRequestDAO.getInstance();

        ArrayList<ItemRequest> listItems = new ArrayList<ItemRequest>();

        for (RequestToItem requestToItem : this.listRequestItems) {
            if (requestToItem.getIdRequest() == idRequest) {
                listItems.add(itemRequestDAO.getItemById(requestToItem.getIdItem()));
            }
        }

        return listItems;
    }

    public int getIdRequestByIdItem(int idItem) {
        int idRequest = 0;

        for (RequestToItem ri : this.listRequestItems) {
            if (ri.getIdItem() == idItem) {
                idRequest = ri.getIdRequest();
                break;
            }
        }

        return idRequest;
    }

    public boolean addRequestItem(int idRequest, int idItem) {
        RequestToItem newRequestToItem = new RequestToItem(idRequest, idItem);

        this.listRequestItems.add(newRequestToItem);

        return true;
    }

    public boolean deleteRequestItem(int idRequest, int idItem) {
        RequestToItem requestToItemDeleted = null;
        boolean deleted = false;

        for (RequestToItem ri : this.listRequestItems) {
            if (ri.getIdRequest() == idRequest) {
                if (ri.getIdItem() == idItem) {
                    requestToItemDeleted = ri;
                    break;
                }
            }
        }

        if (requestToItemDeleted != null) {
            this.listRequestItems.remove(requestToItemDeleted);
            deleted = true;
        }

        return deleted;
    }

    class RequestToItem {
        private int idRequest;
        private int idItem;

        public RequestToItem(int idRequest, int idItem) {
            this.idRequest = idRequest;
            this.idItem = idItem;
        }

        public int getIdRequest() {
            return this.idRequest;
        }

        public int getIdItem() {
            return this.idItem;
        }
    }
}