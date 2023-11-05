package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.obes.dao.BookDAO;
import com.example.obes.dao.CartDAO;
import com.example.obes.dao.CartToItemDAO;
import com.example.obes.dao.CartToUserDAO;
import com.example.obes.dao.ItemCartDAO;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.formSale.SalePreview;
import com.example.obes.model.Book.Book;
import com.example.obes.model.Cart.Cart;
import com.example.obes.model.Cart.ItemCart;
import com.example.obes.model.User.User;
import com.example.obes.perfil.PerfilUserCommon;

public class BookSalePage extends AppCompatActivity {

    private TextView titleTextView;
    private ImageView coverImageView;
    private TextView authorTextView;
    private TextView priceTextView;
    private TextView descriptionTextView;
    private ImageView button_back_arrow;
    private Button button_add_cart;
    private LoginSessionManager loginSessionManager;
    private User userLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_sale_page);

        Intent intent = getIntent();

        int bookId = intent.getIntExtra("book_id", 0);
        int bookCoverResourceId = intent.getIntExtra("book_cover", 0);
        String bookTitle = intent.getStringExtra("book_title");
        String bookAuthor = intent.getStringExtra("book_author");
        double bookPrice = intent.getDoubleExtra("book_price", 0.0);
        String bookDescription = intent.getStringExtra("book_description");

        this.startComponets();

        titleTextView.setText(bookTitle);
        coverImageView.setImageResource(bookCoverResourceId);
        authorTextView.setText(bookAuthor);
        priceTextView.setText("R$ " + String.format("%.2f", bookPrice));
        descriptionTextView.setText(bookDescription);

        this.button_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.button_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartToUserDAO cartToUserDAO = CartToUserDAO.getInstance();

                Cart cartUserLogged = cartToUserDAO.getCartByIdUser(userLogged.getId());

                if (cartUserLogged == null) {
                    cartUserLogged = addCartToUser();
                }

                ItemCart newItemCart = new ItemCart(countIdItemCart(), 1, BookDAO.getInstance().getBookById(bookId));

                ItemCartDAO.getInstance().addItemCart(newItemCart);

                CartToItemDAO.getInstance().addCartItem(cartUserLogged.getId(), newItemCart.getId());

                showModal();
            }
        });
    }

    private void startComponets() {
        this.titleTextView = findViewById(R.id.title);
        this.coverImageView = findViewById(R.id.ivCover);
        this.authorTextView = findViewById(R.id.author);
        this.priceTextView = findViewById(R.id.price);
        this.descriptionTextView = findViewById(R.id.description);
        this.button_back_arrow = findViewById(R.id.back_arrow);
        this.button_add_cart = findViewById(R.id.add_cart);
        this.loginSessionManager = LoginSessionManager.getInstance();
        this.userLogged = this.getUserLogged();
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

    private Cart addCartToUser() {
        CartToUserDAO cartToUserDAO = CartToUserDAO.getInstance();
        CartDAO cartDAO = CartDAO.getInstance();

        int id = 1;

        int amountCarts = cartDAO.getListCarts().size();

        if (amountCarts > 0) {
            id = cartDAO.getListCarts().get(amountCarts - 1).getId() + 1;
        }

        Cart newCart = new Cart(id);

        cartDAO.addCart(newCart);

        CartToUserDAO.getInstance().addCartToUser(newCart.getId(), this.userLogged.getId());

        return cartToUserDAO.getCartByIdUser(this.userLogged.getId());
    }

    private int countIdItemCart() {
        ItemCartDAO itemCartDAO = ItemCartDAO.getInstance();

        int id = 1;

        int amountItems = itemCartDAO.getListItensCart().size();

        if (amountItems > 0) {
            id = itemCartDAO.getListItensCart().get(amountItems - 1).getId() + 1;
        }

        return id;
    }

    private void showModal() {
        final Dialog modal = new Dialog(BookSalePage.this);

        modal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        modal.setCancelable(true);
        modal.setContentView(R.layout.custom_modal);

        final TextView tvTitleModal = modal.findViewById(R.id.title_modal);
        final TextView tvTextModal = modal.findViewById(R.id.text_modal);
        TextView tvButtonModal = modal.findViewById(R.id.button_modal);

        tvTitleModal.setText("Produto adicionado ao carrinho");
        tvTextModal.setText("Seu produto foi adicionado ao carrinho para finalização da compra");
        tvButtonModal.setText("Ir ao carrinho");

        tvButtonModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookSalePage.this, CartPage.class);
                startActivity(intent);
            }
        });

        modal.show();
    }
}