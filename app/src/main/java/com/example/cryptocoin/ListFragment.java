package com.example.cryptocoin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cryptocoin.cryptovalutepojo.CryptoValute;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class ListFragment extends Fragment {
    public static final String BASE_URL = "https://pro-api.coinmarketcap.com";
    ListView listView;
    AdapterCryptoValutePrice adapterCryptoValutePrice;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listView = (ListView) view.findViewById(R.id.listview_top100_cryptovalute);
        return  view;
    }

    @Override
    public void onStart() {
        APIGetPriceCall();
        super.onStart();
    }

    private void APIGetPriceCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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
                    listView.setAdapter(adapterCryptoValutePrice);
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
    }
}
