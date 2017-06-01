package com.example.buddhikajay.mobilepay.Services;

/**
 * Created by supun on 01/06/17.
 */

public class Parameter {

    private static String clientkey= "kmuf4G6ifQNaHLbm0znhEvD2kgYa";
    private static String secretkey = "1lA5dSLYQC5r0li6d3hp_RqLp6Ma";


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

}
