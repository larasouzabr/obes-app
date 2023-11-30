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

public class CartToUserDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("cart_to_user");
    private ArrayList<CartToUser> listCartUser;

    private static CartToUserDAO instance;

    private CartToUserDAO() {
        this.listCartUser = new ArrayList<CartToUser>();
    }

    public static CartToUserDAO getInstance() {
        if (instance == null) {
            instance = new CartToUserDAO();
        }
        return instance;
    }

    public ArrayList<CartToUser> getListCartUser() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<CartToUser> cartUserList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int idCart = dataSnapshot.child("idCart").getValue(int.class);
                    int idUser = dataSnapshot.child("idUser").getValue(int.class);

                    CartToUser cartToUser = new CartToUser(idCart, idUser);

                    cartUserList.add(cartToUser);
                }

                listCartUser = cartUserList;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar carrrinhos do usuário: " + error.getMessage());
            }
        });

        return this.listCartUser;
    }

    public Cart getCartByIdUser(int idUser) {
        CartDAO cartDAO = CartDAO.getInstance();

        Cart cartUser = null;

        for (CartToUser cartToUser : this.listCartUser) {
            if (cartToUser.getIdUser() == idUser) {
                cartUser = cartDAO.getCartById(cartToUser.getIdCart());
                break;
            }
        }

        return cartUser;
    }

    public boolean addCartToUser(int idCart, int idUser) {
        CartToUser newCartToUser = new CartToUser(idCart, idUser);

        this.listCartUser.add(newCartToUser);

        DatabaseReference childReference = this.reference.child(idCart + "_" + idUser);

        Map<String, Object> data = new HashMap<>();
        data.put("idCart", idCart);
        data.put("idUser", idUser);

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Relação de carrinho e usuário cadastrado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao cadastrar o relação de carrinho e usuário: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteCartToUser(int idUser) {
        for (CartToUser cartToUser : this.listCartUser) {
            if (cartToUser.getIdUser() == idUser) {
                this.listCartUser.remove(cartToUser);
                return true;
            }
        }

        return false;
    }

    class CartToUser {
        private int idCart;
        private int idUser;

        public CartToUser(int idCart, int idUser) {
            this.idCart = idCart;
            this.idUser = idUser;
        }

        public int getIdCart() {
            return this.idCart;
        }

        public int getIdUser() {
            return this.idUser;
        }
    }
}