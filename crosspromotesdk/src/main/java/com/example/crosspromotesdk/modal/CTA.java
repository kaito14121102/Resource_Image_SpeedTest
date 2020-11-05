package com.example.crosspromotesdk.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CTA {
    @SerializedName("text_size")
    @Expose
    private String text_size;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("font")
    @Expose
    private String font;
    @SerializedName("link_checking")
    @Expose
    private String link_checking;
    @SerializedName("background_color")
    @Expose
    private String background_color;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("text")
    @Expose
    private String text;

    public String getText_size() {
        return text_size;
    }

    public void setText_size(String text_size) {
        this.text_size = text_size;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getLink_checking() {
        return link_checking;
    }

    public void setLink_checking(String link_checking) {
        this.link_checking = link_checking;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "ClassPojo [text_size = " + text_size + ", token = " + token + ", color = " + color + ", font = " + font + ", link_checking = " + link_checking + ", background_color = " + background_color + ", size = " + size + ", text = " + text + "]";
    }
}
