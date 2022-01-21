package com.example.cryptocoin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptocoin.cryptovalutepojo.CryptoValute;

import org.w3c.dom.Text;

public class AdapterCryptoValutePrice extends BaseAdapter{

    CryptoValute cryptoValuteList;

    public AdapterCryptoValutePrice(CryptoValute _cryptoValuteList){
        cryptoValuteList = _cryptoValuteList;
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
            view = inflater.inflate(R.layout.tv_for_cryptovalute_list, viewGroup, false);
        }

        LinearLayout layout = (LinearLayout) view;
        TextView textViewPositionInTop = (TextView) view.findViewById(R.id.position_in_top);
        TextView textViewNameCryptoValute = (TextView) view.findViewById(R.id.name_cryptovalute_list_top);
        TextView textViewSymbolCryptoValute = (TextView) view.findViewById(R.id.symbol_cryptovalute_list_top);
        TextView textViewPriceCryptoValute = (TextView) view.findViewById(R.id.price_cryptovalute_list_top);
        TextView textViewPercentChange = (TextView) view.findViewById(R.id.percent_change_cryptovalute_list_top);

        textViewPositionInTop.setText(String.valueOf(i+1));
        textViewNameCryptoValute.setText(cryptoValuteList.getData().get(i).getName());
        textViewSymbolCryptoValute.setText(cryptoValuteList.getData().get(i).getSymbol());
        double priceCryptoValute = cryptoValuteList.getData().get(i).getQuote().getUsdDataCoin().getPrice();
        textViewPriceCryptoValute.setText(String.format("$%.2f",priceCryptoValute));
        double percentChange24H = cryptoValuteList.getData().get(i).getQuote().getUsdDataCoin().getPercentChange24h();
        textViewPercentChange.setText(String.format("%.2f%%", percentChange24H));
        if(percentChange24H>=0){ textViewPercentChange.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.green, null)); }
        else {textViewPercentChange.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.red, null)); }

        return view;
    }
}
