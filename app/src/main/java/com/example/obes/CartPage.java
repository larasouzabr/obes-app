package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obes.dao.CartDAO;
import com.example.obes.dao.CartToItemDAO;
import com.example.obes.dao.CartToUserDAO;
import com.example.obes.dao.ItemCartDAO;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.dao.Request.ItemRequestDAO;
import com.example.obes.dao.Request.RequestDAO;
import com.example.obes.dao.Request.RequestToItemDAO;
import com.example.obes.dao.Request.RequestToUserDAO;
import com.example.obes.formDonate.DonateFormPage;
import com.example.obes.model.Book.Book;
import com.example.obes.model.Cart.Cart;
import com.example.obes.model.Cart.ItemCart;
import com.example.obes.model.Request.ItemRequest;
import com.example.obes.model.Request.Request;
import com.example.obes.model.User.User;
import com.example.obes.payment.PaymentPage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CartPage extends AppCompatActivity {
    private TextView tvTitlePage;
    private ImageView button_back_arrow;
    private Button button_clean;
    private TextView tvPriceTotal;
    private RecyclerView rv_items;
    private Button button_buy;
    private LoginSessionManager loginSessionManager;
    private User userLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);
        this.startComponents();

        BottomMenuHandler bottomMenuHandler = new BottomMenuHandler(this);
        bottomMenuHandler.setupBottomMenu();

        userLogged = this.getUserLogged();

        this.tvTitlePage.setText("Carrinho de Compras");

        this.button_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager linearLayoutManagerItems = new LinearLayoutManager(CartPage.this, LinearLayoutManager.VERTICAL, false);

        ArrayList<ItemCart> listItemsCart = this.getListItemsCart();
        ArrayList<Book> listBooks = new ArrayList<Book>();

        for (ItemCart item : listItemsCart) {
            listBooks.add(item.getItem());
        }

        MyAdapterRecyclerViewCart recyclerViewAdapter = new MyAdapterRecyclerViewCart(this, listBooks, listItemsCart, true);

        rv_items.setLayoutManager(linearLayoutManagerItems);
        rv_items.setAdapter(recyclerViewAdapter);

        this.button_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartToUserDAO cartToUserDAO = CartToUserDAO.getInstance();
                Cart cartUserLogged = cartToUserDAO.getCartByIdUser(userLogged.getId());

                CartToItemDAO cartToItemDAO = CartToItemDAO.getInstance();
                ArrayList<ItemCart> itemsUserLogged = cartToItemDAO.getItemsByIdCart(cartUserLogged.getId());

                ItemCartDAO itemCartDAO = ItemCartDAO.getInstance();
                for (ItemCart item : itemsUserLogged) {
                    itemCartDAO.deleteItemCart(item);
                    cartToItemDAO.deleteCartItem(cartUserLogged.getId(), item.getId());
                }

                listBooks.clear();

                recyclerViewAdapter.notifyDataSetChanged();

                tvPriceTotal.setText("R$ 0.00");
            }
        });

        this.button_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request newRequest = new Request(countIdRequest(), getCurrentDate(), "Pendente");
                ArrayList<ItemRequest> newItems = new ArrayList<ItemRequest>();
                ArrayList<Book> booksSelected = new ArrayList<Book>();

                int newId = countIdItemRequest();
                for (ItemCart item : listItemsCart) {
                    if (item.getSelected()) {
                        newItems.add(new ItemRequest(newId, 1, item.getItem(), "Pendente"));
                        newId++;
                        booksSelected.add(item.getItem());
                    }
                }

                if (newItems.size() > 0) {
                    Intent intent = new Intent(CartPage.this, PaymentPage.class);

                    intent.putExtra("new_request", newRequest);
                    intent.putParcelableArrayListExtra("new_items", newItems);
                    intent.putParcelableArrayListExtra("books_selected", booksSelected);

                    startActivity(intent);
                } else {
                    Toast.makeText(CartPage.this, "Por favor, selecione algum item", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void startComponents() {
        this.tvTitlePage = findViewById(R.id.title_page);
        this.button_back_arrow = findViewById(R.id.back_arrow);
        this.button_clean = findViewById(R.id.button_clean);
        this.rv_items = findViewById(R.id.items);
        this.loginSessionManager = LoginSessionManager.getInstance();
        this.tvPriceTotal = findViewById(R.id.price_total);
        this.button_buy = findViewById(R.id.button_buy);
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

    private ArrayList<ItemCart> getListItemsCart() {
        CartToUserDAO cartToUserDAO = CartToUserDAO.getInstance();
        CartToItemDAO cartToItemDAO = CartToItemDAO.getInstance();

        Cart cartUserLogged = cartToUserDAO.getCartByIdUser(this.userLogged.getId());

        if (cartUserLogged == null) {
            cartUserLogged = this.addCartToUser();
        }

        return cartToItemDAO.getItemsByIdCart(cartUserLogged.getId());
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

    public int countIdRequest() {
        RequestDAO requestDAO = RequestDAO.getInstance();

        int id = 1;

        int amountRequests = requestDAO.getListRequests().size();

        if (amountRequests > 0) {
            id = requestDAO.getListRequests().get(amountRequests - 1).getId() + 1;
        }

        return id;
    }

    public String getCurrentDate() {
        Date currentDate = new Date();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        return format.format(currentDate);
    }

    public int countIdItemRequest() {
        ItemRequestDAO itemRequestDAO = ItemRequestDAO.getInstance();

        int id = 1;

        int amountItems = itemRequestDAO.getListItemRequests().size();

        if (amountItems > 0) {
            id = itemRequestDAO.getListItemRequests().get(amountItems - 1).getId() + 1;
        }

        return id;
    }
}