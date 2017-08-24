package com.example.buddhikajay.mobilepay.Model;

/**
 * Created by supun on 18/05/17.
 */

public class Merchant {
    private String id;
    private String registedId;
    private String merchantName;
    private String merchantAddress;
    private String phoneNumber;
    private String brNumber;
    private String accountNumber;


    public String getBrNumber() {
        return brNumber;
    }

    public void setBrNumber(String brNumber) {
        this.brNumber = brNumber;
    }

    public String getRegistedId() {
        return registedId;
    }

    public void setRegistedId(String registedId) {
        this.registedId = registedId;
    }

    public Merchant(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumbers) {
        this.phoneNumber = phoneNumbers;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
