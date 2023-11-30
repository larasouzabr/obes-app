package com.example.obes.dao.Wishlist;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.model.Book.Book;
import com.example.obes.model.Cart.ItemCart;
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

public class ItemWishlistDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("item_wishlist");
    private ArrayList<ItemWishlist> listItems;
    private static ItemWishlistDAO instance;

    private ItemWishlistDAO() {
        this.listItems = new ArrayList<ItemWishlist>();
    }

    public static ItemWishlistDAO getInstance() {
        if (instance == null) {
            instance = new ItemWishlistDAO();
        }
        return instance;
    }

    public ArrayList<ItemWishlist> getListItems() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ItemWishlist> items = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int id = dataSnapshot.child("id").getValue(int.class);

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

                    Book item = new Book(idBook, title, description, category, available, coverResourceId, author, price, condition);

                    ItemWishlist itemWishlist = new ItemWishlist(id, item);

                    items.add(itemWishlist);
                }

                listItems = items;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar itens das listas de desejos: " + error.getMessage());
            }
        });

        return this.listItems;
    }

    public boolean addItemWishlist(ItemWishlist item) {
        this.listItems.add(item);

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
        data.put("item", dataItem);

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Item adicionado na lista de desejos com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao adicionar o item na lista de desejos: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteItemWishlist(ItemWishlist item) {
        ItemWishlist itemDelete = null;
        boolean removed = false;

        for (ItemWishlist i : this.listItems) {
            if (i.getId() == item.getId()) {
                itemDelete = i;
                break;
            }
        }

        if (itemDelete != null) {
            this.listItems.remove(itemDelete);
            removed = true;
        }

        this.reference.child(String.valueOf(item.getId())).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Item removido da lista de desejos com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao remover o item da lista de desejos: " + task.getException().getMessage());
                }
            }
        });

        return removed;
    }

    public ItemWishlist getItemById(int idItem) {
        ItemWishlist itemWishlist = null;

        for (ItemWishlist item : this.listItems) {
            if (item.getId() == idItem) {
                itemWishlist = item;
                break;
            }
        }

        return itemWishlist;
    }

    public ItemWishlist getItemByIdBook(int idBook) {
        ItemWishlist itemWishlist = null;

        for (ItemWishlist item : this.listItems) {
            if (item.getItem().getId() == idBook) {
                itemWishlist = item;
                break;
            }
        }

        return itemWishlist;
    }
}