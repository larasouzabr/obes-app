package com.example.obes.perfil.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.obes.MyAdapterRecyclerView;
import com.example.obes.R;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.dao.Request.OrderDAO;
import com.example.obes.dao.Request.OrderItemDAO;
import com.example.obes.dao.Request.RequestToItemDAO;
import com.example.obes.model.Book.Book;
import com.example.obes.model.Request.ItemRequest;
import com.example.obes.model.Request.Request;
import com.example.obes.model.User.User;

import java.util.ArrayList;

public class BooksRequest extends Fragment {
    private RecyclerView rvBookRequest;
    private LinearLayoutManager linearLayoutManagerRequest;
    private MyAdapterRecyclerView requestAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books_request, container, false);

        this.rvBookRequest = view.findViewById(R.id.rv_books_request);

        this.linearLayoutManagerRequest = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        ArrayList<Request> requestsUserLogged = OrderDAO.getInstance().getRequestsByIdUser(this.getUserLogged().getId());
        ArrayList<ItemRequest> itemsUserLogged = OrderItemDAO.getInstance().getItemsByIdUser(this.getUserLogged().getId());
        ArrayList<Book> booksRequest = new ArrayList<Book>();

        for (Request request : requestsUserLogged) {
            for (ItemRequest item : RequestToItemDAO.getInstance().getItemsByIdRequest(request.getId())) {
                booksRequest.add(item.getItem());
            }
        }

        for (ItemRequest item : itemsUserLogged) {
            booksRequest.add(item.getItem());
        }

        this.requestAdapter = new MyAdapterRecyclerView(getContext(), booksRequest, "request");

        this.rvBookRequest.setLayoutManager(this.linearLayoutManagerRequest);
        this.rvBookRequest.setAdapter(this.requestAdapter);

        return view;
    }

    public User getUserLogged() {
        LoginSessionManager loginSessionManager = LoginSessionManager.getInstance();

        User userLogged = loginSessionManager.getCurrentUserCommon();

        if (userLogged == null) {
            userLogged = loginSessionManager.getCurrentUserInstitutional();
        }

        return userLogged;
    }

}