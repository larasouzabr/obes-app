package com.example.obes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.obes.dao.Review.UserHasReviewDAO;
import com.example.obes.dao.UserCommonDAO;
import com.example.obes.dao.UserInstitutionalDAO;
import com.example.obes.model.Review.Review;
import com.example.obes.model.User.User;

import java.util.ArrayList;

public class MyAdapterRecyclerViewReview extends RecyclerView.Adapter<MyAdapterRecyclerViewReview.MyHolder> {
    private ArrayList<Review> data;
    private boolean isReceived;
    private Context context;

    public MyAdapterRecyclerViewReview(Context context, ArrayList<Review> data, boolean isReceived) {
        this.context = context;
        this.data = data;
        this.isReceived = isReceived;
    }

    @NonNull
    @Override
    public MyAdapterRecyclerViewReview.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);

        return new MyAdapterRecyclerViewReview.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterRecyclerViewReview.MyHolder holder, int position) {
        Review review = data.get(position);

        int idUserSender = UserHasReviewDAO.getInstance().getIdUserSenderByIdReview(review.getId());
        User userReview = UserCommonDAO.getInstance().getUserById(idUserSender);
        if (userReview == null) {
            userReview = UserInstitutionalDAO.getInstance().getUserById(idUserSender);
        }

        holder.rbRating.setRating((float) review.getRate());

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReceived) {
                    System.out.println("Vai para a página de visualização da Review");
                } else {
                    Intent intent = new Intent(context, ReviewPage.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        LinearLayout llItem;
        ImageView ivPhoto;
        RatingBar rbRating;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            llItem = itemView.findViewById(R.id.item);
            ivPhoto = itemView.findViewById(R.id.photo_user_sender);
            rbRating = itemView.findViewById(R.id.rating);
        }
    }
}