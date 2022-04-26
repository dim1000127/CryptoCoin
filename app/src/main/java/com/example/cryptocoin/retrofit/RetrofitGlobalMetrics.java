package com.example.cryptocoin.retrofit;

import com.example.cryptocoin.Const;
import com.example.cryptocoin.pojo.globalmetricspojo.GlobalMetrics;
import com.example.cryptocoin.pojo.idcryptovalutepojo.IdCryptoValute;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class RetrofitGlobalMetrics {

    private static Observable<GlobalMetrics> observableRetrofit;
    private static Subscription subscription;
    private static BehaviorSubject<GlobalMetrics> observableGlobalMetrics;

    private RetrofitGlobalMetrics(){

    }

    public static void initGlobalMetricsRetrofit(){
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();

        RequestsAPI requestsAPI = retrofit.create(RequestsAPI.class);

        observableRetrofit = requestsAPI.getGlobalMetrics();
    }

    //ID криптовалют по CoinMarketCap//
    public static void resetGlobalMetricsObservable(){
        observableGlobalMetrics = BehaviorSubject.create();

        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }

        subscription = observableRetrofit.subscribe(new Subscriber<GlobalMetrics>()
        {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                observableGlobalMetrics.onError(e);
                //Log.d("IdCryptoValute", e.toString());
            }

            @Override
            public void onNext(GlobalMetrics globalMetrics) {
                observableGlobalMetrics.onNext(globalMetrics);
            }
        });
    }

    public static Observable<GlobalMetrics> getGlobalMetricsObservable(){
        if(observableGlobalMetrics == null){
            resetGlobalMetricsObservable();
        }
        return observableGlobalMetrics;
    }

}
