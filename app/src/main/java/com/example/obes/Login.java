package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    private ImageView button_back_arrow;
    private TextView text_register;

    private Button button_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.startComponents();

        this.button_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.text_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RegisterPage.class);
                startActivity(intent);
            }
        });

        this.button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, HomePage.class);
                startActivity(intent);
            }
        });
    }

    private void startComponents() {
        this.button_back_arrow = findViewById(R.id.back_arrow);
        this.text_register = findViewById(R.id.register);
        this.button_login = findViewById(R.id.login);
    }
}