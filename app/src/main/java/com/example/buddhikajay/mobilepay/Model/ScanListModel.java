package com.example.buddhikajay.mobilepay.Model;

import com.example.buddhikajay.mobilepay.R;

import java.util.ArrayList;

/**
 * Created by supun on 31/05/17.
 */

public class ScanListModel {
    private int logo;
    private String tag;
    private int type;

    public ScanListModel(int logo, String tag, int type) {
        this.logo = logo;
        this.tag = tag;
        this.type = type;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static ArrayList<ScanListModel> getModel() {
        ArrayList<ScanListModel> scanListModels = new ArrayList<ScanListModel>();

        scanListModels.add(new ScanListModel(R.mipmap.merchant_pay, "Merchant Pay",0));
        scanListModels.add(new ScanListModel(R.mipmap.fud_transfer, "Fund Transfer",1));
        scanListModels.add(new ScanListModel(R.mipmap.qr_code, "My QR Code",2));
        scanListModels.add(new ScanListModel(R.mipmap.tra_report, "Transactin Report",3));
        //scanListModels.add(new ScanListModel("123234", "10000","2016-3-09"));
        //scanListModels.add(new ScanListModel("123234", "10000","2016-3-09"));
        return scanListModels;
    }
}
