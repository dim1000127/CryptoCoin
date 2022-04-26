package com.example.cryptocoin.pojo.rublepojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ValuteDataUSD implements Serializable {
    @SerializedName("ID")
    private String id;

    @SerializedName("NumCode")
    private String numCode;

    @SerializedName("CharCode")
    private String charCode;

    @SerializedName("Nominal")
    private int nominal;

    @SerializedName("Name")
    private String name;

    @SerializedName("Value")
    private double value;

    @SerializedName("Previous")
    private double previous;

    public String getId() {
        return id;
    }

    public String getNumCode() {
        return numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public int getNominal() {
        return nominal;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public double getPrevious() {
        return previous;
    }
}
