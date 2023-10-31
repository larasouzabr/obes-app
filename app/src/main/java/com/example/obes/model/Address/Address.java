package com.example.obes.model.Address;

public class Address {
    private final int idAddress;
    private final int idUser;
    private String cep;
    private String estado;
    private String cidade;
    private String bairro;
    private String endereco;
    private String complemento;
    private int numero;

    public Address(int idAddress, int idUser, String cep, String estado, String cidade, String bairro, String endereco, String complemento, int numero) {
        this.idAddress = idAddress;
        this.idUser = idUser;
        this.cep = cep;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.endereco = endereco;
        this.complemento = complemento;
        this.numero = numero;
    }

    public int getIdAddress() {
        return this.idAddress;
    }

    public int getIdUser() {
        return this.idUser;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}