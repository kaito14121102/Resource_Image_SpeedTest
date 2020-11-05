package com.example.crosspromotesdk.ad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NativeAdViewHolder {
    private final static String TAG = NativeAdViewHolder.class.getSimpleName();
    @Nullable
    TextView titleText;
    @Nullable
    TextView bodyText;
    @Nullable
    ImageView icon;
    @Nullable
    ImageView adchoice;
    @Nullable
    ImageView adMedia;
    @Nullable
    Button callToActionView;

    public NativeAdViewHolder(@NonNull View parentView,
                              @NonNull NativeAdViewBinder viewBinder) {
        titleText = initView(parentView, viewBinder.getmTitleText(), TextView.class);
        icon = initView(parentView, viewBinder.getmIcon(), ImageView.class);
        adchoice = initView(parentView, viewBinder.getmAdchoice(), ImageView.class);
        adMedia = initView(parentView, viewBinder.getmAdMedia(), ImageView.class);
        bodyText = initView(parentView, viewBinder.getmAdBodyText(), TextView.class);
        callToActionView = initView(parentView, viewBinder.getCallToActionViewId(), Button.class);
    }
    /**
     * Unnecessarily complicated method to initialize a View from a parent View and avoid failing
     * if just one view resource ID is incorrect or not given.
     *
     * @param parentView the parent View containing the View to init
     * @param resourceId the resource ID to search for in parent View
     * @param type the class type of the expected View
     * @param <T> the type of expected View
     * @return the expected View or null if the View was not found
     */
    private static <T extends View> T initView(View parentView, int resourceId, Class<T> type) {
        try {
            return type.cast(parentView.findViewById(resourceId));
        } catch (NullPointerException | ClassCastException ex) {
            Log.w(TAG, "Cannot find invalid resource ID " + resourceId);
            return null;
        }
    }

    @Nullable
    public TextView getTitleText() {
        return titleText;
    }

    public void setTitleText(@Nullable TextView titleText) {
        this.titleText = titleText;
    }

    @Nullable
    public TextView getBodyText() {
        return bodyText;
    }

    public void setBodyText(@Nullable TextView bodyText) {
        this.bodyText = bodyText;
    }

    @Nullable
    public ImageView getIcon() {
        return icon;
    }

    public void setIcon(@Nullable ImageView icon) {
        this.icon = icon;
    }

    @Nullable
    public ImageView getAdchoice() {
        return adchoice;
    }

    public void setAdchoice(@Nullable ImageView adchoice) {
        this.adchoice = adchoice;
    }

    @Nullable
    public ImageView getAdMedia() {
        return adMedia;
    }

    public void setAdMedia(@Nullable ImageView adMedia) {
        this.adMedia = adMedia;
    }

    @Nullable
    public Button getCallToActionView() {
        return callToActionView;
    }

    public void setCallToActionView(@Nullable Button callToActionView) {
        this.callToActionView = callToActionView;
    }
}
