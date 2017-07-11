package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.buddhikajay.mobilepay.Component.VolleyCallback;
import com.example.buddhikajay.mobilepay.Model.UserTransactionModel;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.Parameter;
import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;

import org.json.JSONException;
import org.json.JSONObject;

public class MerchantTransactionDetailActivity extends AppCompatActivity {

    private UserTransactionModel model;
    private String recieptNumber;
    private String name;
    private String amount;
    private String date;
    private String type;
    private String status;
    private String merchantId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_transaction_detail);

        final Intent intent = getIntent();
        recieptNumber = intent.getStringExtra("receipt");
        name = intent.getStringExtra("name");
        amount = intent.getStringExtra("amount");
        date = intent.getStringExtra("date");
        type = intent.getStringExtra("type");
        status = intent.getStringExtra("status");
        merchantId = intent.getStringExtra("merchantId");

        TextView recieptView = (TextView) findViewById(R.id.receipt_number);
        TextView nameView = (TextView) findViewById(R.id.usr_name);
        TextView amountView = (TextView) findViewById(R.id.amount);
        TextView dateView = (TextView) findViewById(R.id.date);
        TextView typeView = (TextView) findViewById(R.id.type);
        TextView statusView = (TextView) findViewById(R.id.status);

        recieptView.setText(recieptNumber);
        nameView.setText(name);
        amountView.setText(amount);
        dateView.setText(date);
        typeView.setText(type);
        statusView.setText(status);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        Button button_void = (Button)findViewById(R.id.button_void);
        if(type.equals("refund") || status.equals("void")){
            button_void.setVisibility(View.GONE);
        }
        else{
            button_void.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refundTransaction(merchantId,recieptNumber);
                    Intent intent = new Intent(MerchantTransactionDetailActivity.this,MerchantTransactionReportActivity.class);
                    startActivity(intent);

                }
            });
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this,MerchantTransactionReportActivity.class);
                startActivity(intent);
                break;
        }
        return true;
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
        }, Parameter.urlVoidTransaction, Api.getAccessToken(getApplicationContext()),payload,getApplicationContext());
    }
    public void moveLogin(){

        Intent myIntent = new Intent(this, loginActivity.class);
        startActivity(myIntent);
        finish();
    }
}
