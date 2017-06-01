package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buddhikajay.mobilepay.Component.ScanAdapter;
import com.example.buddhikajay.mobilepay.Component.TransactionAdapter;
import com.example.buddhikajay.mobilepay.Identities.ScanListModel;
import com.example.buddhikajay.mobilepay.Identities.TransactionModel;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.SecurityHandler;
import com.example.buddhikajay.mobilepay.Identities.Merchant;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

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
        populateScanList();

/*
        Button merchantPayButton=(Button)findViewById(R.id.buttonMerchantPay);
        Button fundTransferButton = (Button)findViewById(R.id.buttonFundTransfer);
        Button myQRCodeButton = (Button)findViewById(R.id.buttonMyQRCode);
        Button transactionList = (Button)findViewById(R.id.transactionlistBtn);

        merchantPayButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                scannerType = true; // merchant pay
                QrScanner(v);
                //
            }
        });

        fundTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerType = false;//direct fund transfer
                //id = "021";
                //name = "Dinesh Eranga";
                //phoneNumber = "+94772448144";
                QrScanner(v);
            }
        });

        myQRCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(scanActivity.this, MyQRActivity.class);
                startActivity(intent);
            }
        });

        transactionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(scanActivity.this, TransactionReportActivity.class);
                startActivity(intent);
            }
        });*/
    }

    private void populateScanList() {
        Log.d("Transaction activity","trnsaction list populating");
        // Construct the data source
        ArrayList<ScanListModel> arrayOfTrnsactions = ScanListModel.getModel();
        // Create the adapter to convert the array to views
        ScanAdapter adapter = new ScanAdapter(this, arrayOfTrnsactions);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.sacanList);


        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                switch (position){

                    case 0 : myQrCode();
                        break;
                    case 1 : merchantPay();
                        break;
                    case 2 : fundTransfer();
                        break;
                    case 3 : transactionList();
                        break;
                    default:
                        break;

                }

            }
        });

    }
    private void myQrCode(){
        Intent intent = new Intent(scanActivity.this, MyQRActivity.class);
        startActivity(intent);

    }
    private void merchantPay(){

        scannerType = true; // merchant pay
        QrScanner();
    }
    private void fundTransfer(){

        scannerType = false; // merchant pay
        QrScanner();
    }
    private void transactionList(){

        Intent intent = new Intent(scanActivity.this, TransactionReportActivity.class);
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
                Merchant merchant = new Merchant(rawResult.getText());
                getMerchantDetail(merchant);
                //AlertDialog alert1 = builder.create();
                //alert1.show();
                /*Intent myIntent = new Intent(scanActivity.this, CheckoutActivity.class);
                myIntent.putExtra("userdata",rawResult.getText());
                myIntent.putExtra("scannerType", this.scannerType);
                myIntent.putExtra("id", this.id);
                myIntent.putExtra("name", this.name);
                myIntent.putExtra("address", this.address);
                myIntent.putExtra("phoneNumber", this.phoneNumber);

                scanActivity.this.startActivity(myIntent);*/
                // If you would like to resume scanning, call this method below:<br />
        // mScannerView.resumeCameraPreview(this);<br />
    }
    private void getMerchantDetail(final Merchant merchant){

        JSONObject detail = new JSONObject();
        try {
            detail.put("id",""+merchant.getId());

            Api.api(new Api.VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {

                    if(result.has("data")){
                        JSONArray array= (JSONArray) result.opt("data");
                        try {
                            JSONObject jsonObject = array.getJSONObject(0);
                            //merchant.setMerchantName(jsonObject.opt("merchantName").toString());
                            //merchant.setMerchantAddress(jsonObject.opt("merchantAddress").toString());
                            merchantDetailResponseHandler(merchant, jsonObject);
                            moveToCheckoutActivity(merchant);
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
                            if(jsonObject.opt("status").toString().equals("5000")){
                                //setContentView(R.layout.activity_scan);
                                Toast.makeText(getApplicationContext(),"wrong qr code",Toast.LENGTH_LONG).show();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);

                                Log.d("error","5000");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }
            },Api.urlMerchantDetail,Api.getAccessToken(getApplicationContext()),detail,getApplicationContext());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void merchantDetailResponseHandler(Merchant merchant,JSONObject result){

                merchant.setMerchantName(result.opt("merchantName").toString());
                merchant.setMerchantAddress(result.opt("merchantAddress").toString());
                merchant.setPhoneNumber(result.opt("phoneNumber").toString());
                //Log.d("phone Number",result.toString());
                /*JSONArray numbers = (JSONArray) result.opt("phoneNumbers");
                PhoneNumber phoneNumber = new PhoneNumber();

                try {
                    JSONObject number = (JSONObject) numbers.get(0);
                    phoneNumber.setType(number.optString("value"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PhoneNumber[] phoneNumbers = new PhoneNumber[1];
                phoneNumbers[0] = phoneNumber;
                merchant.setPhoneNumbers(phoneNumbers);
                */


                Log.d("scanActivity:mDetail", merchant.getMerchantName() );
                Log.d("scanActivity:mDetail", merchant.getMerchantAddress()  );

    }
    private void moveToCheckoutActivity(Merchant merchant) {
        Intent myIntent = new Intent(scanActivity.this, CheckoutActivity.class);
        myIntent.putExtra("id",merchant.getId());
        myIntent.putExtra("name",merchant.getMerchantName());
        myIntent.putExtra("address",merchant.getMerchantAddress());
        myIntent.putExtra("scannerType", this.scannerType);
        myIntent.putExtra("phoneNumber", merchant.getPhoneNumber());
        scanActivity.this.startActivity(myIntent);
        finish();
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
        Toast.makeText(this, "Exit: " , Toast.LENGTH_LONG).show();
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());

    }
    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        /*Intent myIntent = new Intent(scanActivity.this, loginActivity.class);
        scanActivity.this.startActivity(myIntent);
        finish();*/
    }

}
