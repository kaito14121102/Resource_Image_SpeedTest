package com.example.crosspromotesdk.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content {
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("font")
    @Expose
    private String font;
    @SerializedName("size")
    @Expose
    private String size;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "ClassPojo [content = " + content + ", color = " + color + ", font = " + font + ", size = " + size + "]";
    }
}
