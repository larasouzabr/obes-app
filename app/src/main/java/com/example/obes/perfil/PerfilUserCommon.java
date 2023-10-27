package com.example.obes.perfil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TextView;

import com.example.obes.BottomMenuHandler;
import com.example.obes.R;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.model.User.UserCommon;
import com.google.android.material.tabs.TabLayout;

public class PerfilUserCommon extends AppCompatActivity {
    private TextView tvUserName;
    private TabLayout tlReviews;
    private ViewPager2 vpTabReviews;
    private TabLayout tlBooks;
    private ViewPager2 vpTabBooks;
    private MyViewPageAdapterReview myViewPageAdapter;
    private MyViewPageAdapterBooks myViewPageAdapterBooks;
    private LoginSessionManager loginSessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_user_common);

        this.startComponents();
        this.setInfUser();

        BottomMenuHandler bottomMenuHandler = new BottomMenuHandler(this);
        bottomMenuHandler.setupBottomMenu();

        this.showTabLayoutReviews();

        this.showTabLayoutBooks();
    }

    private void startComponents() {
        this.tvUserName = findViewById(R.id.user_name);
        this.tlReviews = findViewById(R.id.tab_reviews);
        this.vpTabReviews = findViewById(R.id.layout_tab_reviews);
        this.myViewPageAdapter = new MyViewPageAdapterReview(this);
        this.tlBooks = findViewById(R.id.tab_books_available);
        this.vpTabBooks = findViewById(R.id.layout_tab_books_available);
        this.myViewPageAdapterBooks = new MyViewPageAdapterBooks(this);
        this.loginSessionManager = LoginSessionManager.getInstance();
    }

    private void setInfUser() {
        UserCommon userLogged = this.getUserLogged();

        this.tvUserName.setText(userLogged.getName());
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
}