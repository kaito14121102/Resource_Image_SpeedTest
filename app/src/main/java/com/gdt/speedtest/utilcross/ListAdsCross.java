package com.gdt.speedtest.utilcross;

import com.gdt.speedtest.data.model.ItemCross;

import java.util.ArrayList;
import java.util.Collections;

import static com.gdt.speedtest.Constants.PACKAGE_APPLOCK;
import static com.gdt.speedtest.Constants.PACKAGE_WATERMARK_PHOTO;

public class ListAdsCross {
    public static ArrayList<ItemCross> getListCrossAdaptive() {
        ArrayList<ItemCross> list = new ArrayList<>();
        list.add(new ItemCross("Add Watermark on Photo", "Start protecting your content today!", PACKAGE_WATERMARK_PHOTO));
        list.add(new ItemCross("AppLock - Lock Apps", "Lock App & App lock fingerprint help you secure all information", PACKAGE_APPLOCK));
        Collections.shuffle(list);
        return list;
    }
    public static ArrayList<CrossItem> getListCrossNative() {
        ArrayList<CrossItem> list = new ArrayList<>();
        list.add(new CrossItem("Add Watermark on Photos - Signature maker", "Start protecting your content today!", PACKAGE_WATERMARK_PHOTO));
        list.add(new CrossItem("AppLock - Lock Apps", "Lock App & App lock fingerprint help you secure all information", PACKAGE_APPLOCK));
        Collections.shuffle(list);
        return list;
    }
}
