package com.example.cryptocoin.cryptovalutepojo;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Quote implements Serializable{
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
