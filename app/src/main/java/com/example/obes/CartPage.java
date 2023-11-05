package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CartPage extends AppCompatActivity {
    private TextView tvTitlePage;
    private ImageView button_back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);
        this.startComponents();

        this.tvTitlePage.setText("Carrinho de Compras");

        this.button_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void startComponents() {
        this.tvTitlePage = findViewById(R.id.title_page);
        this.button_back_arrow = findViewById(R.id.back_arrow);
    }
}