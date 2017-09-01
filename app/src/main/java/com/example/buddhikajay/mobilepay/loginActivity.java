package com.example.buddhikajay.mobilepay;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.TextInputLayout;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;
import com.example.buddhikajay.mobilepay.Component.VolleyCallback;

public class loginActivity extends AppCompatActivity {

    private EditText passField;
    private EditText nicField;
    private View mProgressView;
    //private EditText accountField;
    private TextView signupLink;

    //private String username;
    private String password;

    private TextInputLayout passLayout;
    private TextInputLayout nicLayout;

     Button btn;
    Button reg_btn;


    private boolean innerApp;
    private PaymentModel paymentModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();

        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
        //Api.setRegisterVerify(getApplicationContext(),"false");
         passField = (EditText) findViewById(R.id.login_pin);
        passLayout = (TextInputLayout) findViewById(R.id.pinErr);
        nicField = (EditText) findViewById(R.id.login_nic);
        nicLayout = (TextInputLayout) findViewById(R.id.nic_error);
        mProgressView = findViewById(R.id.login_progress);;

        ImageView logo = (ImageView) findViewById(R.id.logo);

        //accountField = (EditText)findViewById(R.id.accountNo);
        passField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                passLayout.setError(null);
                return false;
            }
        });

        btn=(Button)findViewById(R.id.log_button);
        reg_btn = (Button) findViewById(R.id.register_button);

        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                btn.setEnabled(false);
                login(v);

            }
        });
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this,registerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if(Api.isRegister(getApplication()) && Api.isRegisterVerify(getApplicationContext()) || !Api.isFirstTimeLogin(getApplicationContext())){
            nicField.setVisibility(View.GONE);
            reg_btn.setVisibility(View.GONE);
            nicLayout.setVisibility(View.GONE);
            int pixeldpi = Resources.getSystem().getDisplayMetrics().densityDpi;

            logo.getLayoutParams().height = pixeldpi*150/160;
        }


        innerAppOpen();

        TextView foreget_p = (TextView) findViewById(R.id.foreget_p);

        foreget_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPassword();
            }
        });

        if(Api.isFirstTimeLogin(getApplicationContext())){

        }
        else {
            reg_btn.setVisibility(View.GONE);
            nicField.setVisibility(View.GONE);
            nicLayout.setVisibility(View.GONE);
        }
        nicValidation(nicField);

    }
    private void innerAppOpen(){
        final Intent intent = getIntent();
        innerApp = intent.getBooleanExtra("innerApp",false);
        if(innerApp){
            paymentModel =  (PaymentModel)intent.getSerializableExtra("Paymodel");

        }
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

            showError(passLayout);
        }
        return false;
    }

    private Map<String,String> getLoginCredential(){
        Map<String,String> params=new HashMap<String,String>();
        params.put("grant_type","password");
        if(Api.isFirstTimeLogin(getApplicationContext())){
            params.put("username",""+ nicField.getText().toString());
        }
        else {
            params.put("username",""+ Api.getNic(getApplicationContext()));
        }

        params.put("password",""+password);
        params.put("scope","openid");
        return params;
    }
    private JSONObject getLoginDetail(){
        JSONObject params=new JSONObject();
        try {
            params.put("grant_type","password");

            if(Api.isFirstTimeLogin(getApplicationContext())){
                params.put("username",""+ nicField.getText().toString());
                Log.d("username",""+nicField.getText().toString());
            }
            else {
                params.put("username",""+ Api.getNic(getApplicationContext()));

            }

            params.put("password",""+password);
            params.put("scope","openid");
            params.put("firstLogin",Api.isFirstTimeLogin(getApplicationContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }
    private void login(final View v){
       // Log.d("login data",""+Api.getNic(getApplicationContext()));
        showProgress(true);
        if(isEnterdValideLoginData()){
            Log.d("...login data validate.","");
            JSONObject params = getLoginDetail();

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

                    btn.setEnabled(true);
                    showProgress(false);
                    //v.setVisibility(View.VISIBLE);
                }
            }, Parameter.urlDirectpayLogin,"",params,getApplicationContext());

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
    private void showError(TextInputLayout passField) {
       // Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        //passField.startAnimation(shake);
        passField.setError("empty password");
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
        Intent myIntent = new Intent(loginActivity.this, scanActivity.class);
        loginActivity.this.startActivity(myIntent);
        finish();

    }
    public void moveToRegisterActivity(){

        finish();
        Intent myIntent = new Intent(loginActivity.this, registerActivity.class);
        loginActivity.this.startActivity(myIntent);
    }

    private void responseProcess(JSONObject result){

//        if(result.has("access_token")){
//            Api.setAccessToken(getApplicationContext(),result.opt("access_token").toString());
//            Log.d("accesstoken",Api.getAccessToken(getApplicationContext()));
//            //login successs go totransaction
//            Log.d("loginActivity",""+Api.isMerchant(getApplicationContext()));
//            Toast.makeText(getApplicationContext(),"logged in",Toast.LENGTH_LONG).show();
//
//            btn.setEnabled(true);
//            passField.setText(null);
//            if (innerApp){
//                Log.d("...innerApp...","");
//                payInnappPerchase();
//            }
//            else {
//                if (!Api.isMerchant(getApplicationContext())) {
//                    Log.d("...start scan...", "");
//                    moveToScanActivity();
//
//                } else {
//                    //moveToReportActivity();
//                    //Toast.makeText(getApplicationContext(),"merchant",Toast.LENGTH_LONG).show();
//                    Log.d("...start merchant repor", "");
//                    moveToMerchantReport();
//                }
//
//            }
//
//            if(Api.isFirstTimeLogin(getApplicationContext())){
//                Api.setFirstTimeLogin(getApplicationContext(),nicField.getText().toString());
//                Api.setRegisterId(getApplicationContext(),jsonObject.getString("id").toString());
//            }
//        }
//        else {
//            Toast.makeText(getApplicationContext(),"No Access Token in Response",Toast.LENGTH_LONG).show();
//        }

        if(result.has("data")) {
            showProgress(false);
            JSONArray array = (JSONArray) result.opt("data");
            try {
                if (array.length() != 0) {
                    JSONObject jsonObject = array.getJSONObject(0);
                    Api.setAccessToken(getApplicationContext(), jsonObject.getString("accessToken").toString());

                    btn.setEnabled(true);
                    passField.setText(null);
                    if (innerApp){
                        Log.d("...innerApp...","");
                        payInnappPerchase();
                    }
                    else {
                        if (!Api.isMerchant(getApplicationContext())) {
                            Log.d("...start scan...", "");
                            moveToScanActivity();

                        } else {
                            //moveToReportActivity();
                            //Toast.makeText(getApplicationContext(),"merchant",Toast.LENGTH_LONG).show();
                            Log.d("...start merchant repor", "");
                            moveToMerchantReport();
                        }

                    }

                    //TODO success login


                    if (Api.isFirstTimeLogin(getApplicationContext())) {
                        Log.d("username",jsonObject.getString("name"));
                        Api.setRegisterId(getApplicationContext(),jsonObject.getString("userId"),jsonObject.getString("registedId"),jsonObject.getString("phoneNumber"),nicField.getText().toString(),jsonObject.getString("accountNumber"),jsonObject.getString("firstName"),jsonObject.getString("lastName"));
                        Log.d("first time login","true");
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),"No Access Token in Response",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(result.has("errors")){
            showProgress(false);
            Toast.makeText(getApplicationContext(),"Username or Password wrong",Toast.LENGTH_LONG).show();
            btn.setEnabled(true);
        }
        btn.setEnabled(true);
    }

    private void moveToMerchantReport() {

        Intent intent = new Intent(this,MerchantTransactionReportActivity.class);
        startActivity(intent);
        finish();

    }

    private void moveToReportActivity() {
        Intent intent = new Intent(this,UserTransactionReportActivity.class);
        startActivity(intent);
    }


    //ToDO reuseble component change architecture

    private void getMerchantDetail(final Merchant merchant,final PaymentModel paymentModel){
        showProgress(true);
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
        showProgress(false);
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
        merchant.setAccountNumber(result.opt("merchantAccountNumber").toString());
        merchant.setRegistedId(result.opt("registedId").toString());
        Log.d("scanActivity:mDetail", merchant.getMerchantName() );
        Log.d("scanActivity:mDetail", merchant.getMerchantAddress()  );

    }
    private void moveToCheckoutActivity(Merchant merchant, PaymentModel paymentModel) {
        Intent myIntent = new Intent(this, CheckoutActivity.class);
        myIntent.putExtra("id",  merchant.getId());
        myIntent.putExtra("registedId",merchant.getRegistedId());
        myIntent.putExtra("name",merchant.getMerchantName());
        myIntent.putExtra("address",merchant.getMerchantAddress());
        myIntent.putExtra("scannerType", true);
        myIntent.putExtra("type", "inApp");
        myIntent.putExtra("phoneNumber", merchant.getPhoneNumber());
        myIntent.putExtra("accountNumber", merchant.getAccountNumber());
        myIntent.putExtra("Paymodel",paymentModel);
        startActivity(myIntent);

    }

    private void forgetPassword(){
        //SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(String.valueOf(R.string.register), getApplicationContext().MODE_PRIVATE).edit();
        //editor.putString("register", "false");
        //editor.apply();
        Log.d("forget","click");
        Intent intent = new Intent(this,ForgetPassword.class);
        startActivity(intent);
    }

    private void nicValidation(final EditText nic){

        nic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String regex;
                Pattern p;
                Matcher m;
                nic.removeTextChangedListener(this);
                if (s.length()<=9){
                    Log.d("change","9");
                    regex = "[^\\d]";
                    p = Pattern.compile(regex);
                    m = p.matcher(s.toString());
                    nicField.setInputType(InputType.TYPE_CLASS_NUMBER);
                    if(m.matches()){
                        String cleanString = s.toString().replaceAll(regex, "");
                        nic.setText(cleanString);

                        nic.setSelection(cleanString.length());
                    }
                    if(s.length()==9){
                        nicField.setInputType(InputType.TYPE_CLASS_TEXT);
                    }


                }
                else if(s.length()==10){
                    if(s.charAt(9)=='v' || s.charAt(9)=='V' || s.charAt(9)=='x' || s.charAt(9)=='X' || s.charAt(9)=='B' || s.charAt(9)=='b'){

                    }
                    else {
                        String cleanString = s.toString().replaceAll("[^\\d]", "");
                        nic.setText(cleanString);
                        nic.setSelection(9);
                    }
                }
                nic.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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



}
