package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.buddhikajay.mobilepay.Component.VolleyCallback;
import com.example.buddhikajay.mobilepay.Model.Merchant;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.Parameter;
import com.example.buddhikajay.mobilepay.Services.SecurityHandler;
import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ZXingScannerView.ResultHandler{


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
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           moveLogin();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
       /* int id = item.getItemId();

        if (id == R.id.nav_my_qr) {
            // Handle the camera action
        } else if (id == R.id.nav_merchant_pay) {

        } else if (id == R.id.nav_fund_transfer) {

        } else if (id == R.id.nav_Transaction_report) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        //populateScanList();
        displaySelectedScreen(item.getItemId());
        return true;
    }
    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {

            case R.id.nav_merchant_pay:
                Toast.makeText(getApplicationContext(),"Scan Your Qr Code",Toast.LENGTH_LONG).show();
                merchantPay();
                break;
            case R.id.nav_fund_transfer:
                Toast.makeText(getApplicationContext(),"Scan Your Qr Code",Toast.LENGTH_LONG).show();
                fundTransfer();
                break;
            case R.id.nav_my_qr:
                myQrCode();
                break;
            case R.id.nav_Transaction_report:
                transactionList();
                break;
        }

        //replacing the fragment
       /* if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }*/

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
    }
//    private void populateScanList() {
//        Log.d("Transaction activity","trnsaction list populating");
//        // Construct the data source
//        //ArrayList<ScanListModel> arrayOfTrnsactions = ScanListModel.getModel();
//        // Create the adapter to convert the array to views
//        //ScanAdapter adapter = new ScanAdapter(this, arrayOfTrnsactions);
//        // Attach the adapter to a ListView
//        //ListView listView = (ListView) findViewById(R.id.sacanList);
//
//
//        //listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // When clicked, show a toast with the TextView text
//
//
//                switch (position){
//
//                    case 0 : myQrCode();
//                        break;
//                    case 1 :
//                        Toast.makeText(getApplicationContext(),"Scan Your Qr Code",Toast.LENGTH_LONG).show();
//                        merchantPay();
//
//                        break;
//                    case 2 :
//                        Toast.makeText(getApplicationContext(),"Scan Your Qr Code",Toast.LENGTH_LONG).show();
//                        fundTransfer();
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
        Intent intent = new Intent(Main2Activity.this, MyQRActivity.class);
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

        Intent intent = new Intent(Main2Activity.this, UserTransactionReportActivity.class);
        startActivity(intent);
    }

    @Override
    public void handleResult(Result rawResult) {
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

            VolleyRequestHandlerApi.api(new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {

                    responseProcess(result,merchant);

                }

                @Override
                public void login() {
                    moveLogin();
                }

                @Override
                public void enableButton() {

                }
            }, Parameter.urlMerchantDetail, Api.getAccessToken(getApplicationContext()),detail,getApplicationContext());

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
        Intent myIntent = new Intent(Main2Activity.this, CheckoutActivity.class);
        myIntent.putExtra("id",merchant.getId());
        myIntent.putExtra("name",merchant.getMerchantName());
        myIntent.putExtra("address",merchant.getMerchantAddress());
        myIntent.putExtra("scannerType", this.scannerType);
        myIntent.putExtra("phoneNumber", merchant.getPhoneNumber());
        Main2Activity.this.startActivity(myIntent);
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
    public void moveLogin(){

        Intent myIntent = new Intent(Main2Activity.this, loginActivity.class);
        Main2Activity.this.startActivity(myIntent);
        finish();
    }
    private void responseProcess(JSONObject result,Merchant merchant){
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
                    Toast.makeText(getApplicationContext(),"Requested Merchant ID Does Not Exist",Toast.LENGTH_LONG).show();
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

}
