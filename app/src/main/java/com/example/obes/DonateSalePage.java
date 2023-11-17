package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obes.dao.AddressDAO;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.formDonate.DonateFormPage;
import com.example.obes.formSale.SaleFormPage;
import com.example.obes.model.Address.Address;
import com.example.obes.perfil.PerfilUserCommon;
import com.example.obes.perfil.PerfilUserInstitutional;

public class DonateSalePage extends AppCompatActivity {
    private TextView button_donate;
    private TextView button_sell;
    private ImageView foto_perfil;
    private LoginSessionManager loginSessionManager = LoginSessionManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_sale_page);

        BottomMenuHandler bottomMenuHandler = new BottomMenuHandler(this);
        bottomMenuHandler.setupBottomMenu();

        this.startComponents();

        this.button_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedUserTypeCommon()) {
                    Intent intent;
                    if (checkedAddressUser()) {
                        intent = new Intent(DonateSalePage.this, DonateFormPage.class);
                    } else {
                        intent = new Intent(DonateSalePage.this, InformationUserPage.class);
                        intent.putExtra("next_page", "formDonate");
                    }

                    startActivity(intent);
                } else {
                    Toast.makeText(DonateSalePage.this, "Somente usuários comuns podem doar livros", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.button_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedUserTypeCommon()) {
                    Intent intent;
                    if (checkedAddressUser()) {
                        intent = new Intent(DonateSalePage.this, SaleFormPage.class);

                    } else {
                        intent = new Intent(DonateSalePage.this, InformationUserPage.class);
                        intent.putExtra("next_page", "formSale");
                    }

                    startActivity(intent);
                } else {
                    Toast.makeText(DonateSalePage.this, "Somente usuários comuns podem vender livros", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // =========================

        foto_perfil = findViewById(R.id.foto_perfil);

        foto_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean userLoggedIsCommon = checkedUserLoggedIsCommon();

                Intent intent;
                if (userLoggedIsCommon) {
                    intent = new Intent(DonateSalePage.this, PerfilUserCommon.class);
                } else {
                    intent = new Intent(DonateSalePage.this, PerfilUserInstitutional.class);
                }
                startActivity(intent);
            }
        });
    }

    private void startComponents() {
        this.button_donate = findViewById(R.id.button_donate);
        this.button_sell = findViewById(R.id.button_sell);
    }

    private boolean checkedUserTypeCommon() {
        if (loginSessionManager.getCurrentUserCommon() == null) {
            return false;
        }

        return true;
    }

    private boolean checkedAddressUser() {
        AddressDAO addressDAO = AddressDAO.getInstance();

        Address address =  addressDAO.getAddressByIdUser(loginSessionManager.getCurrentUserCommon().getId());

        if (address == null) {
            return false;
        }

        return true;
    }

    private boolean checkedUserLoggedIsCommon() {
        if (loginSessionManager.getCurrentUserCommon() != null) {
            return true;
        } else {
            return false;
        }
    }
}