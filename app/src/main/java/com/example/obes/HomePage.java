package com.example.obes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.obes.dao.LoginSessionManager;
import com.example.obes.model.Book.Book;
import com.example.obes.model.User.User;
import com.example.obes.perfil.PerfilUserCommon;
import com.example.obes.perfil.PerfilUserInstitutional;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    RecyclerView rv_sale;
    RecyclerView rv_donate;
    ArrayList<Book> dataResource;
    LinearLayoutManager linearLayoutManagerSale;
    LinearLayoutManager linearLayoutManagerDonate;
    MyAdapterRecyclerView saleAdapter;
    MyAdapterRecyclerView donateAdapter;
    private ImageView foto_perfil;
    private LoginSessionManager loginSessionManager = LoginSessionManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        this.rv_sale = findViewById(R.id.books_sale);
        this.rv_donate = findViewById(R.id.books_donate);

        dataResource = new ArrayList<>();
        dataResource.add(new Book(1, "Book 1", "Book 1 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book1, "Machado de Assis", 0, "Usado"));
        dataResource.add(new Book(2, "Book 2", "Book 2 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book2, "Machado de Assis", 0, "Usado"));
        dataResource.add(new Book(3, "Book 3", "Book 3 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book2, "Machado de Assis", 0, "Usado"));
        dataResource.add(new Book(4, "Book 4", "Book 4 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book2, "Machado de Assis", 19.99, "Usado"));
        dataResource.add(new Book(5, "Book 5", "Book 5 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book1, "Machado de Assis", 11.99, "Usado"));

        linearLayoutManagerSale = new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManagerDonate = new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false);

        ArrayList<Book> booksForSale = new ArrayList<>();
        ArrayList<Book> booksForDonate = new ArrayList<>();

        for (Book book : dataResource) {
            if (book.getPrice() > 0) {
                booksForSale.add(book);
            } else if (book.getPrice() == 0) {
                booksForDonate.add(book);
            }
        }

        saleAdapter = new MyAdapterRecyclerView(this, booksForSale);
        donateAdapter = new MyAdapterRecyclerView(this, booksForDonate);

        rv_sale.setLayoutManager(linearLayoutManagerSale);
        rv_sale.setAdapter(saleAdapter);

        rv_donate.setLayoutManager(linearLayoutManagerDonate);
        rv_donate.setAdapter(donateAdapter);

        BottomMenuHandler bottomMenuHandler = new BottomMenuHandler(this);
        bottomMenuHandler.setupBottomMenu();

        // =========================

        foto_perfil = findViewById(R.id.foto_perfil);

        foto_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean userLoggedIsCommon = checkedUserLoggedIsCommon();

                if (userLoggedIsCommon) {
                    Intent intent = new Intent(HomePage.this, PerfilUserCommon.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(HomePage.this, PerfilUserInstitutional.class);
                    startActivity(intent);
                }
            }
        });

        // =================
    }

    private boolean checkedUserLoggedIsCommon() {
        if (loginSessionManager.getCurrentUserCommon() != null) {
            return true;
        } else {
            return false;
        }
    }
}