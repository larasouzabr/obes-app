package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.obes.dao.UserCommonDAO;
import com.example.obes.dao.UserInstitutionalDAO;
import com.example.obes.model.User.User;
import com.example.obes.model.User.UserCommon;
import com.example.obes.model.User.UserInstitutional;

import java.util.ArrayList;
import java.util.Collections;

public class RegisterPageInstitution extends AppCompatActivity {
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private RadioGroup rgType;
    private Button button_cancel;
    private Button button_sign_in;
    private UserCommonDAO userCommonDAO = UserCommonDAO.getInstance();
    private UserInstitutionalDAO userInstitutionalDAO = UserInstitutionalDAO.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page_institution);

        this.startComponents();

        this.button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.button_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean registered = registerUserInstitutional();

                if(registered) {
                    Intent intent = new Intent(RegisterPageInstitution.this, Login.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void startComponents() {
        this.etName = findViewById(R.id.user_name);
        this.etEmail = findViewById(R.id.user_email);
        this.etPassword = findViewById(R.id.user_password);
        this.etPasswordConfirm = findViewById(R.id.user_confirm_password);
        this.rgType = findViewById(R.id.radioGroup_register_institution);
        this.button_cancel = findViewById(R.id.button_cancel);
        this.button_sign_in = findViewById(R.id.sign_in);
    }

    public boolean registerUserInstitutional() {
        int id = countId();
        String name = this.etName.getText().toString();
        String email = this.etEmail.getText().toString();
        String password = this.etPassword.getText().toString();
        String confirmPassword = this.etPasswordConfirm.getText().toString();

        int idRbType = this.rgType.getCheckedRadioButtonId();

        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || idRbType == -1) {
            Toast.makeText(RegisterPageInstitution.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if(confirmPassword.equals(password)) {
                RadioButton rbTypeSelected = findViewById(idRbType);
                String type = rbTypeSelected.getText().toString();

                UserInstitutional userInstitutional = new UserInstitutional(id, name, email, password, type);

                this.userInstitutionalDAO.addUser(userInstitutional);

                return true;
            } else {
                Toast.makeText(RegisterPageInstitution.this, "Confirmação de senha inválido", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    public int countId() {
        ArrayList<User> allUsers = new ArrayList<User>();
        ArrayList<UserCommon> listUsersCommon = this.userCommonDAO.getListUsers();
        ArrayList<UserInstitutional> listUsersInstitutional = this.userInstitutionalDAO.getListUsers();

        allUsers.addAll(listUsersCommon);
        allUsers.addAll(listUsersInstitutional);

        ArrayList<Integer> listIds = new ArrayList<Integer>();

        for (User user : allUsers) {
            if (!listIds.contains(user.getId())) {
                listIds.add(user.getId());
            }
        }

        int id = 1;

        int amountUsers = allUsers.size();

        if(amountUsers > 0) {
            id = Collections.max(listIds) + 1;
        }

        return id;
    }
}