package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class finishActivity extends AppCompatActivity {
    private String amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        final Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
        //Log.d("Amount",amount);
        TextView amountText = (TextView) findViewById(R.id.transaction_amount);
        amountText.setText(amount);

        Button btn=(Button)findViewById(R.id.finish_btn);
        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                finish();
                Intent myIntent = new Intent(finishActivity.this, loginActivity.class);
                finishActivity.this.startActivity(myIntent);
                finish();



            }
        });
    }
}
