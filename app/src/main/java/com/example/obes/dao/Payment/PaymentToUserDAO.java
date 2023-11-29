package com.example.obes.dao.Payment;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.dao.CartToUserDAO;
import com.example.obes.dao.UserCommonDAO;
import com.example.obes.dao.UserInstitutionalDAO;
import com.example.obes.model.Payment.IPayment;
import com.example.obes.model.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentToUserDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("payment_to_user");
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
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<PaymentToUser> paymentUserList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int idPayment = dataSnapshot.child("idPayment").getValue(int.class);
                    int idUser = dataSnapshot.child("idUser").getValue(int.class);

                    PaymentToUser paymentToUser = new PaymentToUser(idPayment, idUser);

                    paymentUserList.add(paymentToUser);
                }

                listPaymentToUser = paymentUserList;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar pagamentos do usuário: " + error.getMessage());
            }
        });

        return this.listPaymentToUser;
    }

    public boolean addPaymentToUser(int idPayment, int idUser) {
        PaymentToUser newPaymentToUser = new PaymentToUser(idPayment, idUser);

        this.listPaymentToUser.add(newPaymentToUser);

        DatabaseReference childReference = this.reference.child(idPayment + "_" + idUser);

        Map<String, Object> data = new HashMap<>();
        data.put("idPayment", idPayment);
        data.put("idUser", idUser);

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Relação de pagamento e usuário cadastrado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao cadastrar o relação de pagamento e usuário: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deletePaymentToUser(int idPayment) {
        for (PaymentToUser pu : this.listPaymentToUser) {
            if (pu.getIdPayment() == idPayment) {
                this.listPaymentToUser.remove(pu);
                return true;
            }
        }

        return false;
    }

    public int getIdUserByIdPayment(int idPayment) {
        for (PaymentToUser pu : this.listPaymentToUser) {
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

        for (PaymentToUser pu : this.listPaymentToUser) {
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