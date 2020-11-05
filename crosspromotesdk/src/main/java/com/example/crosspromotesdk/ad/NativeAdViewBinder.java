package com.example.crosspromotesdk.ad;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

public class NativeAdViewBinder {
    private int mTitleText;
    private int mIcon;
    private int mAdchoice;
    private int mAdMedia;
    private int mAdBodyText;
    private int mCallToActionViewId;

    NativeAdViewBinder() {
        // Prevent instantiation
    }


    int getmTitleText() {
        return mTitleText;
    }

    int getmIcon() {
        return mIcon;
    }

    int getmAdchoice() {
        return mAdchoice;
    }

    int getmAdMedia() {
        return mAdMedia;
    }

    int getmAdBodyText() {
        return mAdBodyText;
    }

    int getCallToActionViewId() {
        return mCallToActionViewId;
    }

    public final static class ViewBinderBuilder {
        NativeAdViewBinder mNativeAdViewBinder;

        public ViewBinderBuilder() {
            mNativeAdViewBinder = new NativeAdViewBinder();
        }

        public ViewBinderBuilder setTitleTextId(@IdRes int mTitleText) {
            mNativeAdViewBinder.mTitleText = mTitleText;
            return this;
        }

        public ViewBinderBuilder setAdIconId(@IdRes int mIcon) {
            mNativeAdViewBinder.mIcon = mIcon;
            return this;
        }

        public ViewBinderBuilder setAdMediaId(@IdRes int mAdMedia) {
            mNativeAdViewBinder.mAdMedia = mAdMedia;
            return this;
        }

        public ViewBinderBuilder setAdchoiceId(@IdRes int mAdchoice) {
            mNativeAdViewBinder.mAdchoice = mAdchoice;
            return this;
        }

        public ViewBinderBuilder setCallToActionViewId(@IdRes int adCTAViewId) {
            mNativeAdViewBinder.mCallToActionViewId = adCTAViewId;
            return this;
        }

        public ViewBinderBuilder setAdBodyTextId(@IdRes int mAdBodyText) {
            mNativeAdViewBinder.mAdBodyText = mAdBodyText;
            return this;
        }

        public NativeAdViewBinder build() {
            return mNativeAdViewBinder;
        }
    }
}
