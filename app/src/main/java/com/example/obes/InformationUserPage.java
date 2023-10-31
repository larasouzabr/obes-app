package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.obes.dao.LoginSessionManager;
import com.example.obes.model.User.UserCommon;

public class InformationUserPage extends AppCompatActivity {
    private TextView tvTitlePage;
    private EditText etDate;
    private EditText etCpf;
    private EditText etPhone;
    private Button button_cancel;
    private LoginSessionManager loginSessionManager;
    private UserCommon userLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_user_page);
        this.startComponents();

        this.userLogged = loginSessionManager.getCurrentUserCommon();

        this.tvTitlePage.setText("Confirme seus dados");

        this.etDate.setText(this.userLogged.getDateOfBirth());
        this.etCpf.setText(this.userLogged.getCpf());
        this.etPhone.setText(this.userLogged.getContact());

        this.button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void startComponents() {
        this.tvTitlePage = findViewById(R.id.title_page);
        this.etDate = findViewById(R.id.date);
        this.etCpf = findViewById(R.id.cpf);
        this.etPhone = findViewById(R.id.phone);
        this.button_cancel = findViewById(R.id.button_cancel);
        this.loginSessionManager = LoginSessionManager.getInstance();
    }
}