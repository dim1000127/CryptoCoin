package com.example.cryptocoin.pojo.globalmetricspojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuoteGlobalMetrics implements Serializable {

    @SerializedName("USD")
    private UsdGlobalMetrics usdGlobalMetrics;

    public UsdGlobalMetrics getUsdGlobalMetrics() {
        return usdGlobalMetrics;
    }
}
