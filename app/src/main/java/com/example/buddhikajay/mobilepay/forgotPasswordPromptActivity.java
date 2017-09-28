package com.example.buddhikajay.mobilepay;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class forgotPasswordPromptActivity extends AppCompatActivity {

    private EditText nicField;
    private View mProgressView;

    Button nextButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_prompt);


        nicField = (EditText) findViewById(R.id.login_nic);
        mProgressView = findViewById(R.id.login_progress);

        nextButton=(Button)findViewById(R.id.btn_next);

        nicValidation(nicField);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ed_text = nicField.getText().toString().trim();

                if(ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null)
                {
                    nicField.setError("Enter NIC number");
                }
                else
                {
                    requestChangePasswordByNic();
                }

            }
        });

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

public void requestChangePasswordByNic() {
    showProgress(true);

        JSONObject params=new JSONObject();
        try {
            params.put("nic",nicField.getText());

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

                nextButton.setEnabled(true);
                showProgress(false);
                //v.setVisibility(View.VISIBLE);
            }
        }, Parameter.urlForgetPasswordNIC,"",params,getApplicationContext());


    }
    private void responseProcess(JSONObject result){

        if(result.has("data")) {
            showProgress(false);
            JSONArray array = (JSONArray) result.opt("data");
            try {
                if (array.length() != 0) {
                    JSONObject jsonObject = array.getJSONObject(0);
                    String phoneNumber = jsonObject.getString("phoneNumber");
                    String verificationCode = jsonObject.getString("verificationCode");
                    String id = jsonObject.getString("id");
                    Api.sendSms(phoneNumber,String.format("%.2f", Double.parseDouble(verificationCode)),getApplicationContext());
                    Api.setNic(getApplicationContext(),nicField.getText().toString());
                    Api.setForgetPasswordRequest(getApplicationContext());
                    Api.setId(getApplicationContext(),id);
                    Intent intent = new Intent(this,pinActivity.class);
                    startActivity(intent);
                    finish();


                }else {
                    Toast.makeText(getApplicationContext(),"No Access Token in Response",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(result.has("errors")){
            showProgress(false);
            if(Api.isFirstTimeLogin(getApplicationContext())){
                Toast.makeText(getApplicationContext(),"Invalide Nic or Password",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"Wrong NIC",Toast.LENGTH_LONG).show();
            }


        }
        nextButton.setEnabled(true);
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
