package com.example.crosspromotesdk.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Media {
    @SerializedName("resources")
    @Expose
    private Resource resources;
    @SerializedName("url")
    @Expose
    private String url;

    public Resource getResources ()
    {
        return resources;
    }

    public void setResources (Resource resources)
    {
        this.resources = resources;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [resources = "+resources+", url = "+url+"]";
    }
}
