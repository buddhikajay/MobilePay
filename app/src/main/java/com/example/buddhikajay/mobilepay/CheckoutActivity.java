package com.example.buddhikajay.mobilepay;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buddhikajay.mobilepay.Model.Merchant;
import com.example.buddhikajay.mobilepay.Model.PaymentModel;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.buddhikajay.mobilepay.Services.Formate;
import com.example.buddhikajay.mobilepay.Services.Parameter;
import com.example.buddhikajay.mobilepay.Services.SecurityHandler;
import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;
import com.example.buddhikajay.mobilepay.Component.VolleyCallback;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
public class CheckoutActivity extends AppCompatActivity {

    String phoneNumber;
    String username;
    Button payButton;
    EditText amountTextView;
    EditText tipTextView;
    boolean complete = false;
    private PaymentModel paymentModel;
    private  String tipperId;
    boolean[] changeAmount ;

    TextView test_bill_amount;
    TextView test_bill_amount_amount;

    private String merchantId;

    private String paymentType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        final Intent intent = getIntent();

        paymentModel = (PaymentModel)intent.getSerializableExtra("Paymodel");
        username = intent.getStringExtra("name");
        phoneNumber = intent.getStringExtra("phoneNumber");
        final boolean scannerType = intent.getBooleanExtra("scannerType", true);// true: Merchant Pay, false : direct pay

        TextView idTextView = (TextView) findViewById(R.id.merchantIdTextView);
        TextView nameTextView = (TextView) findViewById(R.id.merchantNameTextView);
        TextView addressTextView = (TextView) findViewById(R.id.addressTextView);


        amountTextView = (EditText) findViewById(R.id.amountEditText);
        tipTextView = (EditText) findViewById(R.id.tipEdit);
        payButton = (Button) findViewById(R.id.buttonPay);
        Button spitter = (Button) findViewById(R.id.buttonSpilite);

       // test_bill_amount = (TextView) findViewById(R.id.text_bill_amount);
        //test_bill_amount_amount = (TextView)findViewById(R.id.text_bill_amount_value);
        //if direct pay to person
        if(!scannerType){
            //TextView merchantIdLabel = (TextView) findViewById(R.id.merchantIdLable);
            //TextView merchantNameLabel = (TextView) findViewById(R.id.merchantNameLable);
            //TextView merchantAddressLabel = (TextView) findViewById(R.id.merchantAddressLable);

            //merchantIdLabel.setText("Payee ID");
            //merchantNameLabel.setText("Payee Name");

            //hide address
            //merchantAddressLabel.setVisibility(View.INVISIBLE);
            addressTextView.setVisibility(View.GONE);
            tipTextView.setVisibility(View.GONE);
            spitter.setVisibility(View.GONE);
            nameTextView.setText(intent.getStringExtra("accountNumber"));
            paymentType = "Fund_Transfer";


        }
        else {
            addressTextView.setText(intent.getStringExtra("address"));
            Log.d("address",intent.getStringExtra("address"));
            nameTextView.setText(intent.getStringExtra("name"));
            paymentType = "Merchant_Pay";
        }

        merchantId = intent.getStringExtra("id");
        idTextView.setText(Formate.idSplite(merchantId));



        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                payButton.setEnabled(false);

                String amount = amountTextView.getText().toString();

                //Log.d("CheckoutActivity:number",phoneNumber);

                //pay transaction

                Double total = Double.parseDouble(amount.toString().replaceAll("[$, LKR]", ""));
                Log.d("total",total.toString());
                if(paymentModel.isTip() && !tipTextView.getText().toString().equals("") ){
                    total += Double.parseDouble(tipTextView.getText().toString().replaceAll("[$, LKR]", ""));
                    Log.d("total",total.toString());
                }

                showmessgebox(paymentType,total+ " LKR",amount, intent.getStringExtra("name"), intent);


            }
        });

        changeAmount = new boolean[2];
        amountFormat( amountTextView,0);
        amountFormat(tipTextView,1);

        staticAndDynamicQrHandle(paymentModel);

        //splite


        spitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spilteTransaction();


            }
        });

    }

    private void spilteTransaction() {
        String amountText = amountTextView.getText().toString();
       amountText = amountText.toString().replaceAll("[$, LKR]","");
        Log.d("amont",amountText);
        if (!amountText.equals("") ){
            double amountValue = Double.parseDouble(amountText);
            Intent intent = new Intent(CheckoutActivity.this,BillSpliteActivity.class);
            intent.putExtra("merchantName",username);
            intent.putExtra("merchantId",merchantId);
            intent.putExtra("amount",amountValue);
            intent.putExtra("phoneNumber",phoneNumber);

            startActivity(intent);
            finish();
        }


    }

    private void staticAndDynamicQrHandle(PaymentModel paymentModel){
        Log.d("payment",paymentModel.toString());

        Log.d("payment is dynamic",paymentModel.isDynamic()+"");
        if (paymentModel.isDynamic()){
            Log.d("amout paymodel",paymentModel.getQrModels().get(0).getAmount());
            amountTextView.setText(paymentModel.getQrModels().get(0).getAmount());
            amountTextView.setEnabled(false);
            //amountTextView.setVisibility(View.GONE);

        }else{
           /// test_bill_amount.setVisibility(View.GONE);
            //test_bill_amount_amount.setVisibility(View.GONE);
        }

        if(paymentModel.isTip()){

            tipperId = paymentModel.getQrModels().get(1).getId();
        }
        else{
            Log.d("tip","notip");
            tipTextView.setEnabled(false);
            tipTextView.setVisibility(View.GONE);
        }


    }
    //boolean changeAmount = false;

    private void amountFormat(final EditText value,final int i){

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

                    String cleanString = s.toString().replaceAll("[^0-9]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    formatted=formatted.replaceAll("[^0-9.,]", "")+" LKR";
                    current = formatted;
                    value.setText(formatted);

                    if(start ==0 && !changeAmount[i]){

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
                    changeAmount[i] = true;
                }
            }
        });

    }
    private void payTrasaction(String paymentType,String mechantId, final String amount, String accessToken, final Intent intent, final boolean tip){

        JSONObject pay = new JSONObject();
        try {
            pay.put("merchantId",""+mechantId);
            pay.put("amount",""+amount.replaceAll("[$, LKR]", ""));
            pay.put("accessToken",""+accessToken);
            pay.put("paymentType",paymentType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyRequestHandlerApi.api(new VolleyCallback(){


            @Override
            public void onSuccess(JSONObject result) {
                responseProcess(result,amount,intent,tip);
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


    private void showmessgebox(final String paymentType,final String total, final String amount, String username, final Intent intent){
        AlertDialog alertDialog=new AlertDialog.Builder(CheckoutActivity.this).create();
        alertDialog .setTitle("Payment Confirmation");
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog .setMessage("Pay "+ total+" to "+username+"?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        payButton.setEnabled(true);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        payTrasaction(paymentType,intent.getStringExtra("id"),amount, Api.getAccessToken(getApplicationContext()),intent,false);
                        Log.d("merchantId",intent.getStringExtra("id"));
                        if(paymentModel.isTip() ){
                            String tipamount = tipTextView.getText().toString().replaceAll("[$, LKR]", "");;
                            if(!tipamount.equals("")&& Double.parseDouble(tipamount)>0.0){
                                payTrasaction(paymentType,tipperId,tipTextView.getText().toString(), Api.getAccessToken(getApplicationContext()),intent,true);
                                Log.d("tipperid",tipperId);
                            }

                        }

                        //Toast.makeText(getApplicationContext(), "Payment Successful", Toast.LENGTH_LONG).show();

                    }
                });

        alertDialog.show();


    }

    @Override
    public void onBackPressed() {
//        Intent myIntent = new Intent(CheckoutActivity.this, scanActivity.class);
//       CheckoutActivity.this.startActivity(myIntent);
        moveLogin();
        finish();

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
//        if(!complete){
//            moveLogin();
//        }
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
    public void moveHome(){
        Intent myIntent = new Intent(CheckoutActivity.this, scanActivity.class);
        CheckoutActivity.this.startActivity(myIntent);
        finish();

    }
    private void responseProcess(JSONObject result,String amount,Intent intent,boolean tip){
        if(result.has("data")){
            JSONArray array= (JSONArray) result.opt("data");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                Log.d("Transaction:Checkout",jsonObject.toString());
                if(!tip){
                    Log.d("No tip",""+amount+" has been reciveded from "+Api.getNic(getApplicationContext()));
                    Log.d("No tip","LKR "+amount+" has been payed to "+intent.getStringExtra("name").toString());
                    Api.sendSms(phoneNumber, ""+amount+" has been reciveded from "+Api.getNic(getApplicationContext()),getApplicationContext());
                    Api.sendSms(Api.getPhoneNumber(getApplicationContext()), "LKR "+amount+" has been payed to "+intent.getStringExtra("name"),getApplicationContext());
                    moveToFinishActivity(amount,intent.getStringExtra("name"),jsonObject.optString("transactionId").toString());
                }
                else {
                    Log.d("tip",""+amount+" has been reciveded from "+Api.getNic(getApplicationContext()));
                    Log.d("tip","LKR "+amount+" has been payed to "+jsonObject.optString("toAccount").toString());

                   sendSmsToTipMerchant(jsonObject.optString("toAccount").toString(),amount);

                }




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
    private void moveToFinishActivity(String amount,String name,String reciptNumber){

        Intent myIntent = new Intent(CheckoutActivity.this, finishActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        myIntent.putExtra("amount",amount);
        myIntent.putExtra("payee",name);
        myIntent.putExtra("recept",reciptNumber);
        CheckoutActivity.this.startActivity(myIntent);
        this.complete = true;



    }
    private void sendSmsToTipMerchant(String id,final String amount){

        getMerchantDetail(id,amount);
    }
    private void getMerchantDetail(String id,final String amount){

        JSONObject detail = new JSONObject();
        try {
            detail.put("id",""+id);

            VolleyRequestHandlerApi.api(new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {

                    if(result.has("data")){
                        JSONArray array= (JSONArray) result.opt("data");
                        try {
                            JSONObject jsonObject = array.getJSONObject(0);
                            Api.sendSms(jsonObject.opt("phoneNumber").toString(), ""+amount+" has been reciveded tip from "+Api.getNic(getApplicationContext()),getApplicationContext());
                            Api.sendSms(Api.getPhoneNumber(getApplicationContext()), "LKR "+amount+" has been payed to tip "+jsonObject.optString("merchantName").toString(),getApplicationContext());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    else if(result.has("errors")){
                        JSONArray array= (JSONArray) result.opt("errors");
                        try {
                            JSONObject jsonObject = array.getJSONObject(0);
                            if(jsonObject.opt("status").toString().equals("5000")){
                                //setContentView(R.layout.activity_scan);
                                Toast.makeText(getApplicationContext(),"Requested Merchant ID Does Not Exist",Toast.LENGTH_LONG).show();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);

                                Log.d("error","5000");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }

                @Override
                public void login() {
                    moveLogin();
                }

                @Override
                public void enableButton() {

                }
            }, Parameter.urlMerchantDetail,Api.getAccessToken(getApplicationContext()),detail,getApplicationContext());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this,scanActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }



}
