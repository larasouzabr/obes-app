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
import com.example.obes.model.Book.Book;
import com.example.obes.model.User.UserCommon;

import java.util.ArrayList;

public class BooksDonate extends Fragment {
    private RecyclerView rvBookDonate;
    private LinearLayoutManager linearLayoutManagerDonate;
    private MyAdapterRecyclerView donateAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books_donate, container, false);

        this.rvBookDonate = view.findViewById(R.id.rv_books_donate);

        ArrayList<Book> listAllBooksUserLogged = this.getUserLogged().getListBooksAvailable();

        linearLayoutManagerDonate = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        ArrayList<Book> booksForDonate = new ArrayList<>();
        for (Book book : listAllBooksUserLogged) {
            if (book.getPrice() == 0) {
                booksForDonate.add(book);
            }
        }

        donateAdapter = new MyAdapterRecyclerView(getContext(), booksForDonate, "edit");

        this.rvBookDonate.setLayoutManager(linearLayoutManagerDonate);
        this.rvBookDonate.setAdapter(donateAdapter);

        return view;
    }

    private UserCommon getUserLogged() {
        LoginSessionManager loginSessionManager = LoginSessionManager.getInstance();

        UserCommon user = loginSessionManager.getCurrentUserCommon();
        return user;
    }
}