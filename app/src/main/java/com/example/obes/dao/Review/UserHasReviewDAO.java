package com.example.obes.dao.Review;

import com.example.obes.model.Review.Review;

import java.util.ArrayList;

public class UserHasReviewDAO {
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
        return this.listUserHasReview;
    }

    public boolean addUserReview(int userSenderId, int userReceiverId, int reviewId) {
        UserHasReview newUserHasReview = new UserHasReview(userSenderId, userReceiverId, reviewId);
        this.getListUserHasReview().add(newUserHasReview);
        return true;
    }

    public boolean deleteUserReview(int reviewId) {
        for (UserHasReview userHasReview : this.getListUserHasReview()) {
            if (userHasReview.getReviewId() == reviewId) {
                this.getListUserHasReview().remove(userHasReview);
                return true;
            }
        }
        return false;
    }

    public int getIdCommentByIdUsers(int idUserSender, int idUserReceiver) {
        for (UserHasReview userHasReview : this.getListUserHasReview()) {
            if (userHasReview.getUserSenderId() == idUserSender) {
                if (userHasReview.getUserReceiverId() == idUserReceiver) {
                    return userHasReview.getReviewId();
                }
            }
        }

        return 0;
    }

    public int getIdUserSenderByIdReview(int idReview) {
        for (UserHasReview userHasReview : this.getListUserHasReview()) {
            if (userHasReview.getReviewId() == idReview) {
                return userHasReview.getUserSenderId();
            }
        }

        return 0;
    }

    public int getIdUserReceiverByIdReview(int idReview) {
        for (UserHasReview userHasReview : this.getListUserHasReview()) {
            if (userHasReview.getReviewId() == idReview) {
                return userHasReview.getUserReceiverId();
            }
        }

        return 0;
    }

    public ArrayList<Review> getReviewsSenderByIdUser(int idUser) {
        ArrayList<Review> reviewsSenderUser = new ArrayList<Review>();

        for (UserHasReview userHasReview : this.getListUserHasReview()) {
            if (userHasReview.getUserSenderId() == idUser) {
                reviewsSenderUser.add(ReviewDAO.getInstance().getReviewById(userHasReview.getReviewId()));
            }
        }

        return reviewsSenderUser;
    }

    public ArrayList<Review> getReviewsReceivedByIdUser(int idUser) {
        ArrayList<Review> reviewsReceivedUser = new ArrayList<Review>();

        for (UserHasReview userHasReview : this.getListUserHasReview()) {
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