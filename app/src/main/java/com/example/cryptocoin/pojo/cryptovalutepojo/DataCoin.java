package com.example.cryptocoin.pojo.cryptovalutepojo;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class DataCoin implements Serializable{

    @SerializedName("price")
    private double price;

    @SerializedName("volume_24h")
    private double volume24h;

    @SerializedName("volume_change_24h")
    private double volumeChange24h;

    @SerializedName("percent_change_1h")
    private double percentChange1h;

    @SerializedName("percent_change_24h")
    private double percentChange24h;

    @SerializedName("percent_change_7d")
    private double percentChange7d;

    @SerializedName("percent_change_30d")
    private double percentChange30d;

    @SerializedName("percent_change_60d")
    private double percentChange60d;

    @SerializedName("percent_change_90d")
    private double percentChange90d;

    @SerializedName("market_cap")
    private double marketCap;

    @SerializedName("market_cap_dominance")
    private double marketCapDominance;

    @SerializedName("fully_diluted_market_cap")
    private double fullyDilutedMarketCap;

    @SerializedName("last_updated")
    private String lastUpdated;

    public double getPrice() {
        return price;
    }

    public double getVolume24h() {
        return volume24h;
    }

    public double getVolumeChange24h() {
        return volumeChange24h;
    }

    public double getPercentChange1h() {
        return percentChange1h;
    }

    public double getPercentChange24h() {
        return percentChange24h;
    }

    public double getPercentChange7d() {
        return percentChange7d;
    }

    public double getPercentChange30d(){return percentChange30d;}

    public double getPercentChange60d(){return percentChange60d;}

    public double getPercentChange90d(){return percentChange90d;}

    public double getFullyDilutedMarketCap() {
        return fullyDilutedMarketCap;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public double getMarketCapDominance() {
        return marketCapDominance;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }
}
