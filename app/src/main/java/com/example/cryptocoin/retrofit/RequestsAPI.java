package com.example.cryptocoin.retrofit;

import com.example.cryptocoin.BuildConfig;
import com.example.cryptocoin.pojo.cryptovalutepojo.CryptoValute;
import com.example.cryptocoin.pojo.globalmetricspojo.GlobalMetrics;
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
            BuildConfig.API_KEY})
    @GET("/v1/cryptocurrency/listings/latest")
    Observable<CryptoValute> getDataCryptoValute(@Query("start") int start, @Query("limit") int limit);

    @Headers({"Accept: application/json",
            BuildConfig.API_KEY})
    @GET("/v2/cryptocurrency/info")
    Observable<Metadata> getMetadata(@Query("id") String id, @Query("aux") String aux);

    @Headers({"Accept: application/json",
            BuildConfig.API_KEY})
    @GET("/v1/cryptocurrency/map")
    Observable<IdCryptoValute> getIdCryptoValute(@Query("aux") String aux);

    @Headers({"Accept: application/json",
            BuildConfig.API_KEY})
    @GET("/v2/cryptocurrency/quotes/latest")
    Observable<QuotesCryptoValute> getQuoteCryptoValute(@Query("id") String id);

    @Headers({"Accept: application/json",
            BuildConfig.API_KEY})
    @GET("/v1/global-metrics/quotes/latest")
    Observable<GlobalMetrics> getGlobalMetrics();

    @GET("/daily_json.js")
    Observable<RubleExchange> getRubleExchange();
}
