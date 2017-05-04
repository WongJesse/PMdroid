package com.huang.pmdroid.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huang on 2017/4/15.
 *
 */
public class SplitHelper {

    public static List<String> stringSplit(String string){
        int index = 1;
        String substr;
 //       Log.e("SplitHelper", string);
        List<String> list = new ArrayList<>();
        String[] split = string.split("\\.");
    //    Log.e("SplitHelper", ""+split.length);
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

    public static String permissionSplit(String permission){
        String[] split = permission.split("\\.");
        if(split.length == 3){
            return split[2];
        }else
            return "error";
    }
}
