package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class registerActivity extends AppCompatActivity {


    //parameters
    //register url

    private EditText accountNoField;
    private EditText nicField;
    private EditText mobileNoField;
    private EditText passwordField;


    private String accountNo;
    private String nic;
    private String mobileNo;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_register);


        //get register parameters

        accountNoField = (EditText) findViewById(R.id.accountNo);
        nicField = (EditText) findViewById(R.id.nic);
        mobileNoField = (EditText) findViewById(R.id.mobileNo);
        passwordField = (EditText) findViewById(R.id.password);

        Button signup = (Button) findViewById(R.id.signup_button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegister(v);
            }
        });

    }
    private boolean isValideRgisterData(){
        accountNo = accountNoField.getText().toString();
        nic = nicField.getText().toString();
        mobileNo = mobileNoField.getText().toString();
        password = passwordField.getText().toString();
        if( !accountNo.matches("") && !nic.matches("") && !mobileNo.matches("") && !password.matches("")){
            return true;
        }
        else {

            Log.d("register","not valide register Data");
        }
        return false;
    }
    private JSONObject getRegisterData(){
        //create pay load

        JSONObject payload = new JSONObject();
        try {
            payload.put("accountNumber",accountNo);
            payload.put("nic",nic);
            payload.put("phoneNumber",mobileNo);
            payload.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }
    private void userRegister(View v) {
        Log.d("registerActivity", "user validations");

        if(isValideRgisterData()){
            JSONObject payload = getRegisterData();
            Api.userRegister(new Api.VolleyCallback(){
                @Override
                public void onSuccess(JSONObject result){
                    if(result.has("data")){
                        JSONArray array= (JSONArray) result.opt("data");
                        try {
                            JSONObject jsonObject = array.getJSONObject(0);
                            Log.d("sss", jsonObject.opt("id").toString() );
                            Api.setRegisterId(getApplicationContext(),jsonObject.opt("id").toString());
                            //Log.d("sss", Api.getRegisterId(getApplicationContext()) );
                            finish();
                            moveToPinActivity();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }
            },getApplicationContext(),payload);

        }

    }

    public void moveToPinActivity(){
        Intent myIntent = new Intent(registerActivity.this, pinActivity.class);
        registerActivity.this.startActivity(myIntent);

    }


}
