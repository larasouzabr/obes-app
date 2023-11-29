package com.example.obes.dao.Request;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.model.Request.ItemRequest;
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

public class OrderItemDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("order_to_item");
    private ArrayList<ItemToUser> listItemsUser;
    private static OrderItemDAO instance;

    private OrderItemDAO() {
        this.listItemsUser = new ArrayList<ItemToUser>();
    }

    public static OrderItemDAO getInstance() {
        if (instance == null) {
            instance = new OrderItemDAO();
        }
        return instance;
    }

    public ArrayList<ItemToUser> getListItemsUser() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ItemToUser> itemUserList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int idItem = dataSnapshot.child("idItem").getValue(int.class);
                    int idUser = dataSnapshot.child("idUser").getValue(int.class);

                    ItemToUser itemToUser = new ItemToUser(idItem, idUser);

                    itemUserList.add(itemToUser);
                }

                listItemsUser = itemUserList;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar relações de itens e usuários: " + error.getMessage());
            }
        });

        return this.listItemsUser;
    }

    public boolean addItemToUser(int idItem, int idUser) {
        ItemToUser newItemToUser = new ItemToUser(idItem, idUser);

        this.listItemsUser.add(newItemToUser);

        DatabaseReference childReference = this.reference.child(idItem + "_" + idUser);

        Map<String, Object> data = new HashMap<>();
        data.put("idItem", idItem);
        data.put("idUser", idUser);

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Relação de item e usuário cadastrado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao cadastrar o relação de item e usuário: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteItemToUser(int idItem, int idUser) {
        for (ItemToUser iu : this.listItemsUser) {
            if (iu.getIdItem() == idItem) {
                if (iu.getIdUser() == idUser) {
                    this.listItemsUser.remove(iu);
                    return true;
                }
            }
        }

        String itemUser = idItem + "_" + idUser;
        this.reference.child(itemUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Relação de item e usuário removida com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao remover relação de item e usuário: " + task.getException().getMessage());
                }
            }
        });

        return false;
    }

    public int getIdUserByIdItem(int idItem) {
        for (ItemToUser iu : this.listItemsUser) {
            if (iu.getIdItem() == idItem) {
                return iu.getIdUser();
            }
        }

        return 0;
    }

    public ArrayList<ItemRequest> getItemsByIdUser(int idUser) {
        ArrayList<ItemRequest> itemsUser = new ArrayList<ItemRequest>();

        ItemRequestDAO itemRequestDAO = ItemRequestDAO.getInstance();

        for (ItemToUser iu : this.listItemsUser) {
            if (iu.getIdUser() == idUser) {
                itemsUser.add(itemRequestDAO.getItemById(iu.getIdItem()));
            }
        }

        return itemsUser;
    }

    class ItemToUser {
        private int idItem;
        private int idUser;

        public ItemToUser(int idItem, int idUser) {
            this.idItem = idItem;
            this.idUser = idUser;
        }

        public int getIdItem() {
            return this.idItem;
        }

        public int getIdUser() {
            return this.idUser;
        }
    }
}
