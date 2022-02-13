package com.example.cryptocoin.retrofit;

import com.example.cryptocoin.Const;
import com.example.cryptocoin.cryptovalutepojo.CryptoValute;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class RetrofitSingleton{

    private static Observable<CryptoValute> observableRetrofit;
    private static BehaviorSubject<CryptoValute> observableCryptoValute;
    private static Subscription subscription;

    private RetrofitSingleton(){

    }

    public static void init(){
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();

        RequestsAPI requestsAPI = retrofit.create(RequestsAPI.class);

        observableRetrofit = requestsAPI.getDataCryptoValute(1,100);
    }

    public static void resetCryptoValuteObservable(){
        observableCryptoValute = BehaviorSubject.create();

        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }

        subscription = observableRetrofit.subscribe(new Subscriber<CryptoValute>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                observableCryptoValute.onError(e);
            }

            @Override
            public void onNext(CryptoValute cryptoValute) {
                observableCryptoValute.onNext(cryptoValute);
            }
        });
    }

    public static Observable<CryptoValute> getCryptoValuteObservable(){
        if(observableCryptoValute == null){
            resetCryptoValuteObservable();
        }
        return observableCryptoValute;
    }

    public static void resetMetaDataObservable(){

    }

}
