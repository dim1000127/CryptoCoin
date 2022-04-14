package com.example.cryptocoin.pojo.idcryptovalutepojo;

import com.example.cryptocoin.pojo.Status;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class IdCryptoValute implements Serializable {
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
