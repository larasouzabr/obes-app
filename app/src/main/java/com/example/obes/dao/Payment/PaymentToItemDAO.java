package com.example.obes.dao.Payment;

import java.util.ArrayList;

public class PaymentToItemDAO {
    private ArrayList<PaymentToItem> listPaymentToItem;
    private static PaymentToItemDAO instance;

    private PaymentToItemDAO() {
        this.listPaymentToItem = new ArrayList<PaymentToItem>();
    }

    public static PaymentToItemDAO getInstance() {
        if (instance == null) {
            instance = new PaymentToItemDAO();
        }
        return instance;
    }

    public ArrayList<PaymentToItem> getListPaymentToItem() {
        return this.listPaymentToItem;
    }

    public boolean addPaymentToItem(int idPayment, int idItem) {
        PaymentToItem newPaymentToItem = new PaymentToItem(idPayment, idItem);

        this.getListPaymentToItem().add(newPaymentToItem);

        return true;
    }

    public boolean deletePaymentToItem(int idItem) {
        for (PaymentToItem pi : this.getListPaymentToItem()) {
            if (pi.getIdItem() == idItem) {
                this.getListPaymentToItem().remove(pi);
                return true;
            }
        }

        return false;
    }

    public int getIdPaymentByIdItem(int idItem) {
        for (PaymentToItem pi : this.getListPaymentToItem()) {
            if (pi.getIdItem() == idItem) {
                return pi.getIdPayment();
            }
        }

        return 0;
    }

    class PaymentToItem {
        private int idPayment;
        private int idItem;

        public PaymentToItem(int idPayment, int idItem) {
            this.idPayment = idPayment;
            this.idItem = idItem;
        }

        public int getIdPayment() {
            return this.idPayment;
        }

        public int getIdItem() {
            return this.idItem;
        }
    }
}