package com.example.obes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.obes.model.Book;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    RecyclerView rv_sale;
    RecyclerView rv_donate;
    ArrayList<Book> dataResource;
    LinearLayoutManager linearLayoutManagerSale;
    LinearLayoutManager linearLayoutManagerDonate;
    MyRvAdapter saleAdapter;
    MyRvAdapter donateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        this.rv_sale = findViewById(R.id.books_sale);
        this.rv_donate = findViewById(R.id.books_donate);

        dataResource = new ArrayList<>();
        dataResource.add(new Book(1, "Book 1", "Book 1 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book1, "Machado de Assis", 0));
        dataResource.add(new Book(2, "Book 2", "Book 2 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book2, "Machado de Assis", 0));
        dataResource.add(new Book(3, "Book 3", "Book 3 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book2, "Machado de Assis", 0));
        dataResource.add(new Book(4, "Book 4", "Book 4 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book2, "Machado de Assis", 19.99));
        dataResource.add(new Book(5, "Book 5", "Book 5 Seu protagonista é Simão Bacamarte.", "Ficção", true, R.drawable.cover_book1, "Machado de Assis", 11.99));

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

        saleAdapter = new MyRvAdapter(booksForSale);
        donateAdapter = new MyRvAdapter(booksForDonate);

        rv_sale.setLayoutManager(linearLayoutManagerSale);
        rv_sale.setAdapter(saleAdapter);

        rv_donate.setLayoutManager(linearLayoutManagerDonate);
        rv_donate.setAdapter(donateAdapter);
    }

    class MyRvAdapter extends RecyclerView.Adapter<MyRvAdapter.MyHolder> {
        ArrayList<Book> data;
        public MyRvAdapter(ArrayList<Book> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(HomePage.this).inflate(R.layout.book_item, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            Book book = data.get(position);
            holder.ivCover.setImageResource(book.getCoverResourceId());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {
            ImageView ivCover;
            public MyHolder(@NonNull View itemView) {
                super(itemView);
                ivCover = itemView.findViewById(R.id.ivCover);
            }
        }
    }
}