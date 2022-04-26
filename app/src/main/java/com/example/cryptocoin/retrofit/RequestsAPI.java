package com.example.cryptocoin.retrofit;

import com.example.cryptocoin.pojo.cryptovalutepojo.CryptoValute;
import com.example.cryptocoin.pojo.idcryptovalutepojo.IdCryptoValute;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;
import com.example.cryptocoin.pojo.quotescryptovalute.QuotesCryptoValute;
import com.example.cryptocoin.pojo.rublepojo.RubleExchange;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

public interface RequestsAPI {

    @Headers({"Accept: application/json",
            "X-CMC_PRO_API_KEY: 908e2080-ad8d-4d43-bd0a-e65b8587d172"})
    @GET("/v1/cryptocurrency/listings/latest")
    Observable<CryptoValute> getDataCryptoValute(@Query("start") int start, @Query("limit") int limit);

    @Headers({"Accept: application/json",
            "X-CMC_PRO_API_KEY: 908e2080-ad8d-4d43-bd0a-e65b8587d172"})
    @GET("/v2/cryptocurrency/info")
    Observable<Metadata> getMetadata(@Query("id") String id, @Query("aux") String aux);

    @Headers({"Accept: application/json",
            "X-CMC_PRO_API_KEY: 908e2080-ad8d-4d43-bd0a-e65b8587d172"})
    @GET("/v1/cryptocurrency/map")
    Observable<IdCryptoValute> getIdCryptoValute(@Query("aux") String aux);

    @Headers({"Accept: application/json",
            "X-CMC_PRO_API_KEY: 908e2080-ad8d-4d43-bd0a-e65b8587d172"})
    @GET("/v2/cryptocurrency/quotes/latest")
    Observable<QuotesCryptoValute> getQuoteCryptoValute(@Query("id") String id);

    @GET("/daily_json.js")
    Observable<RubleExchange> getRubleExchange();
}
