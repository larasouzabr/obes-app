package com.example.obes.formDonate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.obes.R;

public class DonateFormPage extends AppCompatActivity {
    private Button button_cancel;
    private Button button_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_form_page);

        this.startComponents();

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void startComponents() {
        this.button_cancel = findViewById(R.id.button_cancel);
        this.button_next = findViewById(R.id.button_next);
    }
}