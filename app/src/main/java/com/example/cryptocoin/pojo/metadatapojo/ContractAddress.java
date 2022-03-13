package com.example.cryptocoin.pojo.metadatapojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ContractAddress implements Serializable {

    @SerializedName("contract_address")
    private String contractAddress;

    @SerializedName("platform")
    private PlatformCoin platformCoin;

    public String getContractAddress() { return contractAddress; }

    public PlatformCoin getPlatformCoin() { return platformCoin; }
}
