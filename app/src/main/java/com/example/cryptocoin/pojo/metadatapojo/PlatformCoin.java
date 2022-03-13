package com.example.cryptocoin.pojo.metadatapojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlatformCoin implements Serializable {

    @SerializedName("name")
    private String namePlatform;


    public String getNamePlatform() { return namePlatform; }

}
