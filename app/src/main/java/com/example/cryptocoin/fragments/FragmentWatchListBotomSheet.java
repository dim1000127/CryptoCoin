package com.example.cryptocoin.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.cryptocoin.pojo.metadatapojo.Item;
import com.example.cryptocoin.pojo.quotescryptovalute.QuotesItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

public class FragmentWatchListBotomSheet extends BottomSheetDialogFragment {

    private QuotesItem quotesItem;
    private Item metadataItem;

    private ImageView imageCryptoValute;
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

    public FragmentWatchListBotomSheet(QuotesItem quotesItem, Item metadataItem) {
        this.quotesItem = quotesItem;
        this.metadataItem = metadataItem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        imageCryptoValute = (ImageView) view.findViewById(R.id.bottom_sheet_image_cv);
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
                Intent intent = new Intent(getActivity(), ConvertCryptoValute.class);
                intent.putExtra(Const.START_FROM_BOTTOMSHEET, Boolean.valueOf(true));
                intent.putExtra(Const.ID_CRYPTOVALUTE_ITEM_MESSAGE, String.valueOf(quotesItem.getId()));
                intent.putExtra(Const.PRICE_MESSAGE, quotesItem.getQuote().getUsdDataCoin().getPrice());
                intent.putExtra(Const.SYMBOL_MESSAGE, quotesItem.getSymbol());
                intent.putExtra(Const.LOGO_MESSAGE, metadataItem.getLogo());
                startActivity(intent);
            }
        });

        fillDataSheet();
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return super.onCreateDialog(savedInstanceState);
    }

    private void fillDataSheet(){
        if(quotesItem == null || metadataItem == null){
            return;
        }
        Picasso.get()
                .load(metadataItem.getLogo())
                .placeholder(R.drawable.ic_placeholder_image)
                .into(imageCryptoValute);

        textViewNameCV.setText(quotesItem.getName());
        String symbolCV = quotesItem.getSymbol();
        textViewSymbolCV.setText(symbolCV);

        double priceCV = quotesItem.getQuote().getUsdDataCoin().getPrice();
        if(priceCV <= 1){
            textViewPriceCV.setText(String.format("$%,.6f", priceCV));
        }
        else {
            textViewPriceCV.setText(String.format("$%,.2f", priceCV));
        }

        double percentChange24h = quotesItem.getQuote().getUsdDataCoin().getPercentChange24h();
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

        double percentChange1h = quotesItem.getQuote().getUsdDataCoin().getPercentChange1h();
        textViewPercentChange1h.setText(String.format("%.2f%%", percentChange1h));
        if(percentChange1h>=0){ textViewPercentChange1h.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null)); }
        else {textViewPercentChange1h.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null)); }

        double percentChange7d = quotesItem.getQuote().getUsdDataCoin().getPercentChange7d();
        textViewPercentChange7d.setText(String.format("%.2f%%", percentChange7d));
        if(percentChange7d >=0){ textViewPercentChange7d.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null)); }
        else {textViewPercentChange7d.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null)); }

        double volume24h = quotesItem.getQuote().getUsdDataCoin().getVolume24h();
        textViewVolume24h.setText(String.format("$%,.2f", volume24h));
        double dominance = quotesItem.getQuote().getUsdDataCoin().getMarketCapDominance();
        textViewDominance.setText(String.format("%.2f%%", dominance));
        double marketCap = quotesItem.getQuote().getUsdDataCoin().getMarketCap();
        textViewMarketCap.setText(String.format("$%,.2f", marketCap));

        double circulatingSupply = quotesItem.getCirculatingSupply();
        if (circulatingSupply == 0) { textViewCirculatingSupply.setText("Нет данных"); }
        else{ textViewCirculatingSupply.setText(String.format("%,.0f", circulatingSupply) + " " + symbolCV); }

        double totalSupply = quotesItem.getTotalSupply();
        if (totalSupply == 0) { textViewTotalSupply.setText("Нет данных"); }
        else{ textViewTotalSupply.setText(String.format("%,.0f", totalSupply) + " " + symbolCV); }

        double maxSupply = quotesItem.getMaxSupply();
        if (maxSupply == 0){ textViewMaxSupply.setText("Нет данных"); }
        else{ textViewMaxSupply.setText(String.format("%,.0f", maxSupply) + " " + symbolCV); }
    }
}
