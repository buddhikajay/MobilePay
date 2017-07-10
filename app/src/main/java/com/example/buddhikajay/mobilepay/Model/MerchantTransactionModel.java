package com.example.buddhikajay.mobilepay.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by supun on 09/07/17.
 */

public class MerchantTransactionModel {
    private String userAccountNumber;
    private String merchantAccountNumber;
    private String amount;
    private String date;
    private Merchant merchant;
    private String recieptNumber;
    private String type;
    private String status;


    public MerchantTransactionModel(String userAccountNumber, String amount, String date, Merchant merchant, String recieptNumber,String merchantAccountNumber) {

        this.userAccountNumber = userAccountNumber;
        this.merchantAccountNumber = merchantAccountNumber;
        this.amount = amount;
        this.date = date;
        this.merchant = merchant;
        this.recieptNumber = recieptNumber;
    }

    public MerchantTransactionModel(JSONObject object) {

        Log.d("transactionmodel",object.toString());
        try {

            this.amount = object.getString("originalAmount");
            JSONObject date = object.getJSONObject("dateTime");
            this.recieptNumber = object.getString("id");
            this.status = object.getString("status");
            this.date = dateTimeFilter(date.getString("date"));
            this.type =object.getString("type");
            if(this.type.equals("merchantpay")){
                this.merchantAccountNumber = getMerchantName(object.getJSONObject("payeeDetail")) ;
                this.userAccountNumber = getPayerAccountNumber(object.getJSONObject("payerDetail")) ;
                JSONObject payeeDetaildata = object.getJSONObject("payeeDetail");
                JSONArray data = payeeDetaildata.getJSONArray("data");
                JSONObject payeeDetail = data.getJSONObject(0);

                Merchant merchantTemp = new Merchant(payeeDetail.getString("merchantId"));
                merchantTemp.setMerchantName(payeeDetail.getString("merchantName"));
                this.merchant = merchantTemp;
            }
            else if(this.type.equals("refund")){

                Log.d("psyerDetsil",object.getJSONObject("payerDetail").toString());
                this.merchantAccountNumber = getMerchantName(object.getJSONObject("payerDetail")) ;
                this.userAccountNumber = getPayerAccountNumber(object.getJSONObject("payeeDetail")) ;
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

    public static ArrayList<MerchantTransactionModel> getTransaction() {
        ArrayList<MerchantTransactionModel> transactions = new ArrayList<MerchantTransactionModel>();

        return transactions;
    }

    public static ArrayList<MerchantTransactionModel> getTransaction(JSONArray transaction) {
        ArrayList<MerchantTransactionModel> transactionModels = new ArrayList<MerchantTransactionModel>();
        for (int i = 0; i < transaction.length(); i++) {
            try {
                transactionModels.add(new MerchantTransactionModel(transaction.getJSONObject(i)));
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

//        Intent myIntent = new Intent(this,loginActivity.class);
////        this.startActivity(myIntent);
    }


    public String getMerchantName(JSONObject payeeDetail){
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
    public String getPayerAccountNumber(JSONObject payerDetail){
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
