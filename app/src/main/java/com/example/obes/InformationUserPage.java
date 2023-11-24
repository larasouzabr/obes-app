package com.example.obes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private Button button_address;
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

        this.button_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddress = new Intent(InformationUserPage.this, LocationPage.class);

                Intent intentExtra = getIntent();
                String nextPage = intentExtra.getStringExtra("next_page");

                intentAddress.putExtra("flow", "donateSale");
                intentAddress.putExtra("next_page", nextPage);

                startActivity(intentAddress);
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

                    if (addressDAO.getAddressByIdUser(userLogged.getId()) == null) {
                        Button buttonAddress = findViewById(R.id.button_address);

                        TextView newTextView = new TextView(InformationUserPage.this);
                        newTextView.setText("Adicione/Edite seu endereço");
                        newTextView.setTextColor(getResources().getColor(R.color.red));

                        LinearLayout linearLayout = findViewById(R.id.layout_data);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity = Gravity.CENTER;
                        newTextView.setLayoutParams(layoutParams);

                        linearLayout.addView(newTextView, linearLayout.indexOfChild(buttonAddress) + 1);

                        Toast.makeText(InformationUserPage.this, "Por favor, cadastre um endereço", Toast.LENGTH_SHORT).show();
                    } else {
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
        this.button_address = findViewById(R.id.button_address);
        this.loginSessionManager = LoginSessionManager.getInstance();
    }

    private boolean checkedFieldsEmpty() {
        boolean someEmpty = false;

        String date = this.etDate.getText().toString();
        String cpf = this.etCpf.getText().toString();
        String contact = this.etPhone.getText().toString();

        if (date.isEmpty() || cpf.isEmpty() || contact.isEmpty()) {
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
}