package com.example.obes;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.obes.dao.BookDAO;
import com.example.obes.model.Book.Book;
import com.example.obes.perfil.PerfilUserCommon;

import java.util.ArrayList;

public class MyAdapterRecyclerView extends RecyclerView.Adapter<MyAdapterRecyclerView.MyHolder> {
    private ArrayList<Book> data;
    private Context context;
    private boolean isEditBook;

    public MyAdapterRecyclerView(Context context, ArrayList<Book> data, boolean isEditBook) {
        this.data = data;
        this.context = context;
        this.isEditBook = isEditBook;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (this.isEditBook) {
            view = LayoutInflater.from(context).inflate(R.layout.book_item_edit, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        }
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Book book = data.get(position);
        holder.ivCover.setImageResource(book.getCoverResourceId());

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

        if (this.isEditBook) {
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog modal = new Dialog(context);

                    modal.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    modal.setCancelable(true);
                    modal.setContentView(R.layout.modal_edit_book_donate);

                    EditText tvTitle = modal.findViewById(R.id.book_title);
                    EditText tvDescription = modal.findViewById(R.id.book_description);
                    EditText tvCategory = modal.findViewById(R.id.book_category);
                    EditText tvAuthor = modal.findViewById(R.id.book_author);
                    EditText tvCondition = modal.findViewById(R.id.book_condition);

                    tvTitle.setText(book.getTitle());
                    tvDescription.setText(book.getDescription());
                    tvCategory.setText(book.getCategory());
                    tvAuthor.setText((book.getAuthor()));
                    tvCondition.setText(book.getCondition());

                    Button buttonSave = modal.findViewById(R.id.button_save);
                    Button buttonCancel = modal.findViewById(R.id.button_cancel);

                    buttonSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BookDAO bookDAO = BookDAO.getInstance();

                            String newTitle = tvTitle.getText().toString();
                            String newDescription = tvDescription.getText().toString();
                            String newCategory = tvCategory.getText().toString();
                            String newAuthor = tvAuthor.getText().toString();
                            String newCondition = tvCondition.getText().toString();

                            Book newBook = new Book(book.getId(), newTitle, newDescription, newCategory, book.getAvailable(), book.getCoverResourceId(), newAuthor, book.getPrice(), newCondition);

                            bookDAO.editBook(newBook);

                            modal.dismiss();
                        }
                    });

                    buttonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            modal.dismiss();
                        }
                    });

                    modal.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        ImageView edit;
        ImageView delete;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}