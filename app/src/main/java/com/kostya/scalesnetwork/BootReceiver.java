package com.kostya.scalesnetwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.kostya.scalesnetwork.service.ServiceScalesNet;

/**
 * @author Kostya
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ServiceScalesNet.class));//
        /*
        Intent testIntent = new Intent(context, Test.class);
        testIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(testIntent);*/
    }

}
