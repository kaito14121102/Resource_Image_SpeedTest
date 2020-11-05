package com.example.crosspromotesdk.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;

public class AppUtil {
    private final int TYPE_WIFI = 2;
    private final int TYPE_3G = 1;
    private final int TYPE_NONE = 2;
    private Context context;

    public AppUtil(Context context) {
        this.context = context;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    //return
    // 0:nếu mạng không phải 3G hoặc Wifi
    // 1: nếu là 3G
    // 2: nếu là Wifi
    public int checkTypeNetwork() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //For 3G check
        boolean type3G = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
        //For WiFi Check
        boolean typeWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        if (!type3G && !typeWifi) {
            return TYPE_NONE;
        } else if (type3G) {
            return TYPE_3G;
        } else {
            return TYPE_WIFI;
        }
    }

    /**
     * Return Size screen divice:
     *
     * @return int
     * 0: Mdpi
     * 1: Hdpi
     * 2: Xhdpi
     * 3: Xxhdpi
     * 4: XXXHdpi
     */

    public int getDeviceResolution() {
        int density = context.getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_MEDIUM:
                return 0;
            case DisplayMetrics.DENSITY_HIGH:
                return 1;
            case DisplayMetrics.DENSITY_XHIGH:
                return 2;
            case DisplayMetrics.DENSITY_XXHIGH:
                return 3;
            case DisplayMetrics.DENSITY_XXXHIGH:
                return 4;
            default:
                return 2;
        }
    }

    public void setDensity(int value) {
        context.getResources().getDisplayMetrics().densityDpi = value;
    }
}
