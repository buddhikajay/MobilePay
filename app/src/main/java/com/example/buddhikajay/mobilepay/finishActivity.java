package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class finishActivity extends AppCompatActivity {
    private String amount;
    private String payee;
    private String recept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        final Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
        payee = intent.getStringExtra("payee");
        recept = intent.getStringExtra("recept");
        //Log.d("Amount",amount);
        TextView amountText = (TextView) findViewById(R.id.transaction_amount);
        amountText.setText(amount);

        TextView receiptText = (TextView) findViewById(R.id.receipt_number);
        receiptText.setText(recept);
        String msg = "Amount Paid to "+payee+" :";
        TextView transactionmsg = (TextView) findViewById(R.id.transation_message);
        transactionmsg.setText(msg);
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
}
