package com.example.buddhikajay.mobilepay.Services;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by supun on 29/05/17.
 */

public class MobileNumberPicker {
    private static MobileNumberPicker mobileNumberPicker;

    MobileNumberPicker(){


    }
    public static MobileNumberPicker getInstance(){
        if(mobileNumberPicker ==null){
                mobileNumberPicker = new MobileNumberPicker();
        }
        return mobileNumberPicker;

    }
    public String getPhoneNumber(Context context){

        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        String deviceId = tMgr.getDeviceId();
        Log.d("My Phone Number",mPhoneNumber);
        Log.d("My DEvice Id",deviceId);
        return mPhoneNumber;
    }


}
