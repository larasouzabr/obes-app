package com.example.obes.dao.Payment;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.model.Payment.BoletoPayment;
import com.example.obes.model.Payment.CreditCardPayment;
import com.example.obes.model.Payment.IPayment;
import com.example.obes.model.Payment.OnlinePayment;
import com.example.obes.model.Payment.PixPayment;
import com.example.obes.model.Review.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("payment");
    private ArrayList<IPayment> listPayment;
    private static PaymentDAO instance;

    private PaymentDAO() {
        this.listPayment = new ArrayList<IPayment>();
    }

    public static PaymentDAO getInstance() {
        if (instance == null) {
            instance = new PaymentDAO();
        }
        return instance;
    }

    public ArrayList<IPayment> getListPayment() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<IPayment> payments = new ArrayList<>();

                for (DataSnapshot paymentSnapshot : snapshot.getChildren()) {
                    if (paymentSnapshot.hasChild("pixKey")) {
                        int id = paymentSnapshot.child("id").getValue(int.class);
                        String pixKey = paymentSnapshot.child("pixKey").getValue(String.class);

                        PixPayment pixPayment = new PixPayment(id, pixKey);

                        payments.add(pixPayment);
                    } else if (paymentSnapshot.hasChild("cardNumber")) {
                        int id = paymentSnapshot.child("id").getValue(int.class);
                        String cardNumber = paymentSnapshot.child("cardNumber").getValue(String.class);
                        String cardHolderName = paymentSnapshot.child("cardHolderName").getValue(String.class);
                        String expirationDate = paymentSnapshot.child("expirationDate").getValue(String.class);
                        int securityCode = paymentSnapshot.child("securityCode").getValue(int.class);
                        boolean isPaymentCredit = paymentSnapshot.child("isPaymentCredit").getValue(boolean.class);

                        CreditCardPayment creditCardPayment = new CreditCardPayment(id, cardNumber, cardHolderName, expirationDate, securityCode, isPaymentCredit);

                        payments.add(creditCardPayment);
                    }
                }
                listPayment = payments;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar pagamentos: " + error.getMessage());
            }
        });

        return this.listPayment;
    }

    public boolean addPayment(IPayment payment) {
        this.listPayment.add(payment);

        DatabaseReference childReference = this.reference.child(String.valueOf(payment.getId()));

        Map<String, Object> data = new HashMap<>();
        if (payment instanceof CreditCardPayment) {
            CreditCardPayment credit = (CreditCardPayment) payment;
            data.put("id", credit.getId());
            data.put("cardNumber", credit.getCardNumber());
            data.put("cardHolderName", credit.getCardHolderName());
            data.put("expirationDate", credit.getExpirationDate());
            data.put("securityCode", credit.getSecurityCode());
            data.put("isPaymentCredit", credit.getIsPaymentCredit());
        } else if (payment instanceof BoletoPayment) {
            BoletoPayment boleto = (BoletoPayment) payment;
            data.put("id", boleto.getId());
            data.put("barcode", boleto.getBarcode());
            data.put("dueDate", boleto.getDueDate());
        } else {
            PixPayment pix = (PixPayment) payment;
            data.put("id", pix.getId());
            data.put("pixKey", pix.getPixKey());
        }

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Pagamento cadastrado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao cadastrar o pagamento: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deletePayment(IPayment payment) {
        IPayment paymentDelete = null;
        boolean removed = false;

        for (IPayment p : this.listPayment) {
            if (p.getId() == payment.getId()) {
                paymentDelete = p;
                break;
            }
        }

        if (paymentDelete != null) {
            this.listPayment.remove(paymentDelete);
            removed = true;
        }

        return removed;
    }

    public boolean editPayment(IPayment payment) {
        IPayment paymentEdit = null;
        boolean edited = false;

        for (IPayment p : this.listPayment) {
            if (p.getId() == payment.getId()) {
                paymentEdit = p;
                break;
            }
        }

        if (paymentEdit != null) {
            if (paymentEdit instanceof CreditCardPayment) {
                CreditCardPayment creditCardEdit = (CreditCardPayment) paymentEdit;
                CreditCardPayment creditCard = (CreditCardPayment) payment;

                creditCardEdit.setCardNumber(creditCard.getCardNumber());
                creditCardEdit.setCardHolderName(creditCard.getCardHolderName());
                creditCardEdit.setExpirationDate(creditCard.getExpirationDate());
                creditCardEdit.setSecurityCode(creditCard.getSecurityCode());
                creditCardEdit.setIsPaymentCredit(creditCard.getIsPaymentCredit());
            } else if (paymentEdit instanceof BoletoPayment) {
                BoletoPayment boletoEdit = (BoletoPayment) paymentEdit;
                BoletoPayment boleto = (BoletoPayment) payment;

                boletoEdit.setBarcode(boleto.getBarcode());
                boletoEdit.setDueDate(boleto.getDueDate());
            } else {
                PixPayment pixEdit = (PixPayment) paymentEdit;
                PixPayment pix = (PixPayment) payment;

                pixEdit.setPixKey(pix.getPixKey());
            }

            edited = true;
        }

        DatabaseReference paymentReference = this.reference.child(String.valueOf(payment.getId()));

        Map<String, Object> data = new HashMap<>();
        if (payment instanceof CreditCardPayment) {
            CreditCardPayment credit = (CreditCardPayment) payment;
            data.put("id", credit.getId());
            data.put("cardNumber", credit.getCardNumber());
            data.put("cardHolderName", credit.getCardHolderName());
            data.put("expirationDate", credit.getExpirationDate());
            data.put("securityCode", credit.getSecurityCode());
            data.put("isPaymentCredit", credit.getIsPaymentCredit());
        } else if (payment instanceof BoletoPayment) {
            BoletoPayment boleto = (BoletoPayment) payment;
            data.put("id", boleto.getId());
            data.put("barcode", boleto.getBarcode());
            data.put("dueDate", boleto.getDueDate());
        } else {
            PixPayment pix = (PixPayment) payment;
            data.put("id", pix.getId());
            data.put("pixKey", pix.getPixKey());
        }

        paymentReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Pagamento editada com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao editar o pagamento: " + task.getException().getMessage());
                }
            }
        });

        return edited;
    }

    public IPayment getPaymentById(int idPayment) {
        IPayment payment = null;

        for (IPayment p : this.listPayment) {
            if (p.getId() == idPayment) {
                payment = p;
                break;
            }
        }

        return payment;
    }
}