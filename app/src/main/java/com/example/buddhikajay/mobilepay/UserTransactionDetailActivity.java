package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.buddhikajay.mobilepay.Model.UserTransactionModel;

import static com.example.buddhikajay.mobilepay.R.id.status;

public class UserTransactionDetailActivity extends AppCompatActivity {

    private UserTransactionModel model;
    private String recieptNumber;
    private String name;
    private String amount;
    private String date;
    private String type;
    private String status;
    private boolean roleIsMerchant;
    private boolean appUserAccount_isFromAccountNuber;
    private String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        final Intent intent = getIntent();
        recieptNumber = intent.getStringExtra("receipt");
        name = intent.getStringExtra("name");
        amount = intent.getStringExtra("amount");
        date = intent.getStringExtra("date");
        type = intent.getStringExtra("type");
        status=intent.getStringExtra("status");

        roleIsMerchant = intent.getBooleanExtra("roleIsMerchant",false);
        appUserAccount_isFromAccountNuber=intent.getBooleanExtra("appUserAccount_isFromAccountNuber",false);

        TextView recieptView = (TextView) findViewById(R.id.receipt_number);
        TextView nameView = (TextView) findViewById(R.id.merchant_name);
        TextView amountView = (TextView) findViewById(R.id.amount);
        TextView dateView = (TextView) findViewById(R.id.date);
        TextView typeView = (TextView) findViewById(R.id.type);
        TextView tofromView = (TextView) findViewById(R.id.to_from);
        TextView addressView=(TextView) findViewById(R.id.merchantAddress);

        if(type.equals("Merchant_Pay") || type.equals("refund")) {
            address = intent.getStringExtra("address");
            Log.d("address1111", address);
        }
        else {
            addressView.setVisibility(View.GONE);
        }
        //TextView userType = (TextView) findViewById(R.id.uset_type);
        /*if(!roleIsMerchant){
            userType.setText("user");
        }*/

        recieptView.setText(recieptNumber);
        nameView.setText(name);
        amountView.setText(amount+" LKR");
        dateView.setText(date);
        typeView.setText(type);
        //addressView.setText(address);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        Log.d("transaction_type:",type);
        Log.d("transaction_status:",status);
        if(type.equals("refund")){
            addressView.setText(address);
            tofromView.setText("From");
        }
        else if (type.equals("Fund_Transfer")){
            if (appUserAccount_isFromAccountNuber){
                tofromView.setText("To");
            }
            else{
                tofromView.setText("From");
            }
        }
        else if (type.equals("Merchant_Pay")){
            addressView.setText(address);
            if (appUserAccount_isFromAccountNuber){
                tofromView.setText("To");
            }
            else{
                tofromView.setText("From");
            }
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this,UserTransactionReportActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //moveLogin();
        moveTransactionReport();
        finish();

    }
    @Override
    public void onPause() {
        super.onPause();
        //finish();

    }

    public void moveTransactionReport(){
        Intent intent = new Intent(this,UserTransactionReportActivity.class);
        startActivity(intent);
    }
}
