package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class pinActivity extends AppCompatActivity {
    private EditText pin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_pin);

        pin = (EditText) findViewById(R.id.reg_pin);
        final Button ok_btn = (Button) findViewById(R.id.ok_button);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(v);

            }
        });
    }

    private void registerUser(View v){

        String pintext = pin.getText().toString();
        Log.d("user id",""+Api.getRegisterId(getApplicationContext()));
        JSONObject payload = new JSONObject();
        try {
            payload.put("userId",Api.getRegisterId(getApplicationContext()));
            //payload.put("userId","1234");
            payload.put("verificationCode",pintext);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(!pintext.isEmpty()){
            Api.userRegisterUsingPin(new Api.VolleyCallback(){
                @Override
                public void onSuccess(JSONObject result){
                    if(result.has("data")){
                        JSONArray array= (JSONArray) result.opt("data");
                        try {
                            JSONObject jsonObject = array.getJSONObject(0);
                            Log.d("sss", jsonObject.opt("success").toString() );
                            Api.setRegisterVerify(getApplicationContext(),"true");
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
                                Toast.makeText(getApplicationContext(),"Invalid Verificaton Code",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            },getApplicationContext(),payload);
        }

    }
    private void moveToLogin(){

        Intent myIntent = new Intent(pinActivity.this, loginActivity.class);
        pinActivity.this.startActivity(myIntent);
    }
}
