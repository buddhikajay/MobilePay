package com.example.buddhikajay.mobilepay;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;


public class registerActivity extends AppCompatActivity {


    //parameters
    //register url
    private String url = "https://192.168.8.105:8243/mobilepay/1.0.0/register";
    private EditText accountNo;
    private EditText nic;
    private EditText mobileNo;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_register);


        //get register parameters

        accountNo = (EditText) findViewById(R.id.accountNo);
        nic = (EditText) findViewById(R.id.nic);
        mobileNo = (EditText) findViewById(R.id.mobileNo);
        password = (EditText) findViewById(R.id.password);

        Button signup = (Button) findViewById(R.id.signup_button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userValidation(v);
            }
        });

    }
    private void userValidation(View v) {
        Log.d("registerActivity", "user validations");

        String account_number = accountNo.getText().toString();
        String nic_number = nic.getText().toString();
        String mobile_no = mobileNo.getText().toString();
        String password_t = password.getText().toString();




        //create pay load
        JSONObject payload = new JSONObject();
        try {
            payload.put("accountNo",account_number);
            payload.put("nic",nic_number);
            payload.put("mobileNo",mobile_no);
            payload.put("password",password_t);
        } catch (JSONException e) {
            e.printStackTrace();
        }


            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, url,payload , new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            //mTxtDisplay.setText("Response: " + response.toString());
                            Log.d("response register" ,""+response.toString());


                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub

                        }
                    }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    params.put("Accept", "application/json");
                    params.put("Authorization", "Bearer");
                    return params;
                }

            };


        // Add a request (in this example, called stringRequest) to your RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);






    }


}
