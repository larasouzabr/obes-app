package com.example.obes.perfil.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.obes.MyAdapterRecyclerViewReview;
import com.example.obes.R;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.dao.Review.UserHasReviewDAO;
import com.example.obes.model.Review.Review;
import com.example.obes.model.User.User;

import java.util.ArrayList;

public class ReviewReceived extends Fragment {
    private RecyclerView rvReviewReceived;
    private LinearLayoutManager linearLayoutManager;
    private MyAdapterRecyclerViewReview receivedAdapter;
    private User userLogged;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_received, container, false);

        this.userLogged = this.getUserLogged();

        this.rvReviewReceived = view.findViewById(R.id.rv_review_received);

        this.linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        ArrayList<Review> reviewsReceivedByUserLogged = UserHasReviewDAO.getInstance().getReviewsReceivedByIdUser(this.userLogged.getId());

        this.receivedAdapter = new MyAdapterRecyclerViewReview(getContext(), reviewsReceivedByUserLogged, true);

        this.rvReviewReceived.setLayoutManager(this.linearLayoutManager);
        this.rvReviewReceived.setAdapter(this.receivedAdapter);

        return view;
    }

    public User getUserLogged() {
        User user = LoginSessionManager.getInstance().getCurrentUserCommon();

        if (user == null) {
            user = LoginSessionManager.getInstance().getCurrentUserInstitutional();
        }

        return user;
    }
}