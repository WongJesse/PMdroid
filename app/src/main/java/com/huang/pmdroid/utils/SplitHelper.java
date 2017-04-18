package com.huang.pmdroid.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huang on 2017/4/15.
 */
public class SplitHelper {

    public static List<String> stringSplit(String string){
        int index = 1;
        String substr;
 //       Log.e("SplitHelper", string);
        List<String> list = new ArrayList<>();
        String[] split = string.split("\\.");
    //    Log.e("SplitHelper", ""+split.length);
        for(int i = 0; i < split.length; i++){
    //        Log.e("SplitHelper", split[i]);
        }
        if(split.length > 0){
            substr = split[0];
            while(index < split.length){
                substr += "."+split[index];
                list.add(substr);
         //       Log.e("SplitHelper", substr);
                index++;
            }
        }
        return list;
    }


}
