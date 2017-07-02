package com.example.buddhikajay.mobilepay.Model;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.buddhikajay.mobilepay.Component.VolleyCallback;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.Formate;
import com.example.buddhikajay.mobilepay.Services.Parameter;
import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;
import com.example.buddhikajay.mobilepay.TransactionReportActivity;
import com.example.buddhikajay.mobilepay.loginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by supun on 29/05/17.
 */

public class TransactionModel extends Application {
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
            this.accountNumber =  object.getString("payeeId");
            this.amount = object.getString("amount");
            JSONObject date = object.getJSONObject("dateTime");
            this.date = dateTimeFilter(date.getString("date"));
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
    public String dateTimeFilter(String date){

        String dateout="";
        try {
            Date datein = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").parse(date);
           dateout = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datein);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateout;
    }

    public void moveLogin(){

        Intent myIntent = new Intent(this, loginActivity.class);
        this.startActivity(myIntent);
    }

}
