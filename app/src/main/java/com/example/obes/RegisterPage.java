package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterPage extends AppCompatActivity {
    private Button button_back_page;
    private Button button_next_page;
    private RadioGroup radio_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        startComponents();

        this.button_back_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.button_next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemRadioGroupChecked = radio_group.getCheckedRadioButtonId();

                if(itemRadioGroupChecked == -1) {
                    Toast.makeText(RegisterPage.this, "Por favor, selecione uma opção", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent;

                    if(itemRadioGroupChecked == R.id.rb_pessoa) {
                        intent = new Intent(RegisterPage.this, RegisterPagePerson.class);
                    } else {
                        intent = new Intent(RegisterPage.this, RegisterPageInstitution.class);
                    }

                    startActivity(intent);
                }
            }
        });
    }

    private void startComponents() {
        this.button_back_page = findViewById(R.id.back_page);
        this.button_next_page = findViewById(R.id.sign_in);
        this.radio_group = findViewById(R.id.radioGroup_register_institution);
    }
}