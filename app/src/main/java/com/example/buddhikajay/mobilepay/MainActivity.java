package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.buddhikajay.mobilepay.Model.PaymentModel;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.QrCodeSplite;

public class MainActivity extends AppCompatActivity {
    boolean innerApp;
    PaymentModel paymentModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* Bundle b = getIntent().getExtras();
        if(b!=null){
            //String myString = b.getString("KEY_DATA_EXTRA_FROM_ACTV_B");
            // and any other data that the other app sent
            paymentModel = (PaymentModel) b.getSerializable("Paymodel");
            if(paymentModel!=null) {
                Log.d("paymodel", paymentModel.toString());
            }
        }*/
        /*try {
            paymentModel =  (PaymentModel)getIntent().getSerializableExtra("Paymodel");
            if(paymentModel!=null) {
                Log.d("paymodel", paymentModel.toString());
            }
        }
        catch(RuntimeException e){
            Log.d("exception run",e.toString());
        }*/
        String test =  getIntent().getStringExtra("Paymodel");
        if(test !=null){
            innerApp = true;
            paymentModel = QrCodeSplite.getInstance().spliteQrCode(test);

        }
        else {
            innerApp = false;
        }



        appState();
        finish();
    }
    private void appState(){
        boolean register = Api.isRegister(getApplication());
        boolean verify = Api.isRegisterVerify(getApplicationContext());
        if( register&& verify){
            //signupLink.setVisibility(View.GONE);
            moveToLoginActivity();
        }
        else if(register && !verify){
            moveToPinActivity();
        }
        else{
            moveToRegisterActivity();
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
