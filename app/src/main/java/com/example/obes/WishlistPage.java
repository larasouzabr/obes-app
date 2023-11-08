package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.obes.dao.CartDAO;
import com.example.obes.dao.CartToItemDAO;
import com.example.obes.dao.CartToUserDAO;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.dao.Wishlist.WishlistDAO;
import com.example.obes.dao.Wishlist.WishlistToItemDAO;
import com.example.obes.dao.Wishlist.WishlistToUserDAO;
import com.example.obes.model.Book.Book;
import com.example.obes.model.Cart.Cart;
import com.example.obes.model.Cart.ItemCart;
import com.example.obes.model.User.User;
import com.example.obes.model.Wishlist.ItemWishlist;
import com.example.obes.model.Wishlist.Wishlist;

import java.util.ArrayList;

public class WishlistPage extends AppCompatActivity {
    private RecyclerView rv_items;

    private TextView tvTitlePage;
    private ImageView button_back_arrow;
    private LoginSessionManager loginSessionManager;
    private User userLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_page);
        this.startComponents();

        this.tvTitlePage.setText("Lista de Desejos");

        BottomMenuHandler bottomMenuHandler = new BottomMenuHandler(this);
        bottomMenuHandler.setupBottomMenu();

        this.button_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager linearLayoutManagerItems = new LinearLayoutManager(WishlistPage.this, LinearLayoutManager.VERTICAL, false);

        WishlistToUserDAO wishlistToUserDAO = WishlistToUserDAO.getInstance();
        Wishlist wishUserLogged = wishlistToUserDAO.getWishByIdUser(this.userLogged.getId());

        if (wishUserLogged == null) {
            wishUserLogged = this.addWishToUser();
        }

        ArrayList<ItemWishlist> listItemsWish = wishUserLogged.getListItems();
        ArrayList<Book> listBooks = new ArrayList<Book>();

        for (ItemWishlist item : listItemsWish) {
            listBooks.add(item.getItem());
        }

        MyAdapterRecyclerViewCart recyclerViewAdapter = new MyAdapterRecyclerViewCart(this, listBooks, false);

        rv_items.setLayoutManager(linearLayoutManagerItems);
        rv_items.setAdapter(recyclerViewAdapter);
    }
    private void startComponents() {
        this.rv_items = findViewById(R.id.items);
        this.tvTitlePage = findViewById(R.id.title_page);
        this.button_back_arrow = findViewById(R.id.back_arrow);
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
    private ArrayList<ItemWishlist> getListItemsWish() {
        WishlistToUserDAO wishlistToUserDAO = WishlistToUserDAO.getInstance();
        WishlistToItemDAO wishlistToItemDAO = WishlistToItemDAO.getInstance();

        Wishlist wishUserLogged = wishlistToUserDAO.getWishByIdUser(this.userLogged.getId());

        return wishlistToItemDAO.getItemsByIdWish(wishUserLogged.getId());
    }

    private Wishlist addWishToUser() {
        WishlistToUserDAO wishlistToUserDAO = WishlistToUserDAO.getInstance();
        WishlistDAO wishlistDAO = WishlistDAO.getInstance();

        int id = 1;

        int amountWishs = wishlistDAO.getWishlists().size();

        if (amountWishs > 0) {
            id = wishlistDAO.getWishlists().get(amountWishs - 1).getId() + 1;
        }

        Wishlist newWish = new Wishlist(id);

        wishlistDAO.addWishlist(newWish);

        wishlistToUserDAO.addWishToUser(newWish.getId(), this.userLogged.getId());

        return wishlistToUserDAO.getWishByIdUser(this.userLogged.getId());
    }
}