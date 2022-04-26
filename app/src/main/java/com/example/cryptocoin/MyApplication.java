package com.example.cryptocoin;

import android.app.Application;

import com.example.cryptocoin.retrofit.RetrofitQuotesWatchList;
import com.example.cryptocoin.retrofit.RetrofitIdSingleton;
import com.example.cryptocoin.retrofit.RetrofitQuotesConvertSingleton;
import com.example.cryptocoin.retrofit.RetrofitQuotesSingleton;
import com.example.cryptocoin.retrofit.RetrofitRuble;
import com.example.cryptocoin.retrofit.RetrofitSingleton;

public class MyApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        RetrofitSingleton.init();
        RetrofitIdSingleton.initIdRetrofit();
        RetrofitQuotesSingleton.initQuotes();
        RetrofitQuotesConvertSingleton.initQuotesConvert();
        RetrofitQuotesWatchList.initQuotesWatchList();
        RetrofitRuble.initRetrofitRuble();
    }
}
