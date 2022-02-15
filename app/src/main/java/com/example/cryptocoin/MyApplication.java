package com.example.cryptocoin;

import android.app.Application;

import com.example.cryptocoin.retrofit.RetrofitSingleton;

public class MyApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        RetrofitSingleton.init();
    }
}