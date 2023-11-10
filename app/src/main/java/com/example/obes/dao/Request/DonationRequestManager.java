package com.example.obes.dao.Request;

import com.example.obes.dao.BookDAO;
import com.example.obes.model.Request.ItemRequest;
import com.example.obes.model.Request.Request;

import java.util.ArrayList;

public class DonationRequestManager {
    public DonationRequestManager() {}

    public static void donationRequest(Request request, ItemRequest item, int idUserMaking, int idUserReceiving) {
        RequestDAO requestDAO = RequestDAO.getInstance();
        requestDAO.addRequest(request);

        ItemRequestDAO itemRequestDAO = ItemRequestDAO.getInstance();
        itemRequestDAO.addItemRequest(item);

        RequestToItemDAO requestToItemDAO = RequestToItemDAO.getInstance();
        requestToItemDAO.addRequestItem(request.getId(), item.getId());

        RequestToUserDAO requestToUserDAO = RequestToUserDAO.getInstance();
        requestToUserDAO.addRequestToUser(request.getId(), idUserMaking);

        OrderDAO orderDAO = OrderDAO.getInstance();
        orderDAO.addRequestToUser(request.getId(), idUserReceiving);

        item.getItem().setAvailable(false);
        BookDAO bookDAO = BookDAO.getInstance();
        bookDAO.editBook(item.getItem());
    }

    public static void cancelDonationRequest(Request request, int idUserMaking, int idUserReceiving) {
        RequestDAO requestDAO = RequestDAO.getInstance();
        requestDAO.deleteRequest(request);

        RequestToItemDAO requestToItemDAO = RequestToItemDAO.getInstance();
        ArrayList<ItemRequest> itemsRequest = requestToItemDAO.getItemsByIdRequest(request.getId());

        ItemRequestDAO itemRequestDAO = ItemRequestDAO.getInstance();
        for (ItemRequest item : itemsRequest) {
            itemRequestDAO.deleteItemRequest(item);
            requestToItemDAO.deleteRequestItem(request.getId(), item.getId());

            item.getItem().setAvailable(true);
            BookDAO.getInstance().editBook(item.getItem());
        }

        RequestToUserDAO requestToUserDAO = RequestToUserDAO.getInstance();
        requestToUserDAO.deleteRequestToUser(request.getId(), idUserMaking);

        OrderDAO orderDAO = OrderDAO.getInstance();
        orderDAO.deleteRequestToUser(request.getId(), idUserReceiving);
    }

    public static void updateStatusRequest(Request newRequest) {
        RequestDAO.getInstance().editRequest(newRequest);
    }
}