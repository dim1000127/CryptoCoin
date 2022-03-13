package com.example.cryptocoin.pojo.idcryptovalutepojo;

import com.example.cryptocoin.pojo.cryptovalutepojo.Status;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IdCryptoValute {
    @SerializedName("data")
    private List<ItemID> data;

    @SerializedName("status")
    private Status status;

    public List<ItemID> getData(){
        return data;
    }

    public Status getStatus(){
        return status;
    }

    public void setData(ItemID data) {
        this.data.add(data);
    }
}
