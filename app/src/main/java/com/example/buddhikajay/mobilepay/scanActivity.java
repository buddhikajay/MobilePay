package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.buddhikajay.mobilepay.Component.ScanAdapter;
import com.example.buddhikajay.mobilepay.Component.VolleyCallback;
import com.example.buddhikajay.mobilepay.Model.PaymentModel;
import com.example.buddhikajay.mobilepay.Model.ScanListModel;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.Parameter;
import com.example.buddhikajay.mobilepay.Services.QrCodeSplite;
import com.example.buddhikajay.mobilepay.Services.SecurityHandler;
import com.example.buddhikajay.mobilepay.Model.Merchant;
import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.widget.LinearLayout.LayoutParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import android.util.DisplayMetrics;

public class scanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    //static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private ZXingScannerView mScannerView;
    private boolean scannerType =  true; // true for merchant pay, false for fund transfer
    //private String phoneNumber = "+94772448144";
//    private String phoneNumber = "+94713821925";
    //
    private String id = "0097";
    private String name = "Cargills Foodcity Bambalabitiya";
    private String address = "123 Galle Road, Colombo 04";




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_scan);




        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.manubutton);
            //getSupportActionBar().setIcon(R.drawable.menubutton);

            //populateScanList();
        }
        Button button_merchant_pay = (Button) findViewById(R.id.btn_merchant_pay);
        Button button_fund_transfer = (Button) findViewById(R.id.btn_fund_transfer);
        Button button_my_qr = (Button) findViewById(R.id.btn_my_qr);
        Button button_transaction_report = (Button) findViewById(R.id.btn_transaction_report);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width_px = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height_px = Resources.getSystem().getDisplayMetrics().heightPixels;

        int pixeldpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        float pixeldp = Resources.getSystem().getDisplayMetrics().density;

        int width_dp = (width_px/pixeldpi)*160;
        int height_dp = (height_px/pixeldpi)*160;


        Log.d("widthdp",""+width_dp);
        Log.d("height_dp",""+height_dp);

        int button_width = (width_dp-48)/2;

        int btn_size = (int) (button_width*pixeldp);
        button_merchant_pay.setLayoutParams (new LayoutParams(btn_size, btn_size));
        button_fund_transfer.setLayoutParams (new LayoutParams(btn_size, btn_size));
        button_my_qr.setLayoutParams (new LayoutParams(btn_size, btn_size));
        button_transaction_report.setLayoutParams (new LayoutParams(btn_size, btn_size));


        button_merchant_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                merchantPay();
            }
        });

        button_fund_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fundTransfer();
            }
        });

        button_my_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myQrCode();
            }
        });

        button_transaction_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transactionList();
            }
        });

    }

//    private void populateScanList() {
//        Log.d("Transaction activity","trnsaction list populating");
//        // Construct the data source
//        ArrayList<ScanListModel> arrayOfTrnsactions = ScanListModel.getModel();
//        // Create the adapter to convert the array to views
//        ScanAdapter adapter = new ScanAdapter(this, arrayOfTrnsactions);
//        // Attach the adapter to a ListView
//        ListView listView = (ListView) findViewById(R.id.sacanList);
//
//
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // When clicked, show a toast with the TextView text
//
//
//                switch (position){
//
//
//                    case 0 :
//                            Toast.makeText(getApplicationContext(),"Scan Your Qr Code",Toast.LENGTH_LONG).show();
//                            merchantPay();
//
//                        break;
//                    case 1 :
//                            Toast.makeText(getApplicationContext(),"Scan Your Qr Code",Toast.LENGTH_LONG).show();
//                            fundTransfer();
//                        break;
//                    case 2 : myQrCode();
//                        break;
//                    case 3 : transactionList();
//                        break;
//                    default:
//                        break;
//
//                }
//
//            }
//        });
//
//    }
    private void myQrCode(){
        Intent intent = new Intent(scanActivity.this, MyQRActivity.class);
        startActivity(intent);

    }
    private void merchantPay(){

        Toast.makeText(getApplicationContext(),"Scan Your Qr Code",Toast.LENGTH_LONG).show();
        scannerType = true; // merchant pay
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbtn);
        QrScanner();
    }
    private void fundTransfer(){
        Toast.makeText(getApplicationContext(),"Scan Your Qr Code",Toast.LENGTH_LONG).show();
        scannerType = false; // merchant pay
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbtn);
        QrScanner();
    }
    private void transactionList(){

        if(!Api.isMerchant(getApplicationContext())){
            moveToUserReport();
        }
        else{
            //moveToReportActivity();
            //Toast.makeText(getApplicationContext(),"merchant",Toast.LENGTH_LONG).show();
            moveToMerchantReport();
        }


    }
    private void moveToUserReport(){
        finish();;
        Intent intent = new Intent(scanActivity.this, UserTransactionReportActivity.class);
        startActivity(intent);
    }
    private void moveToMerchantReport() {
        finish();
        Intent intent = new Intent(this,MerchantTransactionReportActivity.class);
        startActivity(intent);
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here</p>
           Log.e("handler", rawResult.getText()); // Prints scan results<br />
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)</p>
             // show the scanner result into dialog box.<br />
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Intents.Scan Result");

                builder.setMessage(rawResult.getText());
                PaymentModel paymentModel = QrCodeSplite.getInstance().spliteQrCode(rawResult.getText());
                Log.d("code",paymentModel.toString());
                //QrCodeDecode
                if(scannerType){

                    Merchant merchant = new Merchant(paymentModel.getQrModels().get(0).getId());
                    getMerchantDetail(merchant,paymentModel);
                }
                else {

                    getUserDetail(paymentModel);
                }



    }


    private void getMerchantDetail(final Merchant merchant,final PaymentModel paymentModel){

        JSONObject detail = new JSONObject();
        try {
            detail.put("id",""+merchant.getId());

             VolleyRequestHandlerApi.api(new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {

                    responseProcess(result,merchant,paymentModel);

                }

                 @Override
                 public void login() {
                     moveLogin();
                 }

                 @Override
                 public void enableButton() {

                 }
             }, Parameter.urlMerchantDetail,Api.getAccessToken(getApplicationContext()),detail,getApplicationContext());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void responseProcess(JSONObject result,Merchant merchant,PaymentModel paymentModel){
        if(result.has("data")){
            JSONArray array= (JSONArray) result.opt("data");
            try {


                JSONObject jsonObject = array.getJSONObject(0);
//                JSONArray roles = jsonObject.getJSONArray("role");
//                if(roles.get(0).equals("user")){
//                    Toast.makeText(getApplicationContext(),"Requested Merchant ID Does Not Exist",Toast.LENGTH_LONG).show();
//                    Intent intent = getIntent();
//                    startActivity(intent);
//                    finish();
//                }
//                else {
                    //merchant.setMerchantName(jsonObject.opt("merchantName").toString());
                    //merchant.setMerchantAddress(jsonObject.opt("merchantAddress").toString());
                    if(jsonObject.getBoolean("active")){
                        merchantDetailResponseHandler(merchant, jsonObject);
                        //merchantList.add(merchant);
                        if(paymentModel.isTip()){
                            paymentModel.setTip(jsonObject.getBoolean("tip"));
                        }

                        moveToCheckoutActivity(merchant, paymentModel);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Merchant Deactivated",Toast.LENGTH_LONG).show();
                        Intent intent = getIntent();
                        startActivity(intent);
                        finish();
                    }

//                }

                // Log.d("scanActivity:mDetail", merchant.getMerchantName() );
                //Log.d("scanActivity:mDetail", merchant.getMerchantAddress()  );

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else if(result.has("errors")){
            JSONArray array= (JSONArray) result.opt("errors");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                if(jsonObject.opt("status").toString().equals("5005")){
                    //setContentView(R.layout.activity_scan);
                    Toast.makeText(getApplicationContext(),"Requested Merchant ID Does Not Exist",Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    startActivity(intent);

                    Log.d("error","5000");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    private void merchantDetailResponseHandler(Merchant merchant,JSONObject result){

                merchant.setMerchantName(result.opt("merchantName").toString());
                JSONObject address = (JSONObject) result.opt("address");
                Log.d("address",address.toString());
                merchant.setMerchantAddress(address.opt("streetAddress").toString()+", "+address.opt("locality").toString()+", "+address.opt("region").toString());
                merchant.setPhoneNumber(result.opt("phoneNumber").toString());
                merchant.setRegistedId(result.opt("registedId").toString());
                merchant.setAccountNumber(result.opt("merchantAccountNumber").toString());
                Log.d("scanActivity:mDetail", merchant.getMerchantName() );
                Log.d("scanActivity:mDetail", merchant.getMerchantAddress()  );

    }
    private void moveToCheckoutActivity(Merchant merchant, PaymentModel paymentModel) {
        Intent myIntent = new Intent(scanActivity.this, CheckoutActivity.class);
        myIntent.putExtra("id",  merchant.getId());
        myIntent.putExtra("name",merchant.getMerchantName());
        myIntent.putExtra("address",merchant.getMerchantAddress());
        myIntent.putExtra("registedId",merchant.getRegistedId());
        myIntent.putExtra("scannerType", this.scannerType);
        myIntent.putExtra("phoneNumber", merchant.getPhoneNumber());
        myIntent.putExtra("accountNumber", merchant.getAccountNumber());
        myIntent.putExtra("Paymodel",paymentModel);
        scanActivity.this.startActivity(myIntent);

    }

    public void QrScanner(){
          mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view<br />
        setContentView(mScannerView);
                mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.<br />
        mScannerView.startCamera();         // Start camera<br />

    }

     @Override
    public void onPause() {
            super.onPause();
         if(mScannerView!=null){
             mScannerView.stopCamera();   // Stop camera on pause<br />
         }
         //Intent myIntent = new Intent(scanActivity.this, loginActivity.class);
         //scanActivity.this.startActivity(myIntent);
         finish();
    }

    public void scanQR() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);

        integrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onBackPressed() {
        moveLogin();
//        Toast.makeText(this, "Exit: " , Toast.LENGTH_LONG).show();
//        finish();
//        android.os.Process.killProcess(android.os.Process.myPid());
        if(mScannerView!=null){
            mScannerView.stopCamera();   // Stop camera on pause<br />
//            mScannerView.setVisibility(View.GONE);
//            mScannerView = null;
//            SecurityHandler.handleSSLHandshake();
//            setContentView(R.layout.activity_scan);
//            populateScanList();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        else {
            moveLogin();
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        /*Intent myIntent = new Intent(scanActivity.this, loginActivity.class);
        scanActivity.this.startActivity(myIntent);
        finish();*/
    }
    public void moveLogin(){

        Intent myIntent = new Intent(scanActivity.this, loginActivity.class);
        scanActivity.this.startActivity(myIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(mScannerView!=null){
//                    mScannerView.stopCamera();   // Stop camera on pause<br />
//                    mScannerView.setVisibility(View.GONE);
//                    mScannerView = null;
//                    SecurityHandler.handleSSLHandshake();
//                    setContentView(R.layout.activity_scan);
                    //populateScanList();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                }
                else {
                    moveLogin();
                }
                break;
        }
        return true;
    }

    private void getUserDetail(final PaymentModel paymentModel){

        JSONObject detail = new JSONObject();
        try {
            detail.put("id",""+paymentModel.getQrModels().get(0).getId());

            VolleyRequestHandlerApi.api(new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {

                    userDetailResponseProcess(result,paymentModel);

                }

                @Override
                public void login() {
                    moveLogin();
                }

                @Override
                public void enableButton() {

                }
            }, Parameter.urlUserRole,Api.getAccessToken(getApplicationContext()),detail,getApplicationContext());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void userDetailResponseProcess(JSONObject result,PaymentModel paymentModel){

        Log.d("userRoele",result.toString());


        if(result.has("data")){
            JSONArray array= (JSONArray) result.opt("data");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                JSONArray roles = jsonObject.getJSONArray("roles");

                if(roles.get(0).equals("user")){
                    Intent myIntent = new Intent(scanActivity.this, CheckoutActivity.class);
                    myIntent.putExtra("id",jsonObject.getString("id"));
                    myIntent.putExtra("firstName",jsonObject.getString("firstName"));
                    myIntent.putExtra("lastName",jsonObject.getString("lastName"));
                    myIntent.putExtra("accountNumber",jsonObject.getString("accountNumber"));
                    myIntent.putExtra("scannerType", this.scannerType);
                    myIntent.putExtra("phoneNumber", jsonObject.getString("phoneNumber"));
                    myIntent.putExtra("Paymodel",paymentModel);
                    myIntent.putExtra("registedId",jsonObject.getString("registedId"));
                    scanActivity.this.startActivity(myIntent);
                }
                else  if(roles.get(0).equals("merchant")){
                        //TODO

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else if(result.has("errors")){
            JSONArray array= (JSONArray) result.opt("errors");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                if(jsonObject.opt("status").toString().equals("5000")){
                    //setContentView(R.layout.activity_scan);
                    Toast.makeText(getApplicationContext(),"Requested Merchant ID Does Not Exist",Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    startActivity(intent);

                    Log.d("error","5000");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }



}
