package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterPagePerson extends AppCompatActivity {

    private Button button_cancel;
    private Button sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page_person);

        this.startComponents();

        this.button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPagePerson.this, MainActivity.class);
                startActivity(intent);
            }
        });

        this.sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPagePerson.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void startComponents() {
        this.button_cancel = findViewById(R.id.button_cancel);
        this.sign_in = findViewById(R.id.sign_in);
    }
}