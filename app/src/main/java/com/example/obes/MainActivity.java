package com.example.obes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.obes.dao.UserCommonDAO;
import com.example.obes.dao.UserInstitutionalDAO;
import com.example.obes.model.User.UserCommon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.obes.databinding.ActivityMainBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Button button_sign_in;
    private Button button_register;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // setContentView(R.layout.activity_book_sale_page);

        //
        if (UserCommonDAO.getInstance().getListUsers().size() == 0 || UserInstitutionalDAO.getInstance().getListUsers().size() == 0) {
            FakeData.generateFakeUsers();
        }

        // UserCommonDAO.getInstance().deleteUser(new UserCommon(1, "dsiofodsf", "sfkdsfd", "sfjdf"));
        //

        startComponents();

        button_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterPage.class);
                startActivity(intent);
            }
        });
    }

    private void startComponents() {
        this.button_sign_in = findViewById(R.id.sign_in);
        this.button_register = findViewById(R.id.sign_out);
    }
}