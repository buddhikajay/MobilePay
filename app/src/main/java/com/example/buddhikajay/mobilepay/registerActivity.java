package com.example.buddhikajay.mobilepay;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.MobileNumberPicker;
import com.example.buddhikajay.mobilepay.Services.SecurityHandler;

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

                Log.d("My Mobile Number",MobileNumberPicker.getInstance().getPhoneNumber(getApplication()));
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

            //Log.d("register","not valide register Data");
            Toast.makeText(getApplicationContext(),"please fill register impormation",Toast.LENGTH_LONG).show();

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
        //Toast.makeText(getApplicationContext(),"Credential not valide",Toast.LENGTH_LONG).show();

        if(isValideRgisterData()){
            JSONObject payload = getRegisterData();
            Api.userRegister(new Api.VolleyCallback(){
                @Override
                public void onSuccess(JSONObject result){
                    //Log.d("register response",result.toString());
                    if(result.has("data")){
                        JSONArray array= (JSONArray) result.opt("data");
                        try {
                            JSONObject jsonObject = array.getJSONObject(0);
                            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                            Log.d("RegisterActivity:status","Succss");
                            Log.d("RegisterActivity:id", jsonObject.opt("id").toString());
                            Api.setRegisterId(getApplicationContext(),jsonObject.opt("id").toString(),nic,mobileNo);
                            Log.d("RegisterActivity:phone",Api.getPhoneNumber(getApplication()));
                            //Log.d("verification code",Api.getPhoneNumber(getApplication())+""+jsonObject.opt("verificationCode").toString());
                            Api.sendSms(Api.getPhoneNumber(getApplication()),jsonObject.opt("verificationCode").toString(),getApplicationContext());
                            finish();
                            moveToPinActivity();
                            //showmessgebox();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    else if(result.has("errors")){
                        JSONArray array= (JSONArray) result.opt("errors");
                        try {
                            JSONObject jsonObject = array.getJSONObject(0);

                            if(jsonObject.opt("status").toString().equals("409")){
                                Toast.makeText(getApplicationContext(),"User Already Exist",Toast.LENGTH_LONG).show();
                            }
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
    private void showError(EditText passField,String error) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        passField.startAnimation(shake);
        passField.setError(""+error);
    }

    private void showmessgebox(){
        AlertDialog alertDialog=new AlertDialog.Builder(registerActivity.this).create();
        alertDialog .setTitle("Account Verification");
        alertDialog .setMessage("please go to the you bank and collect pin");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        moveToPinActivity();
                    }
                });

        alertDialog.show();


    }


}
