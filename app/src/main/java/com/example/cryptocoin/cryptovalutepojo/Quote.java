package com.example.cryptocoin.cryptovalutepojo;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Quote implements Serializable{
    @SerializedName("USD")
    private DataCoin usdDataCoin;

    public DataCoin getUsdDataCoin() {
        return usdDataCoin;
    }
}
