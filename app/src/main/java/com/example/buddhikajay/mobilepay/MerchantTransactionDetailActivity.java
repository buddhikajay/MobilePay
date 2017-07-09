package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.buddhikajay.mobilepay.Model.UserTransactionModel;

public class MerchantTransactionDetailActivity extends AppCompatActivity {

    private UserTransactionModel model;
    private String recieptNumber;
    private String name;
    private String amount;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_transaction_detail);

        final Intent intent = getIntent();
        recieptNumber = intent.getStringExtra("receipt");
        name = intent.getStringExtra("name");
        amount = intent.getStringExtra("amount");
        date = intent.getStringExtra("date");

        TextView recieptView = (TextView) findViewById(R.id.receipt_number);
        TextView nameView = (TextView) findViewById(R.id.usr_name);
        TextView amountView = (TextView) findViewById(R.id.amount);
        TextView dateView = (TextView) findViewById(R.id.date);

        recieptView.setText(recieptNumber);
        nameView.setText(name);
        amountView.setText(amount);
        dateView.setText(date);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

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
}
