package com.example.buddhikajay.mobilepay.Services;

import java.text.NumberFormat;

/**
 * Created by supun on 13/06/17.
 */

public class Formate {

    public static String amountFormate(String formate){
        String cleanString = formate.toString().replaceAll("[$,. LKR]", "");

        double parsed = Double.parseDouble(cleanString);
        String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

        formatted=formatted.replaceAll("[$]", "")+" LKR";
         return formatted;
    }
    public static boolean mobileNumberValidate(String mobileNumber){
        return true;
    }
    public static String idSplite(String id){
        String splite[] = id.split("-");
        return splite[0];

    }
}
