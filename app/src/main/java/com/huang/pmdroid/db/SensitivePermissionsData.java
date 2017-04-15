package com.huang.pmdroid.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.huang.pmdroid.utils.Constants;

/**
 * Created by huang on 2017/4/12.
 */
public class SensitivePermissionsData {
    private Map<String,List> map = new HashMap<>();
    public void setInitData(){
        List<String> list1 = new ArrayList<>();
        list1.add(Constants.INTERNET);
        list1.add(Constants.SEND_SMS);
        map.put(Constants.ACCESS_COARSE_LOCATION, list1);

        List<String> list2 = new ArrayList<>();
        list2.add(Constants.INTERNET);
        list2.add(Constants.SEND_SMS);
        map.put(Constants.ACCESS_FINE_LOCATION, list2);

        List<String> list3 = new ArrayList<>();
        list3.add(Constants.INTERNET);
        list3.add(Constants.SEND_SMS);
        map.put(Constants.READ_CONTACTS, list3);

        List<String> list4 = new ArrayList<>();
        list4.add(Constants.INTERNET);
        map.put(Constants.RECORD_AUDIO, list4);

        List<String> list5 = new ArrayList<>();
        list5.add(Constants.INTERNET);
        list5.add(Constants.SEND_SMS);
        map.put(Constants.READ_SMS, list5);

        List<String> list6 = new ArrayList<>();
        list6.add(Constants.INTERNET);
        list6.add(Constants.SEND_SMS);
        map.put(Constants.RECEIVE_SMS, list6);

        List<String> list7 = new ArrayList<>();
        list7.add(Constants.INTERNET);
        list7.add(Constants.SEND_SMS);
        map.put(Constants.RECEIVE_MMS, list7);

        List<String> list8 = new ArrayList<>();
        list8.add(Constants.INTERNET);
        list8.add(Constants.SEND_SMS);
        map.put(Constants.READ_CALENDAR, list8);

        List<String> list9 = new ArrayList<>();
        list9.add(Constants.INTERNET);
        list9.add(Constants.SEND_SMS);
        map.put(Constants.READ_CALL_LOG, list9);

        List<String> list10 = new ArrayList<>();
        list10.add(Constants.INTERNET);
        list10.add(Constants.SEND_SMS);
        map.put(Constants.PROCESS_OUTGOING_CALLS, list10);

        List<String> list11 = new ArrayList<>();
        list11.add(Constants.INTERNET);
        list11.add(Constants.SEND_SMS);
        map.put(Constants.READ_PHONE_STATE, list11);

        List<String> list12 = new ArrayList<>();
        list12.add(Constants.INTERNET);
        list12.add(Constants.SEND_SMS);
        map.put(Constants.READ_EXTERNAL_STORAGE, list12);

        List<String> list13 = new ArrayList<>();
        list13.add(Constants.ACCESS_COARSE_LOCATION);
        list13.add(Constants.ACCESS_FINE_LOCATION);
        list13.add(Constants.PROCESS_OUTGOING_CALLS);
        list13.add(Constants.READ_CALENDAR);
        list13.add(Constants.READ_CALL_LOG);
        list13.add(Constants.READ_CONTACTS);
        list13.add(Constants.READ_EXTERNAL_STORAGE);
        list13.add(Constants.READ_PHONE_STATE);
        list13.add(Constants.READ_SMS);
        list13.add(Constants.RECEIVE_MMS);
        list13.add(Constants.RECEIVE_SMS);
        map.put(Constants.SEND_SMS, list13);

        List<String> list14 = new ArrayList<>();
        list14.add(Constants.ACCESS_COARSE_LOCATION);
        list14.add(Constants.ACCESS_FINE_LOCATION);
        list14.add(Constants.PROCESS_OUTGOING_CALLS);
        list14.add(Constants.READ_CALENDAR);
        list14.add(Constants.READ_CALL_LOG);
        list14.add(Constants.READ_CONTACTS);
        list14.add(Constants.READ_EXTERNAL_STORAGE);
        list14.add(Constants.READ_PHONE_STATE);
        list14.add(Constants.READ_SMS);
        list14.add(Constants.RECEIVE_MMS);
        list14.add(Constants.RECEIVE_SMS);
        list14.add(Constants.RECORD_AUDIO);
        map.put(Constants.INTERNET, list14);
    }

    public Map<String, List> getData(){
        return map;
    }

    public void setData(String string, List<String> lists){
        map.put(string, lists);
    }

}
