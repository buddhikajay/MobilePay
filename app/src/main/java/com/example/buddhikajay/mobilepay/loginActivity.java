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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
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
    public static final String TOKEN = "token";
    //private SharedPreferences.Editor editor;
    private String clientkey= "hVECPGN0o3eKGYdjzf6TaHfoVIMa";
    private String secretkey = "dCRryHuCJfy2P1di2VV2ImEF65ka";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //editor = getSharedPreferences(TOKEN, MODE_PRIVATE).edit();


        final TextView passField = (TextView)findViewById(R.id.login_pin);
        final TextView accountField = (TextView)findViewById(R.id.accountNo);

        Button btn=(Button)findViewById(R.id.log_button);

        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if( AuthenticateUser(passField,accountField)){
                    finish();
                    //Intent myIntent = new Intent(loginActivity.this, scanActivity.class);
                    Intent myIntent = new Intent(loginActivity.this, registerActivity.class);
                    loginActivity.this.startActivity(myIntent);
                }
                else {

                    showError(passField);

                }


            }
        });
    }
    private void showError(TextView passField) {
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
    public boolean AuthenticateUser(final TextView passField , final TextView accountField){

        JSONObject payload = new JSONObject();
        try {
            payload.put("grant_type","password");
            payload.put("username",""+12345);
            payload.put("password",""+123456);
            payload.put("scope","openid");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url,payload , new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTxtDisplay.setText("Response: " + response.toString());
                        Log.d("response login" ,""+response.toString());
                        //editor.putString("access_token", response.toString());

                        //editor.commit();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        // As of f605da3 the following should work
                        VolleyLog.d("TAG", "Error: " + error);


                    }
                }){
           /* @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("grant_type","password");
                params.put("username",""+12345);
                params.put("password",""+123456);
                params.put("scope","openid");
                return params;

            }*/
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("Content-Type", "application/x-www-form-urlencoded; application/json;charset=UTF-8");
                //params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                String key = clientkey+":"+secretkey;
                params.put("Authorization", "Basic "+Base64.encodeToString(key.getBytes(), Base64.DEFAULT));

                return params;
            }

        };

       // final RequestQueue requestQueue = Volley.newRequestQueue(this, hurlStack);

        // Add a request (in this example, called stringRequest) to your RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

        //requestQueue.add(jsObjRequest);
        return false;
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Exit: " , Toast.LENGTH_LONG).show();
        finish();
        System.exit(0);
    }


}
