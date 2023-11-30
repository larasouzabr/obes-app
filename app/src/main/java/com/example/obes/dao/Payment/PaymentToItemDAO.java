package com.example.obes.dao.Payment;

import android.util.Log;

import androidx.annotation.NonNull;

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

public class PaymentToItemDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("payment_to_item");
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
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<PaymentToItem> paymentItemList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int idPayment = dataSnapshot.child("idPayment").getValue(int.class);
                    int idItem = dataSnapshot.child("idItem").getValue(int.class);

                    PaymentToItem paymentToItem = new PaymentToItem(idPayment, idItem);

                    paymentItemList.add(paymentToItem);
                }

                listPaymentToItem = paymentItemList;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar pagamentos do item: " + error.getMessage());
            }
        });

        return this.listPaymentToItem;
    }

    public boolean addPaymentToItem(int idPayment, int idItem) {
        PaymentToItem newPaymentToItem = new PaymentToItem(idPayment, idItem);

        this.listPaymentToItem.add(newPaymentToItem);

        DatabaseReference childReference = this.reference.child(idPayment + "_" + idItem);

        Map<String, Object> data = new HashMap<>();
        data.put("idPayment", idPayment);
        data.put("idItem", idItem);

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Relação de pagamento e item cadastrado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao cadastrar o relação de pagamento e item: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deletePaymentToItem(int idItem) {
        for (PaymentToItem pi : this.listPaymentToItem) {
            if (pi.getIdItem() == idItem) {
                this.listPaymentToItem.remove(pi);
                return true;
            }
        }

        return false;
    }

    public int getIdPaymentByIdItem(int idItem) {
        for (PaymentToItem pi : this.listPaymentToItem) {
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