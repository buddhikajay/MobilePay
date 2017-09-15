package com.example.buddhikajay.mobilepay;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.buddhikajay.mobilepay.Model.PaymentModel;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.QrCodeSplite;

public class WelcomeActivity extends AppCompatActivity {

    boolean innerApp;
    PaymentModel paymentModel;
    private Handler mHandler ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        finish();
    }
    private void appState(){

        boolean verify = Api.isRegisterVerify(getApplicationContext());
        //boolean firstLigon = Api.isFirstTimeLogin(getApplicationContext());
        boolean regisiter = Api.isRegister(getApplicationContext());
        if(!verify && regisiter){
            moveToPinActivity();
        }
        else {
            //signupLink.setVisibility(View.GONE);
            moveToLoginActivity();
        }


    }
    public void moveToPinActivity(){

        Intent myIntent = new Intent(this, pinActivity.class);
        startActivity(myIntent);

    }
    public void moveToLoginActivity(){

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
}
