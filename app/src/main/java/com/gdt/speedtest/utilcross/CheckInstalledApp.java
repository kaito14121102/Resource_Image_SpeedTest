package com.gdt.speedtest.utilcross;

import android.app.Activity;
import android.content.pm.PackageManager;

public class CheckInstalledApp {
    public static boolean appInstalledOrNot(Activity activity, String uri) {
        try {
            PackageManager pm = activity.getPackageManager();
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }
}
