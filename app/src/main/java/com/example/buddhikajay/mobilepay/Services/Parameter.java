package com.example.buddhikajay.mobilepay.Services;

/**
 * Created by supun on 01/06/17.
 */

public class Parameter {

    //prod
    public static String clientkey= "FXSYUYqymoqIhPrzBJSxfCC2iHQa";
    public static String secretkey = "50N3F6NEDxWmyYibVW46k8_jtnga";
    public static String identityServer = "https://13.58.144.197:9446";
    public static String apim = "https://13.58.144.197:8243";
    //dev
//    public static String clientkey= "kmuf4G6ifQNaHLbm0znhEvD2kgYa";
//    public static String secretkey = "1lA5dSLYQC5r0li6d3hp_RqLp6Ma";
//    public static String identityServer = "https://192.168.8.103:9446";
//    public static String apim = "https://192.168.8.103:8243";



    public static String mechantpayUrl= apim+"/merchantpay/1.0.0/transaction/merchantpay";
    public static String loginUrl = identityServer+"/oauth2/token";
    public static String  registerUrl = apim+"/merchantpay/1.0.0/register";
    public static String  registerVerifyUrl = apim+"/merchantpay/1.0.0/register/verify";
    public static String  urlMerchantDetail = apim+"/merchantpay/1.0.0/merchant/details";
    public static String  urlTransactionDetail = apim+"/merchantpay/1.0.0/transactions/agent/between";
    public static String  urlLogin = apim+"/merchantpay/1.0.0/login";
    //private String url = "https://192.168.8.102:8243/mobilepay/1.0.0/register";
    //authenticate crential

}
