package com.example.crosspromotesdk.adlistener;

import android.text.TextUtils;

public class AdError {
    private final int a;
    private final String b;

    public AdError(int code, String errorMessage) {
        if (TextUtils.isEmpty(errorMessage)) {
            errorMessage = "unknown error";
        }
        this.a = code;
        this.b = errorMessage;
    }

    public int getErrorCode() {
        return this.a;
    }

    public String getErrorMessage() {
        return this.b;
    }
}
