package com.example.cryptocoin.pojo.cryptovalutepojo;

import com.example.cryptocoin.pojo.Status;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

import java.util.List;

public class CryptoValute implements Serializable{
    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("status")
    private Status status;

    public List<DataItem> getData(){
        return data;
    }

    public Status getStatus(){
        return status;
    }
}


