package com.example.buddhikajay.mobilepay.Services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.buddhikajay.mobilepay.Component.VolleyCallback;
import com.example.buddhikajay.mobilepay.Component.VolleyComponent;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by supun on 01/06/17.
 */

public class VolleyRequestHandlerApi {



    public static void getMerchantDetail(final VolleyCallback callback, String url, final String token, JSONObject parameters, final Context context){



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
                        //Log.d("err",error.getMessage());
                        error.printStackTrace();

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            Toast.makeText(context,"Unauthorized",Toast.LENGTH_LONG).show();
                            callback.login();
                        }
                        else if(networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST){
                            Toast.makeText(context,"error",Toast.LENGTH_LONG).show();

                        }
                        else if(error instanceof NoConnectionError){
                            Toast.makeText(context,"Connection Error",Toast.LENGTH_LONG).show();
                        }
                        else if (error instanceof AuthFailureError) {
                            //TODO
                            Toast.makeText(context,"please login again",Toast.LENGTH_LONG).show();
                        }
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
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
        VolleyComponent.getInstance(context).addToRequestQueue(jsObjRequest);
    }


}
