package com.example.obes.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.model.Book.Book;
import com.example.obes.model.Cart.Cart;
import com.example.obes.model.Cart.ItemCart;
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

public class ItemCartDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("item_cart");
    private ArrayList<ItemCart> listItensCart;

    private static ItemCartDAO instance;

    private ItemCartDAO() {
        this.listItensCart = new ArrayList<ItemCart>();
    }

    public static ItemCartDAO getInstance() {
        if (instance == null) {
            instance = new ItemCartDAO();
        }
        return instance;
    }

    public ArrayList<ItemCart> getListItensCart() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ItemCart> items = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int id = dataSnapshot.child("id").getValue(int.class);
                    int amount = dataSnapshot.child("amount").getValue(int.class);
                    boolean selected = dataSnapshot.child("selected").getValue(boolean.class);

                    DataSnapshot itemSnapshot = dataSnapshot.child("item");
                    int idBook = itemSnapshot.child("id").getValue(int.class);
                    String title = itemSnapshot.child("title").getValue(String.class);
                    String description = itemSnapshot.child("description").getValue(String.class);
                    String category = itemSnapshot.child("category").getValue(String.class);
                    boolean available = Boolean.TRUE.equals(itemSnapshot.child("available").getValue(boolean.class));
                    int coverResourceId = itemSnapshot.child("coverResourceId").getValue(int.class);
                    String author = itemSnapshot.child("author").getValue(String.class);
                    double price = itemSnapshot.child("price").getValue(double.class);
                    String condition = itemSnapshot.child("condition").getValue(String.class);

                    Book item = new Book(idBook, title, description, category, available, coverResourceId, author, price, condition);

                    ItemCart itemCart = new ItemCart(id, amount, item);
                    itemCart.setSelected(selected);

                    items.add(itemCart);
                }

                listItensCart = items;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar itens: " + error.getMessage());
            }
        });

        return this.listItensCart;
    }

    public boolean addItemCart(ItemCart item) {
        listItensCart.add(item);

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
        data.put("item", dataItem);
        data.put("selected", item.getSelected());

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Item adicionado ao carrinho com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao adicionar o item no carrinho: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteItemCart(ItemCart item) {
        ItemCart itemDelete = null;
        boolean removed = false;

        for (ItemCart i : listItensCart) {
            if (i.getId() == item.getId()) {
                itemDelete = i;
                break;
            }
        }

        if (itemDelete != null) {
            listItensCart.remove(itemDelete);
            removed = true;
        }

        this.reference.child(String.valueOf(item.getId())).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Item removido do carrinho com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao remover o item do carrinho: " + task.getException().getMessage());
                }
            }
        });

        return removed;
    }

    public boolean editItemCart(ItemCart item) {
        ItemCart itemEdited = null;
        boolean edited = false;

        for (ItemCart i : this.listItensCart) {
            if (i.getId() == item.getId()) {
                itemEdited = i;
                break;
            }
        }

        if (itemEdited != null) {
            itemEdited.setSelected(item.getSelected());

            edited = true;
        }

        DatabaseReference itemReference = this.reference.child(String.valueOf(item.getId()));

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

        Map<String, Object> itemData = new HashMap<>();
        itemData.put("id", item.getId());
        itemData.put("amount", item.getAmount());
        itemData.put("item", dataItem);
        itemData.put("selected", item.getSelected());

        itemReference.setValue(itemData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Item editado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao editar o item: " + task.getException().getMessage());
                }
            }
        });

        return edited;
    }

    public ItemCart getItemById(int idItem) {
        ItemCart itemCart = null;

        for (ItemCart item : this.listItensCart) {
            if (item.getId() == idItem) {
                itemCart = item;
                break;
            }
        }

        return itemCart;
    }

    public ItemCart getItemByIdBook(int idBook) {
        ItemCart itemCart = null;

        for (ItemCart item : this.listItensCart) {
            if (item.getItem().getId() == idBook) {
                itemCart = item;
                break;
            }
        }

        return itemCart;
    }
}