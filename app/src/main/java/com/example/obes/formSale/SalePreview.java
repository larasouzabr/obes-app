package com.example.obes.formSale;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.obes.R;

public class SalePreview extends AppCompatActivity {
    private TextView tvTitlePage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_preview);

        this.startComponents();

        this.tvTitlePage.setText("Preview");
    }
    private void startComponents() {
        this.tvTitlePage = findViewById(R.id.title_page);
    }
}