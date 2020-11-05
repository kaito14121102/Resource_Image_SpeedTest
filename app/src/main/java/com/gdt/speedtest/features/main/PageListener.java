package com.gdt.speedtest.features.main;

import com.gdt.speedtest.database.Result;

public interface PageListener {
    void onTestSuccess(Result result);
    void loadFullAdsGoogleClickTest();
    void callFailOnTestSuccess(Result result);
}
