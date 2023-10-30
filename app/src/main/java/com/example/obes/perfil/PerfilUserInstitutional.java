package com.example.obes.perfil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.obes.R;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.model.User.UserInstitutional;

public class PerfilUserInstitutional extends AppCompatActivity {
    private TextView tvUserName;
    private TextView tvUserAbout;
    private ImageView ivButtonEdit;
    private LoginSessionManager loginSessionManager;
    private UserInstitutional userLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_user_institutional);
        this.startComponents();

        userLogged = this.getUserLogged();

        this.setInfUser(userLogged);

        this.ivButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Vai para a tela de edit do usuário institucional");
            }
        });
    }
    private void startComponents() {
        this.tvUserName = findViewById(R.id.user_name);
        this.tvUserAbout = findViewById(R.id.user_about);
        this.ivButtonEdit = findViewById(R.id.button_edit);
        this.loginSessionManager = LoginSessionManager.getInstance();
    }
    private UserInstitutional getUserLogged() {
        UserInstitutional user = loginSessionManager.getCurrentUserInstitutional();
        return user;
    }
    private void setInfUser(UserInstitutional user) {
        this.tvUserName.setText(user.getName());

        if(this.userLogged.getAbout().isEmpty()) {
            this.tvUserAbout.setText("Olá! Sou apaixonado por doar livros e compartilhar conhecimento.");
        } else {
            this.tvUserAbout.setText(this.userLogged.getAbout());
        }
    }
}