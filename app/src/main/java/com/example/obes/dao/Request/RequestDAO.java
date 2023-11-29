package com.example.obes.dao.Request;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.dao.Wishlist.WishlistDAO;
import com.example.obes.model.Request.Request;
import com.example.obes.model.Review.Review;
import com.example.obes.model.Wishlist.Wishlist;
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

public class RequestDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("request");
    private ArrayList<Request> listRequests;
    private static RequestDAO instance;

    private RequestDAO() {
        this.listRequests = new ArrayList<Request>();
    }

    public static RequestDAO getInstance() {
        if (instance == null) {
            instance = new RequestDAO();
        }
        return instance;
    }

    public ArrayList<Request> getListRequests() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Request> requests = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int id = dataSnapshot.child("id").getValue(int.class);
                    String date = dataSnapshot.child("date").getValue(String.class);
                    String status = dataSnapshot.child("status").getValue(String.class);

                    Request request = new Request(id, date, status);

                    requests.add(request);
                }

                listRequests = requests;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar pedidos: " + error.getMessage());
            }
        });

        return this.listRequests;
    }

    public Request getRequestById(int idRequest) {
        Request request = null;

        for (Request r : this.listRequests) {
            if (r.getId() == idRequest) {
                request = r;
            }
        }

        return request;
    }

    public boolean editRequest(Request request) {
        Request requestEdit = null;
        boolean edited = false;

        for (Request r : this.listRequests) {
            if (r.getId() == request.getId()) {
                requestEdit = r;
                break;
            }
        }

        if (requestEdit != null) {
            requestEdit.setStatus(request.getStatus());

            edited = true;
        }

        DatabaseReference requestReference = this.reference.child(String.valueOf(request.getId()));

        Map<String, Object> data = new HashMap<>();
        data.put("id", request.getId());
        data.put("date", request.getDate());
        data.put("status", request.getStatus());

        requestReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Pedido editado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao editar o pedido: " + task.getException().getMessage());
                }
            }
        });

        return edited;
    }

    public boolean addRequest(Request request) {
        this.listRequests.add(request);

        DatabaseReference childReference = this.reference.child(String.valueOf(request.getId()));

        Map<String, Object> data = new HashMap<>();
        data.put("id", request.getId());
        data.put("date", request.getDate());
        data.put("status", request.getStatus());

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Pedido realizado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao realizar o pedido: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteRequest(Request request) {
        Request requestDelete = null;
        boolean removed = false;

        for (Request r : this.listRequests) {
            if (r.getId() == request.getId()) {
                requestDelete = r;
                break;
            }
        }

        if (requestDelete != null) {
            this.listRequests.remove(request);
            removed = true;
        }

        this.reference.child(String.valueOf(request.getId())).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Pedido removido com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao remover o pedido: " + task.getException().getMessage());
                }
            }
        });

        return removed;
    }
}