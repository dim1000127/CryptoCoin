package com.example.cryptocoin.pojo.globalmetricspojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UsdGlobalMetrics implements Serializable {

    @SerializedName("total_market_cap")
    private double totalMarketCap;

    @SerializedName("total_volume_24h")
    private double totalVolume24h;

    @SerializedName("total_market_cap_yesterday_percentage_change")
    private double totalMarketCapPercentChange;

    @SerializedName("total_volume_24h_yesterday_percentage_change")
    private double totalVolume24hPercentChange;

    public double getTotalMarketCap() {
        return totalMarketCap;
    }

    public double getTotalVolume24h() {
        return totalVolume24h;
    }

    public double getTotalMarketCapPercentChange() {
        return totalMarketCapPercentChange;
    }

    public double getTotalVolume24hPercentChange() {
        return totalVolume24hPercentChange;
    }
}
