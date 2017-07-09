package com.example.buddhikajay.mobilepay.Component;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import com.example.buddhikajay.mobilepay.CheckoutActivity;
import com.example.buddhikajay.mobilepay.MerchantTransactionReportActivity;
import com.example.buddhikajay.mobilepay.Model.MerchantTransactionModel;
import com.example.buddhikajay.mobilepay.R;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.Parameter;
import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;
import com.example.buddhikajay.mobilepay.loginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by supun on 09/07/17.
 */

/**
 * Created by supun on 30/05/17.
 */



public class MerchantTransactionAdapter extends ArrayAdapter<MerchantTransactionModel> {


    public MerchantTransactionAdapter(Context context, ArrayList<MerchantTransactionModel> transactions) {
        super(context, 0, transactions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final MerchantTransactionModel transactionModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.merchant_transaction_item, parent, false);
        }
        // Lookup view for data population
        TextView AccountNumber = (TextView) convertView.findViewById(R.id.userAccountNumber);
        TextView Amount = (TextView) convertView.findViewById(R.id.amount);
        TextView Date = (TextView) convertView.findViewById(R.id.date);
        Button button_void = (Button) convertView.findViewById(R.id.button_void);
        // Populate the data into the template view using the data objec
        AccountNumber.setText(transactionModel.getUserAccountNumber());
        Amount.setText(transactionModel.getAmount()+" LKR");
        Date.setText(transactionModel.getDate());
        // Return the completed view to render on screen
        if(transactionModel.getType().equals("refund") || transactionModel.getStatus().equals("void")){
            button_void.setVisibility(View.GONE);
        }
        button_void.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refundTransaction(transactionModel.getMerchant().getId(),transactionModel.getRecieptNumber());
                Intent intent = new Intent(getContext(),MerchantTransactionReportActivity.class);
                getContext().startActivity(intent);

            }
        });
        return convertView;
    }

    public void refundTransaction(String mechantId,String transactionId){

        JSONObject payload = new JSONObject();
        try {
            payload.put("merchantId",""+mechantId);
            payload.put("transactionId",""+transactionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyRequestHandlerApi.api(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                    Log.d("transactionAdapt",result.toString());
            }

            @Override
            public void login() {
                moveLogin();
            }

            @Override
            public void enableButton() {

            }
        }, Parameter.urlVoidTransaction, Api.getAccessToken(getContext()),payload,getContext());
    }
    public void moveLogin(){

        Intent myIntent = new Intent(getContext(), loginActivity.class);
        getContext().startActivity(myIntent);

    }
}



