package com.example.cryptocoin.pojo.globalmetricspojo;

import com.example.cryptocoin.pojo.Status;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GlobalMetrics implements Serializable {

    @SerializedName("data")
    private DataGlobalMetrics dataGlobalMetrics;

    @SerializedName("status")
    private Status status;

    public DataGlobalMetrics getDataGlobalMetrics() {
        return dataGlobalMetrics;
    }

    public Status getStatus() {
        return status;
    }
}
