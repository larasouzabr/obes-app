package com.example.obes.model.Payment;

public class CreditCardPayment extends OnlinePayment {
    private String cardNumber;
    private String cardHolderName;
    private String expirationDate;
    private int securityCode;
    private boolean isPaymentCredit;

    public CreditCardPayment(int id, String cardNumber, String cardHolderName, String expirationDate, int securityCode, boolean isPaymentCredit) {
        super(id);
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expirationDate = expirationDate;
        this.securityCode = securityCode;
        this.isPaymentCredit = isPaymentCredit;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(int securityCode) {
        this.securityCode = securityCode;
    }

    public boolean getIsPaymentCredit() {
        return this.isPaymentCredit;
    }

    public void setIsPaymentCredit(boolean paymentCredit) {
        this.isPaymentCredit = paymentCredit;
    }

    @Override
    public int getId() {
        return this.id;
    }
}
