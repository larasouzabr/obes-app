package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DonateSalePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_sale_page);

        BottomMenuHandler bottomMenuHandler = new BottomMenuHandler(this);
        bottomMenuHandler.setupBottomMenu();
    }
}