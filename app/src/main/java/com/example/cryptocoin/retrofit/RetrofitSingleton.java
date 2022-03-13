package com.example.cryptocoin.retrofit;

import android.util.Log;

import com.example.cryptocoin.Const;
import com.example.cryptocoin.pojo.cryptovalutepojo.CryptoValute;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;

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

public class RetrofitSingleton{

    private static Observable<CryptoValute> observableRetrofit;
    private static Observable<Metadata> observableRetrofitMetadata;
    private static Subscription subscription;
    private static BehaviorSubject<Map<String, Object>> observableCryptoValuteMetadata;
    private static Map<String, Object> cryptoValuteMetadataMap= new HashMap<String, Object>();

    private static RequestsAPI requestsAPI;

    private RetrofitSingleton(){

    }

    public static void init(){
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
    public static void resetCryptoValuteObservable(){
        observableCryptoValuteMetadata = BehaviorSubject.create();
        cryptoValuteMetadataMap.clear();

        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }

        observableRetrofit = requestsAPI.getDataCryptoValute(1,100);

        subscription = observableRetrofit
                .flatMap(new Func1<CryptoValute, Observable<Metadata>>() {
                    @Override
                    public Observable<Metadata> call(CryptoValute cryptoValute) {
                        int idCrytoValute = cryptoValute.getData().get(0).getId();
                        String idStrQueryMetadata = String.valueOf(idCrytoValute);
                        for (int i = 1; i<cryptoValute.getData().size(); i++){
                            idStrQueryMetadata += "," + String.valueOf(cryptoValute.getData().get(i).getId());
                        }

                        observableRetrofitMetadata = requestsAPI.getMetadata(idStrQueryMetadata, "logo");
                        cryptoValuteMetadataMap.put(Const.CRYPTOVALUTE_KEY_MAP, cryptoValute);

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
                        observableCryptoValuteMetadata.onError(e);
                        Log.d("Metadata", e.toString());
                    }

                    @Override
                    public void onNext(Metadata metadata) {
                        cryptoValuteMetadataMap.put(Const.METADATA_KEY_MAP, metadata);
                        observableCryptoValuteMetadata.onNext(cryptoValuteMetadataMap);
                    }
                });
    }

    public static Observable<Map<String, Object>> getCryptoValuteObservable(){
        if(observableCryptoValuteMetadata == null){
            resetCryptoValuteObservable();
        }
        return observableCryptoValuteMetadata;
    }

}
