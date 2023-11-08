package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.obes.dao.BookDAO;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.dao.Wishlist.ItemWishlistDAO;
import com.example.obes.dao.Wishlist.WishlistDAO;
import com.example.obes.dao.Wishlist.WishlistToItemDAO;
import com.example.obes.dao.Wishlist.WishlistToUserDAO;
import com.example.obes.model.User.User;
import com.example.obes.model.Wishlist.ItemWishlist;
import com.example.obes.model.Wishlist.Wishlist;

import java.util.ArrayList;

public class BookDonatePage extends AppCompatActivity {
    private TextView tvTitlePage;
    private TextView titleTextView;
    private ImageView coverImageView;
    private TextView authorTextView;
    private TextView descriptionTextView;
    private ImageView button_back_arrow;
    private ImageView ivIcFavorite;
    private boolean isFavorite;
    private LoginSessionManager loginSessionManager;
    private User userLogged;
    private Wishlist wishlistUserLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_donate_page);
        this.startComponents();

        Intent intent = getIntent();

        int bookId = intent.getIntExtra("book_id", 0);
        int bookCoverResourceId = intent.getIntExtra("book_cover", 0);
        String bookTitle = intent.getStringExtra("book_title");
        String bookAuthor = intent.getStringExtra("book_author");
        String bookDescription = intent.getStringExtra("book_description");

        this.tvTitlePage.setText(bookTitle);
        titleTextView.setText(bookTitle);
        coverImageView.setImageResource(bookCoverResourceId);
        authorTextView.setText(bookAuthor);
        descriptionTextView.setText(bookDescription);

        this.button_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (wishlistUserLogged == null) {
            this.ivIcFavorite.setImageResource(R.drawable.ic_favorite);
        } else if(this.bookIsFavorite(bookId, this.userLogged.getId())) {
            this.ivIcFavorite.setImageResource(R.drawable.ic_favorite_selected);
            this.isFavorite = true;
        } else {
            this.ivIcFavorite.setImageResource(R.drawable.ic_favorite);
        }

        this.ivIcFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wishlistUserLogged == null) {
                    wishlistUserLogged = addWishToUser();
                }

                if (!isFavorite) {
                    ItemWishlist newItemWishlist = new ItemWishlist(countIdItemWish(), BookDAO.getInstance().getBookById(bookId));

                    wishlistUserLogged.addItem(newItemWishlist);

                    ivIcFavorite.setImageResource(R.drawable.ic_favorite_selected);

                    isFavorite = true;
                } else {
                    ArrayList<ItemWishlist> items = WishlistToItemDAO.getInstance().getItemsByIdWish(wishlistUserLogged.getId());

                    for (ItemWishlist item : items) {
                        if (item.getItem().getId() == bookId) {
                            wishlistUserLogged.deleteItem(item);
                            break;
                        }
                    }

                    ivIcFavorite.setImageResource(R.drawable.ic_favorite);

                    isFavorite = false;
                }
            }
        });
    }
    private void startComponents() {
        this.tvTitlePage = findViewById(R.id.title_page);
        this.titleTextView = findViewById(R.id.title);
        this.coverImageView = findViewById(R.id.ivCover);
        this.authorTextView = findViewById(R.id.author);
        this.descriptionTextView = findViewById(R.id.description);
        this.button_back_arrow = findViewById(R.id.back_arrow);
        this.ivIcFavorite = findViewById(R.id.ic_favorite);
        this.loginSessionManager = LoginSessionManager.getInstance();
        this.userLogged = this.getUserLogged();
        this.isFavorite = false;

        WishlistToUserDAO wishlistToUserDAO = WishlistToUserDAO.getInstance();
        wishlistUserLogged = wishlistToUserDAO.getWishByIdUser(userLogged.getId());
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

    private Wishlist addWishToUser() {
        WishlistToUserDAO wishlistToUserDAO = WishlistToUserDAO.getInstance();
        WishlistDAO wishlistDAO = WishlistDAO.getInstance();

        int id = 1;

        int amountWish = wishlistDAO.getWishlists().size();

        if (amountWish > 0) {
            id = wishlistDAO.getWishlists().get(amountWish - 1).getId() + 1;
        }

        Wishlist newWishlist = new Wishlist(id);

        wishlistDAO.addWishlist(newWishlist);

        wishlistToUserDAO.addWishToUser(newWishlist.getId(), this.userLogged.getId());

        return wishlistToUserDAO.getWishByIdUser(this.userLogged.getId());
    }

    private int countIdItemWish() {
        ItemWishlistDAO itemWishlistDAO = ItemWishlistDAO.getInstance();

        int id = 1;

        int amountItems = itemWishlistDAO.getListItems().size();

        if (amountItems > 0) {
            id = itemWishlistDAO.getListItems().get(amountItems - 1).getId() + 1;
        }

        return id;
    }

    private boolean bookIsFavorite(int idBook, int idUser) {
        ItemWishlist item = null;

        for (ItemWishlist i : ItemWishlistDAO.getInstance().getListItems()) {
            if (i.getItem().getId() == idBook) {
                item = i;
                break;
            }
        }

        if (item == null) {
            return false;
        }

        int idWishItem = WishlistToItemDAO.getInstance().getIdWishByIdItem(item.getId());

        Wishlist wishUser = WishlistToUserDAO.getInstance().getWishByIdUser(idUser);

        if (idWishItem == wishUser.getId()) {
            return true;
        } else {
            return false;
        }
    }
}