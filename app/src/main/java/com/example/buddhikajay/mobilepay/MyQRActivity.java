package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.buddhikajay.mobilepay.Services.Api;

import net.glxn.qrgen.android.QRCode;

public class MyQRActivity extends AppCompatActivity {

    private boolean back =false;
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

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width_px = Resources.getSystem().getDisplayMetrics().widthPixels;
            int height_px = Resources.getSystem().getDisplayMetrics().heightPixels;

            int pixeldpi = Resources.getSystem().getDisplayMetrics().densityDpi;
            float pixeldp = Resources.getSystem().getDisplayMetrics().density;

            int width_dp = (width_px/pixeldpi)*160;
            int height_dp = (height_px/pixeldpi)*160;

            Bitmap myBitmap = QRCode.from(""+code).withSize(width_px,width_px).bitmap();
            ImageView myImage = (ImageView) findViewById(R.id.imageView2);
            myImage.setImageBitmap(myBitmap);
        }
    }

    //to go back
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            back =true;
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
//        if(!back)
//        finish();
    }
    public void moveLogin(){

        Intent myIntent = new Intent(MyQRActivity.this, loginActivity.class);
        MyQRActivity.this.startActivity(myIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back = true;
        moveScan();
        finish();
    }

    private void moveScan() {
        Intent myIntent = new Intent(MyQRActivity.this, scanActivity.class);
        MyQRActivity.this.startActivity(myIntent);
        finish();
    }
}
