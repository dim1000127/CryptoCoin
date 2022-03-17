package com.example.cryptocoin.pojo.quotescryptovalute;

import com.example.cryptocoin.pojo.cryptovalutepojo.DataCoin;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuotesPrice implements Serializable{
    @SerializedName("USD")
    private QuotesDataCoin usdDataCoin;

    public QuotesDataCoin getUsdDataCoin() {
        return usdDataCoin;
    }
}
