package com.example.crosspromotesdk.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Details {
    @SerializedName("icon")
    @Expose
    private Icon icon;
    @SerializedName("body")
    @Expose
    private Body body;
    @SerializedName("title")
    @Expose
    private Title title;
    @SerializedName("context")
    @Expose
    private Content context;
    @SerializedName("adChoices")
    @Expose
    private Adchoice adChoices;
    @SerializedName("media")
    @Expose
    private Media media;
    @SerializedName("CtA")
    @Expose
    private CTA CtA;

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Content getContent() {
        return context;
    }

    public void setContent(Content context) {
        this.context = context;
    }

    public Adchoice getAdChoices() {
        return adChoices;
    }

    public void setAdChoices(Adchoice adChoices) {
        this.adChoices = adChoices;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public CTA getCtA() {
        return CtA;
    }

    public void setCtA(CTA CtA) {
        this.CtA = CtA;
    }

    @Override
    public String toString() {
        return "ClassPojo [icon = " + icon + ", body = " + body + ", title = " + title + ", context = " + context + ", adChoices = " + adChoices + ", media = " + media + ", CtA = " + CtA + "]";
    }
}
