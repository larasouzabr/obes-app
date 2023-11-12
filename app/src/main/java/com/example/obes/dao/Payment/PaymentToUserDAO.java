package com.example.obes.dao.Payment;

import com.example.obes.dao.UserCommonDAO;
import com.example.obes.dao.UserInstitutionalDAO;
import com.example.obes.model.Payment.IPayment;
import com.example.obes.model.User.User;

import java.util.ArrayList;

public class PaymentToUserDAO {
    private ArrayList<PaymentToUser> listPaymentToUser;
    private static PaymentToUserDAO instance;

    private PaymentToUserDAO() {
        this.listPaymentToUser = new ArrayList<PaymentToUser>();
    }

    public static PaymentToUserDAO getInstance() {
        if (instance == null) {
            instance = new PaymentToUserDAO();
        }
        return instance;
    }

    public ArrayList<PaymentToUser> getListPaymentToUser() {
        return this.listPaymentToUser;
    }

    public boolean addPaymentToUser(int idPayment, int idUser) {
        PaymentToUser newPaymentToUser = new PaymentToUser(idPayment, idUser);

        this.getListPaymentToUser().add(newPaymentToUser);

        return true;
    }

    public boolean deletePaymentToUser(int idPayment) {
        for (PaymentToUser pu : this.getListPaymentToUser()) {
            if (pu.getIdPayment() == idPayment) {
                this.getListPaymentToUser().remove(pu);
                return true;
            }
        }

        return false;
    }

    public int getIdUserByIdPayment(int idPayment) {
        for (PaymentToUser pu : this.getListPaymentToUser()) {
            if (pu.getIdPayment() == idPayment) {
                User user = UserCommonDAO.getInstance().getUserById(pu.getIdUser());
                if (user == null) {
                    user = UserInstitutionalDAO.getInstance().getUserById(pu.getIdUser());
                }

                return user.getId();
            }
        }

        return 0;
    }

    public ArrayList<IPayment> getPaymentsByIdUser(int idUser) {
        ArrayList<IPayment> payments = new ArrayList<IPayment>();

        for (PaymentToUser pu : this.getListPaymentToUser()) {
            if (pu.getIdUser() == idUser) {
                IPayment payment = PaymentDAO.getInstance().getPaymentById(pu.getIdPayment());
                payments.add(payment);
            }
        }

        return payments;
    }

    class PaymentToUser {
        private int idPayment;
        private int idUser;

        public PaymentToUser(int idPayment, int idUser) {
            this.idPayment = idPayment;
            this.idUser = idUser;
        }

        public int getIdPayment() {
            return this.idPayment;
        }

        public int getIdUser() {
            return this.idUser;
        }
    }
}