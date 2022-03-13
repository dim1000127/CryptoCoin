package com.example.cryptocoin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.cryptocoin.R;
import com.example.cryptocoin.pojo.cryptovalutepojo.CryptoValute;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;
import com.squareup.picasso.Picasso;

public class CryptoValuteList extends BaseAdapter{

    private CryptoValute cryptoValuteList;
    private Metadata metadata;

    public CryptoValuteList(CryptoValute _cryptoValuteList, Metadata _metadata){
        cryptoValuteList = _cryptoValuteList;
        metadata = _metadata;
    }

    @Override
    public int getCount() {
        return cryptoValuteList.getData().size();
    }

    @Override
    public Object getItem(int i) {
        return cryptoValuteList.getData().get(i);
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
            view = inflater.inflate(R.layout.tv_cryptovalute_list, viewGroup, false);
        }

        LinearLayout layout = (LinearLayout) view;
        String idCryptoValute = String.valueOf(cryptoValuteList.getData().get(i).getId());
        ImageView imageViewCryptoValuteLogo = (ImageView) view.findViewById(R.id.image_logo_cryptovalute_list);
        TextView textViewPositionInTop = (TextView) view.findViewById(R.id.position_in_top);
        TextView textViewNameCryptoValute = (TextView) view.findViewById(R.id.name_cryptovalute_list_top);
        TextView textViewSymbolCryptoValute = (TextView) view.findViewById(R.id.symbol_cryptovalute_list_top);
        TextView textViewPriceCryptoValute = (TextView) view.findViewById(R.id.price_cryptovalute_list_top);
        TextView textViewPercentChange = (TextView) view.findViewById(R.id.percent_change_cryptovalute_list_top);

        Picasso.get().load(metadata.getData().get(idCryptoValute).getLogo()).into(imageViewCryptoValuteLogo);
        textViewPositionInTop.setText(String.valueOf(i+1));
        textViewNameCryptoValute.setText(cryptoValuteList.getData().get(i).getName());
        textViewSymbolCryptoValute.setText(cryptoValuteList.getData().get(i).getSymbol());
        double priceCryptoValute = cryptoValuteList.getData().get(i).getQuote().getUsdDataCoin().getPrice();
        if(priceCryptoValute<=1){
            textViewPriceCryptoValute.setText(String.format("$%,.6f", priceCryptoValute));
        }
        else {
            textViewPriceCryptoValute.setText(String.format("$%,.2f", priceCryptoValute));
        }
        double percentChange24H = cryptoValuteList.getData().get(i).getQuote().getUsdDataCoin().getPercentChange24h();
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
