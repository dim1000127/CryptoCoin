package com.example.cryptocoin.retrofit;

import com.example.cryptocoin.Const;
import com.example.cryptocoin.pojo.idcryptovalutepojo.IdCryptoValute;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class RetrofitIdSingleton {
    private static Observable<IdCryptoValute> observableRetrofit;
    private static Subscription subscription;
    private static BehaviorSubject<IdCryptoValute> observableIdCryptoValute;

    private RetrofitIdSingleton(){

    }

    public static void initIdRetrofit(){
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();

        RequestsAPI requestsAPI = retrofit.create(RequestsAPI.class);

        observableRetrofit = requestsAPI.getIdCryptoValute("");
    }

    //ID криптовалют по CoinMarketCap//
    public static void resetIdCryptoValuteObservable(){
        observableIdCryptoValute = BehaviorSubject.create();

        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }

        subscription = observableRetrofit.subscribe(new Subscriber<IdCryptoValute>()
                {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        observableIdCryptoValute.onError(e);
                    }

                    @Override
                    public void onNext(IdCryptoValute idCryptoValute) {
                        observableIdCryptoValute.onNext(idCryptoValute);
                    }
                });
    }

    public static Observable<IdCryptoValute> getIdCryptoValuteObservable(){
        if(observableIdCryptoValute == null){
            resetIdCryptoValuteObservable();
        }
        return observableIdCryptoValute;
    }
}
