package com.example.obes.dao.Request;

import com.example.obes.dao.Wishlist.WishlistDAO;
import com.example.obes.model.Request.Request;
import com.example.obes.model.Wishlist.Wishlist;

import java.util.ArrayList;

public class RequestDAO {
    private ArrayList<Request> listRequests;
    private static RequestDAO instance;

    private RequestDAO() {
        this.listRequests = new ArrayList<Request>();
    }

    public static RequestDAO getInstance() {
        if (instance == null) {
            instance = new RequestDAO();
        }
        return instance;
    }

    public ArrayList<Request> getListRequests() {
        return this.listRequests;
    }

    public Request getRequestById(int idRequest) {
        Request request = null;

        for (Request r : this.listRequests) {
            if (r.getId() == idRequest) {
                request = r;
            }
        }

        return request;
    }

    public boolean editRequest(Request request) {
        Request requestEdit = null;
        boolean edited = false;

        for (Request r : this.listRequests) {
            if (r.getId() == request.getId()) {
                requestEdit = r;
                break;
            }
        }

        if (requestEdit != null) {
            requestEdit.setStatus(request.getStatus());

            edited = true;
        }

        return edited;
    }

    public boolean addRequest(Request request) {
        this.listRequests.add(request);
        return true;
    }

    public boolean deleteRequest(Request request) {
        Request requestDelete = null;
        boolean removed = false;

        for (Request r : this.listRequests) {
            if (r.getId() == request.getId()) {
                requestDelete = r;
                break;
            }
        }

        if (requestDelete != null) {
            this.listRequests.remove(request);
            removed = true;
        }

        return removed;
    }
}