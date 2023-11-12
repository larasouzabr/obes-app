package com.example.obes.dao.Review;

import com.example.obes.model.Review.Review;

import java.util.ArrayList;

public class ReviewDAO {
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
        return this.listReviews;
    }

    public Review getReviewById(int idReview) {
        Review review = null;

        for (Review r : this.getListReviews()) {
            if (r.getId() == idReview) {
                review = r;
                break;
            }
        }

        return review;
    }

    public boolean addReview(Review review) {
        this.getListReviews().add(review);
        return true;
    }

    public boolean deleteReview(Review review) {
        Review reviewDelete = null;
        boolean removed = false;

        for (Review r : this.getListReviews()) {
            if (r.getId() == review.getId()) {
                reviewDelete = r;
                break;
            }
        }

        if (reviewDelete != null) {
            this.getListReviews().remove(reviewDelete);
            removed = true;
        }

        return removed;
    }

    public boolean editReview(Review review) {
        Review reviewEdit = null;
        boolean edited = false;

        for (Review r : this.getListReviews()) {
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

        return edited;
    }
}