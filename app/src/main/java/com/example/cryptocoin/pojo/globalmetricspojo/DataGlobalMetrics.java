package com.example.cryptocoin.pojo.globalmetricspojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataGlobalMetrics implements Serializable {

    @SerializedName("active_cryptocurrencies")
    private int activeCryptocurrencies;

    @SerializedName("active_market_pairs")
    private int activeMarketPair;

    @SerializedName("active_exchanges")
    private int activeExchanges;

    @SerializedName("eth_dominance")
    private double ethDominance;

    @SerializedName("btc_dominance")
    private double btcDominance;

    @SerializedName("eth_dominance_24h_percentage_change")
    private double ethDominance24hPercentageChange;

    @SerializedName("btc_dominance_24h_percentage_change")
    private double btcDominance24hPercentageChange;

    @SerializedName("quote")
    private QuoteGlobalMetrics quoteGlobalMetrics;

    public int getActiveCryptocurrencies() {
        return activeCryptocurrencies;
    }

    public int getActiveMarketPair() {
        return activeMarketPair;
    }

    public int getActiveExchanges() {
        return activeExchanges;
    }

    public double getBtcDominance() {
        return btcDominance;
    }

    public double getEthDominance() {
        return ethDominance;
    }

    public double getBtcDominance24hPercentageChange() {
        return btcDominance24hPercentageChange;
    }

    public double getEthDominance24hPercentageChange() {
        return ethDominance24hPercentageChange;
    }

    public QuoteGlobalMetrics getQuoteGlobalMetrics() {
        return quoteGlobalMetrics;
    }
}
