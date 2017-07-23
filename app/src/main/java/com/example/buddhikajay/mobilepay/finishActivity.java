package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class finishActivity extends AppCompatActivity {
    private String amount;
    private String payee;
    private String recept;

    private boolean back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);



        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //populateScanList();

        }

        final Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
        payee = intent.getStringExtra("payee");
        recept = intent.getStringExtra("recept");
        //Log.d("Amount",amount);
        TextView amountText = (TextView) findViewById(R.id.transaction_amount);
        amountText.setText(amount);

        TextView receiptText = (TextView) findViewById(R.id.receipt_number);
        receiptText.setText(recept);

        TextView transactionmsg = (TextView) findViewById(R.id.merchant_name);
        transactionmsg.setText(payee);
        Button btn=(Button)findViewById(R.id.finish_btn);
        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                finish();
                Intent myIntent = new Intent(finishActivity.this, scanActivity.class);
                finishActivity.this.startActivity(myIntent);




            }
        });
    }
    @Override
    public void onBackPressed() {
//        Intent myIntent = new Intent(CheckoutActivity.this, scanActivity.class);
//       CheckoutActivity.this.startActivity(myIntent);
        this.back = true;
        finish();
        moveHome();


    }
    @Override
    public void onPause() {
        super.onPause();
//        if(!complete){
//            moveLogin();
//        }
        //
        //Intent myIntent = new Intent(CheckoutActivity.this, loginActivity.class);
        //CheckoutActivity.this.startActivity(myIntent);
        //finish();

        if(!back){
            finish();
            //moveLogin();
        }




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.back = true;
                finish();
                moveHome();
                break;
        }
        return true;
    }

    public void moveHome(){
        Intent myIntent = new Intent(this, scanActivity.class);
        this.startActivity(myIntent);
        finish();

    }
}
