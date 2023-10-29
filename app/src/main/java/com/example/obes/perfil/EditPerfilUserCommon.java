package com.example.obes.perfil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.obes.R;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.model.User.User;
import com.example.obes.model.User.UserCommon;

public class EditPerfilUserCommon extends AppCompatActivity {
    private EditText etUserNameEdit;
    private EditText etContactEdit;
    private EditText etCpfEdit;
    private EditText etEmailEdit;
    private EditText etPasswordEdit;
    private Button button_cancel;
    private Button button_save;
    private LoginSessionManager loginSessionManager;
    private UserCommon userLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil_user_common);

        this.startComponents();

        userLogged = this.getUserLogged();

        this.setInfUser();

        this.button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = etUserNameEdit.getText().toString();
                String newEmail = etEmailEdit.getText().toString();
                String newPassword = etPasswordEdit.getText().toString();

                UserCommon newUser = new UserCommon(userLogged.getId(), newName, newEmail, newPassword);
                userLogged.editUser(newUser);

                Intent intent = new Intent(EditPerfilUserCommon.this, PerfilUserCommon.class);
                startActivity(intent);
            }
        });
    }
    private void startComponents() {
        this.etUserNameEdit = findViewById(R.id.user_name_edit);
        this.etContactEdit = findViewById(R.id.user_contact_edit);
        this.etCpfEdit = findViewById(R.id.user_cpf_edit);
        this.etEmailEdit = findViewById(R.id.user_email_edit);
        this.button_cancel = findViewById(R.id.button_cancel);
        this.button_save = findViewById(R.id.button_save);
        this.etPasswordEdit = findViewById(R.id.user_password_edit);
        this.loginSessionManager = LoginSessionManager.getInstance();
    }
    private UserCommon getUserLogged() {
        UserCommon user = loginSessionManager.getCurrentUserCommon();
        return user;
    }
    private void setInfUser() {
        this.etUserNameEdit.setText(this.userLogged.getName());
        this.etContactEdit.setText(this.userLogged.getContact());
        this.etCpfEdit.setText(this.userLogged.getCpf());
        this.etEmailEdit.setText(this.userLogged.getEmail());
        this.etPasswordEdit.setText(this.userLogged.getPassword());
    }
}