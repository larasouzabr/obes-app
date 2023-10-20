package com.example.obes.formSale;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.obes.R;

public class SaleFormPage extends AppCompatActivity {
    private TextView tvTitlePage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_form_page);

        this.startComponents();

        this.tvTitlePage.setText("Vender um livro");
    }
    private void startComponents() {
        this.tvTitlePage = findViewById(R.id.title_page);
    }
}