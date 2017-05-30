package com.example.buddhikajay.mobilepay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.buddhikajay.mobilepay.Component.TransactionAdapter;
import com.example.buddhikajay.mobilepay.Identities.TransactionModel;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.SecurityHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransactionReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_transaction_report);
        getTransaction();



    }
    private void populateTransactionList(JSONArray transactions) {
        Log.d("Transaction activity","trnsaction list populating");
        // Construct the data source
        ArrayList<TransactionModel> arrayOfTrnsactions = TransactionModel.getTransaction(transactions);
        // Create the adapter to convert the array to views
        TransactionAdapter adapter = new TransactionAdapter(this, arrayOfTrnsactions);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.transactionList);
        listView.setAdapter(adapter);

    }
    public void getTransaction(){
        JSONObject parameter = new JSONObject();
        try {
            parameter.put("userId",""+Api.getRegisterId(getApplicationContext()));
            JSONObject fromdate = new JSONObject();
            fromdate.put("year",2017);
            fromdate.put("month",1);
            fromdate.put("day",1);

            JSONObject todate = new JSONObject();
            todate.put("year",2017);
            todate.put("month",6);
            todate.put("day",1);

            parameter.put("fromDate",fromdate);
            parameter.put("toDate",todate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Api.api(new Api.VolleyCallback(){


            @Override
            public void onSuccess(JSONObject result) {
                if(result.has("data")){
                    JSONArray array= (JSONArray) result.opt("data");
                    try {
                        JSONObject jsonObject = array.getJSONObject(0);
                        Log.d("Transaction:Transaction",jsonObject.toString());
                        populateTransactionList(array);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        },Api.urlTransactionDetail,Api.getAccessToken(getApplicationContext()),parameter,getApplicationContext());

    }
}
