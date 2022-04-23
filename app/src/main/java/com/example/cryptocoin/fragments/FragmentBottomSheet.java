package com.example.cryptocoin.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
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
import com.example.cryptocoin.pojo.cryptovalutepojo.CryptoValute;
import com.example.cryptocoin.pojo.cryptovalutepojo.DataItem;
import com.example.cryptocoin.pojo.metadatapojo.Item;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;
import com.example.cryptocoin.pojo.quotescryptovalute.QuotesCryptoValute;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


public class FragmentBottomSheet extends BottomSheetDialogFragment {

    private DataItem dataItem;
    private Item metadataItem;

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
    private Button buttonConvertation;

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    public FragmentBottomSheet(DataItem dataItem, Item metadataItem) {
        this.dataItem = dataItem;
        this.metadataItem = metadataItem;
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
        imageViewPercentChange30d = (ImageView) view.findViewById(R.id.img_bottom_sheet_percent_change30d);
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
        textViewPercentChange30d = (TextView) view.findViewById(R.id.bottom_sheet_percent_change30d);
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
                intent.putExtra(Const.ID_CRYPTOVALUTE_ITEM_MESSAGE, String.valueOf(dataItem.getId()));
                intent.putExtra(Const.PRICE_MESSAGE, dataItem.getQuote().getUsdDataCoin().getPrice());
                intent.putExtra(Const.SYMBOL_MESSAGE, dataItem.getSymbol());
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
        if(dataItem == null || metadataItem == null){
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

        Picasso.get()
                .load(metadataItem.getLogo())
                .placeholder(R.drawable.ic_placeholder_image)
                .into(imageCryptoValute);

        textViewNameCV.setText(dataItem.getName());
        String symbolCV = dataItem.getSymbol();
        textViewSymbolCV.setText(symbolCV);

        double priceCV = dataItem.getQuote().getUsdDataCoin().getPrice();
        if(priceCV <= 1){
            textViewPriceCV.setText(String.format("$%,.6f", priceCV));
        }
        else {
            textViewPriceCV.setText(String.format("$%,.2f", priceCV));
        }

        double percentChange24h = dataItem.getQuote().getUsdDataCoin().getPercentChange24h();
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

        double percentChange1h = dataItem.getQuote().getUsdDataCoin().getPercentChange1h();
        textViewPercentChange1h.setText(String.format("%.2f%%", percentChange1h));
        if(percentChange1h>=0){ textViewPercentChange1h.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null)); }
        else {textViewPercentChange1h.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null)); }

        double percentChange7d = dataItem.getQuote().getUsdDataCoin().getPercentChange7d();
        textViewPercentChange7d.setText(String.format("%.2f%%", percentChange7d));
        if(percentChange7d >=0){ textViewPercentChange7d.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null)); }
        else {textViewPercentChange7d.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null)); }

        double percentChange30d = dataItem.getQuote().getUsdDataCoin().getPercentChange30d();
        textViewPercentChange30d.setText(String.format("%.2f%%", percentChange30d));
        if(percentChange30d >=0){ textViewPercentChange30d.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null)); }
        else {textViewPercentChange30d.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null)); }

        double volume24h = dataItem.getQuote().getUsdDataCoin().getVolume24h();
        textViewVolume24h.setText(String.format("$%,.2f", volume24h));
        double dominance = dataItem.getQuote().getUsdDataCoin().getMarketCapDominance();
        textViewDominance.setText(String.format("%.2f%%", dominance));
        double marketCap = dataItem.getQuote().getUsdDataCoin().getMarketCap();
        textViewMarketCap.setText(String.format("$%,.2f", marketCap));

        double circulatingSupply = dataItem.getCirculatingSupply();
        if (circulatingSupply == 0) { textViewCirculatingSupply.setText("Нет данных"); }
        else{ textViewCirculatingSupply.setText(String.format("%,.0f", circulatingSupply) + " " + symbolCV); }

        double totalSupply = dataItem.getTotalSupply();
        if (totalSupply == 0) { textViewTotalSupply.setText("Нет данных"); }
        else{ textViewTotalSupply.setText(String.format("%,.0f", totalSupply) + " " + symbolCV); }

        double maxSupply = dataItem.getMaxSupply();
        if (maxSupply == 0){ textViewMaxSupply.setText("Нет данных"); }
        else{ textViewMaxSupply.setText(String.format("%,.0f", maxSupply) + " " + symbolCV); }
    }
}
