package com.example.buddhikajay.mobilepay.Component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.buddhikajay.mobilepay.Model.TransactionModel;
import com.example.buddhikajay.mobilepay.R;
import com.example.buddhikajay.mobilepay.Services.Api;

import java.util.ArrayList;

/**
 * Created by supun on 30/05/17.
 */



public class TransactionAdapter extends ArrayAdapter<TransactionModel> {


    public TransactionAdapter(Context context, ArrayList<TransactionModel> transactions) {
        super(context, 0, transactions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TransactionModel transactionModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_item, parent, false);
        }
        // Lookup view for data population
        TextView AccountNumber = (TextView) convertView.findViewById(R.id.userAccountNumber);
        TextView Amount = (TextView) convertView.findViewById(R.id.amount);
        TextView Date = (TextView) convertView.findViewById(R.id.date);
        // Populate the data into the template view using the data object
        if(Api.isMerchant(getContext())){
            AccountNumber.setText(transactionModel.getMerchantAccountNumber());
        }
        else {
            AccountNumber.setText(transactionModel.getUserAccountNumber());
        }

        Amount.setText(transactionModel.getAmount());
        Date.setText(transactionModel.getDate());
        // Return the completed view to render on screen

        return convertView;
    }
}



