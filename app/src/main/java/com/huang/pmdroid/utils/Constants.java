package com.huang.pmdroid.utils;

/**
 * Created by huang on 2017/4/12.
 *
 */
public interface Constants {

    String TAG = "PMdroid";
    String XPOSED_TAG = "xposed";
    String DATABASE_NAME = "PMdroid.db";
    String SERVICE_NAME = "com.huang.pmdroid.services.MonitorService";

    //定义敏感权限集
    String INTERNET = "android.permission.INTERNET";
    String SEND_SMS = "android.permission.SEND_SMS";
    String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    String READ_CONTACTS = "android.permission.READ_CONTACTS";
    String RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    String READ_SMS = "android.permission.READ_SMS";
    String RECEIVE_SMS = "android.permission.RECEIVE_SMS";
    String RECEIVE_MMS = "android.permission.RECEIVE_MMS";
    String READ_CALENDAR = "android.permission.READ_CALENDAR";
    String READ_CALL_LOG = "android.permission.READ_CALL_LOG";
    String PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS";
    String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
}
