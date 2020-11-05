package com.example.crosspromotesdk.adlistener;

import com.example.crosspromotesdk.ad.NativeAd;
import com.example.crosspromotesdk.modal.Advertisements;

public interface AdListener {
    void onError(AdError var2);

    void onAdLoaded(Advertisements advertisements);

    void onAdClicked(NativeAd ad);

    void onAdLoading();
}
