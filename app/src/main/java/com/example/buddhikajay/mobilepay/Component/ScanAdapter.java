package com.example.buddhikajay.mobilepay.Component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.buddhikajay.mobilepay.Model.ScanListModel;
import com.example.buddhikajay.mobilepay.R;

import java.util.ArrayList;

/**
 * Created by supun on 31/05/17.
 */

public class ScanAdapter extends ArrayAdapter<ScanListModel> {

    public ScanAdapter(Context context, ArrayList<ScanListModel> model) {
        super(context, 0, model);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ScanListModel scanListModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sacn_item, parent, false);
        }
        // Lookup view for data population
        TextView tag = (TextView) convertView.findViewById(R.id.tag);
        ImageView logo =(ImageView) convertView.findViewById(R.id.logo);
        //TextView Amount = (TextView) convertView.findViewById(R.id.amount);
        //TextView Date = (TextView) convertView.findViewById(R.id.date);
        // Populate the data into the template view using the data object
        tag.setText(scanListModel.getTag());
        logo.setImageResource(scanListModel.getLogo());




        // Return the completed view to render on screen
        return convertView;
    }

}
