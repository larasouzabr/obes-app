package com.example.obes.model.Address;

public class Address {
    private final int idAddress;
    private final int idUser;
    private String cep;
    private String estado;
    private String cidade;
    private String rua;
    private String bairro;
    private String complemento;
    private String numero;

    public Address(int idAddress, int idUser, String cep, String estado, String cidade, String rua, String bairro, String numero) {
        this.idAddress = idAddress;
        this.idUser = idUser;
        this.cep = cep;
        this.estado = estado;
        this.cidade = cidade;
        this.rua = rua;
        this.bairro = bairro;
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

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}