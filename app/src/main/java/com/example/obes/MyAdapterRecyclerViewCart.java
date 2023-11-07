package com.example.obes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.obes.dao.CartToItemDAO;
import com.example.obes.dao.ItemCartDAO;
import com.example.obes.model.Book.Book;
import com.example.obes.model.Cart.ItemCart;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MyAdapterRecyclerViewCart extends RecyclerView.Adapter<MyAdapterRecyclerViewCart.MyHolder> {
    private ArrayList<Book> data;
    private Context context;
    private boolean isShoppingCart;
    private double priceTotal;

    public MyAdapterRecyclerViewCart(Context context, ArrayList<Book> data, boolean isShoppingCart) {
        this.data = data;
        this.context = context;
        this.isShoppingCart = isShoppingCart;
        this.priceTotal = 0;
    }

    @NonNull
    @Override
    public MyAdapterRecyclerViewCart.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (this.isShoppingCart) {
            view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_wishlist, parent, false);
        }
        return new MyAdapterRecyclerViewCart.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterRecyclerViewCart.MyHolder holder, int position) {
        Book book = data.get(position);
        holder.ivCover.setImageResource(book.getCoverResourceId());
        holder.tvTitleBook.setText(book.getTitle());
        holder.tvAuthorBook.setText(book.getAuthor());
        String priceBook = "R$ " + book.getPrice();
        holder.tvPriceBook.setText(priceBook);

        holder.ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookSalePage.class);

                intent.putExtra("book_id", book.getId());
                intent.putExtra("book_cover", book.getCoverResourceId());
                intent.putExtra("book_title", book.getTitle());
                intent.putExtra("book_author", book.getAuthor());
                intent.putExtra("book_price", book.getPrice());
                intent.putExtra("book_description", book.getDescription());

                context.startActivity(intent);
            }
        });

        if (this.isShoppingCart) {
            holder.ItemCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Activity activity = (Activity) context;
                    TextView tvPriceTotal = activity.findViewById(R.id.price_total);
                    DecimalFormat df = new DecimalFormat("#.00");
                    String newPriceTotal;

                    if (isChecked) {
                        priceTotal = countPriceTotal(book.getPrice());
                    } else {
                        priceTotal = countPriceTotal(book.getPrice() * -1);
                    }

                    newPriceTotal = "R$ " + (priceTotal > 0 ? df.format(priceTotal) : "0.00");
                    tvPriceTotal.setText(newPriceTotal);
                }
            });
        }

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemCartDAO itemCartDAO = ItemCartDAO.getInstance();

                int itemPosition = data.indexOf(book);

                if (itemPosition != -1) {
                    data.remove(itemPosition);

                    notifyItemRemoved(itemPosition);

                    if (isShoppingCart) {
                        priceTotal = countPriceTotal(book.getPrice() * -1);

                        updatePriceTotalInUI();

                        ItemCart item = itemCartDAO.getItemByIdBook(book.getId());
                        if (item != null) {
                            itemCartDAO.deleteItemCart(item);
                            int idCart = CartToItemDAO.getInstance().getIdCartByIdItem(item.getId());
                            CartToItemDAO.getInstance().deleteCartItem(idCart, item.getId());
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void updatePriceTotalInUI() {
        Activity activity = (Activity) context;
        TextView tvPriceTotal = activity.findViewById(R.id.price_total);
        DecimalFormat df = new DecimalFormat("#.00");
        String newPriceTotal = "R$ " + (priceTotal > 0 ? df.format(priceTotal) : "0.00");
        tvPriceTotal.setText(newPriceTotal);
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        CheckBox ItemCart;
        TextView tvTitleBook;
        TextView tvAuthorBook;
        TextView tvPriceBook;
        ImageView ivDelete;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.cover_book);
            ItemCart = itemView.findViewById(R.id.item_cart);
            tvTitleBook = itemView.findViewById(R.id.title_book);
            tvAuthorBook = itemView.findViewById(R.id.author_book);
            tvPriceBook = itemView.findViewById(R.id.price_book);
            ivDelete = itemView.findViewById(R.id.delete);
        }
    }

    private double countPriceTotal(double newPrice) {
        this.priceTotal += newPrice;

        if (this.priceTotal <= 0) {
            this.priceTotal = 0.00;
        }

        return this.priceTotal;
    }
}