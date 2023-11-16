package com.example.obes.payment.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.obes.R;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.dao.Payment.PaymentDAO;
import com.example.obes.dao.Payment.PaymentToUserDAO;
import com.example.obes.model.Payment.CreditCardPayment;
import com.example.obes.model.Payment.IPayment;
import com.example.obes.model.Payment.PixPayment;
import com.example.obes.model.User.User;

import java.security.SecureRandom;
import java.util.ArrayList;

public class Pix extends Fragment {
    private TextView tvPixKey;
    private Button buttonSave;
    private User userLogged;
    private static final String ALPHANUMERIC_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int PIX_KEY_LENGTH = 20;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pix, container, false);

        this.startComponents(view);

        this.userLogged = this.getUserLogged();

        this.tvPixKey.setText(this.generatePixKey());

        this.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showModalAddPayment();
            }
        });

        return view;
    }

    private void startComponents(View view) {
        this.tvPixKey = view.findViewById(R.id.pix_key);
        this.buttonSave = view.findViewById(R.id.button_save);
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

    private IPayment getPaymentUser() {
        ArrayList<IPayment> paymentsUser = PaymentToUserDAO.getInstance().getPaymentsByIdUser(userLogged.getId());
        IPayment paymentUser = null;

        for (IPayment p : paymentsUser) {
            if (p instanceof PixPayment) {
                paymentUser = p;
            }
        }

        return paymentUser;
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
                String pixKey = tvPixKey.getText().toString();

                IPayment editPayment = getPaymentUser();
                if (editPayment != null) {
                    PixPayment editedPayment = (PixPayment) editPayment;
                    editedPayment.setPixKey(pixKey);

                    PaymentDAO.getInstance().editPayment(editedPayment);
                } else {
                    PixPayment newPayment = new PixPayment(id, pixKey);

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

    private String generatePixKey() {
        SecureRandom random = new SecureRandom();
        StringBuilder pixKey = new StringBuilder();

        for (int i = 0; i < PIX_KEY_LENGTH; i++) {
            int randomIndex = random.nextInt(ALPHANUMERIC_CHARS.length());
            char randomChar = ALPHANUMERIC_CHARS.charAt(randomIndex);
            pixKey.append(randomChar);
        }

        return pixKey.toString();
    }
}