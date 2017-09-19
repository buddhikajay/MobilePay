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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by supun on 07/05/17.
 */

public class Api {


    public static String getAccessToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.access_token), Context.MODE_PRIVATE);
        String token = sharedPref.getString("access_token", null);

        return token;
    }

    public static String getNic(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String nic = sharedPref.getString("nic", null);

        return nic;
    }

    public static void setNic(Context context, String nic) {
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.register), context.MODE_PRIVATE).edit();
        editor.putString("nic", nic);
        editor.apply();
    }

    public static void setId(Context context, String id) {
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.register), context.MODE_PRIVATE).edit();
        editor.putString("id", id);
        editor.apply();
    }

    public static void setForgetPasswordRequest(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.register), context.MODE_PRIVATE).edit();
        editor.putBoolean("forgetPasswordRequest", true);
        editor.apply();
    }

    public static boolean isForgetPasswordRequest(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        boolean token = sharedPref.getBoolean("forgetPasswordRequest", false);
        return token;
    }

    public static String getAccountNumber(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String accountNumber = sharedPref.getString("accountNumber", null);

        return accountNumber;
    }

    public static void setAccountNumber(Context context, String accountNumber) {
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.access_token), context.MODE_PRIVATE).edit();
        editor.putString("accountNumber", accountNumber);
        editor.apply();

    }

    public static String getPhoneNumber(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String nic = sharedPref.getString("phoneNumber", null);

        return nic;
    }

    public static String getFirstName(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String firstName = sharedPref.getString("firstName", null);

        return firstName;
    }

    public static String getLastName(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String lastName = sharedPref.getString("lastName", null);

        return lastName;
    }

    public static boolean setAccessToken(Context context, String token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.access_token), context.MODE_PRIVATE).edit();
        editor.putString("access_token", token);
        editor.apply();
        return true;
    }

    public static boolean setRegisterId(Context context, String id, String registedId, String nic, String phone, String role, String accountNumber, String firstName, String lastName) {
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.register), context.MODE_PRIVATE).edit();
        Log.d("Apirole", role);
        editor.putString("id", id);
        editor.putString("registedId", registedId);
        editor.putString("nic", nic);
        editor.putString("phoneNumber", phone);
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.putString("accountNumber", accountNumber);
        editor.putString("role", role);
        editor.putBoolean("register", true);
        editor.putBoolean("register_verify", false);
        editor.putBoolean("first_login", false);
        editor.apply();

        return true;
    }

    public static boolean setRegisterId(Context context, String id, String registedId, String phone, String nic, String accountNumber, String firstName, String lastName) {
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.register), context.MODE_PRIVATE).edit();
        editor.putString("id", id);
        editor.putString("registedId", registedId);
        editor.putBoolean("register", true);
        editor.putBoolean("register_verify", true);
        editor.putString("phoneNumber", phone);
        editor.putBoolean("first_login", false);
        editor.putString("accountNumber", accountNumber);
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.putString("nic", nic);
        editor.apply();

        return true;
    }

    public static boolean setRegisterVerify(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.register), context.MODE_PRIVATE).edit();

        editor.putBoolean("register_verify", true);
        editor.apply();
        return true;
    }

    public static boolean reSetPin(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.register), context.MODE_PRIVATE).edit();

        editor.putBoolean("register_verify", false);
        editor.apply();
        return true;
    }

    public static String getId(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String token = sharedPref.getString("id", null);

        return token;
    }

    public static String getRegisterId(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String token = sharedPref.getString("registedId", null);

        return token;
    }

    public static boolean isRegisterVerify(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        boolean token = sharedPref.getBoolean("register_verify", false);
        return token;
    }

    public static boolean isFirstTimeLogin(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        boolean token = sharedPref.getBoolean("first_login", true);
        return token;
    }

    public static void setFirstTimeLogin(Context context, String nic) {
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.register), context.MODE_PRIVATE).edit();

        editor.putString("nic", nic);
        editor.putBoolean("first_login", false);
        editor.apply();

    }

    //save transactions
    public static boolean isSave(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.transaction), Context.MODE_PRIVATE);
        boolean token = sharedPref.getBoolean("save_transaction", false);
        return token;
    }

    public static void setSave(Context context, JSONArray transactionData) {
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.transaction), context.MODE_PRIVATE).edit();
        editor.putBoolean("save_transaction", true);
        editor.putString("transactionData", transactionData.toString());
        editor.apply();
    }

    public static JSONArray getTransactionHistory(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.transaction), Context.MODE_PRIVATE);
        String strJson = sharedPref.getString("transactionData", null);

        try {
            return new JSONArray(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JSONArray();
    }




    public static boolean isMerchant(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String token =sharedPref.getString("role",null);
        if(token!=null && token.equals("merchant")){
            return true;
        }
        return false;
    }
    public static boolean isRegister(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        boolean token =sharedPref.getBoolean("register",false);
       return  token;
    }

    public static void sendSms(String phoneNumber, String message,Context context) {
        try{
            SmsManager sms = SmsManager.getDefault();
            if(!Parameter.dev) {
                sms.sendTextMessage(phoneNumber, null, message, null, null);
                //Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
            }
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
