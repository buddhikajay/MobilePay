package com.example.buddhikajay.mobilepay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buddhikajay.mobilepay.Component.VolleyCallback;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.MQTTClient;
import com.example.buddhikajay.mobilepay.Services.Parameter;
import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;

import net.glxn.qrgen.android.QRCode;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import at.grabner.circleprogress.CircleProgressView;

public class BillSpliteActivity extends AppCompatActivity {


    private EditText numberOfSplitter;
    private Button button_qrcodeGenerate;
    private ImageView myImage;
    private Button button_pay;
    private TextView billColectedTest;
    private TextView amount_per_person;

    private double amount;
    private String merchantId;
    private String merchantName;
    private String phoneNumber;


    private boolean back;

    private int spliteNumber;
    private int fundTransfers = 1;

    private CircleProgressView mCircleView;

    int btn_size;
    double collectedMoney;
    double moneyOfUnit;
    double payedMoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_splite);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //populateScanList();

        }

        Intent intent = getIntent();
        amount = intent.getDoubleExtra("amount",0.00);
        merchantId = intent.getStringExtra("merchantId");
        merchantName = intent.getStringExtra("merchantName");
        phoneNumber = intent.getStringExtra("phoneNumber");

        numberOfSplitter = (EditText) findViewById(R.id.number_of_spliters);
        button_qrcodeGenerate = (Button) findViewById(R.id.button_generate_qrcode);
        myImage = (ImageView) findViewById(R.id.image_qrcode);
        button_pay = (Button)findViewById(R.id.button_paynow);
        mCircleView = (CircleProgressView) findViewById(R.id.circleView);
        billColectedTest = (TextView) findViewById(R.id.text_collected);
        amount_per_person = (TextView) findViewById(R.id.txt_amount_per_person);



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

        BroadcastReceiver receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("bill_spit");
        this.registerReceiver(receiver, filter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width_px = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height_px = Resources.getSystem().getDisplayMetrics().heightPixels;

        int pixeldpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        float pixeldp = Resources.getSystem().getDisplayMetrics().density;

        int width_dp = (width_px/pixeldpi)*160;
        int height_dp = (height_px/pixeldpi)*160;
        int qr_width = (width_dp-52);

         btn_size = (int) (qr_width*pixeldp);

        billColectedTest.setVisibility(View.GONE);

        mCircleView.setVisibility(View.GONE);

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
            pay.put("amount",""+amount);
            pay.put("accessToken",""+Api.getAccessToken(getApplicationContext()));
            pay.put("paymentType","Merchant_Pay");
            pay.put("paymentCategory","Bill_Splite");
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
                    //Api.sendSms(phoneNumber, ""+amount+" has been reciveded from "+Api.getNic(getApplicationContext()),getApplicationContext());
                    //Api.sendSms(Api.getPhoneNumber(getApplicationContext()), "LKR "+amount+" has been payed to "+merchantName,getApplicationContext());
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
        myIntent.putExtra("amount",""+amount);
        Log.d("amount to finish",amount+"");
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

        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int width_px = Resources.getSystem().getDisplayMetrics().widthPixels;
//        int height_px = Resources.getSystem().getDisplayMetrics().heightPixels;
//
//        int pixeldpi = Resources.getSystem().getDisplayMetrics().densityDpi;
//        float pixeldp = Resources.getSystem().getDisplayMetrics().density;
//
//        int width_dp = (width_px/pixeldpi)*160;
//        int height_dp = (height_px/pixeldpi)*160;
//        int qr_width = (width_dp-52);
//
//        int btn_size = (int) (qr_width*pixeldp);


        String splitters = numberOfSplitter.getText().toString();
        Log.d("number of splitters",splitters);
        if( !(splitters.equals("0") || splitters.equals(""))){
            Double amountValue = Double.parseDouble(String.valueOf(amount));
            spliteNumber = Integer.parseInt(splitters);
            Random r = new Random();
            int rNumber = r.nextInt(100000000);
            String topic = "main_split_"+rNumber;
            moneyOfUnit = amountValue/spliteNumber;
            String code = ""+ Api.getRegisterId(getApplicationContext())+" "+String.format( "%.2f", amountValue/spliteNumber )+" "+topic;
            Log.d("code",code);
            Bitmap myBitmap = QRCode.from(""+ code).withSize(btn_size/2,btn_size/2).bitmap();
            myImage.setImageBitmap(myBitmap);
            mqttOpenChanel(topic);
            mCircleView.setVisibility(View.VISIBLE);
            mCircleView.setValue(fundTransfers*100/spliteNumber);
            amount_per_person.setText(String.format( "bill for per person : %.2f", amountValue/spliteNumber )+" LKR");

        }
        else {

            numberOfSplitter.setError("insert number of splitter");


        }



    }

    private void mqttOpenChanel(final String topic) {

        new Thread(new Runnable() {
            public void run() {
                // a potentially  time consuming task
                MQTTClient mqttClient = new MQTTClient();

                try {
                    mqttClient.initializeMQTTClient(getBaseContext(), "directpay_"+System.currentTimeMillis(), false, false, null, null);
                    String payload = "scan";
                    byte[] encodedPayload = new byte[0];
                    encodedPayload = payload.getBytes("UTF-8");
                    mqttClient.subscribe(topic,2);

                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }).start();
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

    public class MyBroadcastReceiver extends BroadcastReceiver {

        private static final String TAG = "MyBroadcastReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            StringBuilder sb = new StringBuilder();
            sb.append("Action: " + intent.getAction() + "\n");
            sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
            String log = sb.toString();
//            Log.d(TAG, log);
//            Toast.makeText(context, log, Toast.LENGTH_LONG).show();
            String response_bill_split = intent.getStringExtra("data");
            Log.d("brodacast",""+intent.getStringExtra("data"));

            if(!response_bill_split.equals("")){
                fundTransfers++;
                float value = (fundTransfers*100/spliteNumber);
                Log.d("vaalue",value+"");
                mCircleView.setValue(value);
                collectedMoney += moneyOfUnit;
                if(fundTransfers == spliteNumber){
                    billColectedTest.setVisibility(View.VISIBLE);
                    collectedMoney = amount;
                }

            }


            Log.d("transafers",fundTransfers+"");
        }
    }


}
