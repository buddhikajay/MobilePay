package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buddhikajay.mobilepay.Model.Merchant;
import com.example.buddhikajay.mobilepay.Model.PaymentModel;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.Parameter;
import com.example.buddhikajay.mobilepay.Services.SecurityHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;
import com.example.buddhikajay.mobilepay.Component.VolleyCallback;

public class loginActivity extends AppCompatActivity {

    private EditText passField;
    //private EditText accountField;
    private TextView signupLink;

    //private String username;
    private String password;

     Button btn;


    private boolean innerApp;
    private PaymentModel paymentModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();

        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

         passField = (EditText) findViewById(R.id.login_pin);
         //accountField = (EditText)findViewById(R.id.accountNo);

        btn=(Button)findViewById(R.id.log_button);

        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                btn.setEnabled(false);
                login(v);

            }
        });

        final Intent intent = getIntent();
        innerApp = intent.getBooleanExtra("innerApp",false);
        if(innerApp){
            paymentModel =  (PaymentModel)intent.getSerializableExtra("Paymodel");

        }

        TextView foreget_p = (TextView) findViewById(R.id.foreget_p);

        foreget_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPassword();
            }
        });

    }
    private void payInnappPerchase(){
        Merchant merchant = new Merchant(paymentModel.getQrModels().get(0).getId());
        getMerchantDetail(merchant,paymentModel);

    }
    private boolean isEnterdValideLoginData(){
        //username = accountField.getText().toString();
        password = passField.getText().toString();
        if( !password.matches("")){
            return true;
        }
        else {

            showError(passField);
        }
        return false;
    }

    private Map<String,String> getLoginCredential(){
        Map<String,String> params=new HashMap<String,String>();
        params.put("grant_type","password");
        params.put("username",""+ Api.getNic(getApplicationContext()));
        params.put("password",""+password);
        params.put("scope","openid");
        return params;
    }
    private JSONObject getLoginDetail(){
        JSONObject params=new JSONObject();
        try {
            params.put("grant_type","password");
            params.put("username",""+ Api.getNic(getApplicationContext()));
            params.put("password",""+password);
            params.put("scope","openid");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }
    private void login(final View v){
        Log.d("login data",""+Api.getNic(getApplicationContext()));
        if(isEnterdValideLoginData()){
            Map<String,String> params = getLoginCredential();

            VolleyRequestHandlerApi.authenticateUser(new VolleyCallback(){
                @Override
                public void onSuccess(JSONObject result){
                    responseProcess(result);

                }

                @Override
                public void login() {

                }

                @Override
                public void enableButton() {

                    btn.setEnabled(true);
                    //v.setVisibility(View.VISIBLE);
                }
            },params,getApplicationContext());

           /*JSONObject detailLogin = getLoginDetail();

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
                   btn.setEnabled(true);
               }
           },Parameter.loginUrl,"",detailLogin,getApplicationContext());*/
        }

            else{
            btn.setEnabled(true);
        }



    }
    private void showError(EditText passField) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        passField.startAnimation(shake);
        passField.setError("wrong pinActivity");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean passwordMatch(TextView passField){
        String userpin="1234";
        String toastMessage = passField.getText().toString();
        if(userpin.equals(toastMessage)){
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Exit: " , Toast.LENGTH_LONG).show();
        finish();
        System.exit(0);
    }


    public void moveToPinActivity(){
        finish();
        Intent myIntent = new Intent(loginActivity.this, pinActivity.class);
        loginActivity.this.startActivity(myIntent);

    }
    public void moveToScanActivity(){
        finish();
        Intent myIntent = new Intent(loginActivity.this, scanActivity.class);
        loginActivity.this.startActivity(myIntent);

    }
    public void moveToRegisterActivity(){

        finish();
        Intent myIntent = new Intent(loginActivity.this, registerActivity.class);
        loginActivity.this.startActivity(myIntent);
    }

    private void responseProcess(JSONObject result){

        if(result.has("access_token")){
            Api.setAccessToken(getApplicationContext(),result.opt("access_token").toString());
            Log.d("accesstoken",Api.getAccessToken(getApplicationContext()));
            //login successs go totransaction
            Log.d("loginActivity",""+Api.isMerchant(getApplicationContext()));
            Toast.makeText(getApplicationContext(),"login",Toast.LENGTH_LONG).show();
            if (innerApp){
                payInnappPerchase();
            }
            else
                if(!Api.isMerchant(getApplicationContext())){
                    moveToScanActivity();
                }
                else{
                    //moveToReportActivity();
                    //Toast.makeText(getApplicationContext(),"merchant",Toast.LENGTH_LONG).show();
                    moveToMerchantReport();
                }


        }
        else {
            Toast.makeText(getApplicationContext(),"No Access Token in Response",Toast.LENGTH_LONG).show();
        }
    }

    private void moveToMerchantReport() {
        finish();
        Intent intent = new Intent(this,MerchantTransactionReportActivity.class);
        startActivity(intent);
    }

    private void moveToReportActivity() {
        Intent intent = new Intent(this,UserTransactionReportActivity.class);
        startActivity(intent);
    }


    //ToDO reuseble component change architecture

    private void getMerchantDetail(final Merchant merchant,final PaymentModel paymentModel){

        JSONObject detail = new JSONObject();
        try {
            detail.put("id",""+merchant.getId());

            VolleyRequestHandlerApi.api(new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {

                    responseProcess(result,merchant,paymentModel);

                }

                @Override
                public void login() {

                }

                @Override
                public void enableButton() {

                }
            }, Parameter.urlMerchantDetail,Api.getAccessToken(getApplicationContext()),detail,getApplicationContext());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void responseProcess(JSONObject result,Merchant merchant,PaymentModel paymentModel){
        if(result.has("data")){
            JSONArray array= (JSONArray) result.opt("data");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                //merchant.setMerchantName(jsonObject.opt("merchantName").toString());
                //merchant.setMerchantAddress(jsonObject.opt("merchantAddress").toString());
                merchantDetailResponseHandler(merchant, jsonObject);
                moveToCheckoutActivity(merchant,paymentModel);
                // Log.d("scanActivity:mDetail", merchant.getMerchantName() );
                //Log.d("scanActivity:mDetail", merchant.getMerchantAddress()  );

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else if(result.has("errors")){
            JSONArray array= (JSONArray) result.opt("errors");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                if(jsonObject.opt("status").toString().equals("5000")){
                    //setContentView(R.layout.activity_scan);
                    Toast.makeText(getApplicationContext(),"Requested Merchant ID Does Not Exist",Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                    Log.d("error","5000");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private void merchantDetailResponseHandler(Merchant merchant,JSONObject result){

        merchant.setMerchantName(result.opt("merchantName").toString());
        JSONObject address = (JSONObject) result.opt("address");
        Log.d("address",address.toString());
        merchant.setMerchantAddress(address.opt("streetAddress").toString()+","+address.opt("locality").toString()+","+address.opt("region").toString());
        merchant.setPhoneNumber(result.opt("phoneNumber").toString());
        Log.d("scanActivity:mDetail", merchant.getMerchantName() );
        Log.d("scanActivity:mDetail", merchant.getMerchantAddress()  );

    }
    private void moveToCheckoutActivity(Merchant merchant, PaymentModel paymentModel) {
        Intent myIntent = new Intent(this, CheckoutActivity.class);
        myIntent.putExtra("id",  merchant.getId());
        myIntent.putExtra("name",merchant.getMerchantName());
        myIntent.putExtra("address",merchant.getMerchantAddress());
        myIntent.putExtra("scannerType", true);
        myIntent.putExtra("phoneNumber", merchant.getPhoneNumber());
        myIntent.putExtra("Paymodel",paymentModel);
        startActivity(myIntent);

    }

    private void forgetPassword(){
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(String.valueOf(R.string.register), getApplicationContext().MODE_PRIVATE).edit();
        editor.putString("register", "false");
        editor.apply();
        Log.d("forget","click");
        Intent intent = new Intent(this,registerActivity.class);
        startActivity(intent);
    }



}
