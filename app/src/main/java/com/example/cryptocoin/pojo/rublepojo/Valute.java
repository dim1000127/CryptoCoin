package com.example.cryptocoin.pojo.rublepojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Valute implements Serializable {
    @SerializedName("USD")
    private ValuteDataUSD valuteDataUSD;

    public ValuteDataUSD getValuteDataUSD() {
        return valuteDataUSD;
    }
}
