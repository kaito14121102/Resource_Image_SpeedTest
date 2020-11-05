package com.gdt.speedtest.features.main.fragment.speed;

import com.gdt.speedtest.base.MvpView;
import com.gdt.speedtest.data.model.Address;

import java.util.List;

public interface SpeedChangeListener extends MvpView {
    void onPingChange(double ping);

    void onPingComplete();

    void onDownloadFinished();

    void onDownloadRateChange(double downloadRate);

    void onUploadFinished();

    void onUploadRateChange(double uploadRate);

    void onDownloadRequest();

    void onUploadRequest();

    void onGetDataFromApiCompleted(List<Address> addressList);

    void apiLoadFaild();

    void showDateFailToast();
}
