package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.buddhikajay.mobilepay.Model.UserTransactionModel;

public class UserTransactionDetailActivity extends AppCompatActivity {

    private UserTransactionModel model;
    private String recieptNumber;
    private String name;
    private String amount;
    private String date;
    private String type;
    private boolean roleIsMerchant;
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
        roleIsMerchant = intent.getBooleanExtra("roleIsMerchant",false);


        TextView recieptView = (TextView) findViewById(R.id.receipt_number);
        TextView nameView = (TextView) findViewById(R.id.merchant_name);
        TextView amountView = (TextView) findViewById(R.id.amount);
        TextView dateView = (TextView) findViewById(R.id.date);
        TextView typeView = (TextView) findViewById(R.id.type);
        TextView userType = (TextView) findViewById(R.id.uset_type);
        if(!roleIsMerchant){
            userType.setText("user");
        }

        recieptView.setText(recieptNumber);
        nameView.setText(name);
        amountView.setText(amount+" LKR");
        dateView.setText(date);
        typeView.setText(type);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

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
