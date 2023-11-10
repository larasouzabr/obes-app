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
import com.example.obes.dao.Request.RequestToItemDAO;
import com.example.obes.dao.Request.RequestToUserDAO;
import com.example.obes.model.Book.Book;
import com.example.obes.model.Request.ItemRequest;
import com.example.obes.model.Request.Request;
import com.example.obes.model.User.User;

import java.util.ArrayList;

public class BooksOrder extends Fragment {
    private RecyclerView rvBookOrder;
    private LinearLayoutManager linearLayoutManagerOrder;
    private MyAdapterRecyclerView orderAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books_order, container, false);

        this.rvBookOrder = view.findViewById(R.id.rv_books_order);

        this.linearLayoutManagerOrder = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        ArrayList<Request> requestsUserLogged = RequestToUserDAO.getInstance().getRequestsByIdUser(this.getUserLogged().getId());
        ArrayList<Book> booksRequest = new ArrayList<Book>();

        for (Request r : requestsUserLogged) {
            for (ItemRequest i : RequestToItemDAO.getInstance().getItemsByIdRequest(r.getId())) {
                booksRequest.add(i.getItem());
            }
        }

        this.orderAdapter = new MyAdapterRecyclerView(getContext(), booksRequest, "request");

        this.rvBookOrder.setLayoutManager(this.linearLayoutManagerOrder);
        this.rvBookOrder.setAdapter(this.orderAdapter);

        return view;
    }

    private User getUserLogged() {
        LoginSessionManager loginSessionManager = LoginSessionManager.getInstance();

        User userLogged = loginSessionManager.getCurrentUserCommon();

        if (userLogged == null) {
            userLogged = loginSessionManager.getCurrentUserInstitutional();
        }

        return userLogged;
    }
}