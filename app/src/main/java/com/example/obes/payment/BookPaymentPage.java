package com.example.obes.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obes.BottomMenuHandler;
import com.example.obes.R;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.dao.Payment.PaymentDAO;
import com.example.obes.dao.Payment.PaymentToItemDAO;
import com.example.obes.dao.Payment.PaymentToUserDAO;
import com.example.obes.dao.Request.ItemRequestDAO;
import com.example.obes.dao.Review.UserHasReviewDAO;
import com.example.obes.dao.UserCommonDAO;
import com.example.obes.dao.UserRegisteredBookSaleDAO;
import com.example.obes.dao.Wishlist.WishlistToUserDAO;
import com.example.obes.model.Payment.BoletoPayment;
import com.example.obes.model.Payment.CreditCardPayment;
import com.example.obes.model.Payment.IPayment;
import com.example.obes.model.Payment.PixPayment;
import com.example.obes.model.Request.ItemRequest;
import com.example.obes.model.Review.Review;
import com.example.obes.model.User.User;
import com.example.obes.model.User.UserCommon;
import com.example.obes.perfil.PerfilUserCommon;
import com.example.obes.perfil.PerfilUserInstitutional;

import java.util.ArrayList;

public class BookPaymentPage extends AppCompatActivity {
    private TextView tvTitlePage;
    private TextView titleTextView;
    private ImageView coverImageView;
    private TextView authorTextView;
    private TextView priceTextView;
    private TextView descriptionTextView;
    private ImageView button_back_arrow;
    private TextView tvNameUserSelling;
    private ImageView ivPhotoUserSelling;
    private TextView tvMethodName;
    private RatingBar rbRating;
    private IPayment paymentMethod;
    private Button buttonViewMethod;
    private LoginSessionManager loginSessionManager;
    private User userLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_payment_page);
        this.startComponents();

        BottomMenuHandler bottomMenuHandler = new BottomMenuHandler(this);
        bottomMenuHandler.setupBottomMenu();

        Intent intent = getIntent();

        int bookId = intent.getIntExtra("book_id", 0);
        int bookCoverResourceId = intent.getIntExtra("book_cover", 0);
        String bookTitle = intent.getStringExtra("book_title");
        String bookAuthor = intent.getStringExtra("book_author");
        double bookPrice = intent.getDoubleExtra("book_price", 0.0);
        String bookDescription = intent.getStringExtra("book_description");

        this.startComponents();

        this.tvTitlePage.setText(bookTitle);
        titleTextView.setText(bookTitle);
        coverImageView.setImageResource(bookCoverResourceId);
        authorTextView.setText(bookAuthor);
        priceTextView.setText("R$ " + String.format("%.2f", bookPrice));
        descriptionTextView.setText(bookDescription);

        int idUserSelling = UserRegisteredBookSaleDAO.getInstance().getIdUserByIdBook(bookId);
        UserCommon userSelling = UserCommonDAO.getInstance().getUserById(idUserSelling);
        this.ivPhotoUserSelling.setImageResource(R.drawable.ic_foto_perfil);
        this.tvNameUserSelling.setText(userSelling.getName());
        this.rbRating.setRating(this.getRatingUserSale(userSelling));

        this.paymentMethod = this.getPaymentMethod(bookId);
        if (this.paymentMethod instanceof CreditCardPayment) {
            this.tvMethodName.setText("Cartão");
        } else if (this.paymentMethod instanceof BoletoPayment) {
            this.tvMethodName.setText("Boleto");
        } else {
            this.tvMethodName.setText("Pix");
        }

        this.button_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.buttonViewMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentMethod instanceof CreditCardPayment) {
                    CreditCardPayment card = (CreditCardPayment) paymentMethod;
                    showModalCard(card);
                } else if (paymentMethod instanceof BoletoPayment) {
                    // showModalBoleto();
                } else {
                    PixPayment pix = (PixPayment) paymentMethod;
                    showModalPix(pix);
                }
            }
        });
    }

    private void startComponents() {
        this.tvTitlePage = findViewById(R.id.title_page);
        this.titleTextView = findViewById(R.id.title);
        this.coverImageView = findViewById(R.id.ivCover);
        this.authorTextView = findViewById(R.id.author);
        this.priceTextView = findViewById(R.id.price);
        this.descriptionTextView = findViewById(R.id.description);
        this.button_back_arrow = findViewById(R.id.back_arrow);
        this.loginSessionManager = LoginSessionManager.getInstance();
        this.userLogged = this.getUserLogged();
        this.tvNameUserSelling = findViewById(R.id.name_user_sale);
        this.ivPhotoUserSelling = findViewById(R.id.photo_user_sale);
        this.tvMethodName = findViewById(R.id.method_name);
        this.buttonViewMethod = findViewById(R.id.view_method);
        this.rbRating = findViewById(R.id.rating);
    }

    private User getUserLogged() {
        User userLogged;

        if (loginSessionManager.getCurrentUserCommon() == null) {
            userLogged = loginSessionManager.getCurrentUserInstitutional();
        } else {
            userLogged = loginSessionManager.getCurrentUserCommon();
        }

        return userLogged;
    }

    private float getRatingUserSale(User userSale) {
        float rating = 0;

        ArrayList<Review> reviewsUser = UserHasReviewDAO.getInstance().getReviewsReceivedByIdUser(userSale.getId());

        for (Review review : reviewsUser) {
            rating += review.getRate();
        }

        int amountReviews = reviewsUser.size();

        if (amountReviews > 0) {
            rating /= amountReviews;
        }

        return rating;
    }

    public IPayment getPaymentMethod(int idBook) {
        ItemRequest item = ItemRequestDAO.getInstance().getItemByIdBook(idBook);

        int idPayment = PaymentToItemDAO.getInstance().getIdPaymentByIdItem(item.getId());
        IPayment payment = PaymentDAO.getInstance().getPaymentById(idPayment);

        return payment;
    }

    public void showModalPix(PixPayment pix) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BookPaymentPage.this);
        LayoutInflater inflater = (LayoutInflater) BookPaymentPage.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.modal_pix, null);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        TextView tvPixKey = dialogView.findViewById(R.id.pix_key);
        Button buttonCancel = dialogView.findViewById(R.id.button_cancel);

        tvPixKey.setText(pix.getPixKey());

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showModalCard(CreditCardPayment card) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BookPaymentPage.this);
        LayoutInflater inflater = (LayoutInflater) BookPaymentPage.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.modal_card, null);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        EditText etHolderName = dialogView.findViewById(R.id.holder_name);
        EditText etCardNumber = dialogView.findViewById(R.id.card_number);
        EditText etExpirationDate = dialogView.findViewById(R.id.expiration_date);
        EditText etSecurityCode = dialogView.findViewById(R.id.security_code);
        Button buttonCancel = dialogView.findViewById(R.id.button_cancel);
        Button buttonEdit = dialogView.findViewById(R.id.button_edit);

        etHolderName.setText(card.getCardHolderName());
        etCardNumber.setText(card.getCardNumber());
        etExpirationDate.setText(card.getExpirationDate());
        etSecurityCode.setText(String.valueOf(card.getSecurityCode()));

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCardNumber.getText().toString().isEmpty() || etHolderName.getText().toString().isEmpty() || etExpirationDate.getText().toString().isEmpty() || etSecurityCode.getText().toString().isEmpty()) {
                    Toast.makeText(BookPaymentPage.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    String cardNumber = etCardNumber.getText().toString();
                    String holderName = etHolderName.getText().toString();
                    String expirationDate = etExpirationDate.getText().toString();
                    int securityCode = 000;
                    if (!etSecurityCode.getText().toString().isEmpty()) {
                        securityCode = Integer.parseInt(etSecurityCode.getText().toString());
                    }
                    boolean isPaymentCredit = false;

                    IPayment editPayment = getPaymentUser();
                    CreditCardPayment editedPayment = (CreditCardPayment) editPayment;
                    editedPayment.setCardNumber(cardNumber);
                    editedPayment.setCardHolderName(holderName);
                    editedPayment.setExpirationDate(expirationDate);
                    editedPayment.setSecurityCode(securityCode);
                    editedPayment.setIsPaymentCredit(isPaymentCredit);

                    PaymentDAO.getInstance().editPayment(editedPayment);

                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private IPayment getPaymentUser() {
        ArrayList<IPayment> paymentsUser = PaymentToUserDAO.getInstance().getPaymentsByIdUser(userLogged.getId());
        IPayment paymentUser = null;

        for (IPayment p : paymentsUser) {
            if (p instanceof CreditCardPayment) {
                paymentUser = p;
            }
        }

        return paymentUser;
    }
}