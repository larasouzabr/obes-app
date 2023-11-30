package com.example.obes.dao.Request;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.dao.Wishlist.ItemWishlistDAO;
import com.example.obes.dao.Wishlist.WishlistToItemDAO;
import com.example.obes.model.Request.ItemRequest;
import com.example.obes.model.Request.Request;
import com.example.obes.model.Wishlist.ItemWishlist;
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

public class RequestToItemDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("request_to_item");
    private ArrayList<RequestToItem> listRequestItems;
    private static RequestToItemDAO instance;

    private RequestToItemDAO() {
        this.listRequestItems = new ArrayList<RequestToItem>();
    }

    public static RequestToItemDAO getInstance() {
        if (instance == null) {
            instance = new RequestToItemDAO();
        }
        return instance;
    }

    public ArrayList<RequestToItem> getListRequestItems() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<RequestToItem> requestItemList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int idRequest = dataSnapshot.child("idRequest").getValue(int.class);
                    int idItem = dataSnapshot.child("idItem").getValue(int.class);

                    RequestToItem requestToItem = new RequestToItem(idRequest, idItem);

                    requestItemList.add(requestToItem);
                }

                listRequestItems = requestItemList;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar itens dos pedidos: " + error.getMessage());
            }
        });

        return this.listRequestItems;
    }

    public ArrayList<ItemRequest> getItemsByIdRequest(int idRequest) {
        ItemRequestDAO itemRequestDAO = ItemRequestDAO.getInstance();

        ArrayList<ItemRequest> listItems = new ArrayList<ItemRequest>();

        for (RequestToItem requestToItem : this.listRequestItems) {
            if (requestToItem.getIdRequest() == idRequest) {
                listItems.add(itemRequestDAO.getItemById(requestToItem.getIdItem()));
            }
        }

        return listItems;
    }

    public int getIdRequestByIdItem(int idItem) {
        int idRequest = 0;

        for (RequestToItem ri : this.listRequestItems) {
            if (ri.getIdItem() == idItem) {
                idRequest = ri.getIdRequest();
                break;
            }
        }

        return idRequest;
    }

    public boolean addRequestItem(int idRequest, int idItem) {
        RequestToItem newRequestToItem = new RequestToItem(idRequest, idItem);

        this.listRequestItems.add(newRequestToItem);

        DatabaseReference childReference = this.reference.child(idRequest + "_" + idItem);

        Map<String, Object> data = new HashMap<>();
        data.put("idRequest", idRequest);
        data.put("idItem", idItem);

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Relação de pedido e item cadastrado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao cadastrar o relação de pedido e item: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteRequestItem(int idRequest, int idItem) {
        RequestToItem requestToItemDeleted = null;
        boolean deleted = false;

        for (RequestToItem ri : this.listRequestItems) {
            if (ri.getIdRequest() == idRequest) {
                if (ri.getIdItem() == idItem) {
                    requestToItemDeleted = ri;
                    break;
                }
            }
        }

        if (requestToItemDeleted != null) {
            this.listRequestItems.remove(requestToItemDeleted);
            deleted = true;
        }

        String requestItem = idRequest + "_" + idItem;
        this.reference.child(requestItem).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Relação de pedido e item removido com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao remover o relação de pedido e item: " + task.getException().getMessage());
                }
            }
        });

        return deleted;
    }

    class RequestToItem {
        private int idRequest;
        private int idItem;

        public RequestToItem(int idRequest, int idItem) {
            this.idRequest = idRequest;
            this.idItem = idItem;
        }

        public int getIdRequest() {
            return this.idRequest;
        }

        public int getIdItem() {
            return this.idItem;
        }
    }
}