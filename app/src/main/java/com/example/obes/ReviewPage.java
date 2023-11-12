package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obes.dao.LoginSessionManager;
import com.example.obes.dao.Review.ReviewDAO;
import com.example.obes.dao.Review.UserHasReviewDAO;
import com.example.obes.dao.UserCommonDAO;
import com.example.obes.dao.UserInstitutionalDAO;
import com.example.obes.model.Review.Review;
import com.example.obes.model.User.User;
import com.example.obes.perfil.PerfilUserCommon;
import com.example.obes.perfil.PerfilUserInstitutional;

import java.util.Date;

public class ReviewPage extends AppCompatActivity {
    private TextView tvTitlePage;
    private EditText etComment;
    private RatingBar rbRating;
    private Button button_cancel;
    private Button button_save;
    private TextView tvUserReceiving;
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

        this.tvUserReceiving.setText(userReceiving.getName());

        this.button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etComment.getText().toString().isEmpty()) {
                    Toast.makeText(ReviewPage.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    final Dialog modal = new Dialog(ReviewPage.this);

                    modal.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    modal.setCancelable(true);
                    modal.setContentView(R.layout.modal_delete_book);

                    Button buttonCancel = modal.findViewById(R.id.button_cancel);
                    Button buttonSave = modal.findViewById(R.id.button_delete);
                    TextView tvDescription = modal.findViewById(R.id.description);

                    tvDescription.setText("Tem certeza que deseja salvar o comentário?");
                    buttonSave.setText("Salvar");

                    buttonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            modal.dismiss();
                        }
                    });

                    buttonSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int newId = countIdReview();
                            double newRate = rbRating.getRating();
                            String newComment = etComment.getText().toString();
                            Date newDate = new Date();

                            if (review == null) {
                                Review newReview = new Review(newId, newRate, newComment, newDate);

                                ReviewDAO.getInstance().addReview(newReview);
                                UserHasReviewDAO.getInstance().addUserReview(userLogged.getId(), userReceiving.getId(), newReview.getId());
                            } else {
                                review.setRate(newRate);
                                review.setComment(newComment);
                                review.setDateUpdated(newDate);

                                ReviewDAO.getInstance().editReview(review);
                            }

                            Intent intent;
                            if (userLogged == LoginSessionManager.getInstance().getCurrentUserCommon()) {
                                intent = new Intent(ReviewPage.this, PerfilUserCommon.class);
                            } else {
                                intent = new Intent(ReviewPage.this, PerfilUserInstitutional.class);
                            }
                            startActivity(intent);
                        }
                    });

                    modal.show();
                }
            }
        });
    }

    public void startComponents() {
        this.tvTitlePage = findViewById(R.id.title_page);
        this.etComment = findViewById(R.id.comment);
        this.rbRating = findViewById(R.id.rating);
        this.button_cancel = findViewById(R.id.button_cancel);
        this.button_save = findViewById(R.id.button_save);
        this.tvUserReceiving = findViewById(R.id.user_receiver);
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

    public int countIdReview() {
        int id = 1;

        int amountComments = ReviewDAO.getInstance().getListReviews().size();

        if (amountComments > 0) {
            id = ReviewDAO.getInstance().getListReviews().get(amountComments - 1).getId() + 1;
        }

        return id;
    }
}