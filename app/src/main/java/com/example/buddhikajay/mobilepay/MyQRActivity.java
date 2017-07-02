package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.buddhikajay.mobilepay.AppManu.MyQrCode;
import com.example.buddhikajay.mobilepay.Services.Api;

import net.glxn.qrgen.android.QRCode;

public class MyQRActivity extends AppCompatActivity {

    private boolean complete=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            String code = ""+Api.getRegisterId(getApplicationContext())+" $ user";
            Bitmap myBitmap = QRCode.from(""+ Api.getRegisterId(getApplicationContext())).withSize(400,400).bitmap();
            ImageView myImage = (ImageView) findViewById(R.id.imageView2);
            myImage.setImageBitmap(myBitmap);
        }
    }

    //to go back
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            complete=true;
            moveScan();
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPause() {
        super.onPause();

        //Intent myIntent = new Intent(scanActivity.this, loginActivity.class);
        //scanActivity.this.startActivity(myIntent);
        if(!complete)
        moveLogin();
        finish();
    }
    public void moveLogin(){

        Intent myIntent = new Intent(MyQRActivity.this, loginActivity.class);
        MyQRActivity.this.startActivity(myIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        complete = true;
        moveScan();
        finish();
    }

    private void moveScan() {
        Intent myIntent = new Intent(MyQRActivity.this, scanActivity.class);
        MyQRActivity.this.startActivity(myIntent);
        finish();
    }
}
