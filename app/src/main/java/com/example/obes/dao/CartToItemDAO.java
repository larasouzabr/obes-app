package com.example.obes.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.model.Book.Book;
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

public class CartToItemDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("cart_to_item");
    private ArrayList<CartToItem> listCartsItems;

    private static CartToItemDAO instance;

    private CartToItemDAO() {
        this.listCartsItems = new ArrayList<CartToItem>();
    }

    public static CartToItemDAO getInstance() {
        if (instance == null) {
            instance = new CartToItemDAO();
        }
        return instance;
    }

    public ArrayList<CartToItem> getListCartsItems() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<CartToItem> cartItemList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int idCart = dataSnapshot.child("idCart").getValue(int.class);
                    int idItem = dataSnapshot.child("idItem").getValue(int.class);

                    CartToItem cartToItem = new CartToItem(idCart, idItem);

                    cartItemList.add(cartToItem);
                }

                listCartsItems = cartItemList;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar itens do carrinho: " + error.getMessage());
            }
        });

        return this.listCartsItems;
    }
    public ArrayList<ItemCart> getItemsByIdCart(int idCart) {
        ItemCartDAO itemCartDAO = ItemCartDAO.getInstance();

        ArrayList<ItemCart> listItems = new ArrayList<ItemCart>();

        for (CartToItem cartToItem : this.listCartsItems) {
            if (cartToItem.getIdCart() == idCart) {
                listItems.add(itemCartDAO.getItemById(cartToItem.getIdItem()));
            }
        }

        return listItems;
    }

    public int getIdCartByIdItem(int idItem) {
        int idCart = 0;

        for (CartToItem ci : this.listCartsItems) {
            if (ci.getIdItem() == idItem) {
                idCart = ci.getIdCart();
                break;
            }
        }

        return idCart;
    }

    public boolean addCartItem(int idCart, int idItem) {
        CartToItem newCartToItem = new CartToItem(idCart, idItem);

        this.listCartsItems.add(newCartToItem);

        DatabaseReference childReference = this.reference.child(idCart + "_" + idItem);

        Map<String, Object> data = new HashMap<>();
        data.put("idCart", idCart);
        data.put("idItem", idItem);

        childReference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Relação de carrinho e item cadastrado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao cadastrar o relação de carrinho e item: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }
    public boolean deleteCartItem(int idCart, int idItem) {
        CartToItem cartToItemDeleted = null;
        boolean deleted = false;

        for (CartToItem ci : this.listCartsItems) {
            if (ci.getIdCart() == idCart) {
                if (ci.getIdItem() == idItem) {
                    cartToItemDeleted = ci;
                    break;
                }
            }
        }

        if (cartToItemDeleted != null) {
            listCartsItems.remove(cartToItemDeleted);
            deleted = true;
        }

        String bookUser = idCart + "_" + idItem;
        this.reference.child(bookUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Relação de item e carrinho removido com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao remover o relação de item e carrinho: " + task.getException().getMessage());
                }
            }
        });

        return deleted;
    }

    class CartToItem {
        private int idCart;
        private int idItem;

        public CartToItem(int idCart, int idItem) {
            this.idCart = idCart;
            this.idItem = idItem;
        }

        public int getIdCart() {
            return this.idCart;
        }

        public int getIdItem() {
            return this.idItem;
        }
    }
}