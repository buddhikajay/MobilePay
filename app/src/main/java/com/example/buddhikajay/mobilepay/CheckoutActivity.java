package com.example.buddhikajay.mobilepay;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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

import com.example.buddhikajay.mobilepay.Model.PaymentModel;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Model.User;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.buddhikajay.mobilepay.Services.Formate;
import com.example.buddhikajay.mobilepay.Services.MQTTClient;
import com.example.buddhikajay.mobilepay.Services.Parameter;
import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;
import com.example.buddhikajay.mobilepay.Component.VolleyCallback;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckoutActivity extends AppCompatActivity {

    String phoneNumber;
    String firstName;
    String lastName;
    String accountNumber;
    String address;

    Button payButton;
    EditText amountTextView;
    EditText tipTextView;

    TextInputLayout amountErr;
    TextInputLayout tipErr;

    boolean complete = false;
    private PaymentModel paymentModel;
    private  String tipperId;
    boolean[] changeAmount ;

    TextView test_bill_amount;
    TextView test_bill_amount_amount;

    private String merchantId;
    private String paymentType;
    private String registedId;
    boolean scannerType;
    boolean back;
    boolean inApp =false;

    boolean web_purchase = false;

    private String mainTransactionId;
    private String tipTransactionId;

    private String mainAmount ;
    private View mProgressView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //populateScanList();

        }

        final Intent intent = getIntent();

        paymentModel = (PaymentModel)intent.getSerializableExtra("Paymodel");
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        phoneNumber = intent.getStringExtra("phoneNumber");
        registedId = intent.getStringExtra("registedId");
        scannerType = intent.getBooleanExtra("scannerType", true);// true: Merchant Pay, false : direct pay

        TextView idTextView = (TextView) findViewById(R.id.merchantIdTextView);
        TextView merchantidTextView = (TextView) findViewById(R.id.merchantTextView);
        TextView nameTextView = (TextView) findViewById(R.id.merchantNameTextView);
        TextView addressTextView = (TextView) findViewById(R.id.addressTextView);
        mProgressView = findViewById(R.id.login_progress);;

        amountErr = (TextInputLayout) findViewById(R.id.amountEditText_err);
        tipErr = (TextInputLayout) findViewById(R.id.tipEdit_err);

        amountTextView = (EditText) findViewById(R.id.amountEditText);
        tipTextView = (EditText) findViewById(R.id.tipEdit);
        payButton = (Button) findViewById(R.id.buttonPay);
        Button spitter = (Button) findViewById(R.id.buttonSpilite);

        amountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountErr.setError(null);
            }
        });

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
            nameTextView.setText(intent.getStringExtra("firstName "+"lastName"));
            paymentType = "Fund_Transfer";
            getSupportActionBar().setTitle("FundTransfer");
            merchantidTextView.setText("User ID :");
            payButton.setText("FundTransfer");
            accountNumber = intent.getStringExtra("accountNumber");


        }
        else {
            addressTextView.setText(intent.getStringExtra("address"));
            address = intent.getStringExtra("address");
            Log.d("address",intent.getStringExtra("address"));
            nameTextView.setText(intent.getStringExtra("name"));
            paymentType = "Merchant_Pay";
            accountNumber = intent.getStringExtra("accountNumber");
            getSupportActionBar().setTitle("MerchantPay");
            if(intent.getStringExtra("type")!=null && intent.getStringExtra("type").equals("inApp")){
                inApp = true;
            }
            if (paymentModel.getQrModels().get(0).getPaymentCategory().contains("main_web")){
                Log.d("web_purchase","main_web");

                web_purchase = true;
                //MQTTClient.publish();

            }

        }

        merchantId = intent.getStringExtra("id");
        idTextView.setText(registedId);



        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                payButton.setEnabled(false);

                if(checkError()) {
                    String amount = amountTextView.getText().toString();

                    //Log.d("CheckoutActivity:number",phoneNumber);

                    //pay transaction

                    Double total = Double.parseDouble(amount.toString().replaceAll("[$, LKR]", ""));
                    Log.d("total", total.toString());
                    if (paymentModel.isTip() && !tipTextView.getText().toString().equals("")) {
                        total += Double.parseDouble(tipTextView.getText().toString().replaceAll("[$, LKR]", ""));
                        Log.d("total", total.toString());
                    }
                    showmessgebox(paymentType, total + " LKR", amount, intent.getStringExtra("name"), intent);

                }
                payButton.setEnabled(true);
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

    private boolean checkError(){

        if(amountTextView.getText().toString().replaceAll("[$, LKR]", "").equals("")){
            amountErr.setError("Enter Amount");

            return false;

        }

        return true;

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
    private void payTransaction(String paymentType, String merchantId, final String amount, String accessToken, final Intent intent, final boolean tip){

        JSONObject pay = new JSONObject();
        try {
            pay.put("merchantId",""+merchantId);
            pay.put("amount",""+amount.replaceAll("[$, LKR]", ""));
            pay.put("accessToken",""+accessToken);
            pay.put("paymentType",paymentType);

            if(tip){
                pay.put("paymentCategory",paymentModel.getQrModels().get(1).getPaymentCategory());
                String customType = "";
                for (String type:
                        paymentModel.getQrModels().get(1).getCustomTypes()) {
                    customType = customType+""+type+":";
                }
                if (customType.equals("")){

                }
                else {
                    pay.put("customParam",customType);
                }

            }
            else {
                pay.put("paymentCategory", paymentModel.getQrModels().get(0).getPaymentCategory());
                String customType = "";
                for (String type:
                paymentModel.getQrModels().get(0).getCustomTypes()) {
                    customType = customType+""+type+":";
                }
                if (customType.equals("")){

                }
                else {
                    pay.put("customParam",customType);
                }
            }
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
                showProgress(false);
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
                        showProgress(true);
                        payTransaction(paymentType,intent.getStringExtra("id"),amount, Api.getAccessToken(getApplicationContext()),intent,false);
                        Log.d("merchantId",intent.getStringExtra("id"));
//                        if(paymentModel.isTip() ){
//                            String tipamount = tipTextView.getText().toString().replaceAll("[$, LKR]", "");;
//                            if(!tipamount.equals("")&& Double.parseDouble(tipamount)>0.0){
//                                payTransaction(paymentType,tipperId,tipTextView.getText().toString(), Api.getAccessToken(getApplicationContext()),intent,true);
//                                Log.d("tipperid",tipperId);
//                            }
//
//                        }

                        //Toast.makeText(getApplicationContext(), "Payment Successful", Toast.LENGTH_LONG).show();

                    }
                });

        alertDialog.show();


    }

    @Override
    public void onBackPressed() {
//        Intent myIntent = new Intent(CheckoutActivity.this, scanActivity.class);
//       CheckoutActivity.this.startActivity(myIntent);
        this.back = true;
        finish();
        moveHome();


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

//        if(!back){
//            finish();
//            //moveLogin();
//        }




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
        showProgress(false);
        if(result.has("data")){
            JSONArray array= (JSONArray) result.opt("data");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                Log.d("Transaction:Checkout",jsonObject.toString());
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                if(!tip) {
                    Log.d("No tip", "" + amount + " has been reciveded from " + Api.getNic(getApplicationContext()));
                    Log.d("No tip", "LKR " + amount + " has been payed to " + intent.getStringExtra("name").toString());
                    String msg_to_payee = "payment on your account ending with ";
                    Log.d("phonenumb1", phoneNumber);
                    Log.d("phonenumb2", Api.getPhoneNumber(getApplicationContext()));
                    mainTransactionId = jsonObject.optString("transactionId").toString();
                    mainAmount = amount;
                    if (scannerType) {

                        String addresssplite[] = address.split(",");
                        Log.d("firstName",Api.getFirstName(getApplicationContext()));

                        String payerMsg = Api.getLastName(getApplicationContext())+" a payment on your account ending with "+accountNumber.substring(accountNumber.length()-4,accountNumber.length())+ " for "+amount+" on " +dateFormat.format(new Date())+" at PLACE("+username+"),"+addresssplite[2]+","+ "is approved and Dr from your account.";
                        String payerAccount = Api.getAccountNumber(getApplicationContext());
                        String payeeMsg = username+" a payment has been done to your account ending with "+payerAccount.substring(payerAccount.length()-4,payerAccount.length())+ " for "+amount+" on " +dateFormat.format(new Date())+" by "+Api.getLastName(getApplicationContext())+".";

                        Api.sendSms(Api.getPhoneNumber(getApplicationContext()), payerMsg,getApplicationContext());
                        Api.sendSms(phoneNumber, payeeMsg,getApplicationContext());

                    } else {

                        String payerMsg = Api.getLastName(getApplicationContext())+" a fund transfer has been made by your account ending with "+accountNumber.substring(accountNumber.length()-4,accountNumber.length())+" for "+amount+" on " +dateFormat.format(new Date())+" ,Your fund transfer has been successfully Dr by your account.";
                         Api.sendSms(Api.getPhoneNumber(getApplicationContext()), "LKR "+amount+" has been tranfered to "+intent.getStringExtra("name"),getApplicationContext());
                         Api.sendSms(phoneNumber, ""+amount+" has been transfered from "+Api.getNic(getApplicationContext()),getApplicationContext());
                        FundTransaferMsg(paymentModel.getQrModels().get(0).getPaymentCategory(), "split");
                    }
                    if (paymentModel.isTip()) {

                        String tipamount = tipTextView.getText().toString().replaceAll("[$, LKR]", "");;
                            if(!tipamount.equals("")&& Double.parseDouble(tipamount)>0.0){
                                payTransaction(paymentType,tipperId,tipTextView.getText().toString(), Api.getAccessToken(getApplicationContext()),intent,true);
                                Log.d("tipperid",tipperId);
                            }
                            else {
                                moveToFinishActivity(amount, intent.getStringExtra("name"), jsonObject.optString("transactionId").toString());
                            }

                    } else {

                        moveToFinishActivity(amount, intent.getStringExtra("name"), jsonObject.optString("transactionId").toString());
                    }
                }
                else {
                    Log.d("tip",""+amount+" has been reciveded from "+Api.getNic(getApplicationContext()));
                    Log.d("tip","LKR "+amount+" has been payed to "+jsonObject.optString("toAccount").toString());

                    tipTransactionId = jsonObject.optString("transactionId").toString();
                    moveToFinishActivity(amount, intent.getStringExtra("name"), mainTransactionId,tipTransactionId,tipTextView.getText().toString());
//                        String tipamount = tipTextView.getText().toString().replaceAll("[$, LKR]", "");;
//                        if(!tipamount.equals("")&& Double.parseDouble(tipamount)>0.0){
//                            payTransaction(paymentType,tipperId,tipTextView.getText().toString(), Api.getAccessToken(getApplicationContext()),intent,false);
//                            Log.d("tipperid",tipperId);
//                        }


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
        myIntent.putExtra("inApp",inApp);
        myIntent.putExtra("tip",false);
        CheckoutActivity.this.startActivity(myIntent);
        this.complete = true;

        if(web_purchase){
            sendMsg(reciptNumber,name);
        }



    }
    private void moveToFinishActivity(String amount,String name,String mainTransactionId,String tipTransactionId,String tipamount){

        Intent myIntent = new Intent(CheckoutActivity.this, finishActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        myIntent.putExtra("amount",mainAmount);
        myIntent.putExtra("payee",name);
        myIntent.putExtra("recept",mainTransactionId);
        myIntent.putExtra("tipId",tipTransactionId);
        myIntent.putExtra("tipAmount",tipamount);
        myIntent.putExtra("tip",true);
        myIntent.putExtra("inApp",inApp);
        CheckoutActivity.this.startActivity(myIntent);
        this.complete = true;

        if(web_purchase){
            sendMsg(mainTransactionId,name);
        }



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
                this.back = true;
                finish();
                moveHome();
                break;
        }
        return true;
    }
    private void sendMsg(final String transactionId, final String merchantName){

        new Thread(new Runnable() {
            public void run() {
                // a potentially  time consuming task
                MQTTClient mqttClient = new MQTTClient();

                try {
                    mqttClient.initializeMQTTClient(getBaseContext(), "directpay_"+System.currentTimeMillis(), false, false, null, null);
                    JSONArray payloadArray = new JSONArray();


                    //String payload = '{"id":'"+transactionId+"','name':'"+merchantName+"','success':'true'}'';
                    JSONObject obj = new JSONObject();
                    obj.put("id", transactionId);
                    obj.put("name", merchantName);
                    obj.put("amount", amountTextView.getText().toString());
                    obj.put("success", true);
                    String payload = obj.toString();
                    byte[] encodedPayload = new byte[0];
                    encodedPayload = payload.getBytes("UTF-8");
                    mqttClient.publish("Supun",2,encodedPayload);
                    mqttClient.disconnect();

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
    private void FundTransaferMsg(final String topic, final String payload) {

        new Thread(new Runnable() {
            public void run() {
                // a potentially  time consuming task
                MQTTClient mqttClient = new MQTTClient();

                try {
                    mqttClient.initializeMQTTClient(getBaseContext(), "directpay_"+System.currentTimeMillis(), false, false, null, null);
                    byte[] encodedPayload = new byte[0];
                    encodedPayload = payload.getBytes("UTF-8");
                    mqttClient.publish(topic,2,encodedPayload);
                    mqttClient.disconnect();

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
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

//            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}
