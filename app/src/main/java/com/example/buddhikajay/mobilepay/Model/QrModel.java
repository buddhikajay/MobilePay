package com.example.buddhikajay.mobilepay.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by supun on 29/06/17.
 */

public class QrModel  implements Serializable {
    private String id;
    private String amount;
    private String tag;
    private ArrayList<String> customTypes;
    private boolean haveCustomTypes;

    public QrModel() {
        this.customTypes = new ArrayList<>();
    }





    public boolean isHaveCustomTypes() {
        return haveCustomTypes;
    }

    public void setHaveCustomTypes(boolean haveCustomTypes) {
        this.haveCustomTypes = haveCustomTypes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ArrayList<String> getCustomTypes() {
        return customTypes;
    }

    public void setCustomTypes(ArrayList<String> customTypes) {
        this.customTypes = customTypes;
    }

    @Override
    public String toString() {

        String typesCus="";
        for (String t:customTypes
             ) {
            typesCus = typesCus +t+ " ";
        }
        typesCus.trim();
        return id+" "+amount+" "+tag+" "+typesCus;
    }


}
