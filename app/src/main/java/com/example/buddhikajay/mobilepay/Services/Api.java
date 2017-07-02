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
import com.example.buddhikajay.mobilepay.Component.VolleyComponent;
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



    public static void sendSms(String phoneNumber, String message,Context context) {
        try{

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, null, null);
            //Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
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