package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.buddhikajay.mobilepay.Component.TransactionAdapter;
import com.example.buddhikajay.mobilepay.Model.TransactionModel;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.Parameter;
import com.example.buddhikajay.mobilepay.Services.SecurityHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;
import com.example.buddhikajay.mobilepay.Component.VolleyCallback;

public class TransactionReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_transaction_report);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTransaction);
        //setSupportActionBar(toolbar);
        //final ActionBar bar = getActionBar();
        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getTransaction();
        }





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                moveScan();
                break;
        }
        return true;
    }

    private void moveScan() {
        Intent myIntent = new Intent(TransactionReportActivity.this, scanActivity.class);
        TransactionReportActivity.this.startActivity(myIntent);
        finish();
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
            todate.put("year",2018);
            todate.put("month",6);
            todate.put("day",22);

            parameter.put("fromDate",fromdate);
            parameter.put("toDate",todate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyRequestHandlerApi.api(new VolleyCallback(){


            @Override
            public void onSuccess(JSONObject result) {
                responseProcess(result);
            }

            @Override
            public void login() {
                moveLogin();
            }

            @Override
            public void enableButton() {

            }
        }, Parameter.urlTransactionDetail,Api.getAccessToken(getApplicationContext()),parameter,getApplicationContext());

    }

    private void responseProcess(JSONObject result){

        if(result.has("data")){
            JSONArray array= (JSONArray) result.opt("data");
            try {
                if(array.length()!=0){
                    JSONObject jsonObject = array.getJSONObject(0);
                    Log.d("Transaction:Transaction",jsonObject.toString());
                    populateTransactionList(array);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Not Any Transaction",Toast.LENGTH_LONG).show();
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else if(result.has("errors")){
            JSONArray array= (JSONArray) result.opt("errors");
            try {
                JSONObject jsonObject = array.getJSONObject(0);

                if(jsonObject.opt("status").toString().equals("422")){
                    Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void moveLogin(){

        Intent myIntent = new Intent(TransactionReportActivity.this, loginActivity.class);
        TransactionReportActivity.this.startActivity(myIntent);
        finish();
    }
    @Override
    public void onBackPressed() {
        moveScan();
        //moveLogin();
        finish();

    }
}
