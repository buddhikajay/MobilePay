package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.Parameter;
import com.example.buddhikajay.mobilepay.Services.SecurityHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;
import com.example.buddhikajay.mobilepay.Component.VolleyCallback;

public class pinActivity extends AppCompatActivity {
    private EditText pin;
    private ImageView refreshIcon;
     Button ok_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_pin);

        pin = (EditText) findViewById(R.id.reg_pin);
        ok_btn = (Button) findViewById(R.id.ok_button);
        refreshIcon = (ImageView) findViewById(R.id.refresh_icon);
        refreshIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshIcon.setEnabled(false);
                reSendPin();
            }
        });
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ok_btn.setEnabled(false);
                registerUser(v);

            }
        });
    }

    private void registerUser(View v){
        if(pintextValidation()){
        String pintext = pin.getText().toString();
        Log.d("user id",""+ Api.getRegisterId(getApplicationContext()));
        JSONObject payload = new JSONObject();
        try {
            payload.put("userId",Api.getRegisterId(getApplicationContext()));
            //payload.put("userId","1234");
            payload.put("verificationCode",pintext);

        } catch (JSONException e) {
            e.printStackTrace();
        }



            VolleyRequestHandlerApi.api(new VolleyCallback(){
                @Override
                public void onSuccess(JSONObject result){

                    responseProcess(result);

                }

                @Override
                public void login() {

                }

                @Override
                public void enableButton() {
                    ok_btn.setEnabled(true);
                }
            }, Parameter.registerVerifyUrl,"",payload,getApplicationContext());
        }

    }
    private boolean pintextValidation(){
        String pintext = pin.getText().toString();

        if (pintext.matches("")){
            pin.requestFocus();
            pin.setError("Enter Pin Number");
            //showError(accountNoField,"Enter Account Number");

        }
        else {
            return true;
        }
        return false;
    }
    private void responseProcess(JSONObject result){

        if(result.has("data")){
            JSONArray array= (JSONArray) result.opt("data");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                Log.d("sss", jsonObject.opt("success").toString() );
                Api.setRegisterVerify(getApplicationContext());
                //Log.d("verification code",Api.getPhoneNumber(getApplication())+""+jsonObject.opt("verificationCode").toString());
                //Api.sendSms(Api.getPhoneNumber(getApplication()),jsonObject.opt("verificationCode").toString(),getApplicationContext());
                Toast.makeText(getApplicationContext(),"Registation Success",Toast.LENGTH_LONG).show();
                finish();
                moveToLogin();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else if(result.has("errors")){
            JSONArray array= (JSONArray) result.opt("errors");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                if(jsonObject.opt("status").toString().equals("5000")){
                    pin.requestFocus();
                    pin.setError("Pin Number Wrong");
                    ok_btn.setEnabled(true);
                    //Toast.makeText(getApplicationContext(),"Invalid Verificaton Code",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void moveToLogin(){

        Intent myIntent = new Intent(pinActivity.this, loginActivity.class);
        pinActivity.this.startActivity(myIntent);
        finish();
    }
    private void reSendPin(){
        Log.d("ResendPin:","");
        //Toast.makeText(getApplicationContext(),"pin send",Toast.LENGTH_LONG).show();
        JSONObject payload = new JSONObject();
        try {
            payload.put("id",Api.getRegisterId(getApplicationContext()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyRequestHandlerApi.api(new VolleyCallback(){
            @Override
            public void onSuccess(JSONObject result){

                responseProcessResendPin(result);

            }

            @Override
            public void login() {

            }

            @Override
            public void enableButton() {
                refreshIcon.setEnabled(true);
            }
        }, Parameter.urlResendPin,"",payload,getApplicationContext());

    }

    public void responseProcessResendPin(JSONObject result){
        if(result.has("data")){
            JSONArray array= (JSONArray) result.opt("data");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                //Log.d("verification code",Api.getPhoneNumber(getApplication())+""+jsonObject.opt("verificationCode").toString());
                //Api.sendSms(Api.getPhoneNumber(getApplication()),jsonObject.opt("verificationCode").toString(),getApplicationContext());
                Api.sendSms(Api.getPhoneNumber(getApplication()),jsonObject.opt("verificationCode").toString(),getApplicationContext());

                Toast.makeText(getApplicationContext(),"pin has sent",Toast.LENGTH_LONG).show();


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else if(result.has("errors")){
            JSONArray array= (JSONArray) result.opt("errors");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                if(jsonObject.opt("status").toString().equals("5000")){
                    //Toast.makeText(getApplicationContext(),"Invalid Verificaton Code",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        refreshIcon.setEnabled(true);
    }
}
