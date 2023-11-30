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
import com.example.obes.model.Address.Address;
import com.example.obes.model.User.UserInstitutional;
import com.google.android.material.tabs.TabLayout;

public class PerfilUserInstitutional extends AppCompatActivity {
    private TextView tvUserName;
    private TextView tvUserAbout;
    private ImageView ivButtonEdit;
    private ViewPager2 vpTabBooks;
    private ViewPager2 vpTabReviews;
    private TabLayout tlBooks;
    private TabLayout tlReviews;
    private LinearLayout location;
    private TextView tvUserLocation;
    private MyViewPageAdapterBooks myViewPageAdapterBooks;
    private MyViewPageAdapterReview myViewPageAdapter;
    private LoginSessionManager loginSessionManager;
    private UserInstitutional userLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_user_institutional);
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
                Intent intent = new Intent(PerfilUserInstitutional.this, EditPerfilUserInstitutional.class);
                startActivity(intent);
            }
        });

        this.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilUserInstitutional.this, LocationPage.class);
                intent.putExtra("flow", "profile");
                startActivity(intent);
            }
        });
    }
    private void startComponents() {
        this.tvUserName = findViewById(R.id.user_name);
        this.tvUserAbout = findViewById(R.id.user_about);
        this.ivButtonEdit = findViewById(R.id.button_edit);
        this.loginSessionManager = LoginSessionManager.getInstance();
        this.vpTabBooks = findViewById(R.id.layout_tab_books_available);
        this.vpTabReviews = findViewById(R.id.layout_tab_reviews);
        this.myViewPageAdapterBooks = new MyViewPageAdapterBooks(this);
        this.myViewPageAdapter = new MyViewPageAdapterReview(this);
        this.tlBooks = findViewById(R.id.tab_books_available);
        this.tlReviews = findViewById(R.id.tab_reviews);
        this.location = findViewById(R.id.location);
        this.tvUserLocation = findViewById(R.id.user_location);
    }
    private UserInstitutional getUserLogged() {
        UserInstitutional user = loginSessionManager.getCurrentUserInstitutional();
        return user;
    }
    private void setInfUser(UserInstitutional user) {
        this.tvUserName.setText(user.getName());

        Address addressUser = AddressDAO.getInstance().getAddressByIdUser(this.userLogged.getId());

        if (addressUser == null) {
            this.tvUserLocation.setText("Localização");
        } else {
            String location = addressUser.getCidade() + " - " + addressUser.getEstado();
            this.tvUserLocation.setText(location);
        }

        if(this.userLogged.getAbout().isEmpty()) {
            this.tvUserAbout.setText("Olá! Sou apaixonado por doar livros e compartilhar conhecimento.");
        } else {
            this.tvUserAbout.setText(this.userLogged.getAbout());
        }
    }

    private void showTabLayoutBooks() {
        // TabLayout Books
        vpTabBooks.setAdapter(this.myViewPageAdapterBooks);

        vpTabBooks.setUserInputEnabled(false);

        tlBooks.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpTabBooks.setCurrentItem(1);
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
                tlBooks.getTabAt(0).select();
            }
        });

        vpTabBooks.setCurrentItem(1);
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
}