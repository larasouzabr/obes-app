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

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    RecyclerView rv_sale;
    RecyclerView rv_donate;
    ArrayList<Book> dataResource;
    LinearLayoutManager linearLayoutManagerSale;
    LinearLayoutManager linearLayoutManagerDonate;
    MyRvAdapter myRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        this.rv_sale = findViewById(R.id.books_sale);
        this.rv_donate = findViewById(R.id.books_donate);

        dataResource = new ArrayList<>();
        dataResource.add(new Book("Book 1", R.drawable.item, "Machado de Assis", 10, "Seu protagonista é Simão Bacamarte."));
        dataResource.add(new Book("Book 2", R.drawable.item_other, "Isaac Asimov", 0, "Seu protagonista é Bacamarte."));
        dataResource.add(new Book("Book 3", R.drawable.item, "Machado de Assis", 10, "Seu protagonista é Simão Bacamarte."));
        dataResource.add(new Book("Book 3", R.drawable.item, "Machado de Assis", 10, "Seu protagonista é Simão Bacamarte."));

        linearLayoutManagerSale = new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManagerDonate = new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false);
        myRvAdapter = new MyRvAdapter(dataResource);

        rv_sale.setLayoutManager(linearLayoutManagerSale);
        rv_sale.setAdapter(myRvAdapter);

        rv_donate.setLayoutManager(linearLayoutManagerDonate);
        rv_donate.setAdapter(myRvAdapter);
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