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
    private boolean scanerType;

    private String tipAmount;
    private boolean inApp;
    private boolean tip;

    private boolean back;
    private boolean gotoInapp=true;
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
        inApp = intent.getBooleanExtra("inApp",false);
        tip = intent.getBooleanExtra("tip",false);
        scanerType = intent.getBooleanExtra("scannerType",false);

        //Log.d("Amount",amount);
        TextView amountText = (TextView) findViewById(R.id.transaction_amount);
        amountText.setText(amount);

        TextView receiptText = (TextView) findViewById(R.id.receipt_number);
        receiptText.setText(recept);

        TextView transactionmsg = (TextView) findViewById(R.id.merchant_name);
        TextView merchantOrUser = (TextView) findViewById(R.id.merchant_or_user);

        if(!scanerType){
            merchantOrUser.setText("User");
        }
        TextView tipAmount_text = (TextView) findViewById(R.id.tip_amount_text);
        TextView tipAmount_text_mid = (TextView) findViewById(R.id.tip_amount_text_mid);
        TextView tipAmount_value = (TextView) findViewById(R.id.tip_amount_value);

        if(!tip){
            tipAmount_text.setVisibility(View.GONE);
            tipAmount_text_mid.setVisibility(View.GONE);
            tipAmount_value.setVisibility(View.GONE);
        }
        else {
            tipAmount = intent.getStringExtra("tipAmount");
            tipAmount_value.setText(tipAmount);

        }
        transactionmsg.setText(payee);
        Button btn=(Button)findViewById(R.id.finish_btn);
        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                finish();
                if(inApp){
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.supun.molly");
                    startActivity(launchIntent);
                    finish();
                }
                else {
                    Intent myIntent = new Intent(finishActivity.this, scanActivity.class);
                    finishActivity.this.startActivity(myIntent);
                }





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

//        if(!back){
//            finish();
//            //moveLogin();
//        }




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
