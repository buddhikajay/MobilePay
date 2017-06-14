package com.example.buddhikajay.mobilepay;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buddhikajay.mobilepay.Identities.Merchant;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Identities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.buddhikajay.mobilepay.Services.Formate;
import com.example.buddhikajay.mobilepay.Services.Parameter;
import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;
import com.example.buddhikajay.mobilepay.Component.VolleyCallback;
import com.google.zxing.integration.android.IntentIntegrator;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class CheckoutActivity extends AppCompatActivity {

    String phoneNumber;
    String username;
    Button payButton;
    EditText amountTextView;
    boolean complete = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        final Intent intent = getIntent();

        username = intent.getStringExtra("userdata");
        phoneNumber = intent.getStringExtra("phoneNumber");
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
        amountTextView = (EditText) findViewById(R.id.amountEditText);
        payButton = (Button) findViewById(R.id.buttonPay);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                payButton.setEnabled(false);

                String amount = amountTextView.getText().toString();

                //Log.d("CheckoutActivity:number",phoneNumber);

                //pay transaction
                showmessgebox(amount, intent.getStringExtra("name"),intent);


            }
        });

        amountFormat( amountTextView);
    }
    boolean changeAmount = false;
    private void amountFormat(final EditText value){

        value.addTextChangedListener(new TextWatcher(){
            DecimalFormat dec = new DecimalFormat("0.00");


            int beforelength=0;
            //int afterlength=0;
            @Override
            public void afterTextChanged(Editable arg0) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

                beforelength = s.length();

            }
            private String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    //int cursorpoint = value.getSelectionEnd();
                    Log.d("start",start+"");
                    value.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,. LKR]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    formatted=formatted.replaceAll("[$]", "")+" LKR";
                    current = formatted;
                    value.setText(formatted);

                    if(start ==0 && !changeAmount){

                        value.setSelection(formatted.length() - 4);

                    }
                    else if(start < formatted.length()-4){
                        if(beforelength<s.length()){
                            if((cleanString.length() -4)/3 == (cleanString.length() -3)/3  ){
                                value.setSelection(start+1);

                            }
                            else {

                                value.setSelection(start+2);
                            }
                        }
                        else {
                            if((cleanString.length() -2)/3 == (cleanString.length() -3)/3 ){
                                value.setSelection(start);
                            }
                            else {
                                value.setSelection(start-1);

                            }
                        }



                    }

                    else
                        value.setSelection(formatted.length()-4);



                    value.addTextChangedListener(this);
                    changeAmount = true;
                }
            }
        });

    }
    private void payTrasaction(String mechantId, final String amount, String accessToken, final Intent intent){

        JSONObject pay = new JSONObject();
        try {
            pay.put("merchantId",""+mechantId);
            pay.put("amount",""+amount);
            pay.put("accessToken",""+accessToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyRequestHandlerApi.api(new VolleyCallback(){


            @Override
            public void onSuccess(JSONObject result) {
                responseProcess(result,amount,intent);
            }

            @Override
            public void login() {
                    moveLogin();
            }

            @Override
            public void enableButton() {
                payButton.setEnabled(true);
            }
        }, Parameter.mechantpayUrl,Api.getAccessToken(getApplicationContext()),pay,getApplicationContext());
    }

    private void TransactionFinise(String amount) {
        Intent myIntent = new Intent(CheckoutActivity.this, finishActivity.class);
        myIntent.putExtra("amount",amount);
        CheckoutActivity.this.startActivity(myIntent);
        finish();


    }


    private void showmessgebox(final String amount, String username, final Intent intent){
        AlertDialog alertDialog=new AlertDialog.Builder(CheckoutActivity.this).create();
        alertDialog .setTitle("Payment Confirmation");
        alertDialog .setMessage("Pay "+ amount+" to "+username+"?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        payButton.setEnabled(true);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        payTrasaction(intent.getStringExtra("id"),amount, Api.getAccessToken(getApplicationContext()),intent);
                        //Toast.makeText(getApplicationContext(), "Payment Successful", Toast.LENGTH_LONG).show();

                    }
                });

        alertDialog.show();


    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(CheckoutActivity.this, scanActivity.class);
       CheckoutActivity.this.startActivity(myIntent);
        //moveLogin();
        //finish();

    }
    public User getSellerDetail(String sellerId){
     User user=new User();
        user.setName("Kamal");
        String address[]={"3/36","Dharmapala Mawatha","Katubedda"};
        //user.setAddress(address);
        return user;
    }
    @Override
    public void onPause() {
        super.onPause();
        if(!complete){
            moveLogin();
        }
        //
        //Intent myIntent = new Intent(CheckoutActivity.this, loginActivity.class);
        //CheckoutActivity.this.startActivity(myIntent);
        //finish();
    }

    public void moveLogin(){

        Intent myIntent = new Intent(CheckoutActivity.this, loginActivity.class);
        CheckoutActivity.this.startActivity(myIntent);
        finish();
    }

    private void responseProcess(JSONObject result,String amount,Intent intent){
        if(result.has("data")){
            JSONArray array= (JSONArray) result.opt("data");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                Log.d("Transaction:Checkout",jsonObject.toString());
                Api.sendSms(phoneNumber, ""+amount+" has been reciveded from "+jsonObject.optString("fromAccount").toString(),getApplicationContext());
                Api.sendSms(Api.getPhoneNumber(getApplicationContext()), "LKR "+amount+" has been payed to "+jsonObject.optString("toAccount").toString(),getApplicationContext());
                moveToFinishActivity(amount);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else if(result.has("errors")){
            JSONArray array= (JSONArray) result.opt("errors");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                if(jsonObject.opt("status").toString().equals("422")){
                    //setContentView(R.layout.activity_scan);
                    Toast.makeText(getApplicationContext(), "insufficient Balance", Toast.LENGTH_LONG).show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
    private void moveToFinishActivity(String amount){

        Intent myIntent = new Intent(CheckoutActivity.this, finishActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        myIntent.putExtra("amount",amount);

        CheckoutActivity.this.startActivity(myIntent);
        this.complete = true;
        finish();


    }


}
