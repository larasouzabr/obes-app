package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.obes.model.Book.Book;

import java.util.ArrayList;

public class CartPage extends AppCompatActivity {
    private TextView tvTitlePage;
    private ImageView button_back_arrow;
    private RecyclerView rv_items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);
        this.startComponents();

        BottomMenuHandler bottomMenuHandler = new BottomMenuHandler(this);
        bottomMenuHandler.setupBottomMenu();

        this.tvTitlePage.setText("Carrinho de Compras");

        this.button_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager linearLayoutManagerItems = new LinearLayoutManager(CartPage.this, LinearLayoutManager.VERTICAL, false);

        // livros para teste
        ArrayList<Book> dataResource = new ArrayList<>();
        dataResource.add(new Book(1, "Book 1", "Book 1 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book1, "Machado de Assis", 0, "Usado"));
        dataResource.add(new Book(2, "Book 2", "Book 2 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book2, "Machado de Assis", 0, "Usado"));
        dataResource.add(new Book(3, "Book 3", "Book 3 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book2, "Machado de Assis", 0, "Usado"));
        dataResource.add(new Book(4, "Book 4", "Book 4 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book2, "Machado de Assis", 19.99, "Usado"));
        dataResource.add(new Book(5, "Book 5", "Book 5 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book1, "Machado de Assis", 11.99, "Usado"));
        //
        MyAdapterRecyclerViewCart recyclerViewAdapter = new MyAdapterRecyclerViewCart(this, dataResource, true);

        rv_items.setLayoutManager(linearLayoutManagerItems);
        rv_items.setAdapter(recyclerViewAdapter);
    }

    public void startComponents() {
        this.tvTitlePage = findViewById(R.id.title_page);
        this.button_back_arrow = findViewById(R.id.back_arrow);
        this.rv_items = findViewById(R.id.items);
    }
}