package com.example.hwang.xeed.hmm;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import cooker.core.annotations.CookerAction;
import cooker.core.annotations.CookerSerializable;
import cooker.core.annotations.CookingIngredient;

/**
 * Created by Hwang on 2016-08-18.
 */
@CookingIngredient
public class CallAction {

    @CookerSerializable(key = "phoneNumber")
    String phoneNumber;
    @CookerSerializable(key = "activity")
    Activity activity;

    @CookerAction
    public void callAction() {
        if (phoneNumber == null) {
            Toast.makeText(activity.getApplicationContext(), "sss", Toast.LENGTH_SHORT).show();
        } else {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phoneNumber));
            activity.startActivity(myIntent);
        }
    }
}
