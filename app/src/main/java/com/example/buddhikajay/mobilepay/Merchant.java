package com.example.buddhikajay.mobilepay;

/**
 * Created by supun on 18/05/17.
 */

public class Merchant {
    private String id;
    private String merchantName;
    private String merchantAddress;

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
}
