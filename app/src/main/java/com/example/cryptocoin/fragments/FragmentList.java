package com.example.cryptocoin.fragments;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cryptocoin.Const;
import com.example.cryptocoin.R;
import com.example.cryptocoin.adapter.CryptoValuteList;
import com.example.cryptocoin.adapter.CryptoValuteListEmpty;
import com.example.cryptocoin.pojo.cryptovalutepojo.CryptoValute;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;
import com.example.cryptocoin.retrofit.RetrofitSingleton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class FragmentList extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private Subscription subscription;
    private CryptoValuteList adapterCryptoValutePrice;
    private CryptoValute oldDataCryptoValute;
    private Metadata metadata;

    private ListView listViewTop;
    private ImageButton buttonListTop;
    private SwipeRefreshLayout swipeRefreshLayoutList;
    private RelativeLayout relativeLayout;


    private int firstVisibleElemList = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listViewTop = (ListView) view.findViewById(R.id.listview_top100_cryptovalute);
        fillEmptyList();

        buttonListTop = (ImageButton) view.findViewById(R.id.button_list_up);
        swipeRefreshLayoutList = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutList);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.parentRelativeList);

        swipeRefreshLayoutList.setOnRefreshListener(this);
        buttonListTop.setVisibility(View.GONE);

        oldDataCryptoValute = null;
        metadata = null;

        listViewTop.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                firstVisibleElemList = i;
                if(i == 0) {
                    buttonListTop.setVisibility(View.GONE);
                }
                else
                {
                    buttonListTop.setVisibility(View.VISIBLE);
                }
            }
        });

        listViewTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentBottomSheet fragmentBottomSheet = new FragmentBottomSheet(oldDataCryptoValute, metadata, i);
                fragmentBottomSheet.show(getActivity().getSupportFragmentManager(),fragmentBottomSheet.getTag());
            }
        });

        buttonListTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstVisibleElemList >= 10) {
                    listViewTop.setSelection(1);
                    listViewTop.smoothScrollToPosition(0);
                }
                else {
                    listViewTop.smoothScrollToPosition(0);
                }
            }
        });

        getCryptoValuteData();
        return  view;
    }

    private void fillEmptyList(){
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        CryptoValuteListEmpty adapterCVListEmpty = new CryptoValuteListEmpty(Math.round(dpHeight/60)+2);
        listViewTop.setAdapter(adapterCVListEmpty);
        listViewTop.setEnabled(false);
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
        //swipeRefreshLayoutList.setRefreshing(true);

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
                            swipeRefreshLayoutList.setRefreshing(false);
                            Snackbar.make(relativeLayout, R.string.connection_error, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.try_again, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            swipeRefreshLayoutList.setRefreshing(true);
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
                        metadata = (Metadata) _cryptoValuteMetadata.get(Const.METADATA_KEY_MAP);
                        if (isAdded()) {
                            adapterCryptoValutePrice = new CryptoValuteList(_cryptoValute, metadata);
                            listViewTop.setAdapter(adapterCryptoValutePrice);
                            listViewTop.setEnabled(true);
                            oldDataCryptoValute = _cryptoValute;
                            swipeRefreshLayoutList.setRefreshing(false);

                        }
                    }
                });
    }
}
