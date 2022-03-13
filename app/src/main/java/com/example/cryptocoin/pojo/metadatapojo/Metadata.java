package com.example.cryptocoin.pojo.metadatapojo;

import com.example.cryptocoin.pojo.cryptovalutepojo.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Metadata implements Serializable{
    @SerializedName("data")
        @Expose
        private Map<String, Item> data = new HashMap<String, Item>();

    @SerializedName("status")
    private Status status;

    public Map<String, Item> getData(){
        return data;
    }

    public Status getStatus(){
        return status;
    }
}


