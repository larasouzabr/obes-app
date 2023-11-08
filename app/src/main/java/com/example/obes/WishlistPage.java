package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.obes.model.Book.Book;
import com.example.obes.model.Wishlist.ItemWishlist;
import com.example.obes.model.Wishlist.Wishlist;

import java.util.ArrayList;

public class WishlistPage extends AppCompatActivity {
    private RecyclerView rv_items;

    private TextView tvTitlePage;
    private ImageView button_back_arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_page);
        this.startComponents();

        this.tvTitlePage.setText("Lista de Desejos");

        BottomMenuHandler bottomMenuHandler = new BottomMenuHandler(this);
        bottomMenuHandler.setupBottomMenu();

        this.button_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager linearLayoutManagerItems = new LinearLayoutManager(WishlistPage.this, LinearLayoutManager.VERTICAL, false);

        // livros para teste
        ArrayList<Book> dataResource = new ArrayList<>();
        dataResource.add(new Book(1, "Book 1", "Book 1 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book1, "Machado de Assis", 0, "Usado"));
        dataResource.add(new Book(2, "Book 2", "Book 2 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book2, "Machado de Assis", 0, "Usado"));
        dataResource.add(new Book(3, "Book 3", "Book 3 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book2, "Machado de Assis", 0, "Usado"));
        dataResource.add(new Book(4, "Book 4", "Book 4 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book2, "Machado de Assis", 19.99, "Usado"));
        dataResource.add(new Book(5, "Book 5", "Book 5 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book1, "Machado de Assis", 11.99, "Usado"));
        //



        MyAdapterRecyclerViewCart recyclerViewAdapter = new MyAdapterRecyclerViewCart(this, dataResource, false);

        rv_items.setLayoutManager(linearLayoutManagerItems);
        rv_items.setAdapter(recyclerViewAdapter);
    }
    private void startComponents() {
        this.rv_items = findViewById(R.id.items);
        this.tvTitlePage = findViewById(R.id.title_page);
        this.button_back_arrow = findViewById(R.id.back_arrow);
    }
}