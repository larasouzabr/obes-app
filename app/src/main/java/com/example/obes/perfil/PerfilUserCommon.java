package com.example.obes.perfil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.obes.BottomMenuHandler;
import com.example.obes.LocationPage;
import com.example.obes.R;
import com.example.obes.dao.AddressDAO;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.dao.Review.UserHasReviewDAO;
import com.example.obes.model.Address.Address;
import com.example.obes.model.Review.Review;
import com.example.obes.model.User.UserCommon;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PerfilUserCommon extends AppCompatActivity {
    private TextView tvUserName;
    private TextView tvUserAbout;
    private TextView tvUserRate;
    private TabLayout tlReviews;
    private ViewPager2 vpTabReviews;
    private TabLayout tlBooks;
    private ViewPager2 vpTabBooks;
    private ImageView ivButtonEdit;
    private LinearLayout location;
    private TextView tvUserLocation;
    private MyViewPageAdapterReview myViewPageAdapter;
    private MyViewPageAdapterBooks myViewPageAdapterBooks;
    private LoginSessionManager loginSessionManager;
    private UserCommon userLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_user_common);
        this.startComponents();

        userLogged = this.getUserLogged();

        this.setInfUser(userLogged);

        BottomMenuHandler bottomMenuHandler = new BottomMenuHandler(this);
        bottomMenuHandler.setupBottomMenu();

        this.showTabLayoutReviews();

        this.showTabLayoutBooks();

        this.ivButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilUserCommon.this, EditPerfilUserCommon.class);
                startActivity(intent);
            }
        });

        this.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilUserCommon.this, LocationPage.class);
                intent.putExtra("flow", "profile");
                startActivity(intent);
            }
        });
    }

    private void startComponents() {
        this.tvUserName = findViewById(R.id.user_name);
        this.tvUserAbout = findViewById(R.id.user_about);
        this.tlReviews = findViewById(R.id.tab_reviews);
        this.vpTabReviews = findViewById(R.id.layout_tab_reviews);
        this.myViewPageAdapter = new MyViewPageAdapterReview(this);
        this.tlBooks = findViewById(R.id.tab_books_available);
        this.vpTabBooks = findViewById(R.id.layout_tab_books_available);
        this.myViewPageAdapterBooks = new MyViewPageAdapterBooks(this);
        this.loginSessionManager = LoginSessionManager.getInstance();
        this.ivButtonEdit = findViewById(R.id.button_edit);
        this.location = findViewById(R.id.location);
        this.tvUserLocation = findViewById(R.id.user_location);
        this.tvUserRate = findViewById(R.id.user_rate);
    }

    private void setInfUser(UserCommon user) {
        this.tvUserName.setText(user.getName());

        Address addressUser = AddressDAO.getInstance().getAddressByIdUser(this.userLogged.getId());

        if (addressUser == null) {
            this.tvUserLocation.setText("Localização");
        } else {
            String location = addressUser.getCidade() + " - " + addressUser.getEstado();
            this.tvUserLocation.setText(location);
        }

        if (this.userLogged.getAbout().isEmpty()) {
            this.tvUserAbout.setText("Olá! Sou apaixonado por doar livros e compartilhar conhecimento.");
        } else {
            this.tvUserAbout.setText(this.userLogged.getAbout());
        }

        this.tvUserRate.setText(this.getRatingUser());
    }

    private UserCommon getUserLogged() {
        UserCommon user = loginSessionManager.getCurrentUserCommon();
        return user;
    }

    private void showTabLayoutReviews() {
        // TabLayout Reviews
        vpTabReviews.setAdapter(this.myViewPageAdapter);

        tlReviews.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpTabReviews.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vpTabReviews.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tlReviews.getTabAt(position).select();
            }
        });
    }

    private void showTabLayoutBooks() {
        // TabLayout Books
        vpTabBooks.setAdapter(this.myViewPageAdapterBooks);

        vpTabBooks.setUserInputEnabled(false);

        tlBooks.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpTabBooks.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vpTabBooks.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tlBooks.getTabAt(position).select();
            }
        });
    }

    public String getRatingUser() {
        float rating = 0;

        ArrayList<Review> reviewsUser = UserHasReviewDAO.getInstance().getReviewsReceivedByIdUser(this.userLogged.getId());

        for (Review review : reviewsUser) {
            rating += review.getRate();
        }

        int amountReviews = reviewsUser.size();

        if (amountReviews > 0) {
            rating /= amountReviews;

            DecimalFormat formato = new DecimalFormat("#0.0");

            String ratingFormatado = formato.format(rating);

            return ratingFormatado;
        } else {
            return "0.0";

        }
    }
}