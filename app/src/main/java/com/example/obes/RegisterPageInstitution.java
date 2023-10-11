package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterPageInstitution extends AppCompatActivity {

    private Button button_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page_institution);

        this.startComponents();

        this.button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(RegisterPageInstitution.this, MainActivity.class);
                //startActivity(intent);
                finish();
            }
        });
    }

    private void startComponents() {
        this.button_cancel = findViewById(R.id.button_cancel);
    }
}