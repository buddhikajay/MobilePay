package com.example.buddhikajay.mobilepay;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        final Intent intent = getIntent();

        final String username = intent.getStringExtra("userdata");
        final boolean scannerType = intent.getBooleanExtra("scannerType", true);// true: Merchant Pay, false : direct pay

        TextView idTextView = (TextView) findViewById(R.id.merchantIdTextView);
        TextView nameTextView = (TextView) findViewById(R.id.merchantNameTextView);
        TextView addressTextView = (TextView) findViewById(R.id.addressTextView);

        //if direct pay to person
        if(!scannerType){
            TextView merchantIdLabel = (TextView) findViewById(R.id.merchantIdLable);
            TextView merchantNameLabel = (TextView) findViewById(R.id.merchantNameLable);
            TextView merchantAddressLabel = (TextView) findViewById(R.id.merchantAddressLable);

            merchantIdLabel.setText("Payee ID");
            merchantNameLabel.setText("Payee Name");

            //hide address
            merchantAddressLabel.setVisibility(View.INVISIBLE);
            addressTextView.setVisibility(View.INVISIBLE);
        }


        idTextView.setText(intent.getStringExtra("id"));
        nameTextView.setText(intent.getStringExtra("name"));
        addressTextView.setText(intent.getStringExtra("address"));

        Button payButton = (Button) findViewById(R.id.buttonPay);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TextView amountTextView = (TextView) findViewById(R.id.amountEditText);
                String amount = amountTextView.getText().toString();
                showmessgebox(amount, intent.getStringExtra("name"));
                sendSms(intent.getStringExtra("phoneNumber"), "LKR "+amount+" has been payed to "+intent.getStringExtra("name").toString());

                //pay transaction
                payTrasaction(intent.getStringExtra("id"),amount,Api.getAccessToken(getApplicationContext()));

            }
        });

        //String lName = intent.getStringExtra("lastName");
//        Button btn=(Button)findViewById(R.id.pay_button);
//        final EditText amount=(EditText)findViewById(R.id.prize_edittext);
//        btn.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View v)
//            {
//                //sendSms(amount.getText().toString());
//               showmessgebox(amount.getText().toString(),username);
//
//                //Intent myIntent = new Intent(CheckoutActivity.this, finishActivity.class);
//                //CheckoutActivity.this.startActivity(myIntent);
//               //
//
//            }
//        });
//        TextView userId = (TextView) findViewById(R.id.id_edittext);
//        userId.setText(username);

//        amount.addTextChangedListener(new TextWatcher(){
//            DecimalFormat dec = new DecimalFormat("0.00");
//            @Override
//            public void afterTextChanged(Editable arg0) {
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//            private String current = "";
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(!s.toString().equals(current)){
//                    amount.removeTextChangedListener(this);
//
//                    String cleanString = s.toString().replaceAll("[£$,. LKR]", "");
//
//                    double parsed = Double.parseDouble(cleanString);
//
//                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));
//
//                    formatted=formatted.replaceAll("[£$]", "")+" LKR";
//                    current = formatted;
//                    amount.setText(formatted);
//                    amount.setSelection(formatted.length());
//
//                    amount.addTextChangedListener(this);
//                }
//            }
//        });
        //set user detail
//        User userDetail=getSellerDetail(userId.getText().toString());
//        EditText userName = (EditText) findViewById(R.id.name_edittext);
//        userName.setText(userDetail.getName());
//        EditText address1 = (EditText) findViewById(R.id.address_edittext1);
//        address1.setText(userDetail.getAddress()[0]);
//        EditText address2 = (EditText) findViewById(R.id.address_edittext2);
//        address2.setText(userDetail.getAddress()[1]);
//        EditText address3 = (EditText) findViewById(R.id.address_edittext3);
//        address3.setText(userDetail.getAddress()[2]);

    }
    private void payTrasaction(String mechantId,String amount,String accessToken){

        JSONObject pay = new JSONObject();
        try {
            pay.put("merchantId",""+mechantId);
            pay.put("amount",""+amount);
            pay.put("accessToken",""+accessToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Api.merchantpay(Api.getAccessToken(getApplicationContext()),pay,getApplicationContext());
    }
    private void showmessgebox(String amount,String username){
        AlertDialog alertDialog=new AlertDialog.Builder(CheckoutActivity.this).create();
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
                        Intent myIntent = new Intent(CheckoutActivity.this, scanActivity.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        CheckoutActivity.this.startActivity(myIntent);

                        finish();
                    }
                });

        alertDialog.show();


    }

    private void sendSms(String phoneNumber, String message){
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
        Intent myIntent = new Intent(CheckoutActivity.this, scanActivity.class);
        CheckoutActivity.this.startActivity(myIntent);
        finish();

    }
    public User getSellerDetail(String sellerId){
     User user=new User();
        user.setName("Kamal");
        String address[]={"3/36","Dharmapala Mawatha","Katubedda"};
        user.setAddress(address);
        return user;
    }
    @Override
    public void onPause() {
        super.onPause();
        //Intent myIntent = new Intent(scanActivity.this, loginActivity.class);
        //scanActivity.this.startActivity(myIntent);
        finish();
    }
}
