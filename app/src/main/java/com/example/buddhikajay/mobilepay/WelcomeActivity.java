package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.os.Handler;
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

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                doStuff();
            }
        }, 5000);


    }
    private void doStuff() {

        appState();
        finish();
    }
    private void appState(){

        boolean verify = Api.isRegisterVerify(getApplicationContext());
        boolean firstLigon = Api.isFirstTimeLogin(getApplicationContext());
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
}
