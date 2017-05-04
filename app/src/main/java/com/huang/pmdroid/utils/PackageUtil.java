package com.huang.pmdroid.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by huang on 2017/5/4.
 *
 */
public class PackageUtil {
    /**
     * 通过包名获取应用程序的名称。
     */
    public static String getNameByPackageName(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        String name = null;
        try {
            name = pm.getApplicationLabel(
                    pm.getApplicationInfo(packageName,
                            PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }


}
