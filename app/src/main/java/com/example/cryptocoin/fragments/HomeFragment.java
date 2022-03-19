package com.example.cryptocoin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cryptocoin.activity.ConvertCryptoValute;
import com.example.cryptocoin.Const;
import com.example.cryptocoin.R;
import com.example.cryptocoin.pojo.cryptovalutepojo.CryptoValute;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;
import com.example.cryptocoin.retrofit.RetrofitSingleton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private Button buttonOpenConvertCryptoValute;
    private SwipeRefreshLayout swipeRefreshLayoutHome;

    private Subscription subscription;

    private CryptoValute oldDataCryptoValute = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayoutHome = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayoutHome);
        swipeRefreshLayoutHome.setOnRefreshListener(this);
        buttonOpenConvertCryptoValute = (Button) rootView.findViewById(R.id.btn_open_convert_cryptovalute);
        buttonOpenConvertCryptoValute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ConvertCryptoValute.class);
                startActivity(intent);
            }
        });
        getCryptoValuteData();

        return  rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onRefresh() {
        RetrofitSingleton.resetCryptoValuteObservable();
        getCryptoValuteData();
    }


    private void getCryptoValuteData(){
        //swipeRefreshLayoutHome.setRefreshing(true);

        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        subscription = RetrofitSingleton.getCryptoValuteObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, Object>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("onCompleted", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isAdded()) {
                            swipeRefreshLayoutHome.setRefreshing(false);
                            Snackbar.make(swipeRefreshLayoutHome, R.string.connection_error, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.try_again, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            swipeRefreshLayoutHome.setRefreshing(true);
                                            RetrofitSingleton.resetCryptoValuteObservable();
                                            getCryptoValuteData();
                                        }
                                    })
                                    .show();
                        }
                    }

                    @Override
                    public void onNext(Map<String, Object> _cryptoValuteMetadata) {
                        CryptoValute _cryptoValute = (CryptoValute) _cryptoValuteMetadata.get(Const.CRYPTOVALUTE_KEY_MAP);
                        Metadata _metadata = (Metadata) _cryptoValuteMetadata.get(Const.METADATA_KEY_MAP);

                        if (isAdded()) {
                            if (oldDataCryptoValute != null) {
                                double oldPrice = oldDataCryptoValute.getData().get(0).getQuote().getUsdDataCoin().getPrice();
                                double newPrice = _cryptoValute.getData().get(0).getQuote().getUsdDataCoin().getPrice();
                                if (oldPrice == newPrice) {
                                    Snackbar.make(swipeRefreshLayoutHome, R.string.actual_data, Snackbar.LENGTH_SHORT).show();
                                    swipeRefreshLayoutHome.setRefreshing(false);
                                }
                                else
                                {
                                    fillBlocksTopThree(_cryptoValute, _metadata);
                                    oldDataCryptoValute = _cryptoValute;
                                    swipeRefreshLayoutHome.setRefreshing(false);
                                }
                            }
                            else
                            {
                                fillBlocksTopThree(_cryptoValute, _metadata);
                                oldDataCryptoValute = _cryptoValute;
                                swipeRefreshLayoutHome.setRefreshing(false);
                            }
                        }
                    }
                });
    }

    private void fillBlocksTopThree(CryptoValute dataCryptoValute, Metadata metadata){
        String idCryptoOne = String.valueOf(dataCryptoValute.getData().get(0).getId());
        String idCryptoTwo = String.valueOf(dataCryptoValute.getData().get(1).getId());
        String idCryptoThree = String.valueOf(dataCryptoValute.getData().get(2).getId());

        ImageView imageViewCryptoOne = getActivity().findViewById(R.id.imageCryptovaluteOne);
        ImageView imageViewCryptoTwo = getActivity().findViewById(R.id.imageCryptovaluteTwo);
        ImageView imageViewCryptoThree = getActivity().findViewById(R.id.imageCryptovaluteThree);
        TextView textViewSymbolCryptoOne = getActivity().findViewById(R.id.symbolCryptoOne);
        TextView textViewSymbolCryptoTwo = getActivity().findViewById(R.id.symbolCryptoTwo);
        TextView textViewSymbolCryptoThree = getActivity().findViewById(R.id.symbolCryptoThree);
        TextView textViewPriceOne = getActivity().findViewById(R.id.priceCryptoValuteOne);
        TextView textViewPriceTwo = getActivity().findViewById(R.id.priceCryptoValuteTwo);
        TextView textViewPriceThree = getActivity().findViewById(R.id.priceCryptoValuteThree);
        TextView textViewChange24hCryptoOne = getActivity().findViewById(R.id.percent_change_24hCryptoOne);
        TextView textViewChange24hCryptoTwo = getActivity().findViewById(R.id.percent_change_24hCryptoTwo);
        TextView textViewChange24hCryptoThree = getActivity().findViewById(R.id.percent_change_24hCryptoThree);

        Picasso.get()
                .load(metadata.getData().get(idCryptoOne).getLogo())
                .into(imageViewCryptoOne);
        Picasso.get()
                .load(metadata.getData().get(idCryptoTwo).getLogo())
                .into(imageViewCryptoTwo);
        Picasso.get()
                .load(metadata.getData().get(idCryptoThree).getLogo())
                .into(imageViewCryptoThree);

        textViewSymbolCryptoOne.setText(dataCryptoValute.getData().get(0).getSymbol());
        textViewSymbolCryptoTwo.setText(dataCryptoValute.getData().get(1).getSymbol());
        textViewSymbolCryptoThree.setText(dataCryptoValute.getData().get(2).getSymbol());

        textViewPriceOne.setText(String.format("$%,.2f",dataCryptoValute.getData().get(0).getQuote().getUsdDataCoin().getPrice()));
        textViewPriceTwo.setText(String.format("$%,.2f",dataCryptoValute.getData().get(1).getQuote().getUsdDataCoin().getPrice()));
        textViewPriceThree.setText(String.format("$%,.2f",dataCryptoValute.getData().get(2).getQuote().getUsdDataCoin().getPrice()));

        double valueChange24hCryptoOne = dataCryptoValute.getData().get(0).getQuote().getUsdDataCoin().getPercentChange24h();
        textViewChange24hCryptoOne.setText(String.format("%.2f%%", valueChange24hCryptoOne));
        if(valueChange24hCryptoOne>=0){ textViewChange24hCryptoOne.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null)); }
        else {textViewChange24hCryptoOne.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null)); }

        double valueChange24hCryptoTwo = dataCryptoValute.getData().get(1).getQuote().getUsdDataCoin().getPercentChange24h();
        textViewChange24hCryptoTwo.setText(String.format("%.2f%%", valueChange24hCryptoTwo));
        if(valueChange24hCryptoTwo>=0){ textViewChange24hCryptoTwo.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null)); }
        else {textViewChange24hCryptoTwo.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null)); }

        double valueChange24hCryptoThree = dataCryptoValute.getData().get(2).getQuote().getUsdDataCoin().getPercentChange24h();
        textViewChange24hCryptoThree.setText(String.format("%.2f%%", valueChange24hCryptoThree));
        if(valueChange24hCryptoThree>=0){ textViewChange24hCryptoThree.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null)); }
        else {textViewChange24hCryptoThree.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null)); }
    }
}
