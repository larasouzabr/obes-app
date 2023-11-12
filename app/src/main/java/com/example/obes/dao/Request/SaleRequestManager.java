package com.example.obes.dao.Request;

import com.example.obes.dao.BookSaleDAO;
import com.example.obes.dao.UserRegisteredBookSaleDAO;
import com.example.obes.model.Request.ItemRequest;
import com.example.obes.model.Request.Request;

import java.util.ArrayList;

public class SaleRequestManager {
    public SaleRequestManager() {}

    public static void saleRequest(Request request, ArrayList<ItemRequest> items, int idUserMaking) {
        RequestDAO.getInstance().addRequest(request);

        RequestToUserDAO.getInstance().addRequestToUser(request.getId(), idUserMaking);

        ItemRequestDAO itemRequestDAO = ItemRequestDAO.getInstance();
        for (ItemRequest item : items) {
            itemRequestDAO.addItemRequest(item);

            RequestToItemDAO.getInstance().addRequestItem(request.getId(), item.getId());

            int idUserReceiving = UserRegisteredBookSaleDAO.getInstance().getIdUserByIdBook(item.getItem().getId());
            OrderItemDAO.getInstance().addItemToUser(item.getId(), idUserReceiving);

            item.getItem().setAvailable(false);
            BookSaleDAO.getInstance().editBook(item.getItem());
        }
    }

    public static void cancelSaleItemRequest(ItemRequest item) {
        int idRequest = RequestToItemDAO.getInstance().getIdRequestByIdItem(item.getId());

        RequestToItemDAO.getInstance().deleteRequestItem(idRequest, item.getId());

        int idUserReceiving = UserRegisteredBookSaleDAO.getInstance().getIdUserByIdBook(item.getItem().getId());
        OrderItemDAO.getInstance().deleteItemToUser(item.getId(), idUserReceiving);

        item.getItem().setAvailable(true);
        BookSaleDAO.getInstance().editBook(item.getItem());
    }

    public static void confirmSaleItemOrder(ItemRequest item) {
        int idUserReceiving = UserRegisteredBookSaleDAO.getInstance().getIdUserByIdBook(item.getItem().getId());
        OrderItemDAO.getInstance().deleteItemToUser(item.getId(), idUserReceiving);

        item.setStatus("Confirmado");
        ItemRequestDAO.getInstance().editItemRequest(item);
    }

    public static void cancelSaleItemOrder(ItemRequest item) {
        int idUserReceiving = UserRegisteredBookSaleDAO.getInstance().getIdUserByIdBook(item.getItem().getId());
        OrderItemDAO.getInstance().deleteItemToUser(item.getId(), idUserReceiving);

        item.setStatus("Cancelado");
        ItemRequestDAO.getInstance().editItemRequest(item);

        item.getItem().setAvailable(true);
        BookSaleDAO.getInstance().editBook(item.getItem());
    }
}
