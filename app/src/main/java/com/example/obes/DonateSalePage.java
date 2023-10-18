package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obes.dao.LoginSessionManager;
import com.example.obes.formDonate.DonateFormPage;

public class DonateSalePage extends AppCompatActivity {
    private TextView button_donate;
    private TextView button_sell;
    private LoginSessionManager loginSessionManager = LoginSessionManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_sale_page);

        BottomMenuHandler bottomMenuHandler = new BottomMenuHandler(this);
        bottomMenuHandler.setupBottomMenu();

        this.startComponents();

        this.button_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedUserTypeCommon()) {
                    Intent intent = new Intent(DonateSalePage.this, DonateFormPage.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(DonateSalePage.this, "Somente usuários comuns podem doar livros", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startComponents() {
        this.button_donate = findViewById(R.id.button_donate);
        this.button_sell = findViewById(R.id.button_sell);
    }

    private boolean checkedUserTypeCommon() {
        if (loginSessionManager.getCurrentUserCommon() == null) {
            return false;
        }

        return true;
    }
}