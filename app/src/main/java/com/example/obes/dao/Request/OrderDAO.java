package com.example.obes.dao.Request;

import com.example.obes.model.Request.Request;

import java.util.ArrayList;

public class OrderDAO {
    private ArrayList<RequestToUser> listRequestUser;
    private static OrderDAO instance;

    private OrderDAO() {
        this.listRequestUser = new ArrayList<RequestToUser>();
    }

    public static OrderDAO getInstance() {
        if (instance == null) {
            instance = new OrderDAO();
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
            }
        }

        return requestsUser;
    }

    public int getIdUserByIdRequest(int idRequest) {
        for (RequestToUser ru : this.listRequestUser) {
            if (ru.getIdRequest() == idRequest) {
                return ru.getIdUser();
            }
        }
        return 0;
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