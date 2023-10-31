package com.example.obes.perfil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.obes.R;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.model.User.UserInstitutional;

public class EditPerfilUserInstitutional extends AppCompatActivity {
    private EditText etUserNameEdit;
    private EditText etAbout;
    private EditText etContactEdit;
    private EditText etEmailEdit;
    private EditText etPasswordEdit;
    private Button button_cancel;
    private Button button_save;
    private LoginSessionManager loginSessionManager;
    private UserInstitutional userLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil_user_institutional);
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
                String newAbout = etAbout.getText().toString();
                String newContact = etContactEdit.getText().toString();

                UserInstitutional newUser = new UserInstitutional(userLogged.getId(), newName, newEmail, newPassword, userLogged.getType());
                newUser.setAbout(newAbout);
                newUser.setContact(newContact);

                userLogged.editUser(newUser);

                Intent intent = new Intent(EditPerfilUserInstitutional.this, PerfilUserInstitutional.class);
                startActivity(intent);
            }
        });
    }
    private void startComponents() {
        this.etUserNameEdit = findViewById(R.id.user_name_edit);
        this.etAbout = findViewById(R.id.user_about_edit);
        this.etContactEdit = findViewById(R.id.user_contact_edit);
        this.etEmailEdit = findViewById(R.id.user_email_edit);
        this.button_cancel = findViewById(R.id.button_cancel);
        this.button_save = findViewById(R.id.button_save);
        this.etPasswordEdit = findViewById(R.id.user_password_edit);
        this.loginSessionManager = LoginSessionManager.getInstance();
    }
    private UserInstitutional getUserLogged() {
        UserInstitutional user = loginSessionManager.getCurrentUserInstitutional();
        return user;
    }
    private void setInfUser() {
        this.etUserNameEdit.setText(this.userLogged.getName());
        this.etAbout.setText(this.userLogged.getAbout());
        this.etContactEdit.setText(this.userLogged.getContact());
        this.etEmailEdit.setText(this.userLogged.getEmail());
        this.etPasswordEdit.setText(this.userLogged.getPassword());
    }
}