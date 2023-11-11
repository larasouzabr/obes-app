package com.example.obes.dao.Request;

import com.example.obes.model.Request.ItemRequest;

import java.util.ArrayList;

public class OrderItemDAO {
    private ArrayList<ItemToUser> listItemsUser;
    private static OrderItemDAO instance;

    private OrderItemDAO() {
        this.listItemsUser = new ArrayList<ItemToUser>();
    }

    public static OrderItemDAO getInstance() {
        if (instance == null) {
            instance = new OrderItemDAO();
        }
        return instance;
    }

    public boolean addItemToUser(int idItem, int idUser) {
        ItemToUser newItemToUser = new ItemToUser(idItem, idUser);

        this.listItemsUser.add(newItemToUser);

        return true;
    }

    public boolean deleteItemToUser(int idItem, int idUser) {
        for (ItemToUser iu : this.listItemsUser) {
            if (iu.getIdItem() == idItem) {
                if (iu.getIdUser() == idUser) {
                    this.listItemsUser.remove(iu);
                    return true;
                }
            }
        }

        return false;
    }

    public int getIdUserByIdItem(int idItem) {
        for (ItemToUser iu : this.listItemsUser) {
            if (iu.getIdItem() == idItem) {
                return iu.getIdUser();
            }
        }

        return 0;
    }

    public ArrayList<ItemRequest> getItemsByIdUser(int idUser) {
        ArrayList<ItemRequest> itemsUser = new ArrayList<ItemRequest>();

        ItemRequestDAO itemRequestDAO = ItemRequestDAO.getInstance();

        for (ItemToUser iu : this.listItemsUser) {
            if (iu.getIdUser() == idUser) {
                itemsUser.add(itemRequestDAO.getItemById(iu.getIdItem()));
            }
        }

        return itemsUser;
    }

    class ItemToUser {
        private int idItem;
        private int idUser;

        public ItemToUser(int idItem, int idUser) {
            this.idItem = idItem;
            this.idUser = idUser;
        }

        public int getIdItem() {
            return this.idItem;
        }

        public int getIdUser() {
            return this.idUser;
        }
    }
}
