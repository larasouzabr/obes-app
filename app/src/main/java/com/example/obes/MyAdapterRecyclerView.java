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
import com.example.obes.dao.BookSaleDAO;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.model.Book.Book;
import com.example.obes.model.User.UserCommon;
import com.example.obes.perfil.PerfilUserCommon;

import java.util.ArrayList;

public class MyAdapterRecyclerView extends RecyclerView.Adapter<MyAdapterRecyclerView.MyHolder> {
    private ArrayList<Book> data;
    private Context context;
    private String typeView;

    public MyAdapterRecyclerView(Context context, ArrayList<Book> data, String typeView) {
        this.data = data;
        this.context = context;
        this.typeView = typeView;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (this.typeView.equals("edit")) {
            view = LayoutInflater.from(context).inflate(R.layout.book_item_edit, parent, false);
        } else if (this.typeView.equals("common")){
            view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.book_item_request, parent, false);
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
                Intent intent;

                if (book.getPrice() > 0) {
                    intent = new Intent(context, BookSalePage.class);
                } else {
                    intent = new Intent(context, BookDonatePage.class);
                }

                intent.putExtra("book_id", book.getId());
                intent.putExtra("book_cover", book.getCoverResourceId());
                intent.putExtra("book_title", book.getTitle());
                intent.putExtra("book_author", book.getAuthor());
                intent.putExtra("book_price", book.getPrice());
                intent.putExtra("book_description", book.getDescription());

                context.startActivity(intent);
            }
        });

        if (this.typeView.equals("edit")) {
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog modal = new Dialog(context);

                    modal.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    modal.setCancelable(true);
                    modal.setContentView(R.layout.modal_delete_book);

                    Button buttonCancel = modal.findViewById(R.id.button_cancel);
                    Button buttonDelete = modal.findViewById(R.id.button_delete);

                    buttonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            modal.dismiss();
                        }
                    });

                    buttonDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoginSessionManager loginSessionManager = LoginSessionManager.getInstance();

                            UserCommon userLogged = loginSessionManager.getCurrentUserCommon();

                            userLogged.deleteBook(book);

                            Intent intent = new Intent(context, PerfilUserCommon.class);
                            context.startActivity(intent);
                        }
                    });

                    modal.show();
                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog modal = new Dialog(context);

                    modal.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    modal.setCancelable(true);
                    if (book.getPrice() == 0) {
                        modal.setContentView(R.layout.modal_edit_book_donate);
                    } else {
                        modal.setContentView(R.layout.modal_edit_book_sale);

                        EditText tvPrice = modal.findViewById(R.id.book_price);
                        tvPrice.setText(Double.toString(book.getPrice()));
                    }

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
                            String newTitle = tvTitle.getText().toString();
                            String newDescription = tvDescription.getText().toString();
                            String newCategory = tvCategory.getText().toString();
                            String newAuthor = tvAuthor.getText().toString();
                            String newCondition = tvCondition.getText().toString();

                            Book newBook;

                            if (book.getPrice() == 0) {
                                newBook = new Book(book.getId(), newTitle, newDescription, newCategory, book.getAvailable(), book.getCoverResourceId(), newAuthor, book.getPrice(), newCondition);

                                BookDAO bookDAO = BookDAO.getInstance();
                                bookDAO.editBook(newBook);
                            } else {
                                EditText tvPrice = modal.findViewById(R.id.book_price);
                                double newPrice = Double.parseDouble(String.valueOf(tvPrice.getText()));

                                newBook = new Book(book.getId(), newTitle, newDescription, newCategory, book.getAvailable(), book.getCoverResourceId(), newAuthor, newPrice, newCondition);

                                BookSaleDAO bookSaleDAO = BookSaleDAO.getInstance();
                                bookSaleDAO.editBook(newBook);
                            }

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