package com.example.crosspromotesdk.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Xxhdpi {
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("size")
    @Expose
    private String size;

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    public String getSize ()
    {
        return size;
    }

    public void setSize (String size)
    {
        this.size = size;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [url = "+url+", size = "+size+"]";
    }
}
