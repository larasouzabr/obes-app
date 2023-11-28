package com.example.obes.dao.Wishlist;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.model.Wishlist.Wishlist;
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

public class WishlistDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("wishlist");
    private ArrayList<Wishlist> wishlists;
    private static WishlistDAO instance;

    private WishlistDAO() {
        this.wishlists = new ArrayList<Wishlist>();
    }

    public static WishlistDAO getInstance() {
        if (instance == null) {
            instance = new WishlistDAO();
        }
        return instance;
    }

    public ArrayList<Wishlist> getWishlists() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Wishlist> wishs = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int id = dataSnapshot.child("id").getValue(int.class);

                    Wishlist wishlist = new Wishlist(id);

                    wishs.add(wishlist);
                }

                wishlists = wishs;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar listas de desejos: " + error.getMessage());
            }
        });

        return this.wishlists;
    }

    public Wishlist getWishlistById(int idWishlist) {
        Wishlist wishlist = null;

        for (Wishlist w : this.wishlists) {
            if (w.getId() == idWishlist) {
                wishlist = w;
            }
        }

        return wishlist;
    }

    public boolean addWishlist(Wishlist wishlist) {
        this.wishlists.add(wishlist);

        DatabaseReference childReference = this.reference.child(String.valueOf(wishlist.getId()));

        Map<String, Object> data = new HashMap<>();
        data.put("id", wishlist.getId());

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Lista de desejos cadastrado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao cadastrar a lista de desejos: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteWishlist(Wishlist wishlist) {
        Wishlist wishlistDelete = null;
        boolean removed = false;

        for (Wishlist w : this.wishlists) {
            if (w.getId() == wishlist.getId()) {
                wishlistDelete = w;
                break;
            }
        }

        if (wishlistDelete != null) {
            this.wishlists.remove(wishlist);
            removed = true;
        }

        return removed;
    }
}