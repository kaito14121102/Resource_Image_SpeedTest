package com.example.crosspromotesdk.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Resource {
    @SerializedName("xxxhdpi")
    @Expose
    private Xxxhdpi xxxhdpi;
    @SerializedName("hdpi")
    @Expose
    private Hdpi hdpi;
    @SerializedName("xxhdpi")
    @Expose
    private Xxhdpi xxhdpi;
    @SerializedName("mdpi")
    @Expose
    private Mdpi mdpi;
    @SerializedName("xhdpi")
    @Expose
    private Xhdpi xhdpi;

    public Xxxhdpi getXxxhdpi() {
        return xxxhdpi;
    }

    public void setXxxhdpi(Xxxhdpi xxxhdpi) {
        this.xxxhdpi = xxxhdpi;
    }

    public Hdpi getHdpi() {
        return hdpi;
    }

    public void setHdpi(Hdpi hdpi) {
        this.hdpi = hdpi;
    }

    public Xxhdpi getXxhdpi() {
        return xxhdpi;
    }

    public void setXxhdpi(Xxhdpi xxhdpi) {
        this.xxhdpi = xxhdpi;
    }

    public Mdpi getMdpi() {
        return mdpi;
    }

    public void setMdpi(Mdpi mdpi) {
        this.mdpi = mdpi;
    }

    public Xhdpi getXhdpi() {
        return xhdpi;
    }

    public void setXhdpi(Xhdpi xhdpi) {
        this.xhdpi = xhdpi;
    }

    @Override
    public String toString() {
        return "ClassPojo [xxxhdpi = " + xxxhdpi + ", hdpi = " + hdpi + ", xxhdpi = " + xxhdpi + ", mdpi = " + mdpi + ", xhdpi = " + xhdpi + "]";
    }
}
