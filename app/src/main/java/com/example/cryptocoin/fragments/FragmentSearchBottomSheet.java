package com.example.cryptocoin.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.cryptocoin.Const;
import com.example.cryptocoin.R;
import com.example.cryptocoin.activity.ConvertCryptoValute;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;
import com.example.cryptocoin.pojo.quotescryptovalute.QuotesCryptoValute;
import com.example.cryptocoin.retrofit.RetrofitQuotesSingleton;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

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

    private ImageView imageCryptoValute;
    private ImageView imageNameCv;
    private ImageView imageViewSymbolCv;
    private ImageView imageViewPriceCv;
    private ImageView imageViewValChange24h;
    private ImageView imageViewPercentChange24h;
    private ImageView imageViewPercentChange1h;
    private ImageView imageViewPercentChange7d;
    private ImageView imageViewVolume24h;
    private ImageView imageViewDominance;
    private ImageView imageViewMarketCap;
    private ImageView imageViewCirculatingSupply;
    private ImageView imageViewTotalSupply;
    private ImageView imageViewMaxSupply;
    private TextView textViewNameCV;
    private TextView textViewSymbolCV;
    private TextView textViewPriceCV;
    private TextView textViewValChange24h;
    private TextView textViewPercentChange24h;
    private TextView textViewPercentChange1h;
    private TextView textViewPercentChange7d;
    private TextView textViewVolume24h;
    private TextView textViewDominance;
    private TextView textViewMarketCap;
    private TextView textViewCirculatingSupply;
    private TextView textViewTotalSupply;
    private TextView textViewMaxSupply;
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

        imageCryptoValute = (ImageView) view.findViewById(R.id.bottom_sheet_image_cv);
        imageNameCv = (ImageView) view.findViewById(R.id.img_bottom_sheet_name_cv);
        imageViewSymbolCv = (ImageView) view.findViewById(R.id.img_bottom_sheet_symbol_cv);
        imageViewPriceCv = (ImageView) view.findViewById(R.id.img_bottom_sheet_price_cv);
        imageViewValChange24h = (ImageView) view.findViewById(R.id.img_bottom_sheet_value_change24h);
        imageViewPercentChange24h = (ImageView) view.findViewById(R.id.img_bottom_sheet_percent_change24h);
        imageViewPercentChange1h = (ImageView) view.findViewById(R.id.img_bottom_sheet_percent_change1h);
        imageViewPercentChange7d = (ImageView) view.findViewById(R.id.img_bottom_sheet_percent_change7d);
        imageViewVolume24h = (ImageView) view.findViewById(R.id.img_bottom_sheet_volume24h);
        imageViewDominance = (ImageView) view.findViewById(R.id.img_bottom_sheet_dominance);
        imageViewMarketCap = (ImageView) view.findViewById(R.id.img_bottom_sheet_market_cap);
        imageViewCirculatingSupply = (ImageView) view.findViewById(R.id.img_bottom_sheet_circulating_supply);
        imageViewTotalSupply = (ImageView) view.findViewById(R.id.img_bottom_sheet_total_supply);
        imageViewMaxSupply = (ImageView) view.findViewById(R.id.img_bottom_sheet_max_supply);
        textViewNameCV = (TextView) view.findViewById(R.id.bottom_sheet_name_cv);
        textViewSymbolCV = (TextView) view.findViewById(R.id.bottom_sheet_symbol_cv);
        textViewPriceCV = (TextView) view.findViewById(R.id.bottom_sheet_price_cv);
        textViewValChange24h = (TextView) view.findViewById(R.id.bottom_sheet_value_change24h);
        textViewPercentChange24h = (TextView) view.findViewById(R.id.bottom_sheet_percent_change24h);
        textViewPercentChange1h = (TextView) view.findViewById(R.id.bottom_sheet_percent_change1h);
        textViewPercentChange7d = (TextView) view.findViewById(R.id.bottom_sheet_percent_change7d);
        textViewVolume24h = (TextView) view.findViewById(R.id.bottom_sheet_volume24h);
        textViewDominance = (TextView) view.findViewById(R.id.bottom_sheet_dominance);
        textViewMarketCap = (TextView) view.findViewById(R.id.bottom_sheet_market_cap);
        textViewCirculatingSupply = (TextView) view.findViewById(R.id.bottom_sheet_circulating_supply);
        textViewTotalSupply = (TextView) view.findViewById(R.id.bottom_sheet_total_supply);
        textViewMaxSupply = (TextView) view.findViewById(R.id.bottom_sheet_max_supply);
        buttonConvertation = (Button) view.findViewById(R.id.btn_convertation_cv_bottomsheet);

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


        getQuotesCVData(id);
        return view;
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
    }
}
