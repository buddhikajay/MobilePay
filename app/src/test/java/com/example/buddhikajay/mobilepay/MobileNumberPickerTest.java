package com.example.buddhikajay.mobilepay;

import android.content.Context;

import com.example.buddhikajay.mobilepay.Services.MobileNumberPicker;

import static org.junit.Assert.*;
import org.junit.Test;
/**
 * Created by supun on 29/05/17.
 */

public class MobileNumberPickerTest {

    private Context context;
    @Test
    public void getPhoneNumber(){
        MobileNumberPicker mobileNumberPicker = MobileNumberPicker.getInstance();
        //assertEquals(0711135012,mobileNumberPicker.getPhoneNumber());
    }
}
