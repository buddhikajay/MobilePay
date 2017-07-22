package com.example.buddhikajay.mobilepay;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.buddhikajay.mobilepay.Component.VolleyCallback;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.Parameter;
import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;

import net.glxn.qrgen.android.QRCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BillSpliteActivity extends AppCompatActivity {


    private EditText numberOfSplitter;
    private Button button_qrcodeGenerate;
    private ImageView myImage;
    private Button button_pay;


    private Double amount;
    private String merchantId;
    private String merchantName;
    private String phoneNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_splite);



        Intent intent = getIntent();
        amount = intent.getDoubleExtra("amount",0.00);
        merchantId = intent.getStringExtra("merchantId");
        merchantName = intent.getStringExtra("merchantName");
        phoneNumber = intent.getStringExtra("phoneNumber");

        numberOfSplitter = (EditText) findViewById(R.id.number_of_spliters);
        button_qrcodeGenerate = (Button) findViewById(R.id.button_generate_qrcode);
        myImage = (ImageView) findViewById(R.id.image_qrcode);
        button_pay = (Button)findViewById(R.id.button_paynow);



        //qrcode generate
        button_qrcodeGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    generateQrCodeForSplitter();



            }
        });

        //paynow
        button_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_pay.setEnabled(false);
                pay();
            }
        });

    }

    private void pay() {

        showmessgebox();
    }
    private void showmessgebox(){
        AlertDialog alertDialog=new AlertDialog.Builder(BillSpliteActivity.this).create();
        alertDialog .setTitle("Payment Confirmation");
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog .setMessage("Pay "+ amount+" LKR to "+merchantName+"?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        button_pay.setEnabled(true);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        payTrasaction();

                        //Toast.makeText(getApplicationContext(), "Payment Successful", Toast.LENGTH_LONG).show();

                    }
                });

        alertDialog.show();


    }
    private void payTrasaction(){

        JSONObject pay = new JSONObject();
        try {
            pay.put("merchantId",""+merchantId);
            pay.put("amount",""+amount.toString().replaceAll("[$, LKR]", ""));
            pay.put("accessToken",""+Api.getAccessToken(getApplicationContext()));
            pay.put("paymentType","Bill_Splite");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyRequestHandlerApi.api(new VolleyCallback(){


            @Override
            public void onSuccess(JSONObject result) {
                responseProcess(result);
            }

            @Override
            public void login() {
                moveLogin();
            }

            @Override
            public void enableButton() {
                button_pay.setEnabled(true);
            }
        }, Parameter.mechantpayUrl,Api.getAccessToken(getApplicationContext()),pay,getApplicationContext());
    }

    private void responseProcess(JSONObject result){
        if(result.has("data")){
            JSONArray array= (JSONArray) result.opt("data");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                Log.d("Transaction:Checkout",jsonObject.toString());

                    Log.d("",""+amount+" has been reciveded from "+Api.getNic(getApplicationContext()));
                    Log.d("","LKR "+amount+" has been payed to "+merchantName);
                    Api.sendSms(phoneNumber, ""+amount+" has been reciveded from "+Api.getNic(getApplicationContext()),getApplicationContext());
                    Api.sendSms(Api.getPhoneNumber(getApplicationContext()), "LKR "+amount+" has been payed to "+merchantName,getApplicationContext());
                    moveToFinishActivity(jsonObject.optString("transactionId").toString());

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
    private void moveToFinishActivity(String reciptNumber){

        Intent myIntent = new Intent(this, finishActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        myIntent.putExtra("amount",amount.toString());
        myIntent.putExtra("payee",merchantName);
        myIntent.putExtra("recept",reciptNumber);
        this.startActivity(myIntent);



    }

    public void moveLogin(){

        Intent myIntent = new Intent(this, loginActivity.class);
        this.startActivity(myIntent);
        finish();
    }

    private void generateQrCodeForSplitter() {

        String splitters = numberOfSplitter.getText().toString();
        Log.d("number of splitters",splitters);
        if( !(splitters.equals("0") || splitters.equals(""))){
            Double amountValue = Double.parseDouble(amount.toString().replaceAll("[$, LKR]",""));
            int spliteNumber = Integer.parseInt(splitters);

            String code = ""+ Api.getRegisterId(getApplicationContext())+" "+amountValue/spliteNumber+" main";
            Bitmap myBitmap = QRCode.from(""+ code).withSize(400,400).bitmap();
            myImage.setImageBitmap(myBitmap);
        }
        else {

            numberOfSplitter.setError("insert number of splitter");


        }


    }
}
