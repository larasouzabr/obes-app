package com.example.obes.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.model.Address.Address;
import com.example.obes.model.Book.Book;
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

public class AddressDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("address");
    private ArrayList<Address> listAddress;
    private static AddressDAO instance;

    private AddressDAO() {
        this.listAddress = new ArrayList<Address>();
    }

    public static AddressDAO getInstance() {
        if(instance == null) {
            instance = new AddressDAO();
        }
        return instance;
    }

    public ArrayList<Address> getListAddress() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Address> addresses = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int idAddress = dataSnapshot.child("idAddress").getValue(int.class);
                    int idUser = dataSnapshot.child("idUser").getValue(int.class);
                    String cep = dataSnapshot.child("cep").getValue(String.class);
                    String estado = dataSnapshot.child("estado").getValue(String.class);
                    String cidade = dataSnapshot.child("cidade").getValue(String.class);
                    String rua = dataSnapshot.child("rua").getValue(String.class);
                    String bairro = dataSnapshot.child("bairro").getValue(String.class);
                    String complemento = dataSnapshot.child("complemento").getValue(String.class);
                    String numero = dataSnapshot.child("numero").getValue(String.class);

                    Address address = new Address(idAddress, idUser, cep, estado, cidade, rua, bairro, numero);
                    address.setComplemento(complemento);

                    addresses.add(address);
                }

                listAddress = addresses;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar os endereços: " + error.getMessage());
            }
        });

        return this.listAddress;
    }

    public boolean addAddress(Address address) {
        this.listAddress.add(address);

        DatabaseReference addressReference = this.reference.child(String.valueOf(address.getIdAddress()));

        Map<String, Object> addressData = new HashMap<>();
        addressData.put("idAddress", address.getIdAddress());
        addressData.put("idUser", address.getIdUser());
        addressData.put("cep", address.getCep());
        addressData.put("estado", address.getEstado());
        addressData.put("cidade", address.getCidade());
        addressData.put("rua", address.getRua());
        addressData.put("bairro", address.getBairro());
        addressData.put("complemento", address.getComplemento());
        addressData.put("numero", address.getNumero());

        addressReference.setValue(addressData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Endereço cadastrado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao cadastrar o endereço: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteAddress(Address address) {
        Address addressDelete = null;
        boolean removed = false;

        for (Address a : this.listAddress) {
            if (address.getIdAddress() == a.getIdAddress()) {
                addressDelete = a;
                break;
            }
        }

        if (addressDelete != null) {
            this.listAddress.remove(addressDelete);
            removed = true;
        }

        return removed;
    }

    public boolean editAddress(Address address) {
        Address addressEdit = null;
        boolean edited = false;

        for (Address a : this.listAddress) {
            if (address.getIdAddress() == a.getIdAddress()) {
                addressEdit = a;
                break;
            }
        }

        if (addressEdit != null) {
            addressEdit.setCep(address.getCep());
            addressEdit.setEstado(address.getEstado());
            addressEdit.setCidade(address.getCidade());
            addressEdit.setRua((address.getRua()));
            addressEdit.setBairro(address.getBairro());
            addressEdit.setComplemento(address.getComplemento());
            addressEdit.setNumero(address.getNumero());

            edited = true;
        }

        DatabaseReference addressReference = this.reference.child(String.valueOf(address.getIdAddress()));

        Map<String, Object> addressData = new HashMap<>();
        addressData.put("idAddress", address.getIdAddress());
        addressData.put("idUser", address.getIdUser());
        addressData.put("cep", address.getCep());
        addressData.put("estado", address.getEstado());
        addressData.put("cidade", address.getCidade());
        addressData.put("rua", address.getRua());
        addressData.put("bairro", address.getBairro());
        addressData.put("complemento", address.getComplemento());
        addressData.put("numero", address.getNumero());

        addressReference.setValue(addressData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Endereço editado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao editar o endereço: " + task.getException().getMessage());
                }
            }
        });

        return edited;
    }

    public Address getAddressById(int idAddress) {
        Address address = null;

        for (Address a : this.listAddress) {
            if (a.getIdAddress() == idAddress) {
                address = a;
                break;
            }
        }

        return address;
    }

    public Address getAddressByIdUser(int idUser) {
        Address address = null;

        for (Address a : this.listAddress) {
            if (a.getIdUser() == idUser) {
                address = a;
                break;
            }
        }

        return address;
    }
}