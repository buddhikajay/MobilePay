package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    //static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private ZXingScannerView mScannerView;
    private boolean scannerType =  true; // true for merchant pay, false for fund transfer
//    private String phoneNumber = "714927459";
    private String phoneNumber = "+94713821925";
    //
    private String id = "0097";
    private String name = "Cargills Foodcity Bambalabitiya";
    private String address = "123 Galle Road, Colombo 04";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Button merchantPayButton=(Button)findViewById(R.id.buttonMerchantPay);
        Button fundTransferButton = (Button)findViewById(R.id.buttonFundTransfer);
        Button myQRCodeButton = (Button)findViewById(R.id.buttonMyQRCode);

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
                id = "0021";
                name = "Dinesh Eranga";
//                phoneNumber = "+94772448144";
                QrScanner(v);
            }
        });

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
                //AlertDialog alert1 = builder.create();
                //alert1.show();
                Intent myIntent = new Intent(scanActivity.this, CheckoutActivity.class);
                myIntent.putExtra("userdata",rawResult.getText());
                myIntent.putExtra("scannerType", this.scannerType);
                myIntent.putExtra("id", this.id);
                myIntent.putExtra("name", this.name);
                myIntent.putExtra("address", this.address);
                myIntent.putExtra("phoneNumber", this.phoneNumber);

                scanActivity.this.startActivity(myIntent);
                // If you would like to resume scanning, call this method below:<br />
        // mScannerView.resumeCameraPreview(this);<br />
    }
    public void QrScanner(View view){
          mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view<br />
        setContentView(mScannerView);
                mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.<br />
        mScannerView.startCamera();         // Start camera<br />
    }
     @Override
    public void onPause() {
            super.onPause();
            mScannerView.stopCamera();   // Stop camera on pause<br />
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

}
