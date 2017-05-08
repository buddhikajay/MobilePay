package com.example.buddhikajay.mobilepay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


public class loginActivity extends AppCompatActivity {

    private String url = "https://192.168.8.102:9446/oauth2/token";


    private String clientkey= "kmuf4G6ifQNaHLbm0znhEvD2kgYa";
    private String secretkey = "1lA5dSLYQC5r0li6d3hp_RqLp6Ma";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText passField = (EditText) findViewById(R.id.login_pin);
        final EditText accountField = (EditText)findViewById(R.id.accountNo);

        Button btn=(Button)findViewById(R.id.log_button);

        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                if( !accountField.getText().toString().matches("") && !passField.getText().toString().matches("")){
                        Api.authenticateUser(passField,accountField,getApplicationContext(),loginActivity.this);
                        Api.merchantpay(Api.getAccessToken(getApplicationContext()),getApplicationContext());
                    //AuthenticateUser(passField,accountField);
                    //Intent myIntent = new Intent(loginActivity.this, scanActivity.class);
                    //Intent myIntent = new Intent(loginActivity.this, registerActivity.class);
                    //loginActivity.this.startActivity(myIntent);
                }
                else {

                    showError(passField);


                }

            }
        });
    }
    private void showError(EditText passField) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        passField.startAnimation(shake);
        passField.setError("wrong pin");
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


    /* public void AuthenticateUser(final EditText passField , final EditText accountField){

        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //mTxtDisplay.setText("Response: " + response.toString());
                        Log.d("response",response.toString());
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            if(obj.has("access_token")){
                                Api.setAccessToken(getApplicationContext(),obj.getString("access_token"));
                                Log.d("accesstoken",Api.getAccessToken(getApplicationContext()));
                                //login successs go totransaction
                                Log.d("loginActivity","user login success");
                                finish();
                                Intent myIntent = new Intent(loginActivity.this, scanActivity.class);
                                loginActivity.this.startActivity(myIntent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"No Access Token in Response",Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        // As of f605da3 the following should work
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(),"time out",Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            Toast.makeText(getApplicationContext(),"incorrect account number or password",Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            //TODO
                            Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            //TODO
                            Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                            Toast.makeText(getApplicationContext(),"Error in Application(Parse Error)",Toast.LENGTH_LONG).show();
                        }
                    }
                }){
           @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("grant_type","password");
                params.put("username",""+accountField.getText().toString());
                params.put("password",""+passField.getText().toString());
                params.put("scope","openid");
                return params;

            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                String key = clientkey+":"+secretkey;
                params.put("Authorization", "Basic "+Base64.encodeToString(key.getBytes(), Base64.DEFAULT));
                return params;
            }

        };

       // final RequestQueue requestQueue = Volley.newRequestQueue(this, hurlStack);

        // Add a request (in this example, called stringRequest) to your RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

        //requestQueue.add(jsObjRequest);
    }*/
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Exit: " , Toast.LENGTH_LONG).show();
        finish();
        System.exit(0);
    }

    public void transaction(final String token){


        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, "https://192.168.8.102:8243/merchantpay/1.0.0/transaction/merchantpay", new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //mTxtDisplay.setText("Response: " + response.toString());

                            Log.d("res",response.toString());





                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        // As of f605da3 the following should work
                        Log.d("TAdfghjG", error.getMessage());


                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("merchantId","12345");
                params.put("amount","1000");
                params.put("accessToken",""+token);

                return params;

            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("Content-Type", "application/json");
                //params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");

                params.put("Authorization", "Bearer "+token);

                return params;
            }

        };

        // final RequestQueue requestQueue = Volley.newRequestQueue(this, hurlStack);

        // Add a request (in this example, called stringRequest) to your RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

    }


}
