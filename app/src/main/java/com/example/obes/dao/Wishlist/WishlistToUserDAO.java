package com.example.obes.dao.Wishlist;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.dao.CartToUserDAO;
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

public class WishlistToUserDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("wishlist_to_user");
    private ArrayList<WishToUser> listWishUser;
    private static WishlistToUserDAO instance;

    private WishlistToUserDAO() {
        this.listWishUser = new ArrayList<WishToUser>();
    }

    public static WishlistToUserDAO getInstance() {
        if (instance == null) {
            instance = new WishlistToUserDAO();
        }
        return instance;
    }

    public ArrayList<WishToUser> getListWishUser() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<WishToUser> wishUserList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int idWish = dataSnapshot.child("idWish").getValue(int.class);
                    int idUser = dataSnapshot.child("idUser").getValue(int.class);

                    WishToUser wishToUser = new WishToUser(idWish, idUser);

                    wishUserList.add(wishToUser);
                }

                listWishUser = wishUserList;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar lista de desejos do usuário: " + error.getMessage());
            }
        });

        return this.listWishUser;
    }

    public Wishlist getWishByIdUser(int idUser) {
        WishlistDAO wishlistDAO = WishlistDAO.getInstance();

        Wishlist wishUser = null;

        for (WishToUser wishToUser : this.listWishUser) {
            if (wishToUser.getIdUser() == idUser) {
                wishUser = wishlistDAO.getWishlistById(wishToUser.getIdWish());
                break;
            }
        }

        return wishUser;
    }

    public boolean addWishToUser(int idWish, int idUser) {
        WishToUser newWishToUser = new WishToUser(idWish, idUser);

        this.listWishUser.add(newWishToUser);

        DatabaseReference childReference = this.reference.child(idWish + "_" + idUser);

        Map<String, Object> data = new HashMap<>();
        data.put("idWish", idWish);
        data.put("idUser", idUser);

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Relação de lista de desejos e usuário cadastrado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao cadastrar o relação de lista de desejos e usuário: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteWishToUser(int idUser) {
        for (WishlistToUserDAO.WishToUser wishToUser : this.listWishUser) {
            if (wishToUser.getIdUser() == idUser) {
                this.listWishUser.remove(wishToUser);
                return true;
            }
        }

        return false;
    }

    class WishToUser {
        private int idWish;
        private int idUser;

        public WishToUser(int idWish, int idUser) {
            this.idWish = idWish;
            this.idUser = idUser;
        }

        public int getIdWish() {
            return this.idWish;
        }

        public int getIdUser() {
            return this.idUser;
        }
    }
}