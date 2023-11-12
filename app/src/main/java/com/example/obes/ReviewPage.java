package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ReviewPage extends AppCompatActivity {
    private TextView tvTitlePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_page);
        this.startComponents();

        this.tvTitlePage.setText("Comentário");
    }

    public void startComponents() {
        this.tvTitlePage = findViewById(R.id.title_page);
    }
}