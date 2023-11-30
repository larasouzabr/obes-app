package com.example.obes.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.model.Book.Book;
import com.example.obes.model.Cart.Cart;
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

public class CartDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("cart");
    private ArrayList<Cart> listCarts;
    private static CartDAO instance;

    private CartDAO() {
        this.listCarts = new ArrayList<Cart>();
    }

    public static CartDAO getInstance() {
        if (instance == null) {
            instance = new CartDAO();
        }
        return instance;
    }

    public ArrayList<Cart> getListCarts() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Cart> carts = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int id = dataSnapshot.child("id").getValue(int.class);

                    Cart cart = new Cart(id);

                    carts.add(cart);
                }

                listCarts = carts;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar usuários: " + error.getMessage());
            }
        });

        return this.listCarts;
    }

    public Cart getCartById(int idCart) {
        Cart cart = null;

        for (Cart c : this.listCarts) {
            if (c.getId() == idCart) {
                cart = c;
            }
        }

        return cart;
    }

    public boolean addCart(Cart cart) {
        this.listCarts.add(cart);

        DatabaseReference childReference = this.reference.child(String.valueOf(cart.getId()));

        Map<String, Object> data = new HashMap<>();
        data.put("id", cart.getId());

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Carrinho cadastrado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao cadastrar o carrinho: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteCart(Cart cart) {
        Cart cartDelete = null;
        boolean removed = false;

        for (Cart c : listCarts) {
            if (c.getId() == cart.getId()) {
                cartDelete = c;
                break;
            }
        }

        if (cartDelete != null) {
            listCarts.remove(cartDelete);
            removed = true;
        }

        this.reference.child(String.valueOf(cart.getId())).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Carrinho removido com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao remover o carrinho: " + task.getException().getMessage());
                }
            }
        });

        return removed;
    }
}