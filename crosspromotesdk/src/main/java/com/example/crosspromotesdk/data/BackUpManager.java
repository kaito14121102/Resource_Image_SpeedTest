package com.example.crosspromotesdk.data;

import java.util.HashMap;

public class BackUpManager {
    private HashMap<String,String> apiMiss;
    //type api request: requestAds-1, view ads-2, click Ads-3.
    private int type;

    public BackUpManager(){}

    public BackUpManager(HashMap<String, String> listApiMiss, int type) {
        this.apiMiss = listApiMiss;
        this.type = type;
    }

    public HashMap<String, String> getApiMiss() {
        return apiMiss;
    }

    public void setApiMiss(HashMap<String, String> apiMiss) {
        this.apiMiss = apiMiss;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
