package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.obes.dao.UserCommonDAO;
import com.example.obes.model.User.UserCommon;
//import com.example.obes.model.user.UserInstitutional;

import java.util.ArrayList;

public class RegisterPagePerson extends AppCompatActivity {
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private Button button_cancel;
    private Button sign_in;
    private UserCommonDAO userCommonDAO = UserCommonDAO.getInstance();

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
                boolean registered = registerUserCommon();

                if(registered) {
                    Intent intent = new Intent(RegisterPagePerson.this, Login.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void startComponents() {
        etName = findViewById(R.id.user_name);
        etEmail = findViewById(R.id.user_email);
        etPassword = findViewById(R.id.user_password);
        etPasswordConfirm = findViewById(R.id.user_confirm_password);
        this.button_cancel = findViewById(R.id.button_cancel);
        this.sign_in = findViewById(R.id.sign_in);
    }

    public boolean registerUserCommon() {
        int id = countId();
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etPasswordConfirm.getText().toString();

        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(RegisterPagePerson.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if(confirmPassword.equals(password)) {
                UserCommon userCommon = new UserCommon(id, name, email, password);

                this.userCommonDAO.addUser(userCommon);

                return true;
            } else {
                Toast.makeText(RegisterPagePerson.this, "Confirmação de senha inválido", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    public int countId() {
        ArrayList<UserCommon> listUsersCommon = this.userCommonDAO.getListUsers();

        int id = 1;

        int amountUsersCommon = listUsersCommon.size();

        if(amountUsersCommon > 0) {
            UserCommon lastUserCommon = listUsersCommon.get(amountUsersCommon - 1);
            id = lastUserCommon.getId() + 1;
        }

        return id;
    }
}