package com.example.obes.dao;

import com.example.obes.model.Address.Address;

import java.util.ArrayList;

public class AddressDAO {
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
        return this.listAddress;
    }

    public boolean addAddress(Address address) {
        this.listAddress.add(address);
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
            addressEdit.setBairro(address.getBairro());
            addressEdit.setComplemento(address.getComplemento());
            addressEdit.setNumero(address.getNumero());

            edited = true;
        }

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