package com.huang.pmdroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by huang on 2017/4/14.
 */
public class BroadcastTest extends BroadcastReceiver {
    @Override
    public void onReceive(Context context,Intent intent){
        if(intent.getAction().equals("com.huang.pmdroid.getData")){
            String from = intent.getExtras().getString("from");
            String to = intent.getExtras().getString("to");
      //      Log.e("777", from);
     //       Log.e("777",to);
        }

    }
}
