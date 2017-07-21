package com.example.buddhikajay.mobilepay.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by supun on 29/06/17.
 */

public class PaymentModel  implements Serializable {
    private ArrayList<QrModel> qrModels;
    private boolean dynamic = true;
    private boolean tip;
    private boolean innerApp;

    public PaymentModel() {
        qrModels = new ArrayList<>();

    }

    public boolean isInnerApp() {
        return innerApp;
    }

    public void setInnerApp(boolean innerApp) {
        this.innerApp = innerApp;
    }

    public boolean isTip() {
        return tip;
    }

    public void setTip(boolean tip) {
        this.tip = tip;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public List<QrModel> getQrModels() {
        return qrModels;
    }

    public void setQrModels(ArrayList<QrModel> qrModels) {
        this.qrModels = qrModels;
    }

    public void addQrModel(QrModel model){
        qrModels.add(model);
    }
    @Override
    public String toString() {
        String code = "";
        for (QrModel m: qrModels
             ) {
            code = code + m.toString()+";";
        }
        return code;
    }


}
