package com.example.crosspromotesdk.modal;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Advertisements {
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("application_key")
    @Expose
    private String application_key;

    @SerializedName("details")
    @Expose
    private Details details;
    @SerializedName("advertisement_id")
    @Expose
    private String advertisement_id;
    @SerializedName("age_tag")
    @Expose
    private String age_tag;

    private Bitmap bitmapIcon, bitmapMedia;

    private String encodeIcon, encodeMedia, encodeAdchoice;

    private String availableUrlIcon, availableUrlMedia;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getApplication_key() {
        return application_key;
    }

    public void setApplication_key(String application_key) {
        this.application_key = application_key;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public String getAdvertisement_id() {
        return advertisement_id;
    }

    public void setAdvertisement_id(String advertisement_id) {
        this.advertisement_id = advertisement_id;
    }

    public String getAge_tag() {
        return age_tag;
    }

    public void setAge_tag(String age_tag) {
        this.age_tag = age_tag;
    }

    public void setBitmapIcon(Bitmap bitmap) {
        this.bitmapIcon = bitmap;
    }

    public Bitmap getBitmapIcon() {
        return bitmapIcon;
    }

    public void setBitmapMedia(Bitmap bitmap) {
        this.bitmapMedia = bitmap;
    }

    public Bitmap getBitmapMedia() {
        return bitmapMedia;
    }

    public String getEncodeIcon() {
        return encodeIcon;
    }

    public void setEncodeIcon(String encodeIcon) {
        this.encodeIcon = encodeIcon;
    }

    public String getEncodeMedia() {
        return encodeMedia;
    }

    public void setEncodeMedia(String encodeMedia) {
        this.encodeMedia = encodeMedia;
    }

    public String getEncodeAdchoice() {
        return encodeAdchoice;
    }

    public void setEncodeAdchoice(String encodeAdchoice) {
        this.encodeAdchoice = encodeAdchoice;
    }

    public String getAvailableUrlIcon() {
        return availableUrlIcon;
    }

    public void setAvailableUrlIcon(String availableUrlIcon) {
        this.availableUrlIcon = availableUrlIcon;
    }

    public String getAvailableUrlMedia() {
        return availableUrlMedia;
    }

    public void setAvailableUrlMedia(String availableUrlMedia) {
        this.availableUrlMedia = availableUrlMedia;
    }

    @Override
    public String toString() {
        return "Advertisements{" +
                "region='" + region + '\'' +
                ", application_key='" + application_key + '\'' +
                ", details=" + details +
                ", advertisement_id='" + advertisement_id + '\'' +
                ", age_tag='" + age_tag + '\'' +
                ", bitmapIcon=" + bitmapIcon +
                ", bitmapMedia=" + bitmapMedia +
                ", encodeIcon='" + encodeIcon + '\'' +
                ", encodeMedia='" + encodeMedia + '\'' +
                ", encodeAdchoice='" + encodeAdchoice + '\'' +
                '}';
    }
}
