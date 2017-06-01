package com.example.buddhikajay.mobilepay.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
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
import com.android.volley.NetworkResponse;
import com.example.buddhikajay.mobilepay.MySingleton;
import com.example.buddhikajay.mobilepay.R;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by supun on 07/05/17.
 */

public class Api {

    // url
    //private static String ip="https://192.168.8.103";
    private static String identityServer = "https://192.168.8.103:9446";
    private static String apim = "https://192.168.8.103:8243";
    private static String mechantpayUrl= apim+"/merchantpay/1.0.0/transaction/merchantpay";
    private static String loginUrl = identityServer+"/oauth2/token";
    private static String  registerUrl = apim+"/merchantpay/1.0.0/register";
    private static String  registerVerifyUrl = apim+"/merchantpay/1.0.0/register/verify";
    public static String  urlMerchantDetail = apim+"/merchantpay/1.0.0/merchant/details";
    public static String  urlTransactionDetail = apim+"/merchantpay/1.0.0/transactions/agent/between";
    //private String url = "https://192.168.8.102:8243/mobilepay/1.0.0/register";
    //authenticate crential
    private static String clientkey= "kmuf4G6ifQNaHLbm0znhEvD2kgYa";
    private static String secretkey = "1lA5dSLYQC5r0li6d3hp_RqLp6Ma";


    public static String getAccessToken(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.access_token), Context.MODE_PRIVATE);
        String token =sharedPref.getString("access_token",null);

        return token;
    }
    public static String getNic(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String nic =sharedPref.getString("nic",null);

        return nic;
    }
    public static String getPhoneNumber(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String nic =sharedPref.getString("phoneNumber",null);

        return nic;
    }
    public static boolean setAccessToken(Context context,String token){
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.access_token), context.MODE_PRIVATE).edit();
        editor.putString("access_token", token);
        editor.apply();
        return true;
    }
    public static boolean setRegisterId(Context context,String id,String nic,String phone){
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.register), context.MODE_PRIVATE).edit();
        editor.putString("register_id", id);
        editor.putString("nic", nic);
        editor.putString("phoneNumber", phone);
        editor.putString("register", "true");
        editor.putString("register_verify", "false");
        editor.apply();

        return true;
    }

    public static boolean setRegisterVerify(Context context,String id){
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.register), context.MODE_PRIVATE).edit();

        editor.putString("register_verify", "true");
        editor.apply();
        return true;
    }
    public static String getRegisterId(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String token =sharedPref.getString("register_id",null);

        return token;
    }
    public static boolean isRegisterVerify(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String token =sharedPref.getString("register_verify",null);
        if(token!=null && token.equals("true")){
            return true;
        }
        return false;
    }
    public static boolean isRegister(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String token =sharedPref.getString("register",null);
        if(token!=null && token.equals("true")){
            return true;
        }
        return false;
    }
    public static void userRegister(final VolleyCallback callback,final Context context,JSONObject user){
        Log.d("register payload",user.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, registerUrl,user , new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTxtDisplay.setText("Response: " + response.toString());
                        Log.d("response register" ,""+response.toString());
                        callback.onSuccess(response);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            Toast.makeText(context,"Unauthorized",Toast.LENGTH_LONG).show();
                        }
                        else if(networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR){
                            Toast.makeText(context,"internal server error",Toast.LENGTH_LONG).show();

                        }

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(context,"time out",Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            Toast.makeText(context,"incorrect detail",Toast.LENGTH_LONG).show();
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
    public static void merchantpay(final VolleyCallback callback,final String token,JSONObject pay,Context context){



        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST,mechantpayUrl,pay , new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response",""+response.toString());
                        callback.onSuccess(response);
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


    public static void authenticateUser(final VolleyCallback callback,final Map<String,String> payload, final Context context){

        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, loginUrl, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //mTxtDisplay.setText("Response: " + response.toString());
                        Log.d("response",response.toString());
                        try {
                            callback.onSuccess(new JSONObject(response.toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        // As of f605da3 the following should work
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            Toast.makeText(context,"Unauthorized",Toast.LENGTH_LONG).show();
                        }
                        else if(networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST){
                            Toast.makeText(context,"password wrong",Toast.LENGTH_LONG).show();

                        }
                        else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(context,"time out",Toast.LENGTH_LONG).show();
                        }  else if (error instanceof ServerError || networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
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
                Map<String,String> params=payload;
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
    public static void userRegisterUsingPin(final VolleyCallback callback,Context context,JSONObject user){

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, registerVerifyUrl,user , new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTxtDisplay.setText("Response: " + response.toString());
                        Log.d("response register" ,""+response.toString());
                        callback.onSuccess(response);

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
    public static void api(final VolleyCallback callback,String url,final String token,JSONObject parameters,final Context context){



        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST,url,parameters , new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response",""+response.toString());
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("err",error.getMessage());

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            Toast.makeText(context,"Unauthorized",Toast.LENGTH_LONG).show();
                        }
                        else if(networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST){
                            Toast.makeText(context,"error",Toast.LENGTH_LONG).show();

                        }
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(context,"time out",Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            Toast.makeText(context,"incorrect account number or password",Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError || networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
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




    public interface VolleyCallback{
        void onSuccess(JSONObject result);
    }

    public static void sendSms(String phoneNumber, String message,Context context) {
        try{


            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Log.d("exception:sms","Sms");
            Toast.makeText(context,
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            Log.d("exception:sms","Sms");
        }
        catch (Error error){

        }




    }
}
