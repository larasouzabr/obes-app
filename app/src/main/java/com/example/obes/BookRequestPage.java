package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.obes.dao.Request.ItemRequestDAO;
import com.example.obes.dao.Request.RequestToItemDAO;
import com.example.obes.dao.Request.RequestToUserDAO;
import com.example.obes.dao.Review.UserHasReviewDAO;
import com.example.obes.model.Request.ItemRequest;
import com.example.obes.model.Review.Review;
import com.example.obes.model.User.User;

import java.util.ArrayList;

public class BookRequestPage extends AppCompatActivity {
    private TextView tvTitlePage;
    private TextView titleTextView;
    private ImageView coverImageView;
    private TextView authorTextView;
    private TextView priceTextView;
    private TextView descriptionTextView;
    private RatingBar rbRating;
    private ImageView button_back_arrow;
    private TextView tvNameUserRequesting;
    private ImageView ivPhotoUserRequesting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_request_page);

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


        ItemRequest item = ItemRequestDAO.getInstance().getItemByIdBook(bookId);
        int idRequest = RequestToItemDAO.getInstance().getIdRequestByIdItem(item.getId());

        User userRequesting = RequestToUserDAO.getInstance().getUserByIdRequest(idRequest);

        this.ivPhotoUserRequesting.setImageResource(R.drawable.ic_foto_perfil);
        this.tvNameUserRequesting.setText(userRequesting.getName());
        this.rbRating.setRating(this.getRatingUserRequest(userRequesting));

        this.button_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        this.tvNameUserRequesting = findViewById(R.id.name_user_donate);
        this.ivPhotoUserRequesting = findViewById(R.id.photo_user_donate);
        this.rbRating = findViewById(R.id.rating);
    }

    public float getRatingUserRequest(User userRequest) {
        float rating = 0;

        ArrayList<Review> reviewsUser = UserHasReviewDAO.getInstance().getReviewsReceivedByIdUser(userRequest.getId());

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