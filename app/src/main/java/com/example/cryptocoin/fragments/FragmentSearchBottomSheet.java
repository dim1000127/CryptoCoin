package com.example.cryptocoin.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.cryptocoin.Const;
import com.example.cryptocoin.R;
import com.example.cryptocoin.activity.ConvertCryptoValute;
import com.example.cryptocoin.adapter.ExpListContracts;
import com.example.cryptocoin.pojo.metadatapojo.ContractAddress;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;
import com.example.cryptocoin.pojo.quotescryptovalute.QuotesCryptoValute;
import com.example.cryptocoin.retrofit.RetrofitQuotesSingleton;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FragmentSearchBottomSheet extends BottomSheetDialogFragment {

    private String id = "1";
    private Subscription subscription;
    private QuotesCryptoValute quotesCryptoValute;
    private Metadata metadata;

    private final List<List<ContractAddress>> listGroups = new ArrayList<>();
    private List<ContractAddress> listChild;

    private ExpListContracts adapterExpListContracts;

    private ImageView imageCryptoValute;
    private ImageView imageNameCv;
    private ImageView imageViewSymbolCv;
    private ImageView imageViewPriceCv;
    private ImageView imageViewValChange24h;
    private ImageView imageViewPercentChange24h;
    private ImageView imageViewPercentChange1h;
    private ImageView imageViewPercentChange7d;
    private ImageView imageViewPercentChange30d;
    private ImageView imageViewVolume24h;
    private ImageView imageViewDominance;
    private ImageView imageViewMarketCap;
    private ImageView imageViewCirculatingSupply;
    private ImageView imageViewTotalSupply;
    private ImageView imageViewMaxSupply;
    private TextView textViewNameCV;
    private TextView textViewSymbolCV;
    private TextView textViewPriceCV;
    private TextView textViewCmcRank;
    private TextView textViewValChange24h;
    private TextView textViewPercentChange24h;
    private TextView textViewPercentChange1h;
    private TextView textViewPercentChange7d;
    private TextView textViewPercentChange30d;
    private TextView textViewVolume24h;
    private TextView textViewDominance;
    private TextView textViewMarketCap;
    private TextView textViewCirculatingSupply;
    private TextView textViewTotalSupply;
    private TextView textViewMaxSupply;
    private ExpandableListView expandableListViewContracts;
    private Button buttonConvertation;

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    public FragmentSearchBottomSheet(String id) {
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        imageCryptoValute = view.findViewById(R.id.bottom_sheet_image_cv);
        imageNameCv = view.findViewById(R.id.img_bottom_sheet_name_cv);
        imageViewSymbolCv = view.findViewById(R.id.img_bottom_sheet_symbol_cv);
        imageViewPriceCv = view.findViewById(R.id.img_bottom_sheet_price_cv);
        imageViewValChange24h = view.findViewById(R.id.img_bottom_sheet_value_change24h);
        imageViewPercentChange24h = view.findViewById(R.id.img_bottom_sheet_percent_change24h);
        imageViewPercentChange1h = view.findViewById(R.id.img_bottom_sheet_percent_change1h);
        imageViewPercentChange7d = view.findViewById(R.id.img_bottom_sheet_percent_change7d);
        imageViewPercentChange30d = view.findViewById(R.id.img_bottom_sheet_percent_change30d);
        imageViewVolume24h = view.findViewById(R.id.img_bottom_sheet_volume24h);
        imageViewDominance = view.findViewById(R.id.img_bottom_sheet_dominance);
        imageViewMarketCap = view.findViewById(R.id.img_bottom_sheet_market_cap);
        imageViewCirculatingSupply = view.findViewById(R.id.img_bottom_sheet_circulating_supply);
        imageViewTotalSupply = view.findViewById(R.id.img_bottom_sheet_total_supply);
        imageViewMaxSupply = view.findViewById(R.id.img_bottom_sheet_max_supply);
        textViewNameCV = view.findViewById(R.id.bottom_sheet_name_cv);
        textViewSymbolCV = view.findViewById(R.id.bottom_sheet_symbol_cv);
        textViewPriceCV = view.findViewById(R.id.bottom_sheet_price_cv);
        textViewCmcRank = view.findViewById(R.id.bottom_sheet_cmc_rank);
        textViewValChange24h = view.findViewById(R.id.bottom_sheet_value_change24h);
        textViewPercentChange24h = view.findViewById(R.id.bottom_sheet_percent_change24h);
        textViewPercentChange1h = view.findViewById(R.id.bottom_sheet_percent_change1h);
        textViewPercentChange7d = view.findViewById(R.id.bottom_sheet_percent_change7d);
        textViewPercentChange30d = view.findViewById(R.id.bottom_sheet_percent_change30d);
        textViewVolume24h = view.findViewById(R.id.bottom_sheet_volume24h);
        textViewDominance = view.findViewById(R.id.bottom_sheet_dominance);
        textViewMarketCap = view.findViewById(R.id.bottom_sheet_market_cap);
        textViewCirculatingSupply = view.findViewById(R.id.bottom_sheet_circulating_supply);
        textViewTotalSupply = view.findViewById(R.id.bottom_sheet_total_supply);
        textViewMaxSupply = view.findViewById(R.id.bottom_sheet_max_supply);
        expandableListViewContracts = view.findViewById(R.id.expandeble_lv_contracts);
        buttonConvertation = view.findViewById(R.id.btn_convertation_cv_bottomsheet);

        buttonConvertation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quotesCryptoValute != null && metadata != null) {
                    Intent intent = new Intent(getActivity(), ConvertCryptoValute.class);
                    intent.putExtra(Const.START_FROM_BOTTOMSHEET, Boolean.valueOf(true));
                    intent.putExtra(Const.ID_CRYPTOVALUTE_ITEM_MESSAGE, id);
                    intent.putExtra(Const.PRICE_MESSAGE, quotesCryptoValute.getData().get(id).getQuote().getUsdDataCoin().getPrice());
                    intent.putExtra(Const.SYMBOL_MESSAGE, quotesCryptoValute.getData().get(id).getSymbol());
                    intent.putExtra(Const.LOGO_MESSAGE, metadata.getData().get(id).getLogo());
                    startActivity(intent);
                }
            }
        });

        expandableListViewContracts.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                setListViewHeight(expandableListView, i);
                return false;
            }
        });

        getQuotesCVData(id);
        return view;
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    private void getQuotesCVData(String id) {
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }

        subscription = RetrofitQuotesSingleton.getQuotesCVObservable(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, Object>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("onCompleted", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Map<String, Object> _quotesCVMetadata) {
                        QuotesCryptoValute _quotesCryptoValute = (QuotesCryptoValute) _quotesCVMetadata.get(Const.CRYPTOVALUTE_KEY_MAP);
                        Metadata _metadata = (Metadata) _quotesCVMetadata.get(Const.METADATA_KEY_MAP);
                        fillDataSheet(_quotesCryptoValute, _metadata, id);
                    }
                });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    private void fillDataSheet(QuotesCryptoValute _quotesCruptoValute, Metadata _metadata, String id){

        quotesCryptoValute = _quotesCruptoValute;
        metadata = _metadata;

        if(quotesCryptoValute == null || metadata == null){
            return;
        }

        imageNameCv.setVisibility(View.GONE);
        imageViewSymbolCv.setVisibility(View.GONE);
        imageViewPriceCv.setVisibility(View.GONE);
        imageViewValChange24h.setVisibility(View.GONE);
        imageViewPercentChange24h.setVisibility(View.GONE);
        imageViewPercentChange1h.setVisibility(View.GONE);
        imageViewPercentChange7d.setVisibility(View.GONE);
        imageViewPercentChange30d.setVisibility(View.GONE);
        imageViewVolume24h.setVisibility(View.GONE);
        imageViewDominance.setVisibility(View.GONE);
        imageViewMarketCap.setVisibility(View.GONE);
        imageViewCirculatingSupply.setVisibility(View.GONE);
        imageViewTotalSupply.setVisibility(View.GONE);
        imageViewMaxSupply.setVisibility(View.GONE);

        String idCryptoValute = String.valueOf(quotesCryptoValute.getData().get(id).getId());
        Picasso.get()
                .load(metadata.getData().get(idCryptoValute).getLogo())
                .into(imageCryptoValute);
        textViewNameCV.setText(quotesCryptoValute.getData().get(id).getName());
        String symbolCV =quotesCryptoValute.getData().get(id).getSymbol();
        textViewSymbolCV.setText(symbolCV);

        double priceCV = quotesCryptoValute.getData().get(id).getQuote().getUsdDataCoin().getPrice();
        if(priceCV <= 1){
            textViewPriceCV.setText(String.format("$%,.6f", priceCV));
        }
        else {
            textViewPriceCV.setText(String.format("$%,.2f", priceCV));
        }

        String cmcRank = "#"+quotesCryptoValute.getData().get(id).getCmcRank();
        textViewCmcRank.setText(cmcRank);

        double percentChange24h = quotesCryptoValute.getData().get(id).getQuote().getUsdDataCoin().getPercentChange24h();
        double valChange24h = (priceCV * percentChange24h)/100;

        if(valChange24h >= 0){
            if(valChange24h <=1) {
                textViewValChange24h.setText(String.format("$%,.6f", valChange24h));
            }
            else{
                textViewValChange24h.setText(String.format("$%,.2f", valChange24h));
            }
        }
        else {
            if(valChange24h >=-1f) {
                textViewValChange24h.setText("-"+String.format("$%,.6f", Math.abs(valChange24h)));
            }
            else{
                textViewValChange24h.setText("-"+String.format("$%,.2f", Math.abs(valChange24h)));
            }

        }

        textViewPercentChange24h.setText(String.format("%.2f%%", percentChange24h));
        if(percentChange24h>=0){
            textViewPercentChange24h.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
            textViewPercentChange24h.setCompoundDrawablesWithIntrinsicBounds(R.drawable.style_arrow_green, 0, 0, 0);
        }
        else {
            textViewPercentChange24h.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
            textViewPercentChange24h.setCompoundDrawablesWithIntrinsicBounds(R.drawable.style_arrow_red, 0, 0, 0);
        }

        double percentChange1h = quotesCryptoValute.getData().get(id).getQuote().getUsdDataCoin().getPercentChange1h();
        textViewPercentChange1h.setText(String.format("%.2f%%", percentChange1h));
        if(percentChange1h>=0){ textViewPercentChange1h.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null)); }
        else {textViewPercentChange1h.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null)); }

        double percentChange7d = quotesCryptoValute.getData().get(id).getQuote().getUsdDataCoin().getPercentChange7d();
        textViewPercentChange7d.setText(String.format("%.2f%%", percentChange7d));
        if(percentChange7d >=0){ textViewPercentChange7d.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null)); }
        else {textViewPercentChange7d.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null)); }

        double percentChange30d = quotesCryptoValute.getData().get(id).getQuote().getUsdDataCoin().getPercentChange30d();
        textViewPercentChange30d.setText(String.format("%.2f%%", percentChange30d));
        if(percentChange30d >=0){ textViewPercentChange30d.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null)); }
        else {textViewPercentChange30d.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null)); }

        double volume24h = quotesCryptoValute.getData().get(id).getQuote().getUsdDataCoin().getVolume24h();
        textViewVolume24h.setText(String.format("$%,.2f", volume24h));

        double dominance = quotesCryptoValute.getData().get(id).getQuote().getUsdDataCoin().getMarketCapDominance();
        textViewDominance.setText(String.format("%.2f%%", dominance));

        double marketCap = quotesCryptoValute.getData().get(id).getQuote().getUsdDataCoin().getMarketCap();
        textViewMarketCap.setText(String.format("$%,.2f", marketCap));

        double circulatingSupply = quotesCryptoValute.getData().get(id).getCirculatingSupply();
        if (circulatingSupply == 0) { textViewCirculatingSupply.setText("Нет данных"); }
        else{ textViewCirculatingSupply.setText(String.format("%,.0f", circulatingSupply) + " " + symbolCV); }

        double totalSupply = quotesCryptoValute.getData().get(id).getTotalSupply();
        if (totalSupply == 0) { textViewTotalSupply.setText("Нет данных"); }
        else{ textViewTotalSupply.setText(String.format("%,.0f", totalSupply) + " " + symbolCV); }

        double maxSupply = quotesCryptoValute.getData().get(id).getMaxSupply();
        if (maxSupply == 0){ textViewMaxSupply.setText("Нет данных"); }
        else{ textViewMaxSupply.setText(String.format("%,.0f", maxSupply) + " " + symbolCV); }

        listChild = metadata.getData().get(idCryptoValute).getContractAddress();
        if (listChild.size() != 0){
            listGroups.add(listChild);
            adapterExpListContracts = new ExpListContracts(listGroups);
            expandableListViewContracts.setAdapter(adapterExpListContracts);
        }
        else{
            expandableListViewContracts.setVisibility(View.GONE);
        }
    }
}
