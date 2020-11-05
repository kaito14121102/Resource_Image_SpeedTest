package com.gdt.speedtest.util;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

public final class ViewUtil {

    public static void transparentStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = activity.getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
}
