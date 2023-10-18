package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obes.dao.LoginSessionManager;
import com.example.obes.dao.UserCommonDAO;
import com.example.obes.dao.UserInstitutionalDAO;
import com.example.obes.model.User.UserCommon;
import com.example.obes.model.User.UserInstitutional;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    private ImageView button_back_arrow;
    private TextView text_register;
    private Button button_login;
    private EditText etUserEmail;
    private EditText etUserPassword;
    private LoginSessionManager loginSessionManager = LoginSessionManager.getInstance();

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
                String email = etUserEmail.getText().toString();
                String password = etUserPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isCheckedLogin = checkedLogin(email, password);

                    if (isCheckedLogin) {
                        Intent intent = new Intent(Login.this, HomePage.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "E-mail/Senha inválidos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void startComponents() {
        this.button_back_arrow = findViewById(R.id.back_arrow);
        this.text_register = findViewById(R.id.register);
        this.button_login = findViewById(R.id.login);
        this.etUserEmail = findViewById(R.id.user_email);
        this.etUserPassword = findViewById(R.id.user_password);
    }

    private boolean checkedLogin(String email, String password) {
        UserCommonDAO userCommonDAO = UserCommonDAO.getInstance();
        UserInstitutionalDAO userInstitutionalDAO = UserInstitutionalDAO.getInstance();

        ArrayList<UserCommon> usersCommon = userCommonDAO.getListUsers();
        ArrayList<UserInstitutional> usersInstitutional = userInstitutionalDAO.getListUsers();

        for (UserCommon user : usersCommon) {
            if (user.getEmail().equals(email)) {
                if (user.getPassword().equals(password)) {
                    loginSessionManager.loginUserCommon(user);
                    return true;
                }
            }
        }

        for (UserInstitutional user : usersInstitutional) {
            if (user.getEmail().equals(email)) {
                if (user.getPassword().equals(password)) {
                    loginSessionManager.loginUserInstitutional(user);
                    return true;
                }
            }
        }

        return false;
    }
}