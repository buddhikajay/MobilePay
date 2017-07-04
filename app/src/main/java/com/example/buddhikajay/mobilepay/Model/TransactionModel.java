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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by supun on 29/05/17.
 */

public class TransactionModel extends Application implements Serializable {
    private String accountNumber;
    private String amount;
    private String date;
    private Merchant merchant;
    private String recieptNumber;


    public TransactionModel(String accountNumber, String amount, String date,Merchant merchant,String recieptNumber) {

        this.accountNumber = accountNumber;
        this.amount = amount;
        this.date = date;
        this.merchant = merchant;
        this.recieptNumber = recieptNumber;
    }

    public TransactionModel(JSONObject object) {

        Log.d("transactionmodel",object.toString());
        try {
            this.accountNumber = getPayeeName(object.getJSONObject("payeeDetail")) ;
            this.amount = object.getString("payingAmount");
            JSONObject date = object.getJSONObject("dateTime");
            this.date = dateTimeFilter(date.getString("date"));
            this.recieptNumber = object.getString("id");
            JSONObject payeeDetaildata = object.getJSONObject("payeeDetail");
            JSONArray data = payeeDetaildata.getJSONArray("data");
            JSONObject payeeDetail = data.getJSONObject(0);

            Merchant merchantTemp = new Merchant(payeeDetail.getString("merchantId"));
            merchantTemp.setMerchantName(payeeDetail.getString("merchantName"));
            this.merchant = merchantTemp;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getRecieptNumber() {
        return recieptNumber;
    }

    public void setRecieptNumber(String recieptNumber) {
        this.recieptNumber = recieptNumber;
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


    public String getPayeeName(JSONObject payeeDetail){
        if(payeeDetail.has("data")){
            JSONArray array= (JSONArray) payeeDetail.opt("data");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                return jsonObject.opt("merchantName").toString();



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return "";
    }

}
