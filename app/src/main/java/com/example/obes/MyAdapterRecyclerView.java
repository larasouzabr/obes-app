package com.example.obes;

import android.app.AlertDialog;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.obes.dao.BookDAO;
import com.example.obes.dao.BookSaleDAO;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.dao.Request.DonationRequestManager;
import com.example.obes.dao.Request.ItemRequestDAO;
import com.example.obes.dao.Request.OrderDAO;
import com.example.obes.dao.Request.RequestDAO;
import com.example.obes.dao.Request.RequestToItemDAO;
import com.example.obes.dao.Review.UserHasReviewDAO;
import com.example.obes.dao.UserCommonDAO;
import com.example.obes.dao.UserRegisteredBookDonateDAO;
import com.example.obes.dao.UserRegisteredBookSaleDAO;
import com.example.obes.model.Book.Book;
import com.example.obes.model.Request.ItemRequest;
import com.example.obes.model.Request.Request;
import com.example.obes.model.User.User;
import com.example.obes.model.User.UserCommon;
import com.example.obes.payment.BookPaymentPage;
import com.example.obes.perfil.PerfilUserCommon;

import java.util.ArrayList;

public class MyAdapterRecyclerView extends RecyclerView.Adapter<MyAdapterRecyclerView.MyHolder> {
    private ArrayList<Book> data;
    private Context context;
    private String typeView;
    private User userLogged;

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
        } else if (this.typeView.equals("request")){
            view = LayoutInflater.from(context).inflate(R.layout.book_item_request, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.book_item_my_request, parent, false);
        }
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Book book = data.get(position);
        holder.ivCover.setImageResource(book.getCoverResourceId());

        LoginSessionManager loginSessionManager = LoginSessionManager.getInstance();

        userLogged = loginSessionManager.getCurrentUserCommon();
        if (userLogged == null) {
            userLogged = loginSessionManager.getCurrentUserInstitutional();
        }

        holder.ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                if (typeView.equals("request")) {
                    intent = new Intent(context, BookRequestPage.class);
                } else {
                    if (book.getPrice() > 0) {
                        if (typeView.equals("myRequest")) {
                            intent = new Intent(context, BookPaymentPage.class);
                        } else {
                            intent = new Intent(context, BookSalePage.class);
                        }
                    } else {
                        intent = new Intent(context, BookDonatePage.class);
                    }
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

        if (this.typeView.equals("myRequest")) {
            ItemRequest item = ItemRequestDAO.getInstance().getItemByIdBook(book.getId());
            int color;

            if (item.getStatus().equals("Confirmado")) {
                holder.status.setText("Confirmado");
                color = ContextCompat.getColor(context, R.color.teal_origin);
                holder.status.setBackgroundColor(color);

                int idUserReceiver;
                if (book.getPrice() > 0) {
                    idUserReceiver = UserRegisteredBookSaleDAO.getInstance().getIdUserByIdBook(book.getId());
                } else {
                    idUserReceiver = UserRegisteredBookDonateDAO.getInstance().getUserByIdBook(book.getId()).getId();
                }

                User userReceiver = UserCommonDAO.getInstance().getUserById(idUserReceiver);

                int idReview = UserHasReviewDAO.getInstance().getIdCommentByIdUsers(userLogged.getId(), userReceiver.getId());

                if (idReview == 0) {
                    holder.buttonComment.setEnabled(true);
                } else {
                    holder.buttonComment.setEnabled(false);
                    holder.buttonComment.setText("Avaliado");
                }
            } else if (item.getStatus().equals("Cancelado")){
                holder.status.setText("Cancelado");
                color = ContextCompat.getColor(context, R.color.red);
                holder.status.setBackgroundColor(color);
            }

            holder.buttonComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReviewPage.class);

                    int idUserReceiver;
                    if (book.getPrice() == 0) {
                        idUserReceiver = UserRegisteredBookDonateDAO.getInstance().getUserByIdBook(book.getId()).getId();
                    } else {
                        idUserReceiver = UserRegisteredBookSaleDAO.getInstance().getIdUserByIdBook(book.getId());
                    }

                    intent.putExtra("user_receiver_id", idUserReceiver);

                    context.startActivity(intent);
                }
            });
        }

        if (this.typeView.equals("request")) {
            holder.confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView = inflater.inflate(R.layout.modal_delete_book, null);

                    builder.setView(dialogView);
                    final AlertDialog dialog = builder.create();

                    Button buttonCancel = dialogView.findViewById(R.id.button_cancel);
                    Button buttonDelete = dialogView.findViewById(R.id.button_delete);
                    TextView tvDescription = dialogView.findViewById(R.id.description);

                    tvDescription.setText("Tem certeza que deseja confirmar o pedido deste item?");
                    buttonDelete.setText("Confirmar");

                    buttonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    buttonDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ItemRequest item = ItemRequestDAO.getInstance().getItemByIdBook(book.getId());

                            userLogged.confirmDonationRequest(item);
                            userLogged.confirmSaleItemOrder(item);

                            int itemPosition = data.indexOf(book);
                            data.remove(itemPosition);
                            notifyItemRemoved(itemPosition);

                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });
        }

        if (!this.typeView.equals("common")) {
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView = inflater.inflate(R.layout.modal_delete_book, null);

                    builder.setView(dialogView);
                    final AlertDialog dialog = builder.create();

                    Button buttonCancel = dialogView.findViewById(R.id.button_cancel);
                    Button buttonDelete = dialogView.findViewById(R.id.button_delete);
                    TextView tvDescription = dialogView.findViewById(R.id.description);

                    if (!typeView.equals("edit")) {
                        tvDescription.setText("Tem certeza que deseja cancelar o pedido deste item?");
                    }

                    buttonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    buttonDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if (typeView.equals("edit")) {
                                if (loginSessionManager.getCurrentUserCommon() != null) {
                                    loginSessionManager.getCurrentUserCommon().deleteBook(book);
                                }
                            } else if (typeView.equals("myRequest")){
                                ItemRequest item = ItemRequestDAO.getInstance().getItemByIdBook(book.getId());
                                int idRequest = RequestToItemDAO.getInstance().getIdRequestByIdItem(item.getId());
                                Request request = RequestDAO.getInstance().getRequestById(idRequest);
                                int idUserReceiving = OrderDAO.getInstance().getIdUserByIdRequest(idRequest);

                                if (book.getPrice() == 0) {
                                    userLogged.cancelDonationRequest(request, userLogged.getId(), idUserReceiving);
                                } else {
                                    userLogged.cancelSaleItemRequest(item);
                                }

                            } else if (typeView.equals("request")) {
                                book.setAvailable(true);
                                BookDAO.getInstance().editBook(book);

                                ItemRequestDAO itemRequestDAO = ItemRequestDAO.getInstance();
                                ItemRequest item = itemRequestDAO.getItemByIdBook(book.getId());
                                item.setStatus("Cancelado");
                                DonationRequestManager.updateStatusItemRequest(item);

                                int request = RequestToItemDAO.getInstance().getIdRequestByIdItem(item.getId());
                                OrderDAO.getInstance().deleteRequestToUser(request, userLogged.getId());

                                userLogged.cancelSaleItemOrder(item);
                            }

                            int itemPosition = data.indexOf(book);
                            data.remove(itemPosition);
                            notifyItemRemoved(itemPosition);

                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });

            if (typeView.equals("edit")) {
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

            if (typeView.equals("request")) {

            }
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
        TextView status;
        ImageView confirm;
        Button buttonComment;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            status = itemView.findViewById(R.id.status);
            confirm = itemView.findViewById(R.id.confirm);
            buttonComment = itemView.findViewById(R.id.button_comment);
        }
    }
}