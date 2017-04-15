package com.huang.pmdroid.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by huang on 2017/4/14.
 */
public class MonitorService extends Service {
    private DataReceiver receiver;
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.e("777","startSuccess");
        receiver = new DataReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.huang.pmdroid.receive");
        registerReceiver(receiver, filter);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void decisionMaker(String from, String to){

    }

    private class DataReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            if(intent.getAction().equals("com.huang.pmdroid.receive")){
                String from = intent.getExtras().getString("from");
                String to = intent.getExtras().getString("to");
                Log.e("777", from);
                Log.e("777",to);
                decisionMaker(from, to);

            }
        }
    }
}
