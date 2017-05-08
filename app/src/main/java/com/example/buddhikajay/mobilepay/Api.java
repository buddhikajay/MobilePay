package com.example.buddhikajay.mobilepay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by supun on 07/05/17.
 */

public class Api {

    // url
    private static String mechantpayUrl= "https://192.168.8.102:8243/merchantpay/1.0.0/transaction/merchantpay";
    private static String loginUrl = "https://192.168.8.102:9446/oauth2/token";
    private static String  registerUrl = "https://192.168.8.102:8243/merchantpay/1.0.0/register";

    //authenticate crential
    private static String clientkey= "kmuf4G6ifQNaHLbm0znhEvD2kgYa";
    private static String secretkey = "1lA5dSLYQC5r0li6d3hp_RqLp6Ma";


    public static String getAccessToken(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.access_token), Context.MODE_PRIVATE);
        String token =sharedPref.getString("access_token",null);

        return token;
    }
    public static boolean setAccessToken(Context context,String token){
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.access_token), context.MODE_PRIVATE).edit();
        editor.putString("access_token", token);
        editor.apply();
        return true;
    }
    public static void userRegister(Context context,JSONObject user){

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, registerUrl,user , new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTxtDisplay.setText("Response: " + response.toString());
                        Log.d("response register" ,""+response.toString());


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("dss",error.getMessage());
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
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);


    }
    public static void merchantpay(final String token,JSONObject pay,Context context){



        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST,mechantpayUrl,pay , new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response",""+response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("err",error.getMessage());
                    }
                }){
            /* @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 Map<String,String> params=new HashMap<String,String>();
                 params.put("merchantId","12345");
                 params.put("amount","1000");
                 params.put("accessToken",""+token);

                 return params;

             }*/
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+token);
                return params;
            }

        };
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }


    public static void authenticateUser(final EditText passField , final EditText accountField, final Context context, final Activity activity){

        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, loginUrl, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //mTxtDisplay.setText("Response: " + response.toString());
                        Log.d("response",response.toString());
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            if(obj.has("access_token")){
                                Api.setAccessToken(context,obj.getString("access_token"));
                                Log.d("accesstoken",Api.getAccessToken(context));
                                //login successs go totransaction
                                Log.d("loginActivity","user login success");

                                activity.finish();

                                Intent myIntent = new Intent(activity, scanActivity.class);
                                activity.startActivity(myIntent);
                            }
                            else {
                                Toast.makeText(context,"No Access Token in Response",Toast.LENGTH_LONG).show();
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
                            Toast.makeText(context,"time out",Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            Toast.makeText(context,"incorrect account number or password",Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            //TODO
                            Toast.makeText(context,"Server Error",Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            //TODO
                            Toast.makeText(context,"Network Error",Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                            Toast.makeText(context,"Error in Application(Parse Error)",Toast.LENGTH_LONG).show();
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("grant_type","password");
               // params.put("username",""+accountField.getText().toString());
               // params.put("password",""+passField.getText().toString());
                params.put("username","9100");
               params.put("password","password");
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

        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);

    }


}
