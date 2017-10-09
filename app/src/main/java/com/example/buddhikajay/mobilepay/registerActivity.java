package com.example.buddhikajay.mobilepay;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.Parameter;
import com.example.buddhikajay.mobilepay.Services.SecurityHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;
import com.example.buddhikajay.mobilepay.Component.VolleyCallback;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registerActivity extends AppCompatActivity implements View.OnClickListener,DialogInterface.OnClickListener {


    //parameters
    //register url

    private EditText accountNoField;
    private EditText nicField;
    private EditText mobileNoField;
    private EditText passwordField;
    private EditText rePasswordField;
    private EditText firstNameField;
    private EditText lastNameField;
    private View mProgressView;

    private String accountNo;
    private String nic;
    private String mobileNo;
    private String password;
    private String rePassword;
    private String firstName;
    private String lastName;

    private Button termsB;
    private TextView termsT;

     Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_register);
        termsB=(Button)findViewById(R.id.termsButton);
        termsB.setOnClickListener(this);

        termsT=(TextView)findViewById(R.id.termsTextView);

        //get register parameters

        accountNoField = (EditText) findViewById(R.id.accountNo);
        nicField = (EditText) findViewById(R.id.nic);
        mobileNoField = (EditText) findViewById(R.id.mobileNo);
        passwordField = (EditText) findViewById(R.id.password);
        rePasswordField = (EditText) findViewById(R.id.retypepassword);
        firstNameField = (EditText) findViewById(R.id.first_name);
        lastNameField = (EditText) findViewById(R.id.last_name);
        mProgressView = findViewById(R.id.login_progress);;
        signup = (Button) findViewById(R.id.signup_button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup.setEnabled(false);
                //Log.d("My Mobile Number",MobileNumberPicker.getInstance().getPhoneNumber(getApplication()));
                if( isValideRgisterData()){
                    userRegister(v);
                }
                else {
                    signup.setEnabled(true);
                }


            }
        });
        mobileValidate(mobileNoField);
        nicValidation(nicField);
        //nicField.setInputType(InputType.TYPE_CLASS_NUMBER);

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
        if (id == R.id.action_logout) {
            moveLogin();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void moveLogin(){

        Intent myIntent = new Intent(registerActivity.this, loginActivity.class);
        registerActivity.this.startActivity(myIntent);
        finish();
    }


    public void onClick(View view) {
        // TODO Auto-generated method stub
        AlertDialog ad = new AlertDialog.Builder(this)
                .setMessage("By downloading, browsing, accessing or using this DirectPay mobile application, you agree to be bound by these Terms and Conditions of Use. We reserve the right to amend these terms and conditions at any time. If you disagree with any of these Terms and Conditions, you must immediately discontinue your access to the Mobile Application and your use of the services offered on the Mobile Application. Continued use of the Mobile Application will constitute acceptance of these Terms and Conditions, as may be amended from time to time.\n Do you accept all our terms and conditions?")
                .setIcon(R.drawable.ic_reprt)
                .setTitle("Terms and Conditions of DirectPay")
                .setPositiveButton("Yes", (this))
                .setNegativeButton("No", (this))
                .setNeutralButton("Cancel", (this))
                .setCancelable(false)
                .create();

        ad.show();
    }

    public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub
        switch(which){
            case DialogInterface.BUTTON_POSITIVE: // yes
                termsT.setText("You have accepted the terms and conditions! Welcom to DirectPay App!");
                break;
            case DialogInterface.BUTTON_NEGATIVE: // no
                termsT.setText("You have denied the terms and conditions. You may not register!");
                break;
            case DialogInterface.BUTTON_NEUTRAL: // neutral
                termsT.setText("Please select yes or no!");
                break;
            default:
                // nothing
                break;
        }
    }

//    private void nicValidation(final EditText nic){
//
//        nic.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String regex;
//                Pattern p;
//                Matcher m;
//                nic.removeTextChangedListener(this);
//                if (s.length()<=9 || s.length()>10){
//                    Log.d("change","9");
//                    regex = "[^\\d]";
//                    p = Pattern.compile(regex);
//                    m = p.matcher(s.toString());
//                    nicField.setInputType(InputType.TYPE_CLASS_NUMBER);
//                    if(m.matches()){
//                        String cleanString = s.toString().replaceAll(regex, "");
//                        nic.setText(cleanString);
//
//                        nic.setSelection(cleanString.length());
//                    }
//                    if(s.length()==9){
//                        nicField.setInputType(InputType.TYPE_CLASS_TEXT);
//                    }
//
//
//                }
//                else if(s.length()==10){
//                    if(s.charAt(9)=='v' || s.charAt(9)=='V' || s.charAt(9)=='x' || s.charAt(9)=='X' || s.charAt(9)=='B' || s.charAt(9)=='b' || s.toString().matches("\\d+")){
//                            if(s.toString().matches("\\d+$")){
//                                int maxLength = 12;
//                                InputFilter[] fArray = new InputFilter[1];
//                                fArray[0] = new InputFilter.LengthFilter(maxLength);
//                                nicField.setFilters(fArray);
//                                nicField.setInputType(InputType.TYPE_CLASS_NUMBER);
//                            }
//                    }
//                    else {
//                        String cleanString = s.toString().replaceAll("[^\\d]", "");
//                        nic.setText(cleanString);
//                        nic.setSelection(9);
//                    }
//                }
//                nic.addTextChangedListener(this);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//
//    }



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
                if (s.length()<=9 || s.length()>10){
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
                    if(s.charAt(9)=='v' || s.charAt(9)=='V' || s.charAt(9)=='x' || s.charAt(9)=='X' || s.charAt(9)=='B' || s.charAt(9)=='b' || s.toString().matches("\\d+")){
                        if(s.toString().matches("\\d+$")){
                            int maxLength = 12;
                            InputFilter[] fArray = new InputFilter[1];
                            fArray[0] = new InputFilter.LengthFilter(maxLength);
                            nicField.setFilters(fArray);
                            nicField.setInputType(InputType.TYPE_CLASS_NUMBER);
                        }
                        else{

                            int maxLength = 10;
                            InputFilter[] fArray = new InputFilter[1];
                            fArray[0] = new InputFilter.LengthFilter(maxLength);
                            nicField.setFilters(fArray);
                            nicField.setInputType(InputType.TYPE_CLASS_TEXT);
                        }
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
    private void mobileValidate(final EditText mobileText){
        mobileText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mobileText.removeTextChangedListener(this);
                Log.d("mobile listene","true");
                if (s.length()!=0){
                        String regex;
                        Pattern p;
                        Matcher m;
                        if(s.length() == 1){
                            //Log.d("lenght","1");
                            regex = "^[0]";
                            p = Pattern.compile(regex);
                            m = p.matcher(s.toString());
                            if(!m.matches()){
                                Log.d("length","1");
                                String cleanString = s.toString().replaceAll(regex, "");

                                mobileText.setText("");


                            }

                        }
                        else if(s.length() ==2){
                            regex = "^[0][1,7,9]";
                            p = Pattern.compile(regex);
                            m = p.matcher(s.toString());
                            if(!m.matches()){
                                //String cleanString = s.toString().replaceAll(regex, "");
                                //Log.d("length",""+s.charAt(0));
                                mobileText.setText(s.charAt(0)+"");
                                mobileText.setSelection(1);
                            }
                        }
                        else if(s.length() ==3){
                            regex = "^[0](11|71|70|75|76|77|72|78)";
                            p = Pattern.compile(regex);
                            m = p.matcher(s.toString());
                            if(!m.matches()){
                                Log.d("length3",""+s.toString());
                                //String cleanString = s.toString().replaceAll(regex, "");
                                mobileText.setText(s.charAt(0)+""+s.charAt(1));
                                mobileText.setSelection(2);
                            }

                        }


                    }

                mobileText.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private boolean isValideRgisterData(){
        accountNo = accountNoField.getText().toString();
        nic = nicField.getText().toString();
        mobileNo = mobileNoField.getText().toString();
        password = passwordField.getText().toString();
        rePassword =rePasswordField.getText().toString();
        firstName = firstNameField.getText().toString();
        lastName = lastNameField.getText().toString();
        if(firstName.equals("")){
            firstNameField.requestFocus();
            firstNameField.setError("Enter First Name");

        }
        else if(firstName.equals("")){
            firstNameField.requestFocus();
            firstNameField.setError("Enter Last Name");

        }

        else if (accountNo.matches("")){
            accountNoField.requestFocus();
            accountNoField.setError("Enter Account Number");
            //showError(accountNoField,"Enter Account Number");
        }
        else if  (nic.matches("") || nic.length() < 10){
            nicField.requestFocus();
            nicField.setError("Enter NIC");
            //showError(nicField,"Enter NIC");
        }
        else if (mobileNo.matches("") || mobileNo.length() < 10){
            mobileNoField.requestFocus();
            mobileNoField.setError("Enter Mobile Number");
        }
        else if (password.matches("") || password.length() <6){
            if(password.length() <6){
                passwordField.requestFocus();
                passwordField.setError("password length greater than 6");
            }
            else {
                passwordField.requestFocus();
                passwordField.setError("Enter Password");
            }
        }
        else if (rePassword.matches("") || rePasswordField.length() <6){
            if(rePasswordField.length() <6){
                rePasswordField.requestFocus();
                rePasswordField.setError("password length greater than 5");
            }
            else {
                rePasswordField.requestFocus();
                rePasswordField.setError("Re Enter Password");
            }

        }
        else if(password.matches(rePassword.toString()) && rePassword.length() >=6){
                return true;
            }
        else {
            Toast.makeText(getApplicationContext(),"password doesnt match",Toast.LENGTH_LONG).show();
            rePasswordField.requestFocus();
            rePasswordField.setError("password doesnt match");
        }



        return false;
    }
    private JSONObject getRegisterData(){
        //create pay load
        String imei;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();

        }
        catch (java.lang.SecurityException e){
            imei = "NO_PERMISSION";
        }
        JSONObject payload = new JSONObject();
        try {
            payload.put("accountNumber",accountNo);
            payload.put("nic",nic);
            payload.put("firstName", firstName);
            payload.put("lastName", lastName);
            payload.put("phoneNumber",mobileNo);
            payload.put("ime",imei);
            payload.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }
    private void userRegister(View v) {
        Log.d("registerActivity", "user validations");
        //Toast.makeText(getApplicationContext(),"Credential not valide",Toast.LENGTH_LONG).show();

        showProgress(true);
            JSONObject payload = getRegisterData();
            VolleyRequestHandlerApi.api(new VolleyCallback(){
                @Override
                public void onSuccess(JSONObject result){
                    //Log.d("register response",result.toString());
                    responseProcess(result);


                }

                @Override
                public void login() {

                }

                @Override
                public void enableButton() {
                    signup.setEnabled(true);
                    showProgress(false);
                }
            }, Parameter.registerUrl,"",payload,getApplicationContext());



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
        alertDialog .setMessage("Please Collect Your Verification Code From Bank");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        moveToPinActivity();
                    }
                });

        alertDialog.show();


    }
    private void responseProcess(JSONObject result){

        if(result.has("data")){
            JSONArray array= (JSONArray) result.opt("data");
            try {

                JSONObject jsonObject = array.getJSONObject(0);
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                Log.d("RegisterActivity:status","Succss");
                Log.d("RegisterActivity:id", jsonObject.opt("registedId").toString());

                JSONArray roles = jsonObject.getJSONArray("role");
                Log.d("RegisterActivity:role", roles.get(0).toString());
                Api.setRegisterId(getApplicationContext(),jsonObject.opt("id").toString(),jsonObject.opt("registedId").toString(),nic,mobileNo,roles.get(0).toString(),accountNo, firstName,lastName);
                Log.d("nic",Api.getNic(getApplicationContext()));
                Log.d("RegisterActivity:phone",Api.getPhoneNumber(getApplication()));
                //Log.d("verification code",Api.getPhoneNumber(getApplication())+""+jsonObject.opt("verificationCode").toString());


                String verificatioCode = jsonObject.opt("verificationCode").toString();
                Api.sendSms(Api.getPhoneNumber(getApplication()),String.format("%.2f", Double.parseDouble(verificatioCode)),getApplicationContext());

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

                    signup.setEnabled(true);
                }
                else if(jsonObject.opt("status").toString().equals("422")){
                    Toast.makeText(getApplicationContext(),"Account Does Not Exist",Toast.LENGTH_LONG).show();
                    signup.setEnabled(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        showProgress(false);
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
