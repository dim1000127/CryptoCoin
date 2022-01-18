package com.example.cryptocoin.cryptovalutepojo;

import com.google.gson.annotations.SerializedName;

public class Quote {
    @SerializedName("USD")
    private DataCoin usdDataCoin;

    @SerializedName("BTC")
    private DataCoin dataCoin;

    public DataCoin getUsdDataCoin() {
        return usdDataCoin;
    }

    public DataCoin getDataCoin() {
        return dataCoin;
    }
}
