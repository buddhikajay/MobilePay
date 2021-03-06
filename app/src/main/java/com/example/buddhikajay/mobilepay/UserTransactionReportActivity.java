package com.example.buddhikajay.mobilepay;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;

import com.example.buddhikajay.mobilepay.Component.UserTransactionAdapter;
import com.example.buddhikajay.mobilepay.Model.UserTransactionModel;
import com.example.buddhikajay.mobilepay.Services.Api;
import com.example.buddhikajay.mobilepay.Services.Parameter;
import com.example.buddhikajay.mobilepay.Services.SecurityHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.buddhikajay.mobilepay.Services.VolleyRequestHandlerApi;
import com.example.buddhikajay.mobilepay.Component.VolleyCallback;
import android.text.TextWatcher;
import android.text.Editable;
import java.util.Locale;

public class UserTransactionReportActivity extends AppCompatActivity {

    private int headersize;
    private Button btn_load;
    private TextView text_from_date;
    private TextView text_to_date;
    private int mYear, mMonth, mDay;
    private int fYear, fMonth, fDay;
    private int tYear, tMonth, tDay;

    private boolean selectDate;
    private boolean selectAccount;

    private EditText searchView_account;
    private UserTransactionAdapter adapter;
    private ListView listView;
    private boolean loadDate;
    private RelativeLayout loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_transaction_report);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTransaction);
        //setSupportActionBar(toolbar);
        //final ActionBar bar = getActionBar();
        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //getTransaction();
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width_px = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height_px = Resources.getSystem().getDisplayMetrics().heightPixels;

        int pixeldpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        float pixeldp = Resources.getSystem().getDisplayMetrics().density;

        int width_dp = (width_px/pixeldpi)*160;
        int height_dp = (height_px/pixeldpi)*160;

        int button_width = (width_dp-32);

        int screen_rest_width = (int) (button_width*pixeldp);
        headersize = screen_rest_width/3;

        TextView date_header = (TextView) findViewById(R.id.date_header);
        TextView detail_header = (TextView)findViewById(R.id.detail_header);
        TextView amount_header = (TextView)findViewById(R.id.amount_header);
        listView = (ListView) findViewById(R.id.transactionList);

        searchView_account = (EditText) findViewById(R.id.search_box);


        text_from_date = (TextView) findViewById(R.id.txt_from);
        text_to_date = (TextView) findViewById(R.id.txt_todate);

        btn_load = (Button) findViewById(R.id.btn_load);
        loadingbar = (RelativeLayout) findViewById(R.id.loadingPanel);
        loadingbar.setVisibility(View.GONE);


        date_header.setWidth(headersize);
        detail_header.setWidth(headersize);
        amount_header.setWidth(headersize);

        text_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFromDate();
            }
        });
        text_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToDate(
                );
            }
        });
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setAdapter(null);
                loadingbar.setVisibility(View.VISIBLE);
                getTransaction();


            }
        });
        Search();
        currentDate();


    }

    private void Search() {
        // Capture Text in EditText
        searchView_account.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if(loadDate){
                    UserTransactionReportActivity.this.adapter.getFilter().filter(cs);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Load Transaction First",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                moveScan();
                break;
        }
        return true;
    }

    private void moveScan() {
        Intent myIntent = new Intent(UserTransactionReportActivity.this, scanActivity.class);
        UserTransactionReportActivity.this.startActivity(myIntent);
        finish();
    }

    private void populateTransactionList(JSONArray transactions) {
        Log.d("Transaction activity",transactions.toString());
        // Construct the data source
        final ArrayList<UserTransactionModel> arrayOfTrnsactions = UserTransactionModel.getTransaction(transactions,Api.getId(getApplicationContext()));
        // Create the adapter to convert the array to views
        adapter = new UserTransactionAdapter(this, arrayOfTrnsactions);
        // Attach the adapter to a ListView

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserTransactionReportActivity.this,UserTransactionDetailActivity.class);
                UserTransactionModel model = arrayOfTrnsactions.get(position);
                intent.putExtra("receipt",model.getRecieptNumber());
                intent.putExtra("roleIsMerchant",model.isOtherAccountOwnerRoleIsMerchant());
                if(model.isOtherAccountOwnerRoleIsMerchant()){
                    intent.putExtra("name",model.getMerchant().getMerchantName());
                }
                else {
                    intent.putExtra("name",model.getUser().getLastName());
                }
                intent.putExtra("amount",model.getAmount());
                intent.putExtra("date",model.getDate());
                intent.putExtra("type",model.getType());

                startActivity(intent);

            }
        });


    }
    public void getTransaction(){
        JSONObject parameter = new JSONObject();
        try {
            parameter.put("userId",""+ Api.getId(getApplicationContext()));
            JSONObject fromdate = new JSONObject();
            fromdate.put("year",fYear);
            fromdate.put("month",fMonth);
            fromdate.put("day",fDay);

            JSONObject todate = new JSONObject();
            if(fYear == tYear && fMonth==tMonth && fDay == tDay){
                getnextDay(fYear,fMonth,fDay);
            }
            todate.put("year",tYear);
            todate.put("month",tMonth);
            todate.put("day",tDay);

            parameter.put("fromDate",fromdate);
            parameter.put("toDate",todate);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        VolleyRequestHandlerApi.api(new VolleyCallback(){


            @Override
            public void onSuccess(JSONObject result) {
                responseProcess(result);
            }

            @Override
            public void login() {
                moveLogin();
            }

            @Override
            public void enableButton() {

            }
        }, Parameter.urlTransactionDetail,Api.getAccessToken(getApplicationContext()),parameter,getApplicationContext());

    }

    private void responseProcess(JSONObject result){

        if(result.has("data")){
            JSONArray array= (JSONArray) result.opt("data");
            try {
                if(array.length()!=0){
                    JSONObject jsonObject = array.getJSONObject(0);
                    Log.d("Transaction:Transaction",jsonObject.toString());
                    populateTransactionList(array);
                    loadDate=true;
                    loadingbar.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(getApplicationContext(),"No Any Transaction",Toast.LENGTH_LONG).show();
                    loadingbar.setVisibility(View.GONE);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else if(result.has("errors")){
            JSONArray array= (JSONArray) result.opt("errors");
            try {
                JSONObject jsonObject = array.getJSONObject(0);

                if(jsonObject.opt("status").toString().equals("422")){
                    Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void moveLogin(){

        Intent myIntent = new Intent(UserTransactionReportActivity.this, loginActivity.class);
        UserTransactionReportActivity.this.startActivity(myIntent);
        finish();
    }
    @Override
    public void onBackPressed() {
        //moveLogin();
        moveScan();
        finish();

    }
    @Override
    public void onPause() {
        super.onPause();
            //finish();

    }

    public void setToDate() {



        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tYear = year;
                        tMonth = monthOfYear+1;
                        tDay = dayOfMonth;

                        text_to_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();


    }



    public void setFromDate() {



        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        fYear = year;
                        fMonth = monthOfYear+1;
                        fDay = dayOfMonth;

                        text_from_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();


    }

    public void currentDate(){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        fYear =  c.get(Calendar.YEAR);
        fMonth = c.get(Calendar.MONTH) + 1;
        fDay = c.get(Calendar.DAY_OF_MONTH);

        tYear =  c.get(Calendar.YEAR);
        tMonth = c.get(Calendar.MONTH) + 1;
        tDay = c.get(Calendar.DAY_OF_MONTH);

        text_to_date.setText(fDay + "-" + fMonth + "-" + fYear);
        text_from_date.setText(tDay + "-" + tMonth + "-" + tYear);
    }
    public void getnextDay(int year,int month,int day){

        final Calendar c = Calendar.getInstance();
        c.set(year,month,day);
        c.add(Calendar.DATE,+1);
        tYear = c.get(Calendar.YEAR);
        tMonth = c.get(Calendar.MONTH)+1;
        tDay = c.get(Calendar.DAY_OF_MONTH);
    }


}
