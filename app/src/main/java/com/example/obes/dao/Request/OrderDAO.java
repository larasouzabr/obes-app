package com.example.obes.dao.Request;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.model.Request.Request;
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

public class OrderDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("order");
    private ArrayList<RequestToUser> listRequestUser;
    private static OrderDAO instance;

    private OrderDAO() {
        this.listRequestUser = new ArrayList<RequestToUser>();
    }

    public static OrderDAO getInstance() {
        if (instance == null) {
            instance = new OrderDAO();
        }
        return instance;
    }

    public ArrayList<RequestToUser> getListRequestUser() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<RequestToUser> requestUserList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int idRequest = dataSnapshot.child("idRequest").getValue(int.class);
                    int idUser = dataSnapshot.child("idUser").getValue(int.class);

                    RequestToUser requestToUser = new RequestToUser(idRequest, idUser);

                    requestUserList.add(requestToUser);
                }

                listRequestUser = requestUserList;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar relações de pedido e usuários: " + error.getMessage());
            }
        });

        return this.listRequestUser;
    }

    public ArrayList<Request> getRequestsByIdUser(int idUser) {
        RequestDAO requestDAO = RequestDAO.getInstance();

        ArrayList<Request> requestsUser = new ArrayList<Request>();

        for (RequestToUser requestToUser : this.listRequestUser) {
            if (requestToUser.getIdUser() == idUser) {
                requestsUser.add(requestDAO.getRequestById(requestToUser.getIdRequest()));
            }
        }

        return requestsUser;
    }

    public int getIdUserByIdRequest(int idRequest) {
        for (RequestToUser ru : this.listRequestUser) {
            if (ru.getIdRequest() == idRequest) {
                return ru.getIdUser();
            }
        }
        return 0;
    }

    public boolean addRequestToUser(int idRequest, int idUser) {
        RequestToUser newRequestToUser = new RequestToUser(idRequest, idUser);

        this.listRequestUser.add(newRequestToUser);

        DatabaseReference childReference = this.reference.child(idRequest + "_" + idUser);

        Map<String, Object> data = new HashMap<>();
        data.put("idRequest", idRequest);
        data.put("idUser", idUser);

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Relação de pedido e usuário cadastrado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao cadastrar o relação de pedido e usuário: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteRequestToUser(int idRequest, int idUser) {
        for (RequestToUser requestToUser : this.listRequestUser) {
            if (requestToUser.getIdUser() == idUser) {
                if (requestToUser.getIdRequest() == idRequest) {
                    this.listRequestUser.remove(requestToUser);
                    return true;
                }
            }
        }

        String requestUser = idRequest + "_" + idUser;
        this.reference.child(requestUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Relação de pedido e usuário removida com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao remover relação de pedido e usuário: " + task.getException().getMessage());
                }
            }
        });

        return false;
    }

    class RequestToUser {
        private int idRequest;
        private int idUser;

        public RequestToUser(int idRequest, int idUser) {
            this.idRequest = idRequest;
            this.idUser = idUser;
        }

        public int getIdRequest() {
            return this.idRequest;
        }

        public int getIdUser() {
            return this.idUser;
        }
    }
}