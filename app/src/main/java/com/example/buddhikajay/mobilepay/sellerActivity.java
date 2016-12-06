package com.example.buddhikajay.mobilepay;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class sellerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_detail);
        Intent intent = getIntent();

        final String username = intent.getStringExtra("userdata");
        //String lName = intent.getStringExtra("lastName");
        Button btn=(Button)findViewById(R.id.pay_button);
        final EditText amount=(EditText)findViewById(R.id.prize_edittext);
        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                //sendSms(amount.getText().toString());
               showmessgebox(amount.getText().toString(),username);

                //Intent myIntent = new Intent(sellerActivity.this, finishActivity.class);
                //sellerActivity.this.startActivity(myIntent);
               //

            }
        });
        TextView userId = (TextView) findViewById(R.id.id_edittext);
        userId.setText(username);

        amount.addTextChangedListener(new TextWatcher(){
            DecimalFormat dec = new DecimalFormat("0.00");
            @Override
            public void afterTextChanged(Editable arg0) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            private String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    amount.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[£$,. LKR]", "");

                    double parsed = Double.parseDouble(cleanString);

                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    formatted=formatted.replaceAll("[£$]", "")+" LKR";
                    current = formatted;
                    amount.setText(formatted);
                    amount.setSelection(formatted.length());

                    amount.addTextChangedListener(this);
                }
            }
        });
        //set user detail
        User userDetail=getSellerDetail(userId.getText().toString());
        EditText userName = (EditText) findViewById(R.id.name_edittext);
        userName.setText(userDetail.getName());
        EditText address1 = (EditText) findViewById(R.id.address_edittext1);
        address1.setText(userDetail.getAddress()[0]);
        EditText address2 = (EditText) findViewById(R.id.address_edittext2);
        address2.setText(userDetail.getAddress()[1]);
        EditText address3 = (EditText) findViewById(R.id.address_edittext3);
        address3.setText(userDetail.getAddress()[2]);

    }
    private void showmessgebox(String amount,String username){
        AlertDialog alertDialog=new AlertDialog.Builder(sellerActivity.this).create();
        alertDialog .setTitle("Payment Confirmation");
        alertDialog .setMessage("Pay "+amount+" to "+username+"?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Payment Successful", Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(sellerActivity.this, scanActivity.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        sellerActivity.this.startActivity(myIntent);

                        finish();
                    }
                });

        alertDialog.show();


    }

    private void sendSms(String amount){

        String phoneNumber="+94711135012";
        String message="User Pay Rs "+ amount;
        try{




            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }



    }
    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(sellerActivity.this, scanActivity.class);
        sellerActivity.this.startActivity(myIntent);
        finish();

    }
    public User getSellerDetail(String sellerId){
     User user=new User();
        user.setName("Kamal");
        String address[]={"3/36","Dharmapala Mawatha","Katubedda"};
        user.setAddress(address);
        return user;
    }
}
