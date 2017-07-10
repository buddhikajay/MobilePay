package com.example.buddhikajay.mobilepay.Model;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

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

public class UserTransactionModel extends Application implements Serializable {
    private String userAccountNumber;
    private String merchantAccountNumber;
    private String amount;
    private String date;
    private Merchant merchant;
    private String recieptNumber;
    private String type;
    private String status;

    public UserTransactionModel(String userAccountNumber, String amount, String date, Merchant merchant, String recieptNumber, String merchantAccountNumber) {

        this.userAccountNumber = userAccountNumber;
        this.merchantAccountNumber = merchantAccountNumber;
        this.amount = amount;
        this.date = date;
        this.merchant = merchant;
        this.recieptNumber = recieptNumber;

    }

    public UserTransactionModel(JSONObject object) {

        Log.d("transactionmodel",object.toString());
        try {


            this.amount = object.getString("originalAmount");
            JSONObject date = object.getJSONObject("dateTime");
            this.date = dateTimeFilter(date.getString("date"));
            this.recieptNumber = object.getString("id");
            this.type =object.getString("type");
            this.status = object.getString("status");

            if(this.type.equals("merchantpay")){
                this.userAccountNumber = getUserAccountNumber(object.getJSONObject("payerDetail")) ;
                this.merchantAccountNumber =getPayeeName(object.getJSONObject("payeeDetail")) ;
                JSONObject payeeDetaildata = object.getJSONObject("payeeDetail");
                JSONArray data = payeeDetaildata.getJSONArray("data");
                JSONObject payeeDetail = data.getJSONObject(0);
                Merchant merchantTemp = new Merchant(payeeDetail.getString("merchantId"));
                merchantTemp.setMerchantName(payeeDetail.getString("merchantName"));
                this.merchant = merchantTemp;

            }
            else if(this.type.equals("refund")){
                this.userAccountNumber = getUserAccountNumber(object.getJSONObject("payeeDetail")) ;
                this.merchantAccountNumber =getPayeeName(object.getJSONObject("payerDetail")) ;
                JSONObject payerDetaildata = object.getJSONObject("payerDetail");
                JSONArray data = payerDetaildata.getJSONArray("data");
                JSONObject payerDetail = data.getJSONObject(0);
                Merchant merchantTemp = new Merchant(payerDetail.getString("merchantId"));
                merchantTemp.setMerchantName(payerDetail.getString("merchantName"));
                this.merchant = merchantTemp;
            }



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

    public String getUserAccountNumber() {
        return userAccountNumber;
    }

    public void setUserAccountNumber(String userAccountNumber) {
        this.userAccountNumber = userAccountNumber;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static ArrayList<UserTransactionModel> getTransaction() {
        ArrayList<UserTransactionModel> transactions = new ArrayList<UserTransactionModel>();

        return transactions;
    }

    public static ArrayList<UserTransactionModel> getTransaction(JSONArray transaction) {
        ArrayList<UserTransactionModel> transactionModels = new ArrayList<UserTransactionModel>();
        for (int i = 0; i < transaction.length(); i++) {
            try {
                transactionModels.add(new UserTransactionModel(transaction.getJSONObject(i)));
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
    public String getUserAccountNumber(JSONObject payerDetail){
        if(payerDetail.has("data")){
            JSONArray array= (JSONArray) payerDetail.opt("data");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                Log.d("payerDetail",payerDetail.toString());
                return jsonObject.opt("accountNumber").toString();



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return "";
    }

    public String getMerchantAccountNumber() {
        return merchantAccountNumber;
    }

    public void setMerchantAccountNumber(String merchantAccountNumber) {
        this.merchantAccountNumber = merchantAccountNumber;
    }
}
