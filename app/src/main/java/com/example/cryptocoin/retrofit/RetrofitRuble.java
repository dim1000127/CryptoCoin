package com.example.cryptocoin.retrofit;

import com.example.cryptocoin.Const;
import com.example.cryptocoin.pojo.rublepojo.RubleExchange;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class RetrofitRuble {

    private static Observable<RubleExchange> observableRetrofit;
    private static Subscription subscription;
    private static BehaviorSubject<RubleExchange> observableRubleExchange;

    public static void initRetrofitRuble(){
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Const.BASE_URL_CB)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();

        RequestsAPI requestsAPI = retrofit.create(RequestsAPI.class);

        observableRetrofit = requestsAPI.getRubleExchange();
    }

    public static void resetRubleExchangeObservable(){
        observableRubleExchange = BehaviorSubject.create();

        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }

        subscription = observableRetrofit.subscribe(new Subscriber<RubleExchange>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                observableRubleExchange.onError(e);
            }

            @Override
            public void onNext(RubleExchange rubleExchange) {
                observableRubleExchange.onNext(rubleExchange);
            }
        });
    }

    public static Observable<RubleExchange> getRubleExchangeObservable(){
        if(observableRubleExchange == null){
            resetRubleExchangeObservable();
        }
        return observableRubleExchange;
    }
}
