package com.example.obes.dao.Wishlist;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.dao.CartToItemDAO;
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

public class WishlistToItemDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("wishlist_to_item");
    private ArrayList<WishToItem> listWishItem;
    private static WishlistToItemDAO instance;

    private WishlistToItemDAO() {
        this.listWishItem = new ArrayList<WishToItem>();
    }

    public static WishlistToItemDAO getInstance() {
        if (instance == null) {
            instance = new WishlistToItemDAO();
        }
        return instance;
    }

    public ArrayList<WishToItem> getListWishItem() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<WishToItem> wishItemList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int idWish = dataSnapshot.child("idWish").getValue(int.class);
                    int idItem = dataSnapshot.child("idItem").getValue(int.class);

                    WishToItem wishToItem = new WishToItem(idWish, idItem);

                    wishItemList.add(wishToItem);
                }

                listWishItem = wishItemList;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar itens da lista de desejos: " + error.getMessage());
            }
        });

        return this.listWishItem;
    }

    public ArrayList<ItemWishlist> getItemsByIdWish(int idWish) {
        ItemWishlistDAO itemWishlistDAO = ItemWishlistDAO.getInstance();

        ArrayList<ItemWishlist> listItems = new ArrayList<ItemWishlist>();

        for (WishToItem wishToItem : this.listWishItem) {
            if (wishToItem.getIdWish() == idWish) {
                listItems.add(itemWishlistDAO.getItemById(wishToItem.getIdItem()));
            }
        }

        return listItems;
    }

    public int getIdWishByIdItem(int idItem) {
        int idWish = 0;

        for (WishToItem wi : this.listWishItem) {
            if (wi.getIdItem() == idItem) {
                idWish = wi.getIdWish();
                break;
            }
        }

        return idWish;
    }

    public boolean addWishItem(int idWish, int idItem) {
        WishToItem newWishToItem = new WishToItem(idWish, idItem);

        this.listWishItem.add(newWishToItem);

        DatabaseReference childReference = this.reference.child(idWish + "_" + idItem);

        Map<String, Object> data = new HashMap<>();
        data.put("idWish", idWish);
        data.put("idItem", idItem);

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Relação de lista de desejos e item cadastrado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao cadastrar o relação de lista de desejos e item: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteWishItem(int idWish, int idItem) {
        WishlistToItemDAO.WishToItem wishToItemDeleted = null;
        boolean deleted = false;

        for (WishlistToItemDAO.WishToItem wi : this.listWishItem) {
            if (wi.getIdWish() == idWish) {
                if (wi.getIdItem() == idItem) {
                    wishToItemDeleted = wi;
                    break;
                }
            }
        }

        if (wishToItemDeleted != null) {
            this.listWishItem.remove(wishToItemDeleted);
            deleted = true;
        }

        String wishItem = idWish + "_" + idItem;
        this.reference.child(wishItem).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Relação de item e lista de desejos removido com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao remover o relação de item e lista de desejos: " + task.getException().getMessage());
                }
            }
        });

        return deleted;
    }

    class WishToItem {
        private int idWish;
        private int idItem;

        public WishToItem(int idWish, int idItem) {
            this.idWish = idWish;
            this.idItem = idItem;
        }

        public int getIdWish() {
            return this.idWish;
        }

        public int getIdItem() {
            return this.idItem;
        }
    }
}