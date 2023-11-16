package com.example.obes.payment.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obes.R;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.dao.Payment.PaymentDAO;
import com.example.obes.dao.Payment.PaymentToUserDAO;
import com.example.obes.dao.Review.ReviewDAO;
import com.example.obes.dao.Review.UserHasReviewDAO;
import com.example.obes.dao.UserCommonDAO;
import com.example.obes.formSale.SaleFormPage;
import com.example.obes.model.Payment.CreditCardPayment;
import com.example.obes.model.Payment.IPayment;
import com.example.obes.model.User.User;

import java.util.ArrayList;

public class Card extends Fragment {
    private EditText etHolderName;
    private EditText etCardNumber;
    private EditText etExpirationDate;
    private EditText etSecurityCode;
    private Button buttonSave;
    private User userLogged;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        this.startComponents(view);

        this.userLogged = this.getUserLogged();

        IPayment payment = getPaymentUser();
        if (payment != null) {
            this.setInfPayment(payment);
        }

        this.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCardNumber.getText().toString().isEmpty() || etHolderName.getText().toString().isEmpty() || etExpirationDate.getText().toString().isEmpty() || etSecurityCode.getText().toString().isEmpty()) {
                    Toast.makeText(view.getContext(), "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {

                    showModalAddPayment();
                }
            }
        });

        return view;
    }

    private void startComponents(View view) {
        this.etHolderName = view.findViewById(R.id.holder_name);
        this.etCardNumber = view.findViewById(R.id.card_number);
        this.etExpirationDate = view.findViewById(R.id.expiration_date);
        this.etSecurityCode = view.findViewById(R.id.security_code);
        this.buttonSave = view.findViewById(R.id.button_save);
    }

    private IPayment getPaymentUser() {
        ArrayList<IPayment> paymentsUser = PaymentToUserDAO.getInstance().getPaymentsByIdUser(userLogged.getId());
        IPayment paymentUser = null;

        for (IPayment p : paymentsUser) {
            if (p instanceof CreditCardPayment) {
                paymentUser = p;
            }
        }

        return paymentUser;
    }

    private User getUserLogged() {
        User user = LoginSessionManager.getInstance().getCurrentUserCommon();
        if (user == null) {
            user = LoginSessionManager.getInstance().getCurrentUserInstitutional();
        }

        return user;
    }

    public int countId() {
        PaymentDAO paymentDAO = PaymentDAO.getInstance();

        int amountPayments = paymentDAO.getListPayment().size();

        int id = 1;

        if (amountPayments > 0) {
            id = paymentDAO.getListPayment().get(amountPayments - 1).getId() + 1;
        }

        return id;
    }

    private void setInfPayment(IPayment payment) {
        CreditCardPayment paymentType = (CreditCardPayment) payment;

        this.etHolderName.setText(paymentType.getCardHolderName());
        this.etCardNumber.setText(paymentType.getCardNumber());
        this.etExpirationDate.setText(paymentType.getExpirationDate());
        this.etSecurityCode.setText(String.valueOf(paymentType.getSecurityCode()));
    }

    private void showModalAddPayment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.modal_delete_book, null);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        TextView tvDescription = dialogView.findViewById(R.id.description);
        Button buttonConfirm = dialogView.findViewById(R.id.button_delete);
        Button buttonCancel = dialogView.findViewById(R.id.button_cancel);

        tvDescription.setText("Tem certeza de que deseja adicionar/atualizar este pagamento?");
        buttonConfirm.setText("Confirmar");

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = countId();
                String cardNumber = etCardNumber.getText().toString();
                String holderName = etHolderName.getText().toString();
                String expirationDate = etExpirationDate.getText().toString();
                int securityCode = 000;
                if (!etSecurityCode.getText().toString().isEmpty()) {
                    securityCode = Integer.parseInt(etSecurityCode.getText().toString());
                }
                boolean isPaymentCredit = false;

                IPayment editPayment = getPaymentUser();
                if (editPayment != null) {
                    CreditCardPayment editedPayment = (CreditCardPayment) editPayment;
                    editedPayment.setCardNumber(cardNumber);
                    editedPayment.setCardHolderName(holderName);
                    editedPayment.setExpirationDate(expirationDate);
                    editedPayment.setSecurityCode(securityCode);
                    editedPayment.setIsPaymentCredit(isPaymentCredit);

                    PaymentDAO.getInstance().editPayment(editedPayment);
                } else {
                    CreditCardPayment newPayment = new CreditCardPayment(id, cardNumber, holderName, expirationDate, securityCode, isPaymentCredit);

                    PaymentDAO.getInstance().addPayment(newPayment);
                    PaymentToUserDAO.getInstance().addPaymentToUser(id, userLogged.getId());
                }

                dialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}