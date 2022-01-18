package com.example.cryptocoin;

import com.example.cryptocoin.cryptovalutepojo.CryptoValute;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RequestsAPI {

    @Headers({"Accept: application/json",
            "X-CMC_PRO_API_KEY: 908e2080-ad8d-4d43-bd0a-e65b8587d172"})
   @GET("/v1/cryptocurrency/listings/latest")
    Call<List<CryptoValute>> getDataCryptoValute(@Query("start") int start, @Query("limit") int limit, @Query("convert") String convert);
}
