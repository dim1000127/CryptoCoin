package com.example.cryptocoin.retrofit;

import android.util.Log;

import com.example.cryptocoin.Const;
import com.example.cryptocoin.pojo.cryptovalutepojo.CryptoValute;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;
import com.example.cryptocoin.pojo.quotescryptovalute.QuotesCryptoValute;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class RetrofitQuotesSingleton {

    private static Observable<QuotesCryptoValute> observableRetrofit;
    private static Observable<Metadata> observableRetrofitMetadata;
    private static Subscription subscription;
    private static BehaviorSubject<Map<String, Object>> observableQuotesCVMetadata;
    private static Map<String, Object> quotesCVMetadataMap= new HashMap<String, Object>();

    private static RequestsAPI requestsAPI;

    private RetrofitQuotesSingleton(){

    }

    public static void initQuotes(){
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();

        requestsAPI = retrofit.create(RequestsAPI.class);

        //observableRetrofit = requestsAPI.getDataCryptoValute(1,100);
    }

    //Криптовалюта + метаданные//
    public static void resetCryptoValuteObservable(String idStr){
        observableQuotesCVMetadata = BehaviorSubject.create();
        quotesCVMetadataMap.clear();

        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }

        observableRetrofit = requestsAPI.getQuoteCryptoValute(idStr);

        subscription = observableRetrofit
                .flatMap(new Func1<QuotesCryptoValute, Observable<Metadata>>() {
                    @Override
                    public Observable<Metadata> call(QuotesCryptoValute quotesCryptoValute) {
                        observableRetrofitMetadata = requestsAPI.getMetadata(idStr, "logo");
                        quotesCVMetadataMap.put(Const.CRYPTOVALUTE_KEY_MAP, quotesCryptoValute);

                        return observableRetrofitMetadata;
                    }
                })
                .subscribe(new Subscriber<Metadata>()
                {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        observableQuotesCVMetadata.onError(e);
                        Log.d("Metadata", e.toString());
                    }

                    @Override
                    public void onNext(Metadata metadata) {
                        quotesCVMetadataMap.put(Const.METADATA_KEY_MAP, metadata);
                        observableQuotesCVMetadata.onNext(quotesCVMetadataMap);
                    }
                });
    }

    public static Observable<Map<String, Object>> getQuotesCVObservable(String idStr){
        if(observableQuotesCVMetadata == null){
            resetCryptoValuteObservable(idStr);
        }
        return observableQuotesCVMetadata;
    }
}
