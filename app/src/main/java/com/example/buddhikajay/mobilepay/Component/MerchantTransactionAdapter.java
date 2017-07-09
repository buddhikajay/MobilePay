package com.example.buddhikajay.mobilepay.Component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import com.example.buddhikajay.mobilepay.Model.MerchantTransactionModel;
import com.example.buddhikajay.mobilepay.R;

import java.util.ArrayList;

/**
 * Created by supun on 09/07/17.
 */

/**
 * Created by supun on 30/05/17.
 */



public class MerchantTransactionAdapter extends ArrayAdapter<MerchantTransactionModel> {


    public MerchantTransactionAdapter(Context context, ArrayList<MerchantTransactionModel> transactions) {
        super(context, 0, transactions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MerchantTransactionModel transactionModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.merchant_transaction_item, parent, false);
        }
        // Lookup view for data population
        TextView AccountNumber = (TextView) convertView.findViewById(R.id.userAccountNumber);
        TextView Amount = (TextView) convertView.findViewById(R.id.amount);
        TextView Date = (TextView) convertView.findViewById(R.id.date);
        Button button_void = (Button) convertView.findViewById(R.id.button_void);
        // Populate the data into the template view using the data objec
        AccountNumber.setText(transactionModel.getUserAccountNumber());
        Amount.setText(transactionModel.getAmount()+" LKR");
        Date.setText(transactionModel.getDate());
        // Return the completed view to render on screen

        button_void.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button","click");

            }
        });
        return convertView;
    }
}



