package com.example.cryptocoin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cryptocoin.Const;
import com.example.cryptocoin.R;
import com.example.cryptocoin.activity.ConvertCryptoValute;
import com.example.cryptocoin.activity.WatchList;
import com.example.cryptocoin.adapter.GrowthFallRecyclerView;
import com.example.cryptocoin.adapter.GrowthFallRecyclerViewEmpty;
import com.example.cryptocoin.pojo.cryptovalutepojo.CryptoValute;
import com.example.cryptocoin.pojo.cryptovalutepojo.DataItem;
import com.example.cryptocoin.pojo.globalmetricspojo.GlobalMetrics;
import com.example.cryptocoin.pojo.metadatapojo.Item;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;
import com.example.cryptocoin.retrofit.RetrofitGlobalMetrics;
import com.example.cryptocoin.retrofit.RetrofitSingleton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FragmentHome extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private Subscription subscription;
    private Subscription subscriptionGlobalMetrics;


    private GrowthFallRecyclerView adapterRVGrowth;
    private GrowthFallRecyclerView adapterRVFall;
    private GrowthFallRecyclerView adapterRVCap;
    private CryptoValute oldDataCryptoValute;
    private Metadata oldMetadata;

    private Button buttonOpenConvertCryptoValute;
    private Button buttonOpenWatchList;
    private SwipeRefreshLayout swipeRefreshLayoutHome;
    private RecyclerView recyclerViewLeadersGrowth;
    private RecyclerView recyclerViewLeadersFall;
    private RecyclerView recyclerViewLeadersCap;
    private TextView textViewMarcetCap;
    private TextView textViewMarketCapPercentChange;
    private TextView textViewVolume24h;
    private TextView textViewVolume24hPercentChange;
    private TextView textViewDominanceBtc;
    private TextView textViewDominanceEth;
    private TextView textViewMarketcapEmpty;
    private TextView textViewVolume24hEmpty;
    private TextView textViewDominanceBtcEmpty;
    private TextView textViewDominanceEthEmpty;
    private NestedScrollView nestedScrollView;

    private GrowthFallRecyclerView.OnDatalickListener dataClickListenerLeadersCap;
    private GrowthFallRecyclerView.OnDatalickListener dataClickListenerLeadersFall;
    private GrowthFallRecyclerView.OnDatalickListener dataClickListenerLeadersGrowth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerViewLeadersGrowth = rootView.findViewById(R.id.rvLeadersGrowth);
        recyclerViewLeadersFall = rootView.findViewById(R.id.rvLeadersFall);
        recyclerViewLeadersCap = rootView.findViewById(R.id.rvLeadersCap);
        fillRecyclerViewEmpty();

        textViewMarcetCap = rootView.findViewById(R.id.tv_market_cap);
        textViewMarketCapPercentChange = rootView.findViewById(R.id.tv_market_cap_percent_change);
        textViewVolume24h = rootView.findViewById(R.id.tv_volume_24h);
        textViewVolume24hPercentChange = rootView.findViewById(R.id.tv_volume_24h_percent_change);
        textViewDominanceBtc = rootView.findViewById(R.id.tv_dominance_btc);
        textViewDominanceEth = rootView.findViewById(R.id.tv_dominance_eth);
        textViewMarketcapEmpty = rootView.findViewById(R.id.tv_market_cap_empty);
        textViewVolume24hEmpty = rootView.findViewById(R.id.tv_volume24h_empty);
        textViewDominanceBtcEmpty = rootView.findViewById(R.id.tv_dominance_btc_empty);
        textViewDominanceEthEmpty = rootView.findViewById(R.id.tv_dominance_eth_empty);
        nestedScrollView = rootView.findViewById(R.id.layout_home_nestedScrollView);
        swipeRefreshLayoutHome = rootView.findViewById(R.id.swipeRefreshLayoutHome);
        swipeRefreshLayoutHome.setOnRefreshListener(this);

        oldDataCryptoValute = null;

        buttonOpenConvertCryptoValute = rootView.findViewById(R.id.btn_open_convert_cryptovalute);
        buttonOpenConvertCryptoValute.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ConvertCryptoValute.class);
            startActivity(intent);
        });

        buttonOpenWatchList = rootView.findViewById(R.id.btn_open_watchlist);
        buttonOpenWatchList.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), WatchList.class);
            startActivity(intent);
        });


        dataClickListenerLeadersCap = (dataItem, position) -> {
            String idItem = String.valueOf(dataItem.getId());
            Item metadataItem = oldMetadata.getData().get(idItem);
            FragmentBottomSheet fragmentBottomSheet = new FragmentBottomSheet(dataItem, metadataItem);
            fragmentBottomSheet.show(getActivity().getSupportFragmentManager(),fragmentBottomSheet.getTag());
        };

        dataClickListenerLeadersFall = (dataItem, position) -> {
            String idItem = String.valueOf(dataItem.getId());
            Item metadataItem = oldMetadata.getData().get(idItem);
            FragmentBottomSheet fragmentBottomSheet = new FragmentBottomSheet(dataItem, metadataItem);
            fragmentBottomSheet.show(getActivity().getSupportFragmentManager(),fragmentBottomSheet.getTag());
        };

        dataClickListenerLeadersGrowth = (dataItem, position) -> {
            String idItem = String.valueOf(dataItem.getId());
            Item metadataItem = oldMetadata.getData().get(idItem);
            FragmentBottomSheet fragmentBottomSheet = new FragmentBottomSheet(dataItem, metadataItem);
            fragmentBottomSheet.show(getActivity().getSupportFragmentManager(),fragmentBottomSheet.getTag());
        };

        recyclerViewLeadersCap.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    swipeRefreshLayoutHome.setEnabled(false);
                    nestedScrollView.setEnabled(false);
                }

                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    swipeRefreshLayoutHome.setEnabled(true);
                    nestedScrollView.setEnabled(true);
                }
            }
        });

        recyclerViewLeadersFall.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    swipeRefreshLayoutHome.setEnabled(false);
                    nestedScrollView.setEnabled(false);
                }

                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    swipeRefreshLayoutHome.setEnabled(true);
                    nestedScrollView.setEnabled(true);
                }
            }
        });

        recyclerViewLeadersGrowth.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    swipeRefreshLayoutHome.setEnabled(false);
                    nestedScrollView.setEnabled(false);
                }

                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    swipeRefreshLayoutHome.setEnabled(true);
                    nestedScrollView.setEnabled(true);
                }
            }
        });


        getCryptoValuteData();
        getGlobalMetrics();

        return  rootView;
    }

    private void getGlobalMetrics() {
        if (subscriptionGlobalMetrics != null && !subscriptionGlobalMetrics.isUnsubscribed()){
            subscriptionGlobalMetrics.unsubscribe();
        }
        subscriptionGlobalMetrics = RetrofitGlobalMetrics.getGlobalMetricsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GlobalMetrics>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GlobalMetrics globalMetrics) {
                        fillGlobalMetrics(globalMetrics);
                    }
                });
    }

    private void fillGlobalMetrics(GlobalMetrics globalMetrics) {

        textViewMarketcapEmpty.setVisibility(View.GONE);
        textViewVolume24hEmpty.setVisibility(View.GONE);
        textViewDominanceBtcEmpty.setVisibility(View.GONE);
        textViewDominanceEthEmpty.setVisibility(View.GONE);

        double marketCap = globalMetrics.getDataGlobalMetrics().getQuoteGlobalMetrics().getUsdGlobalMetrics().getTotalMarketCap();
        textViewMarcetCap.setText(String.format("$%,.2f", marketCap));

        double percentChangeMarketCap = globalMetrics.getDataGlobalMetrics().getQuoteGlobalMetrics().getUsdGlobalMetrics().getTotalMarketCapPercentChange();
        textViewMarketCapPercentChange.setText(String.format("%.2f%%", percentChangeMarketCap));
        if(percentChangeMarketCap>=0){
            textViewMarketCapPercentChange.setTextColor(ResourcesCompat.getColor(getContext().getResources(), R.color.green, null));
        }
        else {
            textViewMarketCapPercentChange.setTextColor(ResourcesCompat.getColor(getContext().getResources(), R.color.red, null));
        }


        double volume24h = globalMetrics.getDataGlobalMetrics().getQuoteGlobalMetrics().getUsdGlobalMetrics().getTotalVolume24h();
        textViewVolume24h.setText(String.format("$%,.2f",volume24h));

        double percentChangeVolume24h = globalMetrics.getDataGlobalMetrics().getQuoteGlobalMetrics().getUsdGlobalMetrics().getTotalVolume24hPercentChange();
        textViewVolume24hPercentChange.setText(String.format("%.2f%%", percentChangeVolume24h));
        if(percentChangeVolume24h>=0){
            textViewVolume24hPercentChange.setTextColor(ResourcesCompat.getColor(getContext().getResources(), R.color.green, null));
        }
        else {
            textViewVolume24hPercentChange.setTextColor(ResourcesCompat.getColor(getContext().getResources(), R.color.red, null));
        }

        double dominanceBTC = globalMetrics.getDataGlobalMetrics().getBtcDominance();
        textViewDominanceBtc.setText(String.format("%.2f%%", dominanceBTC));

        double dominanceETH = globalMetrics.getDataGlobalMetrics().getEthDominance();
        textViewDominanceEth.setText(String.format("%.2f%%", dominanceETH));
    }


    private void fillRecyclerViewEmpty() {
        LinearLayoutManager horizontalLayoutManagerGrowth
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewLeadersGrowth.setLayoutManager(horizontalLayoutManagerGrowth);
        LinearLayoutManager horizontalLayoutManagerFall
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewLeadersFall.setLayoutManager(horizontalLayoutManagerFall);
        LinearLayoutManager horizontalLayoutManagerCap
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewLeadersCap.setLayoutManager(horizontalLayoutManagerCap);

        GrowthFallRecyclerViewEmpty growthFallRecyclerViewEmpty = new GrowthFallRecyclerViewEmpty();
        recyclerViewLeadersGrowth.setAdapter(growthFallRecyclerViewEmpty);
        recyclerViewLeadersFall.setAdapter(growthFallRecyclerViewEmpty);
        recyclerViewLeadersCap.setAdapter(growthFallRecyclerViewEmpty);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        if(subscriptionGlobalMetrics != null && !subscriptionGlobalMetrics.isUnsubscribed()){
            subscriptionGlobalMetrics.unsubscribe();
        }
    }

    @Override
    public void onRefresh() {
        RetrofitSingleton.resetCryptoValuteObservable();
        RetrofitGlobalMetrics.resetGlobalMetricsObservable();
        getCryptoValuteData();
        getGlobalMetrics();
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
                                            RetrofitGlobalMetrics.resetGlobalMetricsObservable();
                                            getCryptoValuteData();
                                            getGlobalMetrics();
                                        }
                                    })
                                    .show();
                        }
                    }

                    @Override
                    public void onNext(Map<String, Object> _cryptoValuteMetadata) {
                        CryptoValute _cryptoValute = (CryptoValute) _cryptoValuteMetadata.get(Const.CRYPTOVALUTE_KEY_MAP);
                        Metadata _metadata = (Metadata) _cryptoValuteMetadata.get(Const.METADATA_KEY_MAP);
                        _cryptoValute.getData().sort(Comparator.comparing(DataItem::getCmcRank));
                        if (isAdded()) {
                            fillBlocksTopThree(_cryptoValute, _metadata);
                            fillRVGrowthFall(_cryptoValute, _metadata);
                            oldDataCryptoValute = _cryptoValute;
                            oldMetadata =_metadata;
                            swipeRefreshLayoutHome.setRefreshing(false);
                        }
                    }
                });
    }

    private void fillRVGrowthFall(CryptoValute cryptoValute, Metadata metadata) {
        List<DataItem> dataCVItems = new ArrayList<>();
        dataCVItems.addAll(cryptoValute.getData());

        List<DataItem> dataItemsGrowth = new ArrayList<>();
        List<DataItem> dataItemsFall = new ArrayList<>();

        DataItem maxItem;
        DataItem minItem;

        for (int i = 0; i<5; i++){
            maxItem = dataCVItems.get(0);
            for (DataItem dataItem:dataCVItems){
                double percentValue = dataItem.getQuote().getUsdDataCoin().getPercentChange24h();
                if(maxItem.getQuote().getUsdDataCoin().getPercentChange24h()<percentValue){
                    maxItem = dataItem;
                }
            }
            dataItemsGrowth.add(maxItem);
            dataCVItems.remove(maxItem);
        }

        for (int i = 0; i<5; i++){
            minItem = dataCVItems.get(0);
            for (DataItem dataItem:dataCVItems){
                double percentValue = dataItem.getQuote().getUsdDataCoin().getPercentChange24h();
                if(minItem.getQuote().getUsdDataCoin().getPercentChange24h()>percentValue){
                    minItem = dataItem;
                }
            }
            dataItemsFall.add(minItem);
            dataCVItems.remove(minItem);
        }

        LinearLayoutManager horizontalLayoutManagerGrowth
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewLeadersGrowth.setLayoutManager(horizontalLayoutManagerGrowth);
        adapterRVGrowth = new GrowthFallRecyclerView(dataItemsGrowth, metadata, dataClickListenerLeadersGrowth);
        recyclerViewLeadersGrowth.setAdapter(adapterRVGrowth);

        LinearLayoutManager horizontalLayoutManagerFall
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewLeadersFall.setLayoutManager(horizontalLayoutManagerFall);
        adapterRVFall = new GrowthFallRecyclerView(dataItemsFall, metadata, dataClickListenerLeadersFall);
        recyclerViewLeadersFall.setAdapter(adapterRVFall);
    }

    private void fillBlocksTopThree(CryptoValute dataCryptoValute, Metadata metadata){
        List<DataItem> dataItemsCV = new ArrayList<>();
        dataItemsCV = dataCryptoValute.getData().subList(0,5);

        LinearLayoutManager horizontalLayoutManagerCap
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewLeadersCap.setLayoutManager(horizontalLayoutManagerCap);
        adapterRVCap = new GrowthFallRecyclerView(dataItemsCV, metadata, dataClickListenerLeadersCap);
        recyclerViewLeadersCap.setAdapter(adapterRVCap);
    }
}

