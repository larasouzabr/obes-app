package com.example.obes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.obes.dao.BookDAO;
import com.example.obes.dao.BookSaleDAO;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.dao.UserCommonDAO;
import com.example.obes.dao.UserInstitutionalDAO;
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

        linearLayoutManagerSale = new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManagerDonate = new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false);

        ArrayList<Book> booksSale = new ArrayList<Book>();
        ArrayList<Book> booksDonate = new ArrayList<Book>();

        for (Book book : BookSaleDAO.getInstance().getListBooks()) {
            if (book.getAvailable()) {
                booksSale.add(book);
            }
        }

        for (Book book : BookDAO.getInstance().getListBooks()) {
            if (book.getAvailable()) {
                booksDonate.add(book);
            }
        }

        saleAdapter = new MyAdapterRecyclerView(this, booksSale, "common");
        donateAdapter = new MyAdapterRecyclerView(this, booksDonate, "common");

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

        Button buttonWishlist = findViewById(R.id.button_wishlist);

        buttonWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, WishlistPage.class);
                startActivity(intent);
            }
        });

        Button buttonLogout = findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showModalLogout();
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

    public void showModalLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
        LayoutInflater inflater = (LayoutInflater) HomePage.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.modal_delete_book, null);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        TextView tvDescription = dialogView.findViewById(R.id.description);
        Button buttonConfirm = dialogView.findViewById(R.id.button_delete);
        Button buttonCancel = dialogView.findViewById(R.id.button_cancel);

        tvDescription.setText("Tem certeza de que deseja sair?");
        buttonConfirm.setText("Sair");

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSessionManager.logout();
                dialog.dismiss();
                Intent intentLogout = new Intent(HomePage.this, MainActivity.class);
                startActivity(intentLogout);
                finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}