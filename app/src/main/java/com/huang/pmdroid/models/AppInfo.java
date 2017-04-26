package com.huang.pmdroid.models;

import android.graphics.drawable.Drawable;

/**
 * Created by huang on 2017/4/25.
 */
public class AppInfo {
    private Drawable appIcon;
    private String appName;
    private String packName;

    public Drawable getAppIcon(){return appIcon;}

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

}
