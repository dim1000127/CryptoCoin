package com.example.cryptocoin.pojo.metadatapojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Item implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("slug")
    private String slug;

    @SerializedName("category")
    private String category;

    @SerializedName("logo")
    private String logo;

    @SerializedName("contract_address")
    private List<ContractAddress> contractAddress;

    public int getId() { return id; }

    public String getName() { return name; }

    public String getSymbol() { return symbol; }

    public String getSlug() { return slug; }

    public String getCategory() { return category; }

    public String getLogo() { return logo; }

    public List<ContractAddress> getContractAddress() {
        return contractAddress;
    }
}
