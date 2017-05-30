package com.example.buddhikajay.mobilepay.Identities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by supun on 29/05/17.
 */

public class TransactionModel {
    private String accountNumber;
    private String amount;
    private String date;

    public TransactionModel(String accountNumber, String amount, String date) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.date = date;
    }

    public TransactionModel(JSONObject object) {
        try {
            this.accountNumber = object.getString("payerId");
            this.amount = object.getString("amount");
            JSONObject date = object.getJSONObject("dateTime");
            this.date = date.getString("date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static ArrayList<TransactionModel> getTransaction() {
        ArrayList<TransactionModel> transactions = new ArrayList<TransactionModel>();
        transactions.add(new TransactionModel("123234", "10000","2016-3-09"));
        transactions.add(new TransactionModel("123234", "10000","2016-3-09"));
        transactions.add(new TransactionModel("123234", "10000","2016-3-09"));
        return transactions;
    }

    public static ArrayList<TransactionModel> getTransaction(JSONArray transaction) {
        ArrayList<TransactionModel> transactionModels = new ArrayList<TransactionModel>();
        for (int i = 0; i < transaction.length(); i++) {
            try {
                transactionModels.add(new TransactionModel(transaction.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return transactionModels;
    }
}
