package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obes.dao.AddressDAO;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.dao.UserCommonDAO;
import com.example.obes.formDonate.DonateFormPage;
import com.example.obes.formSale.SaleFormPage;
import com.example.obes.model.Address.Address;
import com.example.obes.model.User.UserCommon;

public class InformationUserPage extends AppCompatActivity {
    private TextView tvTitlePage;
    private EditText etDate;
    private EditText etCpf;
    private EditText etPhone;
    private Button button_cancel;
    private Button button_save;
    private EditText etCep;
    private EditText etState;
    private EditText etCity;
    private EditText etStreet;
    private EditText etNeighborhood;
    private EditText etNumber;
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

        this.setInfAddress();

        this.button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedFieldsEmpty()) {
                    Toast.makeText(InformationUserPage.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    UserCommon newUser = new UserCommon(userLogged.getId(), userLogged.getName(), userLogged.getEmail(), userLogged.getPassword());
                    newUser.setDateOfBirth(etDate.getText().toString());
                    newUser.setCpf(etCpf.getText().toString());
                    newUser.setContact(etPhone.getText().toString());

                    UserCommonDAO.getInstance().editUser(newUser);

                    AddressDAO addressDAO = AddressDAO.getInstance();

                    String cep = etCep.getText().toString();
                    String state = etState.getText().toString();
                    String city = etCity.getText().toString();
                    String street = etStreet.getText().toString();
                    String neighborhood = etNeighborhood.getText().toString();
                    String number = etNumber.getText().toString();

                    Address newAddress = new Address(countIdAddress(), userLogged.getId(), cep, state, city, street, neighborhood, number);

                    if (addressDAO.getAddressByIdUser(userLogged.getId()) == null) {
                        addressDAO.addAddress(newAddress);
                    } else {
                        addressDAO.editAddress(newAddress);
                    }

                    Intent intentExtra = getIntent();
                    String nextPage = intentExtra.getStringExtra("next_page");

                    Intent intent;

                    if (nextPage.equals("formDonate")) {
                        intent = new Intent(InformationUserPage.this, DonateFormPage.class);
                    } else {
                        intent = new Intent(InformationUserPage.this, SaleFormPage.class);
                    }

                    startActivity(intent);
                }
            }
        });
    }
    private void startComponents() {
        this.tvTitlePage = findViewById(R.id.title_page);
        this.etDate = findViewById(R.id.date);
        this.etCpf = findViewById(R.id.cpf);
        this.etPhone = findViewById(R.id.phone);
        this.button_cancel = findViewById(R.id.button_cancel);
        this.button_save = findViewById(R.id.button_save);
        this.etCep = findViewById(R.id.cep);
        this.etState = findViewById(R.id.state);
        this.etCity = findViewById(R.id.city);
        this.etStreet = findViewById(R.id.street);
        this.etNeighborhood = findViewById(R.id.neighborhood);
        this.etNumber = findViewById(R.id.number);
        this.loginSessionManager = LoginSessionManager.getInstance();
    }

    private boolean checkedFieldsEmpty() {
        boolean someEmpty = false;

        String date = this.etDate.getText().toString();
        String cpf = this.etCpf.getText().toString();
        String contact = this.etPhone.getText().toString();
        String cep = this.etCep.getText().toString();
        String state = this.etState.getText().toString();
        String city = this.etCity.getText().toString();
        String neighborhood = this.etNeighborhood.getText().toString();
        String number = this.etNumber.getText().toString();

        if (date.isEmpty() || cpf.isEmpty() || contact.isEmpty() || cep.isEmpty() || state.isEmpty() || city.isEmpty() || neighborhood.isEmpty() || number.isEmpty()) {
            someEmpty = true;
        }

        return someEmpty;
    }
    private int countIdAddress() {
        int idAddress = 1;

        int amountAddress = AddressDAO.getInstance().getListAddress().size();

        if (amountAddress > 0) {
            idAddress = AddressDAO.getInstance().getListAddress().get(amountAddress - 1).getIdAddress() + 1;
        }

        return idAddress;
    }

    private void setInfAddress() {
        Address userAddress = AddressDAO.getInstance().getAddressByIdUser(this.userLogged.getId());

        if (userAddress != null) {
            this.etCep.setText(userAddress.getCep());
            this.etState.setText(userAddress.getEstado());
            this.etCity.setText(userAddress.getCidade());
            this.etStreet.setText(userAddress.getRua());
            this.etNeighborhood.setText(userAddress.getBairro());
            this.etNumber.setText(userAddress.getNumero());
        }
    }
}