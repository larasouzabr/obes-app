package com.example.obes.dao.Request;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.dao.Wishlist.ItemWishlistDAO;
import com.example.obes.model.Book.Book;
import com.example.obes.model.Request.ItemRequest;
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

public class ItemRequestDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("item_request");
    private ArrayList<ItemRequest> listItemRequests;
    private static ItemRequestDAO instance;

    private ItemRequestDAO() {
        this.listItemRequests = new ArrayList<ItemRequest>();
    }

    public static ItemRequestDAO getInstance() {
        if (instance == null) {
            instance = new ItemRequestDAO();
        }
        return instance;
    }

    public ArrayList<ItemRequest> getListItemRequests() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ItemRequest> items = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int id = dataSnapshot.child("id").getValue(int.class);
                    int amount = dataSnapshot.child("amount").getValue(int.class);

                    DataSnapshot itemSnapshot = dataSnapshot.child("item");
                    int idBook = itemSnapshot.child("id").getValue(int.class);
                    String title = itemSnapshot.child("title").getValue(String.class);
                    String description = itemSnapshot.child("description").getValue(String.class);
                    String category = itemSnapshot.child("category").getValue(String.class);
                    boolean available = Boolean.TRUE.equals(itemSnapshot.child("available").getValue(boolean.class));
                    String coverResourceId = itemSnapshot.child("coverResourceId").getValue(String.class);
                    String author = itemSnapshot.child("author").getValue(String.class);
                    double price = itemSnapshot.child("price").getValue(double.class);
                    String condition = itemSnapshot.child("condition").getValue(String.class);

                    String status = dataSnapshot.child("status").getValue(String.class);;

                    Book item = new Book(idBook, title, description, category, available, coverResourceId, author, price, condition);

                    ItemRequest itemRequest = new ItemRequest(id, amount, item, status);

                    items.add(itemRequest);
                }

                listItemRequests = items;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar itens dos pedidos: " + error.getMessage());
            }
        });

        return this.listItemRequests;
    }

    public boolean addItemRequest(ItemRequest item) {
        this.listItemRequests.add(item);

        DatabaseReference childReference = this.reference.child(String.valueOf(item.getId()));

        Map<String, Object> dataItem = new HashMap<>();
        dataItem.put("id", item.getItem().getId());
        dataItem.put("title", item.getItem().getTitle());
        dataItem.put("description", item.getItem().getDescription());
        dataItem.put("category", item.getItem().getCategory());
        dataItem.put("available", item.getItem().isAvailable());
        dataItem.put("coverResourceId", item.getItem().getCoverResourceId());
        dataItem.put("author", item.getItem().getAuthor());
        dataItem.put("price", item.getItem().getPrice());
        dataItem.put("condition", item.getItem().getCondition());

        Map<String, Object> data = new HashMap<>();
        data.put("id", item.getId());
        data.put("amount", item.getAmount());
        data.put("price", item.getPrice());
        data.put("item", dataItem);
        data.put("status", item.getStatus());

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Item adicionado ao pedido com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao adicionar o item no pedido: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteItemRequest(ItemRequest item) {
        ItemRequest itemDelete = null;
        boolean removed = false;

        for (ItemRequest i : this.listItemRequests) {
            if (i.getId() == item.getId()) {
                itemDelete = i;
                break;
            }
        }

        if (itemDelete != null) {
            this.listItemRequests.remove(itemDelete);
            removed = true;
        }

        this.reference.child(String.valueOf(item.getId())).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Item removido do pedido com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao remover o item do pedido: " + task.getException().getMessage());
                }
            }
        });

        return removed;
    }

    public boolean editItemRequest(ItemRequest item) {
        ItemRequest itemEdited = null;
        boolean edited = false;

        for (ItemRequest i : this.listItemRequests) {
            if (i.getId() == item.getId()) {
                itemEdited = i;
                break;
            }
        }

        if (itemEdited != null) {
            itemEdited.setStatus(item.getStatus());

            edited = true;
        }

        DatabaseReference itemRequestReference = this.reference.child(String.valueOf(item.getId()));

        Map<String, Object> dataItem = new HashMap<>();
        dataItem.put("id", item.getItem().getId());
        dataItem.put("title", item.getItem().getTitle());
        dataItem.put("description", item.getItem().getDescription());
        dataItem.put("category", item.getItem().getCategory());
        dataItem.put("available", item.getItem().isAvailable());
        dataItem.put("coverResourceId", item.getItem().getCoverResourceId());
        dataItem.put("author", item.getItem().getAuthor());
        dataItem.put("price", item.getItem().getPrice());
        dataItem.put("condition", item.getItem().getCondition());

        Map<String, Object> data = new HashMap<>();
        data.put("id", item.getId());
        data.put("amount", item.getAmount());
        data.put("price", item.getPrice());
        data.put("item", dataItem);
        data.put("status", item.getStatus());

        itemRequestReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public ItemRequest getItemById(int idItem) {
        ItemRequest itemRequest = null;

        for (ItemRequest item : this.listItemRequests) {
            if (item.getId() == idItem) {
                itemRequest = item;
                break;
            }
        }

        return itemRequest;
    }

    public ItemRequest getItemByIdBook(int idBook) {
        ItemRequest itemRequest = null;

        for (ItemRequest item : this.listItemRequests) {
            if (item.getItem().getId() == idBook) {
                itemRequest = item;
                break;
            }
        }

        return itemRequest;
    }
}