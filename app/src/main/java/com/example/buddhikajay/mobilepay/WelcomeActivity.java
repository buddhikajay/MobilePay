package com.example.buddhikajay.mobilepay;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.buddhikajay.mobilepay.Component.VolleyCallback;
import com.example.buddhikajay.mobilepay.Model.PaymentModel;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.Parameter;
import com.example.buddhikajay.mobilepay.Services.QrCodeSplite;
import com.example.buddhikajay.mobilepay.Services.SecurityHandler;
import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WelcomeActivity extends AppCompatActivity {

    boolean innerApp;
    PaymentModel paymentModel;
    private Handler mHandler ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_welcome);

        String test =  getIntent().getStringExtra("Paymodel");
        if(test !=null){
            innerApp = true;
            paymentModel = QrCodeSplite.getInstance().spliteQrCode(test);

        }
        else {
            innerApp = false;
        }
        int time = 1;
        if(Api.isFirstTimeLogin(getApplicationContext())){
            time =5000;
        }
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            public void run() {
                doStuff();
            }
        }, time);
Log.d("client_key", Parameter.clientkey);
Log.d("identity_server",Parameter.identityServer);

    }
    private void doStuff() {
        // Here, thisActivity is the current activity
//        if (!appPermission()) {
//
//
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)) {}
//            else {ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);}
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_PHONE_STATE)) {}
//            else {ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},2);}
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS)) {}
//            else {ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},3);}
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {}
//            else {ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},4);}
//        }
        appState();

    }
    private void appState(){

        boolean verify = Api.isRegisterVerify(getApplicationContext());
        //boolean firstLigon = Api.isFirstTimeLogin(getApplicationContext());
        boolean regisiter = Api.isRegister(getApplicationContext());
        Log.d("appstate:",Api.getAppStatus(getApplicationContext())+"");
        switch (Api.getAppStatus(getApplicationContext())){

            case 0 :
                isAppRegister();
                //moveToRegisterActivity();
                break;
            case 1 :
                moveToPinActivity();
                break;
            case 2 :
                moveToLoginActivity();
                break;
            case 3 :
                ForgetPassword();
                break;
        }

//        if(!verify && regisiter){
//            moveToPinActivity();
//        }
//        else {
//            //signupLink.setVisibility(View.GONE);
//            moveToLoginActivity();
//        }


    }
    public void moveToPinActivity(){

        Intent myIntent = new Intent(this, pinActivity.class);
        startActivity(myIntent);

    }
    public void moveToLoginActivity(){
        Log.d("Inapp","1"+innerApp);
        Intent myIntent = new Intent(this, loginActivity.class);

        if(innerApp){
            myIntent.putExtra("innerApp",true);
            myIntent.putExtra("Paymodel",paymentModel);
        }
        startActivity(myIntent);
    }
    public void moveToRegisterActivity(){

        Intent myIntent = new Intent(this, registerActivity.class);
        startActivity(myIntent);
    }
    /*private boolean appPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)return false;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)return false;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)return false;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)return false;

        return  true;
    }
    */

    public void ForgetPassword(){
        Intent myIntent = new Intent(this, ForgetPassword.class);
        startActivity(myIntent);
    }

    private void isAppRegister() {


        if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    3);
        }
        else {

            sendIme();

        }
    }

    private void sendIme(){
        String imei;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
            Log.d("ime:", imei);

        } catch (java.lang.SecurityException e) {

            imei = "NO_PERMISSION";
        }
        JSONObject payload = new JSONObject();
        try {
            payload.put("ime", imei);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Payeload", payload.toString());
        VolleyRequestHandlerApi.api(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                //Log.d("register response",result.toString());
                responseProcess(result);


            }

            @Override
            public void login() {

            }

            @Override
            public void enableButton() {

            }
        }, Parameter.urlIme, "", payload, getApplicationContext());
    }
    private void responseProcess(JSONObject result){
        Log.d("response",result.toString());
        if(result.has("data")){
            JSONArray array= (JSONArray) result.opt("data");
            try {

                JSONObject jsonObject = array.getJSONObject(0);

                boolean  isRegisterPhone = jsonObject.getBoolean("PhoneRegister");
                Log.d("IsRegisterPhone:",isRegisterPhone+"");
                if(isRegisterPhone){
                    moveToLoginActivity();
                }
                else {
                    moveToRegisterActivity();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else if(result.has("errors")){
            JSONArray array= (JSONArray) result.opt("errors");
            try {
                JSONObject jsonObject = array.getJSONObject(0);

                if(jsonObject.opt("status").toString().equals("500")){
                    Toast.makeText(getApplicationContext(),"ime not send",Toast.LENGTH_LONG).show();


                }
                else if(jsonObject.opt("status").toString().equals("422")){
                    //Toast.makeText(getApplicationContext(),"Account Does Not Exist",Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 3
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendIme();
        }
    }

}
