package com.example.buddhikajay.mobilepay;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by supun on 07/05/17.
 */

public class Api {

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
    public static boolean authenticateUser(Context context,String account,String password){



        return false;
    }
    public static boolean isTokenExpire(){

        return  false;
    }
    public static void request(int method,Context context,String url, final Map<String,String> parameters,final Map<String, String> headers, Response.Listener<String> responseLisner){

        StringRequest jsObjRequest = new StringRequest
                (method, url, responseLisner, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        // As of f605da3 the following should work
                        Log.d("Volley Error", error.getMessage());


                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=parameters;
                return params;

            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = headers;
                return params;
            }

        };

        // final RequestQueue requestQueue = Volley.newRequestQueue(this, hurlStack);

        // Add a request (in this example, called stringRequest) to your RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);


    }


}
