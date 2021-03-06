package com.example.cryptocoin.pojo.cryptovalutepojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DataItem implements Serializable{
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("slug")
    private String slug;

    @SerializedName("cmc_rank")
    private int cmcRank;

    @SerializedName("num_market_pairs")
    private int numMarketPairs;

    @SerializedName("circulating_supply")
    private double circulatingSupply;

    @SerializedName("total_supply")
    private double totalSupply;

    @SerializedName("max_supply")
    private double maxSupply;

    @SerializedName("last_updated")
    private String lastUpdated;

    @SerializedName("date_added")
    private String dateAdded;

    @SerializedName("quote")
    //private HashMap<String, DataCoin> quote;
    private Quote quote;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getSlug() {
        return slug;
    }

    public int getCmcRank() {
        return cmcRank;
    }

    public int getNumMarketPairs() {
        return numMarketPairs;
    }

    public double getCirculatingSupply() {
        return circulatingSupply;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

    public double getMaxSupply() {
        return maxSupply;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public Quote getQuote() {
        return quote;
    }
}
