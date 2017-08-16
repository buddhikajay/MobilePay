package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.buddhikajay.mobilepay.Component.VolleyCallback;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.Parameter;
import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPassword extends AppCompatActivity {

    private EditText passField;
    private EditText rePassField;
    private TextInputLayout rePassLayout;
    private Button passwordChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        passField = (EditText) findViewById(R.id.password);
        final TextInputLayout passLayout = (TextInputLayout) findViewById(R.id.password_error);
        rePassField = (EditText) findViewById(R.id.re_enter_password);
        rePassLayout = (TextInputLayout) findViewById(R.id.re_enter_password_error);

        passwordChange = (Button) findViewById(R.id.button_change_password);

        passField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                passLayout.setError(null);
                return false;
            }
        });

        rePassField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rePassLayout.setError(null);
                return false;
            }
        });

        passwordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordChange.setEnabled(false);
                changePassword();
            }
        });

    }

    private void changePassword() {

        String password = passField.getText().toString();
        String rePassword = rePassField.getText().toString();

        if (password.equals(rePassword)){

            JSONObject payload = new JSONObject();
            try {
                payload.put("password",password);
                payload.put("id", Api.getId(getApplicationContext()));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            VolleyRequestHandlerApi.api(new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    responseProcess(result);
                }

                @Override
                public void login() {

                }

                @Override
                public void enableButton() {
                    passwordChange.setEnabled(true);
                }
            }, Parameter.urlForgotPassword,"",payload,getApplicationContext());
        }
        else {
            rePassLayout.setError("password not match");
        }
    }
    private void responseProcess(JSONObject result){

        if(result.has("data")){
            JSONArray array= (JSONArray) result.opt("data");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();

                Api.reSetPin(getApplicationContext());
                Api.sendSms(Api.getPhoneNumber(getApplication()),jsonObject.opt("verificationCode").toString(),getApplicationContext());
                moveToLogin();
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

                    passwordChange.setEnabled(true);
                }
                else if(jsonObject.opt("status").toString().equals("422")){
                    Toast.makeText(getApplicationContext(),"Account Does Not Exist",Toast.LENGTH_LONG).show();
                    passwordChange.setEnabled(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveToLogin(){

        Intent intent  = new Intent(this,pinActivity.class);
        startActivity(intent);

    }

}
