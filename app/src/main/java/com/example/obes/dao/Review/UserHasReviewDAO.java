package com.example.obes.dao.Review;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.model.Cart.Cart;
import com.example.obes.model.Review.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserHasReviewDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("user_has_review");
    private ArrayList<UserHasReview> listUserHasReview;
    private static UserHasReviewDAO instance;

    private UserHasReviewDAO() {
        this.listUserHasReview = new ArrayList<UserHasReview>();
    }

    public static UserHasReviewDAO getInstance() {
        if (instance == null) {
            instance = new UserHasReviewDAO();
        }
        return instance;
    }

    public ArrayList<UserHasReview> getListUserHasReview() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<UserHasReview> userHasReviews = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int userSenderId = dataSnapshot.child("userSenderId").getValue(int.class);
                    int userReceiverId = dataSnapshot.child("userReceiverId").getValue(int.class);
                    int reviewId = dataSnapshot.child("reviewId").getValue(int.class);

                    UserHasReview userHasReview = new UserHasReview(userSenderId, userReceiverId, reviewId);

                    userHasReviews.add(userHasReview);
                }

                listUserHasReview = userHasReviews;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar usuários: " + error.getMessage());
            }
        });

        return this.listUserHasReview;
    }

    public boolean addUserReview(int userSenderId, int userReceiverId, int reviewId) {
        UserHasReview newUserHasReview = new UserHasReview(userSenderId, userReceiverId, reviewId);
        this.listUserHasReview.add(newUserHasReview);

        DatabaseReference childReference = this.reference.child(userSenderId + "_" + userReceiverId + "_" + reviewId);

        Map<String, Object> data = new HashMap<>();
        data.put("userSenderId", userSenderId);
        data.put("userReceiverId", userReceiverId);
        data.put("reviewId", reviewId);

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Relação de usuário e avaliação cadastrada com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao cadastrar o relação de usuário e avaliação: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteUserReview(int reviewId) {
        UserHasReview userHasReviewDelete = null;

        for (UserHasReview userHasReview : this.listUserHasReview) {
            if (userHasReview.getReviewId() == reviewId) {
                userHasReviewDelete = userHasReview;
                this.listUserHasReview.remove(userHasReview);
                return true;
            }
        }

        String reviewDelete = userHasReviewDelete.getUserSenderId() + "_" + userHasReviewDelete.getUserReceiverId() + "_" + reviewId;
        this.reference.child(reviewDelete).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Avaliação removida com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao remover a avaliação: " + task.getException().getMessage());
                }
            }
        });

        return false;
    }

    public int getIdCommentByIdUsers(int idUserSender, int idUserReceiver) {
        for (UserHasReview userHasReview : this.listUserHasReview) {
            if (userHasReview.getUserSenderId() == idUserSender) {
                if (userHasReview.getUserReceiverId() == idUserReceiver) {
                    return userHasReview.getReviewId();
                }
            }
        }

        return 0;
    }

    public int getIdUserSenderByIdReview(int idReview) {
        for (UserHasReview userHasReview : this.listUserHasReview) {
            if (userHasReview.getReviewId() == idReview) {
                return userHasReview.getUserSenderId();
            }
        }

        return 0;
    }

    public int getIdUserReceiverByIdReview(int idReview) {
        for (UserHasReview userHasReview : this.listUserHasReview) {
            if (userHasReview.getReviewId() == idReview) {
                return userHasReview.getUserReceiverId();
            }
        }

        return 0;
    }

    public ArrayList<Review> getReviewsSenderByIdUser(int idUser) {
        ArrayList<Review> reviewsSenderUser = new ArrayList<Review>();

        for (UserHasReview userHasReview : this.listUserHasReview) {
            if (userHasReview.getUserSenderId() == idUser) {
                reviewsSenderUser.add(ReviewDAO.getInstance().getReviewById(userHasReview.getReviewId()));
            }
        }

        return reviewsSenderUser;
    }

    public ArrayList<Review> getReviewsReceivedByIdUser(int idUser) {
        ArrayList<Review> reviewsReceivedUser = new ArrayList<Review>();

        for (UserHasReview userHasReview : this.listUserHasReview) {
            if (userHasReview.getUserReceiverId() == idUser) {
                reviewsReceivedUser.add(ReviewDAO.getInstance().getReviewById(userHasReview.getReviewId()));
            }
        }

        return reviewsReceivedUser;
    }

    class UserHasReview {
        private int userSenderId;
        private int userReceiverId;
        private int reviewId;

        public UserHasReview(int userSenderId, int userReceiverId, int reviewId) {
            this.userSenderId = userSenderId;
            this.userReceiverId = userReceiverId;
            this.reviewId = reviewId;
        }

        public int getUserSenderId() {
            return userSenderId;
        }

        public int getUserReceiverId() {
            return userReceiverId;
        }

        public int getReviewId() {
            return reviewId;
        }
    }
}