package com.example.cryptocoin.pojo.idcryptovalutepojo;

import com.google.gson.annotations.SerializedName;

public class ItemID {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("slug")
    private String slug;

    @SerializedName("rank")
    private int rank;

    public int getId() { return id; }

    public String getName() { return name; }

    public String getSymbol() { return symbol; }

    public String getSlug() { return slug; }

    public int getRank() { return rank; }
}
