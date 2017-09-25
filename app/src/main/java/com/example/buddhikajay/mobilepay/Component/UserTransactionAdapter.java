package com.example.buddhikajay.mobilepay.Component;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.buddhikajay.mobilepay.Model.UserTransactionModel;
import com.example.buddhikajay.mobilepay.R;
import com.example.buddhikajay.mobilepay.UserTransactionDetailActivity;

import java.util.ArrayList;
import java.util.List;
import android.widget.Filter;
import android.widget.Filterable;
/**
 * Created by supun on 30/05/17.
 */



public class UserTransactionAdapter extends ArrayAdapter<UserTransactionModel> {

    private List<UserTransactionModel>originalData = null;
    private List<UserTransactionModel>filteredData = null;
    private LayoutInflater mInflater;
    private ItemFilter mFilter = new ItemFilter();

    public UserTransactionAdapter(Context context, ArrayList<UserTransactionModel> transactions) {
        super(context, 0, transactions);
        this.filteredData = transactions ;
        this.originalData = transactions ;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        UserTransactionModel transactionModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_item, parent, false);
        }
        // Lookup view for data population
        TextView AccountNumber = (TextView) convertView.findViewById(R.id.userAccountNumber);
        TextView Amount = (TextView) convertView.findViewById(R.id.amount);
        TextView Date = (TextView) convertView.findViewById(R.id.date);
        // Populate the data into the template view using the data object


            if(transactionModel.isOtherAccountOwnerRoleIsMerchant()){
                AccountNumber.setText(transactionModel.getMerchant().getMerchantName());
            }
            else{
                AccountNumber.setText(transactionModel.getUser().getLastName());

            }
            if(transactionModel.isAppUserAccount_isFromAccountNuber()){
                AccountNumber.setTextColor(Color.BLUE);
                Amount.setTextColor(Color.BLUE);
                Date.setTextColor(Color.BLUE);
            }
            else{
                AccountNumber.setTextColor(Color.RED);
                Amount.setTextColor(Color.RED);
                Date.setTextColor(Color.RED);
            }
//            if (UserTransactionDetailActivity.type.equals("refund"))

        Amount.setText(transactionModel.getAmount()+" LKR");
        Date.setText(transactionModel.getDate());
        // Return the completed view to render on screen

        return convertView;
    }
    public int getCount() {
        return filteredData.size();
    }

    public UserTransactionModel getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString();

            FilterResults results = new FilterResults();

            final List<UserTransactionModel> list = originalData;

            int count = list.size();
            final ArrayList<UserTransactionModel> nlist = new ArrayList<UserTransactionModel>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                UserTransactionModel model = list.get(i);
                if(model.isOtherAccountOwnerRoleIsMerchant()){
                    filterableString = list.get(i).getMerchant().getMerchantName();
                }
                else {
                    filterableString = list.get(i).getUser().getLastName();
                }

                if (filterableString.toLowerCase().contains(filterString.toLowerCase())) {
                    nlist.add(model);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<UserTransactionModel>) results.values;
            notifyDataSetChanged();
        }

    }
}



