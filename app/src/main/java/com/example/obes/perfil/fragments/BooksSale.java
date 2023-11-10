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

public class BooksSale extends Fragment {
    private RecyclerView rvBookSale;
    private LinearLayoutManager linearLayoutManagerSale;
    private MyAdapterRecyclerView saleAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books_sale, container, false);

        this.rvBookSale = view.findViewById(R.id.rv_books_sale);

        ArrayList<Book> listAllBooksUserLogged = this.getUserLogged().getListBooksAvailable();

        linearLayoutManagerSale = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        ArrayList<Book> booksForSale = new ArrayList<>();
        for (Book book : listAllBooksUserLogged) {
            if (book.getPrice() > 0) {
                booksForSale.add(book);
            }
        }

        saleAdapter = new MyAdapterRecyclerView(getContext(), booksForSale, "edit");

        this.rvBookSale.setLayoutManager(linearLayoutManagerSale);
        this.rvBookSale.setAdapter(saleAdapter);

        return view;
    }

    private UserCommon getUserLogged() {
        LoginSessionManager loginSessionManager = LoginSessionManager.getInstance();

        UserCommon user = loginSessionManager.getCurrentUserCommon();
        return user;
    }
}