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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cryptocoin.Const;
import com.example.cryptocoin.R;
import com.example.cryptocoin.adapter.CryptoValuteList;
import com.example.cryptocoin.adapter.CryptoValuteListEmpty;
import com.example.cryptocoin.pojo.cryptovalutepojo.CryptoValute;
import com.example.cryptocoin.pojo.cryptovalutepojo.DataItem;
import com.example.cryptocoin.pojo.metadatapojo.Item;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;
import com.example.cryptocoin.retrofit.RetrofitSingleton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.util.Comparator;
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
    private TextView textViewPercentChangeSelect;
    private TextView textViewSortBySelect;

    private boolean isChange1h = false;
    private boolean isChange24h = true;
    private boolean isChange7d = false;
    private boolean isChange30d = false;

    private boolean isSortByRatingUp = true;
    private boolean isSortByNameUp = false;
    private boolean isSortByPriceUp = false;
    private boolean isSortByPercentChangeUp = false;

    private boolean isSortByRating = true;
    private boolean isSortByName = false;
    private boolean isSortByPrice = false;
    private boolean isSortByPercentChange = false;

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
        textViewPercentChangeSelect = (TextView) view.findViewById(R.id.tv_list_percent_change_select);
        textViewSortBySelect = (TextView) view.findViewById(R.id.tv_list_sort_by_select);

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
                if (listViewTop.getChildAt(0) != null) {
                    swipeRefreshLayoutList.setEnabled(listViewTop.getFirstVisiblePosition() == 0 && listViewTop.getChildAt(0).getTop() == 0);
                }
            }
        });

        listViewTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String idItem = String.valueOf(oldDataCryptoValute.getData().get(i).getId());
                DataItem dataItem = oldDataCryptoValute.getData().get(i);
                Item metadataItem = metadata.getData().get(idItem);
                FragmentBottomSheet fragmentBottomSheet = new FragmentBottomSheet(dataItem, metadataItem);
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

        textViewPercentChangeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(relativeLayout, "Выберите процентное изменение", Snackbar.LENGTH_LONG)
                        .show();*/
                showDialogSelectPercentChange();
            }
        });

        textViewSortBySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(relativeLayout, "Выберите поле для сортировки", Snackbar.LENGTH_LONG)
                        .show();*/
                showDialogSelectSortBy();
            }
        });

        textViewSortBySelect.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.style_ic_up, 0);

        getCryptoValuteData();
        return  view;
    }

    private void showDialogSelectSortBy() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.modal_bottom_sheet_sort_by);

        RelativeLayout selectRating = (RelativeLayout) bottomSheetDialog.findViewById(R.id.rl_select_rating_sort_by);
        RelativeLayout selectName = (RelativeLayout) bottomSheetDialog.findViewById(R.id.rl_select_name_sort_by);
        RelativeLayout selectPrice = (RelativeLayout)  bottomSheetDialog.findViewById(R.id.rl_select_price_sort_by);
        RelativeLayout selectPercentChange = (RelativeLayout) bottomSheetDialog.findViewById(R.id.rl_select_percent_change_sort_by);
        TextView tvSelectRating = (TextView) bottomSheetDialog.findViewById(R.id.tv_sort_by_rating);
        TextView tvSelectName = (TextView) bottomSheetDialog.findViewById(R.id.tv_sort_by_name);
        TextView tvSelectPrice = (TextView) bottomSheetDialog.findViewById(R.id.tv_sort_by_price);
        TextView tvSelectPercentChangew = (TextView) bottomSheetDialog.findViewById(R.id.tv_sort_by_percent_change);

        if(isSortByRating){
            tvSelectRating.setTextColor(ResourcesCompat.getColor(getContext().getResources(), R.color.primaryColor, null));
            if(isSortByRatingUp){
                tvSelectRating.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.style_ic_sort_up, 0);
            }
            else{
                tvSelectRating.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.style_ic_sort_down, 0);
            }
        }
        if(isSortByName){
            tvSelectName.setTextColor(ResourcesCompat.getColor(getContext().getResources(), R.color.primaryColor, null));
            if(isSortByNameUp){
                tvSelectName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.style_ic_sort_up, 0);
            }
            else{
                tvSelectName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.style_ic_sort_down, 0);
            }
        }
        if(isSortByPrice){
            tvSelectPrice.setTextColor(ResourcesCompat.getColor(getContext().getResources(), R.color.primaryColor, null));
            if(isSortByPriceUp){
                tvSelectPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.style_ic_sort_up, 0);
            }
            else{
                tvSelectPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.style_ic_sort_down, 0);
            }
        }
        if(isSortByPercentChange){
            tvSelectPercentChangew.setTextColor(ResourcesCompat.getColor(getContext().getResources(), R.color.primaryColor, null));
            if(isSortByPercentChangeUp){
                tvSelectPercentChangew.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.style_ic_sort_up, 0);
            }
            else{
                tvSelectPercentChangew.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.style_ic_sort_down, 0);
            }
        }

        bottomSheetDialog.show();

        selectRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSortByRatingUp){
                    //сортировка по убыванию
                    oldDataCryptoValute.getData().sort(Comparator.comparing(DataItem::getCmcRank).reversed());
                    adapterCryptoValutePrice = new CryptoValuteList(oldDataCryptoValute, metadata);
                    adapterCryptoValutePrice.setIsChange(isChange1h, isChange24h, isChange7d, isChange30d);
                    listViewTop.setAdapter(adapterCryptoValutePrice);
                    isSortByRatingUp = false;
                    textViewSortBySelect.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.style_ic_down, 0);
                }
                else{
                    //сортировка по возрастанию
                    oldDataCryptoValute.getData().sort(Comparator.comparing(DataItem::getCmcRank));
                    adapterCryptoValutePrice = new CryptoValuteList(oldDataCryptoValute, metadata);
                    adapterCryptoValutePrice.setIsChange(isChange1h, isChange24h, isChange7d, isChange30d);
                    listViewTop.setAdapter(adapterCryptoValutePrice);
                    textViewSortBySelect.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.style_ic_up, 0);
                    isSortByRatingUp = true;
                }
                isSortByPriceUp = false;
                isSortByNameUp = false;
                isSortByPercentChangeUp = false;
                isSortByRating = true;
                isSortByName = false;
                isSortByPrice = false;
                isSortByPercentChange = false;
                textViewSortBySelect.setText(getString(R.string.sort_by_rating));
                bottomSheetDialog.dismiss();
            }
        });

        selectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSortByNameUp){
                    //сортировка по убыванию
                    oldDataCryptoValute.getData().sort(Comparator.comparing(DataItem::getName).reversed());
                    adapterCryptoValutePrice = new CryptoValuteList(oldDataCryptoValute, metadata);
                    adapterCryptoValutePrice.setIsChange(isChange1h, isChange24h, isChange7d, isChange30d);
                    listViewTop.setAdapter(adapterCryptoValutePrice);
                    textViewSortBySelect.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.style_ic_down, 0);
                    isSortByNameUp = false;
                }
                else{
                    //сортировка по возрастанию
                    oldDataCryptoValute.getData().sort(Comparator.comparing(DataItem::getName));
                    adapterCryptoValutePrice = new CryptoValuteList(oldDataCryptoValute, metadata);
                    adapterCryptoValutePrice.setIsChange(isChange1h, isChange24h, isChange7d, isChange30d);
                    listViewTop.setAdapter(adapterCryptoValutePrice);
                    textViewSortBySelect.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.style_ic_up, 0);
                    isSortByNameUp = true;
                }
                isSortByRatingUp = false;
                isSortByPriceUp = false;
                isSortByPercentChangeUp = false;
                isSortByRating = false;
                isSortByName = true;
                isSortByPrice = false;
                isSortByPercentChange = false;
                textViewSortBySelect.setText(getString(R.string.sort_by_name));
                bottomSheetDialog.dismiss();
            }
        });

        selectPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSortByPriceUp){
                    //сортировка по убыванию
                    oldDataCryptoValute.getData().sort(new Comparator<DataItem>() {
                        @Override
                        public int compare(DataItem dataItem, DataItem t1) {
                            return Double.compare(t1.getQuote().getUsdDataCoin().getPrice(), dataItem.getQuote().getUsdDataCoin().getPrice());
                        }

                        @Override
                        public boolean equals(Object o) {
                            return false;
                        }
                    });
                    adapterCryptoValutePrice = new CryptoValuteList(oldDataCryptoValute, metadata);
                    adapterCryptoValutePrice.setIsChange(isChange1h, isChange24h, isChange7d, isChange30d);
                    listViewTop.setAdapter(adapterCryptoValutePrice);
                    textViewSortBySelect.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.style_ic_down, 0);
                    isSortByPriceUp = false;
                }
                else{
                    //сортировка по возрастанию
                    oldDataCryptoValute.getData().sort(new Comparator<DataItem>() {
                        @Override
                        public int compare(DataItem dataItem, DataItem t1) {
                            return Double.compare(dataItem.getQuote().getUsdDataCoin().getPrice(), t1.getQuote().getUsdDataCoin().getPrice());
                        }

                        @Override
                        public boolean equals(Object o) {
                            return false;
                        }
                    });
                    adapterCryptoValutePrice = new CryptoValuteList(oldDataCryptoValute, metadata);
                    adapterCryptoValutePrice.setIsChange(isChange1h, isChange24h, isChange7d, isChange30d);
                    listViewTop.setAdapter(adapterCryptoValutePrice);
                    textViewSortBySelect.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.style_ic_up, 0);
                    isSortByPriceUp = true;
                }
                isSortByRatingUp = false;
                isSortByNameUp = false;
                isSortByPercentChangeUp = false;
                isSortByRating = false;
                isSortByName = false;
                isSortByPrice = true;
                isSortByPercentChange = false;
                textViewSortBySelect.setText(getString(R.string.sort_by_price));
                bottomSheetDialog.dismiss();
            }
        });

        selectPercentChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSortByPercentChangeUp){
                    //сортировка по убыванию
                    adapterCryptoValutePrice = new CryptoValuteList(sortByPercentChange(oldDataCryptoValute), metadata);
                    adapterCryptoValutePrice.setIsChange(isChange1h, isChange24h, isChange7d, isChange30d);
                    listViewTop.setAdapter(adapterCryptoValutePrice);
                    textViewSortBySelect.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.style_ic_down, 0);
                    isSortByPercentChangeUp = false;
                }
                else{
                    //сортировка по возрастанию
                    adapterCryptoValutePrice = new CryptoValuteList(sortByPercentChange(oldDataCryptoValute), metadata);
                    adapterCryptoValutePrice.setIsChange(isChange1h, isChange24h, isChange7d, isChange30d);
                    listViewTop.setAdapter(adapterCryptoValutePrice);
                    textViewSortBySelect.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.style_ic_up, 0);
                    isSortByPercentChangeUp = true;
                }
                isSortByRatingUp = false;
                isSortByNameUp = false;
                isSortByPriceUp = false;
                isSortByRating = false;
                isSortByName = false;
                isSortByPrice = false;
                isSortByPercentChange = true;
                textViewSortBySelect.setText(getString(R.string.sort_by_percent_change));
                bottomSheetDialog.dismiss();
            }
        });
    }

    private CryptoValute sortByPercentChange(CryptoValute cryptoValute){
        if (!isSortByPercentChangeUp){
            //сортировка по убыванию
            cryptoValute.getData().sort(new Comparator<DataItem>() {
                @Override
                public int compare(DataItem dataItem, DataItem t1) {
                    if(isChange1h){
                        return Double.compare(dataItem.getQuote().getUsdDataCoin().getPercentChange1h(), t1.getQuote().getUsdDataCoin().getPercentChange1h());
                    }else if(isChange7d){
                        return Double.compare(dataItem.getQuote().getUsdDataCoin().getPercentChange7d(), t1.getQuote().getUsdDataCoin().getPercentChange7d());
                    }
                    else if(isChange30d){
                        return Double.compare(dataItem.getQuote().getUsdDataCoin().getPercentChange30d(), t1.getQuote().getUsdDataCoin().getPercentChange30d());
                    }
                    else {
                        return Double.compare(dataItem.getQuote().getUsdDataCoin().getPercentChange24h(), t1.getQuote().getUsdDataCoin().getPercentChange24h());
                    }
                }

                @Override
                public boolean equals(Object o) {
                    return false;
                }
            });
        }
        else{
            //сортировка по возрастанию
            cryptoValute.getData().sort(new Comparator<DataItem>() {
                @Override
                public int compare(DataItem dataItem, DataItem t1) {
                    if(isChange1h){
                        return Double.compare(t1.getQuote().getUsdDataCoin().getPercentChange1h(), dataItem.getQuote().getUsdDataCoin().getPercentChange1h());
                    }
                    else if(isChange7d){
                        return Double.compare(t1.getQuote().getUsdDataCoin().getPercentChange7d(), dataItem.getQuote().getUsdDataCoin().getPercentChange7d());
                    }
                    else if(isChange30d){
                        return Double.compare(t1.getQuote().getUsdDataCoin().getPercentChange30d(), dataItem.getQuote().getUsdDataCoin().getPercentChange30d());
                    }
                    else {
                        return Double.compare(t1.getQuote().getUsdDataCoin().getPercentChange24h(), dataItem.getQuote().getUsdDataCoin().getPercentChange24h());
                    }
                }

                @Override
                public boolean equals(Object o) {
                    return false;
                }
            });
        }
        return cryptoValute;
    }

    private void showDialogSelectPercentChange() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.modal_bottom_sheet_percent_change);

        RelativeLayout select1h = (RelativeLayout) bottomSheetDialog.findViewById(R.id.rl_select_1h_percent_change);
        RelativeLayout select24h = (RelativeLayout) bottomSheetDialog.findViewById(R.id.rl_select_24hd_percent_change);
        RelativeLayout select7d = (RelativeLayout)  bottomSheetDialog.findViewById(R.id.rl_select_7d_percent_change);
        RelativeLayout select30d = (RelativeLayout) bottomSheetDialog.findViewById(R.id.rl_select_30d_percent_change);
        TextView tvSelect1h = (TextView) bottomSheetDialog.findViewById(R.id.tv_select_1h);
        TextView tvSelect24h = (TextView) bottomSheetDialog.findViewById(R.id.tv_select_24h);
        TextView tvSelect7d = (TextView) bottomSheetDialog.findViewById(R.id.tv_select_7d);
        TextView tvSelect30d = (TextView) bottomSheetDialog.findViewById(R.id.tv_select_30d);
        ImageView imageSelect1h = (ImageView) bottomSheetDialog.findViewById(R.id.image_baseline_done_select_1h);
        ImageView imageSelect24h = (ImageView) bottomSheetDialog.findViewById(R.id.image_baseline_done_select_24h);
        ImageView imageSelect7d = (ImageView) bottomSheetDialog.findViewById(R.id.image_baseline_done_select_7d);
        ImageView imageSelect30d = (ImageView) bottomSheetDialog.findViewById(R.id.image_baseline_done_select_30d);
        if(isChange1h){
            imageSelect1h.setVisibility(View.VISIBLE);
            tvSelect1h.setTextColor(ResourcesCompat.getColor(getContext().getResources(), R.color.primaryColor, null));
        }
        if(isChange24h){
            imageSelect24h.setVisibility(View.VISIBLE);
            tvSelect24h.setTextColor(ResourcesCompat.getColor(getContext().getResources(), R.color.primaryColor, null));
        }
        if(isChange7d){
            imageSelect7d.setVisibility(View.VISIBLE);
            tvSelect7d.setTextColor(ResourcesCompat.getColor(getContext().getResources(), R.color.primaryColor, null));
        }
        if(isChange30d){
            imageSelect30d.setVisibility(View.VISIBLE);
            tvSelect30d.setTextColor(ResourcesCompat.getColor(getContext().getResources(), R.color.primaryColor, null));
        }
        bottomSheetDialog.show();

        select1h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChange1h = true;
                isChange24h = false;
                isChange7d = false;
                isChange30d = false;
                if(isSortByPercentChange) {
                    adapterCryptoValutePrice = new CryptoValuteList(sortByPercentChangeSelect(oldDataCryptoValute), metadata);
                    adapterCryptoValutePrice.setIsChange(isChange1h, isChange24h, isChange7d, isChange30d);
                    listViewTop.setAdapter(adapterCryptoValutePrice);
                }
                else{
                    adapterCryptoValutePrice.setIsChange(true, false, false, false);
                    adapterCryptoValutePrice.notifyDataSetChanged();
                }
                textViewPercentChangeSelect.setText(getString(R.string._1h));
                bottomSheetDialog.dismiss();
            }
        });

        select24h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChange1h = false;
                isChange24h = true;
                isChange7d = false;
                isChange30d = false;
                if(isSortByPercentChange) {
                    adapterCryptoValutePrice = new CryptoValuteList(sortByPercentChangeSelect(oldDataCryptoValute), metadata);
                    adapterCryptoValutePrice.setIsChange(isChange1h, isChange24h, isChange7d, isChange30d);
                    listViewTop.setAdapter(adapterCryptoValutePrice);
                }
                else {
                    adapterCryptoValutePrice.setIsChange(false, true, false, false);
                    adapterCryptoValutePrice.notifyDataSetChanged();
                }
                textViewPercentChangeSelect.setText(getString(R.string._24h));
                bottomSheetDialog.dismiss();
            }
        });

        select7d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChange1h = false;
                isChange24h = false;
                isChange7d = true;
                isChange30d = false;
                if(isSortByPercentChange) {
                    adapterCryptoValutePrice = new CryptoValuteList(sortByPercentChangeSelect(oldDataCryptoValute), metadata);
                    adapterCryptoValutePrice.setIsChange(isChange1h, isChange24h, isChange7d, isChange30d);
                    listViewTop.setAdapter(adapterCryptoValutePrice);
                }
                else {
                    adapterCryptoValutePrice.setIsChange(false, false, true, false);
                    adapterCryptoValutePrice.notifyDataSetChanged();
                }
                textViewPercentChangeSelect.setText(getString(R.string._7d));
                bottomSheetDialog.dismiss();
            }
        });

        select30d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChange1h = false;
                isChange24h = false;
                isChange7d = false;
                isChange30d = true;
                if(isSortByPercentChange) {
                    adapterCryptoValutePrice = new CryptoValuteList(sortByPercentChangeSelect(oldDataCryptoValute), metadata);
                    adapterCryptoValutePrice.setIsChange(isChange1h, isChange24h, isChange7d, isChange30d);
                    listViewTop.setAdapter(adapterCryptoValutePrice);
                }
                else {
                    adapterCryptoValutePrice.setIsChange(false, false, false, true);
                    adapterCryptoValutePrice.notifyDataSetChanged();
                }
                textViewPercentChangeSelect.setText(getString(R.string._30d));
                bottomSheetDialog.dismiss();
            }
        });
    }

    private CryptoValute sortByPercentChangeSelect(CryptoValute cryptoValute){
        if (isSortByPercentChangeUp){
            //сортировка по убыванию
            cryptoValute.getData().sort(new Comparator<DataItem>() {
                @Override
                public int compare(DataItem dataItem, DataItem t1) {
                    if(isChange1h){
                        return Double.compare(dataItem.getQuote().getUsdDataCoin().getPercentChange1h(), t1.getQuote().getUsdDataCoin().getPercentChange1h());
                    }else if(isChange7d){
                        return Double.compare(dataItem.getQuote().getUsdDataCoin().getPercentChange7d(), t1.getQuote().getUsdDataCoin().getPercentChange7d());
                    }
                    else if(isChange30d){
                        return Double.compare(dataItem.getQuote().getUsdDataCoin().getPercentChange30d(), t1.getQuote().getUsdDataCoin().getPercentChange30d());
                    }
                    else {
                        return Double.compare(dataItem.getQuote().getUsdDataCoin().getPercentChange24h(), t1.getQuote().getUsdDataCoin().getPercentChange24h());
                    }
                }

                @Override
                public boolean equals(Object o) {
                    return false;
                }
            });
        }
        else{
            //сортировка по возрастанию
            cryptoValute.getData().sort(new Comparator<DataItem>() {
                @Override
                public int compare(DataItem dataItem, DataItem t1) {
                    if(isChange1h){
                        return Double.compare(t1.getQuote().getUsdDataCoin().getPercentChange1h(), dataItem.getQuote().getUsdDataCoin().getPercentChange1h());
                    }
                    else if(isChange7d){
                        return Double.compare(t1.getQuote().getUsdDataCoin().getPercentChange7d(), dataItem.getQuote().getUsdDataCoin().getPercentChange7d());
                    }
                    else if(isChange30d){
                        return Double.compare(t1.getQuote().getUsdDataCoin().getPercentChange30d(), dataItem.getQuote().getUsdDataCoin().getPercentChange30d());
                    }
                    else {
                        return Double.compare(t1.getQuote().getUsdDataCoin().getPercentChange24h(), dataItem.getQuote().getUsdDataCoin().getPercentChange24h());
                    }
                }

                @Override
                public boolean equals(Object o) {
                    return false;
                }
            });
        }
        return cryptoValute;
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
                        _cryptoValute.getData().sort(Comparator.comparing(DataItem::getCmcRank));
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
