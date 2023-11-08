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
import com.example.obes.dao.Wishlist.ItemWishlistDAO;
import com.example.obes.dao.Wishlist.WishlistDAO;
import com.example.obes.dao.Wishlist.WishlistToItemDAO;
import com.example.obes.dao.Wishlist.WishlistToUserDAO;
import com.example.obes.formSale.SalePreview;
import com.example.obes.model.Book.Book;
import com.example.obes.model.Cart.Cart;
import com.example.obes.model.Cart.ItemCart;
import com.example.obes.model.User.User;
import com.example.obes.model.Wishlist.ItemWishlist;
import com.example.obes.model.Wishlist.Wishlist;
import com.example.obes.perfil.PerfilUserCommon;

import java.util.ArrayList;

public class BookSalePage extends AppCompatActivity {
    private TextView tvTitlePage;
    private TextView titleTextView;
    private ImageView coverImageView;
    private TextView authorTextView;
    private TextView priceTextView;
    private TextView descriptionTextView;
    private ImageView button_back_arrow;
    private Button button_add_cart;
    private ImageView ivIcFavorite;
    private boolean isFavorite;
    private LoginSessionManager loginSessionManager;
    private User userLogged;
    private Wishlist wishlistUserLogged;

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

        this.tvTitlePage.setText(bookTitle);
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

    private void startComponets() {
        this.tvTitlePage = findViewById(R.id.title_page);
        this.titleTextView = findViewById(R.id.title);
        this.coverImageView = findViewById(R.id.ivCover);
        this.authorTextView = findViewById(R.id.author);
        this.priceTextView = findViewById(R.id.price);
        this.descriptionTextView = findViewById(R.id.description);
        this.button_back_arrow = findViewById(R.id.back_arrow);
        this.button_add_cart = findViewById(R.id.add_cart);
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
}