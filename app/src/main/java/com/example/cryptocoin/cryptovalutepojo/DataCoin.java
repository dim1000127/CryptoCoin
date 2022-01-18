package com.example.cryptocoin.cryptovalutepojo;

import com.google.gson.annotations.SerializedName;

public class DataCoin {

    @SerializedName("price")
    private double price;

    @SerializedName("volume_24h")
    private long volume24h;

    @SerializedName("volume_change_24h")
    private double volumeChange24h;

    @SerializedName("percent_change_1h")
    private double percentChange1h;

    @SerializedName("percent_change_24h")
    private double percentChange24h;

    @SerializedName("percent_change_7d")
    private double percentChange7d;

    @SerializedName("market_cap")
    private double marketCap;

    @SerializedName("market_cap_dominance")
    private int marketCapDominance;

    @SerializedName("fully_diluted_market_cap")
    private double fullyDilutedMarketCap;

    @SerializedName("last_updated")
    private String lastUpdated;

    public double getPrice() {
        return price;
    }

    public long getVolume24h() {
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

    public double getFullyDilutedMarketCap() {
        return fullyDilutedMarketCap;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public int getMarketCapDominance() {
        return marketCapDominance;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }
}
