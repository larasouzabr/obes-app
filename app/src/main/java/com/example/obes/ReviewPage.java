package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.obes.dao.LoginSessionManager;
import com.example.obes.dao.Review.ReviewDAO;
import com.example.obes.dao.Review.UserHasReviewDAO;
import com.example.obes.dao.UserCommonDAO;
import com.example.obes.dao.UserInstitutionalDAO;
import com.example.obes.model.Review.Review;
import com.example.obes.model.User.User;

public class ReviewPage extends AppCompatActivity {
    private TextView tvTitlePage;
    private EditText etComment;
    private RatingBar rbRating;
    private User userReceiving;
    private User userLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_page);
        this.startComponents();

        this.tvTitlePage.setText("Comentário");

        Intent intentComment = getIntent();
        int idUserReceiving = intentComment.getIntExtra("user_receiver_id", 0);
        userReceiving = this.getUserReceiving(idUserReceiving);
        this.userLogged = this.getUserLogged();

        int idReview = UserHasReviewDAO.getInstance().getIdCommentByIdUsers(this.userLogged.getId(), userReceiving.getId());
        Review review = ReviewDAO.getInstance().getReviewById(idReview);

        if (review != null) {
            this.etComment.setText(review.getComment());
            this.rbRating.setRating((float) review.getRate());
        }
    }

    public void startComponents() {
        this.tvTitlePage = findViewById(R.id.title_page);
        this.etComment = findViewById(R.id.comment);
        this.rbRating = findViewById(R.id.rating);
    }

    public User getUserReceiving(int idUserReceiving) {
        User userReceiving = UserCommonDAO.getInstance().getUserById(idUserReceiving);

        if (userReceiving == null) {
            userReceiving = UserInstitutionalDAO.getInstance().getUserById(idUserReceiving);
        }

        return userReceiving;
    }

    public User getUserLogged() {
        User user = LoginSessionManager.getInstance().getCurrentUserCommon();

        if (user == null) {
            user = LoginSessionManager.getInstance().getCurrentUserInstitutional();
        }

        return user;
    }
}