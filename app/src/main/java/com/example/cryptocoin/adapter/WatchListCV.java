package com.example.cryptocoin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.cryptocoin.R;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;
import com.example.cryptocoin.pojo.quotescryptovalute.QuotesCryptoValute;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WatchListCV extends BaseAdapter {

    private QuotesCryptoValute quotesCryptoValute;
    private Metadata metadata;
    private ArrayList<String> idArray;

    public WatchListCV(QuotesCryptoValute quotesCryptoValute, Metadata metadata, ArrayList<String> idArray){
        this.quotesCryptoValute = quotesCryptoValute;
        this.metadata = metadata;
        this.idArray = idArray;
    }

    @Override
    public int getCount() {
        return quotesCryptoValute.getData().size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();

        if (view == null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_watchlist, viewGroup, false);
        }

        String idItem = idArray.get(i);

        String idCryptoValute = String.valueOf(quotesCryptoValute.getData().get(idItem).getId());
        ImageView imageViewCryptoValuteLogo = (ImageView) view.findViewById(R.id.image_logo_cryptovalute_watchlist);
        TextView textViewNameCryptoValute = (TextView) view.findViewById(R.id.name_cryptovalute_watchlist);
        TextView textViewSymbolCryptoValute = (TextView) view.findViewById(R.id.symbol_cryptovalute_watchlist);
        TextView textViewPriceCryptoValute = (TextView) view.findViewById(R.id.price_cryptovalute_watchlist);
        TextView textViewPercentChange = (TextView) view.findViewById(R.id.percent_change_cryptovalute_watchlist);

        Picasso.get()
                .load(metadata.getData().get(idCryptoValute).getLogo())
                .into(imageViewCryptoValuteLogo);;
        textViewNameCryptoValute.setText(quotesCryptoValute.getData().get(idItem).getName());
        textViewSymbolCryptoValute.setText(quotesCryptoValute.getData().get(idItem).getSymbol());
        double priceCryptoValute = quotesCryptoValute.getData().get(idItem).getQuote().getUsdDataCoin().getPrice();
        if(priceCryptoValute<=1){
            textViewPriceCryptoValute.setText(String.format("$%,.6f", priceCryptoValute));
        }
        else {
            textViewPriceCryptoValute.setText(String.format("$%,.2f", priceCryptoValute));
        }
        double percentChange24H = quotesCryptoValute.getData().get(idItem).getQuote().getUsdDataCoin().getPercentChange24h();
        textViewPercentChange.setText(String.format("%.2f%%", percentChange24H));
        if(percentChange24H>=0){
            textViewPercentChange.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.green, null));
            textViewPercentChange.setCompoundDrawablesWithIntrinsicBounds(R.drawable.style_arrow_green, 0, 0, 0);
        }
        else {
            textViewPercentChange.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.red, null));
            textViewPercentChange.setCompoundDrawablesWithIntrinsicBounds(R.drawable.style_arrow_red,0,0,0);
        }

        return view;
    }
}
