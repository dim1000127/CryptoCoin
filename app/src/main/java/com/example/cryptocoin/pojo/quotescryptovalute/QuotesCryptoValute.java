package com.example.cryptocoin.pojo.quotescryptovalute;

import com.example.cryptocoin.pojo.Status;
import com.example.cryptocoin.pojo.cryptovalutepojo.DataItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class QuotesCryptoValute implements Serializable {
    @SerializedName("data")
        @Expose
        private Map<String, DataItem> data = new HashMap<String, DataItem>();

    @SerializedName("status")
    private Status status;

    public Map<String, DataItem> getData(){
        return data;
    }

    public Status getStatus(){
        return status;
    }
}
