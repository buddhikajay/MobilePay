package com.example.buddhikajay.mobilepay.Services;

/**
 * Created by supun on 01/06/17.
 */

public class Parameter {
    public static boolean dev=false;


    //prod
    public static String clientkey= "FXSYUYqymoqIhPrzBJSxfCC2iHQa";
    public static String secretkey = "50N3F6NEDxWmyYibVW46k8_jtnga";
    public static String identityServer = "https://13.58.144.197:9446";
    public static String apim = "https://13.58.144.197:8243";
    //dev
//    public static String clientkey= "i8XRnR_jYfpntyN_smyhZQXyr7Qa";
//    public static String secretkey = "VcrOMqj6g0MPT4gj5dM47Zvu4Cwa";
//    public static String identityServer = "https://192.168.8.103:9446";
//    public static String apim = "https://192.168.8.103:8243";

    //http://10.254.102.141
//    public static String clientkey= "i8XRnR_jYfpntyN_smyhZQXyr7Qa";
//    public static String secretkey = "VcrOMqj6g0MPT4gj5dM47Zvu4Cwa";
//    public static String identityServer = "https://192.168.43.82:9446";
//    public static String apim = "https://192.168.43.82:8243";

    //dev
//    public static String clientkey= "R7kXceG1BtY962yH5PJTIHiyPlMa";
//    public static String secretkey = "NVbkmXYt25V3fMXAXx41gZOpqVIa";
//    public static String identityServer = "https://192.168.8.100:9446";
//    public static String apim = "https://192.168.8.100:8243";


    public static String mechantpayUrl= apim+"/merchantpay/1.0.0/transaction/merchantpay";
    public static String loginUrl = identityServer+"/oauth2/token";
    public static String  registerUrl = apim+"/merchantpay/1.0.0/register";
    public static String  registerVerifyUrl = apim+"/merchantpay/1.0.0/register/verify";
    public static String  urlMerchantDetail = apim+"/merchantpay/1.0.0/merchant/details";
    public static String  urlTransactionDetail = apim+"/merchantpay/1.0.0/transactions/agent/between";
    public static String  urlLogin = apim+"/merchantpay/1.0.0/requestChangePasswordByNic";
    public static String  urlVoidTransaction = apim+"/merchantpay/1.0.0/transaction/void";
    public static String  urlUserRole = apim+"/merchantpay/1.0.0/default/user/role";
    public static String  urlResendPin = apim+"/merchantpay/1.0.0/resend/pin";
    public static String  urlForgotPassword = apim+"/merchantpay/1.0.0/forgot/password";
    public static String  urlDirectpayLogin = apim+"/merchantpay/1.0.0/directpay/user/login";
    //private String url = "https://192.168.8.102:8243/mobilepay/1.0.0/register";
    //authenticate crential

    //MQTT param

    public static String MQTT_BROKER_URL ="tcp://iot.eclipse.org:1883";

    public static String urlForgetPasswordNIC = apim+"/merchantpay/1.0.0/forgot/password/nic";

    //parameters to limit transaction details
    public static int page=1;
    public static int offset=5;


//static {
//    if (dev){
//        clientkey = "i8XRnR_jYfpntyN_smyhZQXyr7Qa";
//        secretkey = "VcrOMqj6g0MPT4gj5dM47Zvu4Cwa";
//        identityServer = "https://192.168.8.103:9446";
//        apim = "https://192.168.8.103:8243";
//    }
//}
}
