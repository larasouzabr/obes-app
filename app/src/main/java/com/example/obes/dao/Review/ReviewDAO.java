package com.example.obes.dao.Review;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.model.Review.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReviewDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("review");
    private ArrayList<Review> listReviews;
    private static ReviewDAO instance;

    private ReviewDAO() {
        this.listReviews = new ArrayList<Review>();
    }

    public static ReviewDAO getInstance() {
        if (instance == null) {
            instance = new ReviewDAO();
        }
        return instance;
    }

    public ArrayList<Review> getListReviews() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Review> reviews = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int id = dataSnapshot.child("id").getValue(int.class);
                    int rate = dataSnapshot.child("rate").getValue(int.class);
                    String comment = dataSnapshot.child("comment").getValue(String.class);

                    Date dateCreated = new Date();
                    Date dateUpdated = new Date();

                    Review review = new Review(id, rate, comment, dateCreated);
                    review.setDateUpdated(dateUpdated);

                    reviews.add(review);
                }

                listReviews = reviews;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar avaliações: " + error.getMessage());
            }
        });

        return this.listReviews;
    }

    public Review getReviewById(int idReview) {
        Review review = null;

        for (Review r : this.listReviews) {
            if (r.getId() == idReview) {
                review = r;
                break;
            }
        }

        return review;
    }

    public boolean addReview(Review review) {
        this.listReviews.add(review);

        DatabaseReference childReference = this.reference.child(String.valueOf(review.getId()));

        Map<String, Object> data = new HashMap<>();
        data.put("id", review.getId());
        data.put("rate", review.getRate());
        data.put("comment", review.getComment());
        data.put("dateCreated", review.getDateCreated());
        data.put("dateUpdated", review.getDateUpdated());

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Avaliação cadastrado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao avaliar o usuário: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteReview(Review review) {
        Review reviewDelete = null;
        boolean removed = false;

        for (Review r : this.listReviews) {
            if (r.getId() == review.getId()) {
                reviewDelete = r;
                break;
            }
        }

        if (reviewDelete != null) {
            this.listReviews.remove(reviewDelete);
            removed = true;
        }

        this.reference.child(String.valueOf(review.getId())).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Avaliação removida com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao remover a avaliação: " + task.getException().getMessage());
                }
            }
        });

        return removed;
    }

    public boolean editReview(Review review) {
        Review reviewEdit = null;
        boolean edited = false;

        for (Review r : this.listReviews) {
            if (r.getId() == review.getId()) {
                reviewEdit = r;
                break;
            }
        }

        if (reviewEdit != null) {
            reviewEdit.setRate(review.getRate());
            reviewEdit.setComment(review.getComment());
            reviewEdit.setDateUpdated(review.getDateUpdated());

            edited = true;
        }

        DatabaseReference reviewReference = this.reference.child(String.valueOf(review.getId()));

        Map<String, Object> data = new HashMap<>();
        data.put("id", review.getId());
        data.put("rate", review.getRate());
        data.put("comment", review.getComment());
        data.put("dateCreated", review.getDateCreated());
        data.put("dateUpdated", review.getDateUpdated());

        reviewReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Avaliação editada com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao editar a avaliação: " + task.getException().getMessage());
                }
            }
        });

        return edited;
    }
}