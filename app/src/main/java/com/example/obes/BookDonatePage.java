package com.example.obes;

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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.obes.dao.BookDAO;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.dao.Request.DonationRequestManager;
import com.example.obes.dao.Request.ItemRequestDAO;
import com.example.obes.dao.Request.OrderDAO;
import com.example.obes.dao.Request.RequestDAO;
import com.example.obes.dao.Review.UserHasReviewDAO;
import com.example.obes.dao.UserRegisteredBookDonateDAO;
import com.example.obes.dao.Wishlist.ItemWishlistDAO;
import com.example.obes.dao.Wishlist.WishlistDAO;
import com.example.obes.dao.Wishlist.WishlistToItemDAO;
import com.example.obes.dao.Wishlist.WishlistToUserDAO;
import com.example.obes.model.Request.ItemRequest;
import com.example.obes.model.Request.Request;
import com.example.obes.model.Review.Review;
import com.example.obes.model.User.User;
import com.example.obes.model.User.UserCommon;
import com.example.obes.model.Wishlist.ItemWishlist;
import com.example.obes.model.Wishlist.Wishlist;
import com.example.obes.perfil.PerfilUserCommon;
import com.example.obes.perfil.PerfilUserInstitutional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BookDonatePage extends AppCompatActivity {
    private TextView tvTitlePage;
    private TextView titleTextView;
    private ImageView coverImageView;
    private TextView authorTextView;
    private TextView descriptionTextView;
    private ImageView button_back_arrow;
    private ImageView ivIcFavorite;
    private boolean isFavorite;
    private TextView tvNameUserDonating;
    private ImageView ivPhotoUserDonating;
    private Button buttonRequestDonation;
    private RatingBar rbRating;
    private LoginSessionManager loginSessionManager;
    private User userLogged;
    private Wishlist wishlistUserLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_donate_page);
        BottomMenuHandler bottomMenuHandler = new BottomMenuHandler(this);
        bottomMenuHandler.setupBottomMenu();

        this.startComponents();

        Intent intent = getIntent();

        int bookId = intent.getIntExtra("book_id", 0);
        String bookCoverResourceId = intent.getStringExtra("book_cover");
        String bookTitle = intent.getStringExtra("book_title");
        String bookAuthor = intent.getStringExtra("book_author");
        String bookDescription = intent.getStringExtra("book_description");

        this.tvTitlePage.setText(bookTitle);
        titleTextView.setText(bookTitle);

        Glide.with(BookDonatePage.this)
                .load(bookCoverResourceId)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(coverImageView);

        authorTextView.setText(bookAuthor);
        descriptionTextView.setText(bookDescription);

        UserCommon userDonating = UserRegisteredBookDonateDAO.getInstance().getUserByIdBook(bookId);
        this.ivPhotoUserDonating.setImageResource(R.drawable.ic_foto_perfil);
        this.tvNameUserDonating.setText(userDonating.getName());
        this.rbRating.setRating(this.getRatingUserDonating(userDonating));

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

        this.buttonRequestDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request newRequest = new Request(countIdRequest(), getCurrentDate(), "Pendente");
                ItemRequest newItem = new ItemRequest(countIdItemRequest(), 1, BookDAO.getInstance().getBookById(bookId), "Pendente");

                userLogged.donationRequest(newRequest, newItem, userLogged.getId(), userDonating.getId());

                showModal();
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
        this.tvNameUserDonating = findViewById(R.id.name_user_donate);
        this.ivPhotoUserDonating = findViewById(R.id.photo_user_donate);
        this.buttonRequestDonation = findViewById(R.id.button_request_donation);
        this.rbRating = findViewById(R.id.rating);

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

    public void showModal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BookDonatePage.this);
        LayoutInflater inflater = (LayoutInflater) BookDonatePage.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.custom_modal, null);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        final TextView tvTitleModal = dialogView.findViewById(R.id.title_modal);
        final TextView tvTextModal = dialogView.findViewById(R.id.text_modal);
        TextView tvButtonModal = dialogView.findViewById(R.id.button_modal);

        tvTitleModal.setText("Doação Solicitada");
        tvTextModal.setText("Notificamos ao vendedor sobre sua solicitação de receber o produto, você pode visualizar o status no seu perfil");
        tvButtonModal.setText("Ver no perfil");

        tvButtonModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                if (loginSessionManager.getCurrentUserCommon() == null) {
                    intent = new Intent(BookDonatePage.this, PerfilUserInstitutional.class);
                } else {
                    intent = new Intent(BookDonatePage.this, PerfilUserCommon.class);
                }

                startActivity(intent);
            }
        });

        dialog.show();
    }

    public float getRatingUserDonating(User userDonate) {
        float rating = 0;

        ArrayList<Review> reviewsUser = UserHasReviewDAO.getInstance().getReviewsReceivedByIdUser(userDonate.getId());

        for (Review review : reviewsUser) {
            rating += review.getRate();
        }

        int amountReviews = reviewsUser.size();

        if (amountReviews > 0) {
            rating /= amountReviews;
        }

        return rating;
    }
}