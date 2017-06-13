package com.example.buddhikajay.mobilepay;

import android.content.Intent;
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

import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.SecurityHandler;

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
            Log.d("loginActivity","user login success");
            Toast.makeText(getApplicationContext(),"login",Toast.LENGTH_LONG).show();
            moveToScanActivity();

        }
        else {
            Toast.makeText(getApplicationContext(),"No Access Token in Response",Toast.LENGTH_LONG).show();
        }
    }

}
