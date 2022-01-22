package com.example.cryptocoin.cryptovalutepojo;

import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("error_message")
    private String errorMessage;

    @SerializedName("elapsed")
    private int elapsed;

    @SerializedName("credit_count")
    private int creditCount;

    @SerializedName("error_code")
    private int errorCode;

    @SerializedName("timestamp")
    private String timestamp;

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getElapsed() {
        return elapsed;
    }

    public int getCreditCount() {
        return creditCount;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getTimestamp() {
        return timestamp;
    }
}