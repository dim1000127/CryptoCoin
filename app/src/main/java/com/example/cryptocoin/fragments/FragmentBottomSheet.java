package com.example.cryptocoin.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.cryptocoin.R;
import com.example.cryptocoin.pojo.cryptovalutepojo.CryptoValute;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;


public class FragmentBottomSheet extends BottomSheetDialogFragment {

    private CryptoValute dataCryptoValute;
    private Metadata metadata;

    private int position;
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

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    public FragmentBottomSheet(CryptoValute _cryptoValute, Metadata _metadata, int _position) {
        dataCryptoValute = _cryptoValute;
        metadata = _metadata;
        position = _position;
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

        fillDataSheet();

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        
        return super.onCreateDialog(savedInstanceState);
    }

    private void fillDataSheet(){
        if(dataCryptoValute == null){
            return;
        }
        String idCryptoValute = String.valueOf(dataCryptoValute.getData().get(position).getId());
        Picasso.get().load(metadata.getData().get(idCryptoValute).getLogo()).into(imageCryptoValute);
        textViewNameCV.setText(dataCryptoValute.getData().get(position).getName());
        String symbolCV = dataCryptoValute.getData().get(position).getSymbol();
        textViewSymbolCV.setText(symbolCV);
        double priceCV = dataCryptoValute.getData().get(position).getQuote().getUsdDataCoin().getPrice();
        if(priceCV <= 1){
            textViewPriceCV.setText(String.format("$%,.6f", priceCV));
        }
        else {
            textViewPriceCV.setText(String.format("$%,.2f", priceCV));
        }
        double percentChange24h = dataCryptoValute.getData().get(position).getQuote().getUsdDataCoin().getPercentChange24h();
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
        double percentChange1h = dataCryptoValute.getData().get(position).getQuote().getUsdDataCoin().getPercentChange1h();
        textViewPercentChange1h.setText(String.format("%.2f%%", percentChange1h));
        if(percentChange1h>=0){ textViewPercentChange1h.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null)); }
        else {textViewPercentChange1h.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null)); }
        double percentChange7d = dataCryptoValute.getData().get(position).getQuote().getUsdDataCoin().getPercentChange7d();
        textViewPercentChange7d.setText(String.format("%.2f%%", percentChange7d));
        if(percentChange7d >=0){ textViewPercentChange7d.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null)); }
        else {textViewPercentChange7d.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null)); }
        double volume24h = dataCryptoValute.getData().get(position).getQuote().getUsdDataCoin().getVolume24h();
        textViewVolume24h.setText(String.format("$%,.2f", volume24h));
        double dominance = dataCryptoValute.getData().get(position).getQuote().getUsdDataCoin().getMarketCapDominance();
        textViewDominance.setText(String.format("%.2f%%", dominance));
        double marketCap = dataCryptoValute.getData().get(position).getQuote().getUsdDataCoin().getMarketCap();
        textViewMarketCap.setText(String.format("$%,.2f", marketCap));
        double circulatingSupply = dataCryptoValute.getData().get(position).getCirculatingSupply();
        if (circulatingSupply == 0) { textViewCirculatingSupply.setText("Нет данных"); }
        else{ textViewCirculatingSupply.setText(String.format("%,.0f", circulatingSupply) + " " + symbolCV); }
        double totalSupply = dataCryptoValute.getData().get(position).getTotalSupply();
        if (totalSupply == 0) { textViewTotalSupply.setText("Нет данных"); }
        else{ textViewTotalSupply.setText(String.format("%,.0f", totalSupply) + " " + symbolCV); }
        double maxSupply = dataCryptoValute.getData().get(position).getMaxSupply();
        if (maxSupply == 0){ textViewMaxSupply.setText("Нет данных"); }
        else{ textViewMaxSupply.setText(String.format("%,.0f", maxSupply) + " " + symbolCV); }
    }
}
