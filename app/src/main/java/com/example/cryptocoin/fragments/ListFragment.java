package com.example.cryptocoin.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cryptocoin.R;
import com.example.cryptocoin.RetrofitSingleton;
import com.example.cryptocoin.adapter.AdapterCryptoValutePrice;
import com.example.cryptocoin.cryptovalutepojo.CryptoValute;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ListFragment extends Fragment {
    private ListView listViewTop;
    private ImageButton buttonListTop;
    private AdapterCryptoValutePrice adapterCryptoValutePrice;
    private Subscription subscription;
    private boolean isLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listViewTop = (ListView) view.findViewById(R.id.listview_top100_cryptovalute);
        buttonListTop = (ImageButton) view.findViewById(R.id.button_list_up);
        buttonListTop.setVisibility(View.GONE);
        listViewTop.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(i == 0) {
                    buttonListTop.setVisibility(View.GONE);
                }
                else
                {
                    buttonListTop.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonListTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //listViewTop.setSelection(0);
                listViewTop.smoothScrollToPosition(0);
            }
        });
        getCryptoValuteData();
        return  view;
    }

    private void getCryptoValuteData(){
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        subscription = RetrofitSingleton.getCryptoValuteObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CryptoValute>() {
                    @Override
                    public void onCompleted() {
                        Log.d("onCompleted", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CryptoValute _cryptoValute) {
                        isLoading = false;
                        adapterCryptoValutePrice = new AdapterCryptoValutePrice(_cryptoValute);
                        listViewTop.setAdapter(adapterCryptoValutePrice);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    /*private void APIGetPriceCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestsAPI requestsAPI = retrofit.create(RequestsAPI.class);

        Call<CryptoValute> dataCryptoValute = requestsAPI.getDataCryptoValute(1,100);

        dataCryptoValute.enqueue(new Callback<CryptoValute>() {
            @Override
            public void onResponse(Call<CryptoValute> call, Response<CryptoValute> response) {
                if(response.isSuccessful()){
                    CryptoValute dataCryptoValute = null;
                    dataCryptoValute = response.body();
                    adapterCryptoValutePrice = new AdapterCryptoValutePrice(dataCryptoValute);
                    listViewTop.setAdapter(adapterCryptoValutePrice);
                    Log.d("List ", String.valueOf(response.body()));
                }
                else{
                    Log.d("Response code ", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<CryptoValute> call, Throwable t) {
                Log.d("Failure", t.toString());
            }
        });
    }*/
}
