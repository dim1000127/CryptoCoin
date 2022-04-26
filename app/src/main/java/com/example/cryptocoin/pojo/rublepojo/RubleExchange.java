package com.example.cryptocoin.pojo.rublepojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RubleExchange implements Serializable {
    @SerializedName("Date")
    private String date;

    @SerializedName("PreviousDate")
    private String previousDate;

    @SerializedName("PreviousURL")
    private String previousURL;

    @SerializedName("Timestamp")
    private String timestamp;

    @SerializedName("Valute")
    private Valute valute;

    public String getDate(){
        return date;
    }

    public String getPreviousDate(){
        return previousDate;
    }

    public String getPreviousURL(){return previousURL;}

    public String getTimestamp(){return timestamp;}

    public Valute getValute(){return valute;}

}


