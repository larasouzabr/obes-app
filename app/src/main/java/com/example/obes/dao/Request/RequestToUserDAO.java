package com.example.obes.dao.Request;

import com.example.obes.dao.Wishlist.WishlistDAO;
import com.example.obes.dao.Wishlist.WishlistToUserDAO;
import com.example.obes.model.Request.Request;
import com.example.obes.model.Wishlist.Wishlist;

import java.util.ArrayList;

public class RequestToUserDAO {
    private ArrayList<RequestToUser> listRequestUser;
    private static RequestToUserDAO instance;

    private RequestToUserDAO() {
        this.listRequestUser = new ArrayList<RequestToUser>();
    }

    public static RequestToUserDAO getInstance() {
        if (instance == null) {
            instance = new RequestToUserDAO();
        }
        return instance;
    }

    public ArrayList<RequestToUser> getListRequestUser() {
        return this.listRequestUser;
    }

    public ArrayList<Request> getRequestsByIdUser(int idUser) {
        RequestDAO requestDAO = RequestDAO.getInstance();

        ArrayList<Request> requestsUser = new ArrayList<Request>();

        for (RequestToUser requestToUser : this.listRequestUser) {
            if (requestToUser.getIdUser() == idUser) {
                requestsUser.add(requestDAO.getRequestById(requestToUser.getIdRequest()));
                break;
            }
        }

        return requestsUser;
    }

    public boolean addRequestToUser(int idRequest, int idUser) {
        RequestToUser newRequestToUser = new RequestToUser(idRequest, idUser);

        this.listRequestUser.add(newRequestToUser);

        return true;
    }

    public boolean deleteRequestToUser(int idRequest, int idUser) {
        for (RequestToUser requestToUser : this.listRequestUser) {
            if (requestToUser.getIdUser() == idUser) {
                if (requestToUser.getIdRequest() == idRequest) {
                    this.listRequestUser.remove(requestToUser);
                    return true;
                }
            }
        }

        return false;
    }

    class RequestToUser {
        private int idRequest;
        private int idUser;

        public RequestToUser(int idRequest, int idUser) {
            this.idRequest = idRequest;
            this.idUser = idUser;
        }

        public int getIdRequest() {
            return this.idRequest;
        }

        public int getIdUser() {
            return this.idUser;
        }
    }
}