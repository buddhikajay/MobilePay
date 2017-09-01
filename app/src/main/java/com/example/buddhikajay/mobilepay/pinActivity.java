package com.example.buddhikajay.mobilepay;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class pinActivity extends AppCompatActivity {
    private EditText pin;
    private ImageView refreshIcon;
     Button ok_btn;

    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_pin);

        pin = (EditText) findViewById(R.id.reg_pin);
        ok_btn = (Button) findViewById(R.id.ok_button);
        refreshIcon = (ImageView) findViewById(R.id.refresh_icon);
        mProgressView = findViewById(R.id.login_progress);;
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

        pinValidate(pin);

    }

    private void registerUser(View v){
        showProgress(true);
        if(pintextValidation()){
        String pintext = pin.getText().toString();
        Log.d("user id",""+ Api.getRegisterId(getApplicationContext()));
        JSONObject payload = new JSONObject();
        try {
            payload.put("userId",Api.getId(getApplicationContext()));
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
                    showProgress(false);
                }
            }, Parameter.registerVerifyUrl,"",payload,getApplicationContext());
        }
        else {
            showProgress(false);
            ok_btn.setEnabled(true);
        }

    }
    private boolean pintextValidation(){
        String pintext = pin.getText().toString();

        if (pintext.matches("")){
            pin.requestFocus();
            pin.setError("Enter Pin Number");
            showProgress(false);
            //showError(accountNoField,"Enter Account Number");

        }
        else if(!pinValidation(pin)){
            pin.requestFocus();
            pin.setError("EInvalide Pin Number");
            showProgress(false);
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
                showProgress(false);
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
            showProgress(false);
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

    private boolean pinValidation(final EditText pin) {

        if (pin.getText().toString().length() > 0) {
            Double pinVal = Double.parseDouble(pin.getText().toString());
            if (1 <= pinVal && pinVal < 2) {
                return true;
            } else return false;

        } else {
            return false;
        }

    }

    private void reSendPin(){
        Log.d("ResendPin:","");
        //Toast.makeText(getApplicationContext(),"pin send",Toast.LENGTH_LONG).show();
        JSONObject payload = new JSONObject();
        try {
            payload.put("id",Api.getId(getApplicationContext()));

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
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

//            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }

    }

    private void pinValidate(final EditText pinText) {
        pinText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pinText.removeTextChangedListener(this);
                if (s.length() == 1) {
                    if (s.charAt(0) != '1') {
                        pinText.setText(null);
                    } else {
                        pinText.setText(s + ".");
                        pinText.setSelection(2);
                    }
                } else if (s.length() == 3) {
                    if (s.charAt(2) == '.') {
                        pinText.setText(s.subSequence(0, 2));
                    }
                } else if (s.length() == 4) {
                    if (s.charAt(3) == '.') {
                        pinText.setText(s.subSequence(0, 3));
                    }
                }
                pinText.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
