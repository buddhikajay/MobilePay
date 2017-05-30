package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.buddhikajay.mobilepay.Services.Api;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        startActivity(myIntent);

    }
    public void moveToRegisterActivity(){


        Intent myIntent = new Intent(this, registerActivity.class);
        startActivity(myIntent);
    }
}
