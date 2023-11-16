package com.example.obes.dao.Payment;

import com.example.obes.model.Payment.BoletoPayment;
import com.example.obes.model.Payment.CreditCardPayment;
import com.example.obes.model.Payment.IPayment;
import com.example.obes.model.Payment.OnlinePayment;
import com.example.obes.model.Payment.PixPayment;

import java.util.ArrayList;

public class PaymentDAO {
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
        return this.listPayment;
    }

    public boolean addPayment(IPayment payment) {
        this.getListPayment().add(payment);
        return true;
    }

    public boolean deletePayment(IPayment payment) {
        IPayment paymentDelete = null;
        boolean removed = false;

        for (IPayment p : this.getListPayment()) {
            if (p.getId() == payment.getId()) {
                paymentDelete = p;
                break;
            }
        }

        if (paymentDelete != null) {
            this.getListPayment().remove(paymentDelete);
            removed = true;
        }

        return removed;
    }

    public boolean editPayment(IPayment payment) {
        IPayment paymentEdit = null;
        boolean edited = false;

        for (IPayment p : this.getListPayment()) {
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

        return edited;
    }

    public IPayment getPaymentById(int idPayment) {
        IPayment payment = null;

        for (IPayment p : this.getListPayment()) {
            if (p.getId() == idPayment) {
                payment = p;
                break;
            }
        }

        return payment;
    }
}